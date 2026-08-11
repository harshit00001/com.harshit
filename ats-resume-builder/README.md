# ATS Resume Builder

Tailor your resume to a **job description**, get an **ATS score**, edit summary/skills/experience, and **export PDF**.

Pre-loaded from your resume → `resume-default.json`. PDF uses `Harshit_Raj_Resume.pdf` as header template.

## Run

```powershell
cd "com.harshit\ats-resume-builder"
mvn spring-boot:run
```

Open **http://localhost:8082/**

Uses `.mvn/settings.xml` for Maven Central (same as job-hunter).

## Workflow

1. Paste **job description** in the UI.
2. **Analyze ATS score** — keyword match, structure, suggestions.
3. **Tailor resume to JD** — reorders skills & bullets, enhances summary (no fake skills).
4. Edit **summary / skills / title** → **Save resume**.
5. **Download PDF** — `resume-tailored.pdf`.

Data persists in `./data/resume.json`.

## API

| Method | URL | Body |
|--------|-----|------|
| GET | `/api/resume` | — |
| PUT | `/api/resume` | Full `ResumeDocument` JSON |
| POST | `/api/resume/reset` | Reset to default |
| POST | `/api/ats/analyze` | `{ "jobDescription": "..." }` |
| POST | `/api/ats/tailor` | `{ "jobDescription": "..." }` |
| GET | `/api/resume/pdf` | Download PDF |

## ATS scoring (MVP)

- **65%** JD keyword match (Java, Spring, Kafka, etc.)
- **20%** sections (summary, skills, experience, education)
- **15%** contact info

**Important:** Only add skills you actually have. Tailoring **reorders** and **emphasizes** existing content.

## Customize

- Default data: `src/main/resources/resume-default.json` (live copy: `data/resume.json`)
- PDF template: copy `Harshit_Raj_Resume.pdf` → `src/main/resources/pdf/resume-template.pdf`
- Add JD tech terms: `JdKeywordService.TECH_DICTIONARY`

## Future ideas

- LLM summary rewrite (OpenAI API)
- Import PDF upload
- Experience date editor in UI
- Side-by-side PDF preview
