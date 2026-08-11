# Job Hunter

Personal **Java backend job aggregator**: fetches openings from public APIs, scores them against your skills (Java, Spring, microservices, Hibernate, JPA, JWT, etc.), and shows **direct apply links**.

## Run

```bash
cd com.harshit/job-hunter
mvn spring-boot:run
```

This project includes **`.mvn/settings.xml`** so Maven uses **repo.maven.apache.org** when the corporate mirror (`moncclin8.corp.amdocs.com`) is unreachable. No extra flags needed if you run Maven from this folder.

If dependencies still fail, run explicitly:

```bash
mvn -s .mvn/settings.xml spring-boot:run
```

Open **http://localhost:8081/** (static UI calls REST API)

If port 8081 is also in use, run: `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090`

**Port 8080 already in use?** Another app (or old job-hunter) is running. Either use 8081 above, or stop it:

```powershell
netstat -ano | findstr :8080
taskkill /PID <pid> /F
```

- Search box: natural language e.g. `Java microservices JWT remote`
- **Refresh jobs** — pulls from configured sources
- **Apply →** opens official apply URL

## API

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/jobs/recommended?days=14&minScore=3&limit=30` | Ranked matches |
| POST | `/api/jobs/search` | `{"query":"Java backend 4 years India","days":14,"limit":20}` |
| POST | `/api/jobs/fetch` | Trigger ingestion now |

## Configure your profile & companies

Edit `src/main/resources/application.yml`:

- **`job-hunter.profile`** — skills, title keywords, experience band, locations
- **`job-hunter.sources`** — job sources

### Source types

| type | meaning |
|------|--------|
| `remotive` | Public Remotive API (software-dev) |
| `greenhouse` | Set `board-token` (company Greenhouse board) |
| `bookmark` | Manual careers page (e.g. [IBM Careers](https://www.ibm.com/in-en/careers/search)) — shows search link |

Example — add a Greenhouse board:

```yaml
- name: Acme Corp
  type: greenhouse
  board-token: acme
  enabled: true
```

## Architecture

- **Adapters** (`JobSourceAdapter`) — pluggable sources (Strategy pattern)
- **JPA** — store & dedupe jobs
- **Matcher** — keyword + title scoring
- **Scheduler** — fetch on startup + cron (6 AM / 6 PM default)

## Notes

- Does **not** auto-apply; only collects and links.
- IBM and many enterprise sites need **bookmark** or custom adapters; prefer Greenhouse/Lever tokens when you find them.
- H2 database file: `./data/job-hunter` (created on first run)

## Example prompts (search box)

- `Java spring backend remote`
- `microservices hibernate jpa`
- `Java JWT kafka India`
