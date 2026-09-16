# JobPulse

Config-driven career page aggregator. You register a company's career URL in
`config/companies.yml`; JobPulse fetches its vacancies, **keeps only India roles**, extracts
skills from each description, scores the posting against your profile, and serves the
result through a REST API and a dashboard.

Built for a Java backend search: Java, Spring Boot, Microservices, SQL, REST, Docker,
Kubernetes, Kafka.

---

## Verified against real career pages

Both connectors were run against live sites:

| Company | Career URL | Connector | Fetched | India | Matched |
|---|---|---|---|---|---|
| Accenture | `accenture.com/in-en/careers/jobsearch` | `WORKDAY` | 45 | 45 | 28 |
| Tekion | `tekion.com/careers` | `ASHBY` | 79 | 61 | 12 |

Every indexed posting resolved to **country = India**, across Bengaluru, Pune, Hyderabad,
Chennai, Mumbai, Noida, Gurugram, Kolkata, Kochi and Indore.

Example output at `minScore=60`:

```
[ 87] Tekion     | Bengaluru | Staff Software Engineer - Backend
      Java, Spring Boot, Microservices, SQL, Docker, Kafka, MongoDB, AWS
[ 87] Accenture  | Hyderabad | Custom Software Engineer
      Java, Spring Boot, Microservices, REST APIs, Docker, Kubernetes, Kafka, PostgreSQL
[ 81] Accenture  | Kochi     | Architect - Java
      Java, Spring Boot, Microservices, SQL, REST APIs, Docker, Kubernetes, Kafka
```

---

## Run it

Requires **JDK 17** and Maven.

```bash
mvn spring-boot:run
```

Open <http://localhost:8090>. A crawl runs at startup, so the dashboard has data within
about 90 seconds. Data is stored in an H2 file under `./data`, so no database setup.

With Postgres instead:

```bash
docker compose up --build
```

---

## How a company is onboarded

Everything lives in `config/companies.yml` — no code changes:

```yaml
jobpulse:
  companies:
    - id: tekion
      name: Tekion
      enabled: true
      connector: ASHBY
      career-url: https://tekion.com/careers
      options:
        board: tekion
```

### Picking the connector

Most career pages are a front end over a hiring platform that already exposes JSON. Using
that API is faster, more reliable and more polite than scraping HTML.

| Connector | Use when the career page is served by | Required options |
|---|---|---|
| `WORKDAY` | `*.myworkdayjobs.com` (Accenture, many enterprises) | `cxsBase`, `externalBase`, `countryFacetId` |
| `ASHBY` | `jobs.ashbyhq.com` (Tekion) | `board` |
| `GREENHOUSE` | `boards.greenhouse.io` | `boardToken` |
| `LEVER` | `jobs.lever.co` | `account` |
| `HTML` | Server-rendered list, no vendor API | `itemSelector`, `titleSelector`, `locationSelector` |

To identify it for a new URL: open the career page, click **Apply** on any job, and look at
the domain it lands on. That domain is the platform.

The `HTML` connector cannot read pages that build their job list in the browser with
JavaScript. That is why `accenture.com` uses `WORKDAY` rather than HTML scraping — the page
itself renders client-side, but the Workday API behind it returns clean JSON.

### Why Accenture needs `countryFacetId`

Workday accepts facets in the search request, so the India restriction is pushed
**upstream** rather than filtered after downloading. `c4f78be1a8f14da0ab49ce1162348a5e` is
Workday's internal identifier for India.

---

## India filtering

Two layers, applied before scoring so a non-India role can never be indexed even with a
perfect skill match:

1. **Upstream** — Workday receives the India country facet, so other countries are never
   downloaded.
2. **Local gate** (`LocationFilter`) — for feeds without a country facet:
   - explicit country from the feed is matched against the allow list, then the block list
   - otherwise the free-text location is matched against a list of Indian cities
   - unresolvable locations (for example Tekion's "Virtual - Canada") are dropped, unless
     `keep-unknown-locations: true`

Tune this in `application.yml` under `jobpulse.location-filter`. To search a different
country, replace `countries` and `cities`.

---

## Scoring

Transparent 0-100, reported per posting so a rank can be explained:

| Component | Max | Basis |
|---|---|---|
| Title relevance | 30 | Title matches a target role; generic engineering titles get half |
| Must-have coverage | 45 | Share of `must-have-skills` found |
| Nice-to-have depth | 20 | 4 points per bonus skill |
| Freshness | 5 | Posted within 7 days, else 30 days |
| Exclusion penalty | −40 | Title contains intern, fresher, recruiter, ... |

A posting is flagged `matched` only when the score clears `min-score`, at least
`min-must-have-matches` must-have skills are present, and no exclusion fired.

Skill detection is alias-aware and word-boundary aware, so `SpringBoot`, `k8s` and `PLSQL`
are recognised while **JavaScript is not counted as Java**.

Every posting exposes its arithmetic:

```
title=30/30, mustHave=36/45 (4 of 5), niceToHave=20/20 (8), freshness=1/5, penalty=-0
```

---

## API

| Method | Path | Purpose |
|---|---|---|
| `GET` | `/api/jobs/search` | Search with facets (below) |
| `GET` | `/api/jobs/{id}` | Full posting including description |
| `GET` | `/api/jobs/top` | Top 50 by score |
| `GET` | `/api/jobs/cities` | Cities present in the index |
| `GET` | `/api/companies` | Configured companies and connectors |
| `GET` | `/api/companies/profile` | Active skill profile |
| `POST` | `/api/crawl/run` | Crawl every enabled company |
| `POST` | `/api/crawl/run/{companyId}` | Crawl one company |
| `GET` | `/api/crawl/last` | Last run report, including failures |
| `GET` | `/api/crawl/stats` | Index counts by company |

Search parameters: `q`, `skills` (comma separated, all must match), `company`, `city`,
`minScore`, `matchedOnly`, `sinceDays`, `page`, `size`.

```bash
# Java + Spring Boot + Kubernetes roles in India, strong matches only
curl "http://localhost:8090/api/jobs/search?skills=Java,Spring%20Boot,Kubernetes&minScore=70"

# Anything new in Pune this week
curl "http://localhost:8090/api/jobs/search?city=pune&sinceDays=7"
```

---

## Architecture

Deployed as one service with strict package boundaries, each of which maps to a future
microservice. The pipeline is already a chain of single-purpose stages, so splitting it is
a matter of putting a queue between stages rather than untangling code.

```
config/     company registry, skill profile, India rules
connector/  one class per hiring platform, all behind JobConnector
pipeline/   LocationFilter -> SkillExtractor -> MatchScorer -> JobIngestService
repo/       JPA persistence with dedup on (companyId, externalId)
api/        REST controllers + static dashboard
```

Splitting into separate deployables:

| Package | Becomes | Boundary |
|---|---|---|
| `connector` + `CrawlOrchestrator` | crawler-service | publishes raw jobs |
| `pipeline` | matcher-service | consumes raw, publishes scored |
| `repo` + `api` | search-service | consumes scored, serves queries |

One service was the right call here: the crawl is IO-bound and runs twice a day, so the
operational cost of three deployables and a broker buys nothing yet. The seam is in place
for when volume justifies it.

---

## Operational behaviour

- **Deduplication** — upsert keyed on company plus the vendor's job id; re-crawling
  refreshes `lastSeenAt` instead of inserting again.
- **Detail-call budget** — Workday needs one request per posting for its description, so
  `max-detail-fetches` caps that; other connectors return descriptions in the list call.
- **Politeness** — single shared HTTP client with a descriptive user agent, configurable
  delay between requests, bounded retries and timeouts.
- **Failure isolation** — one company failing does not abort the run; the error is recorded
  in the crawl report.

### Corporate networks that inspect TLS

On a network with TLS interception, the proxy's CA is trusted by the OS but absent from the
JDK's `cacerts`, so every HTTPS call fails with `PKIX path building failed`. JobPulse
defaults `jobpulse.crawl.trust-store-type` to `Windows-ROOT`, which validates against the
Windows certificate store. In a Linux container the type does not exist and the JDK default
is used automatically.

---

## Tests

```bash
mvn test
```

`MatchingPipelineTest` covers the parts most likely to break silently: India acceptance by
country and by city, rejection of other countries and of unresolvable locations, alias
matching, JavaScript not counting as Java, internship exclusion, and missing-skill
reporting.

---

## Extending

- **Elasticsearch** — replace `JobPostingRepository.search` with an ES query; the rest is
  unaffected.
- **Notifications** — schedule a job over `search(minScore=80, sinceDays=1)` and send a digest.
- **JavaScript-only career pages** — add a `PLAYWRIGHT` connector implementing
  `JobConnector`; register it by adding the enum value.
- **LLM enrichment** — summarise descriptions or extract skills beyond the dictionary.

---

## Etiquette

Public job data intended for applicants, fetched at a low rate with an identifying user
agent, preferring official APIs. Keep `request-delay-millis` and `max-jobs` conservative,
check a site's terms and `robots.txt` before enabling it, and do not republish full
descriptions.

---

## Layout

```
jobpulse/
├── config/companies.yml          company registry (edit this)
├── src/main/java/com/harshit/jobpulse/
│   ├── config/                   properties + company/skill/location model
│   ├── connector/                Workday, Ashby, Greenhouse, Lever, HTML
│   ├── pipeline/                 filter, extract, score, ingest, orchestrate
│   ├── domain/ repo/             entity + persistence
│   └── api/                      REST controllers
├── src/main/resources/
│   ├── application.yml           skill profile + India rules
│   └── static/index.html         dashboard
├── tools/                        API discovery + verification scripts
├── k8s/jobpulse.yaml             namespace, ConfigMap, Deployment, Service, Ingress
├── Dockerfile  docker-compose.yml
└── pom.xml
```

`tools/` holds the scripts used to discover the two APIs — `probe_accenture.py` finds the
platform behind a career page, `probe_workday.py` and `probe_workday_india.py` confirm the
Workday search and India facet, and `verify_api.py` smoke-checks a running instance. Use
them when onboarding a new company.
