# Document Q&A Service (RAG)

Ask questions about your own documents and get answers **with citations** — or an explicit
"I could not find this", which is the part most RAG demos skip.

Java 17, Spring Boot 3.2, no database, no API key, no model download required to run.
The embedding and language-model providers are swappable through configuration.

For the full design reasoning and interview preparation, read **[PROJECT_DEEP_DIVE.md](PROJECT_DEEP_DIVE.md)**.

---

## Run it

```powershell
cd ai-doc-qa-rag
mvn spring-boot:run
```

Open <http://localhost:8085>.

That is the whole setup. The service indexes everything under `./docs` at startup and runs entirely
offline in its default configuration.

Requirements: JDK 17 and Maven. If `JAVA_HOME` points at an older JDK:

```powershell
$env:JAVA_HOME = "C:\path\to\jdk-17"
mvn spring-boot:run
```

### Point it at your own notes

```powershell
mvn spring-boot:run "-Dspring-boot.run.arguments=--rag.docs-path=../jvm-gc-interview-lab"
```

Supported file types: `.md`, `.txt`, `.java`, `.yml`, `.yaml`, `.json`, `.sql`.

---

## Try it

```powershell
# a question the documents answer
curl -X POST http://localhost:8085/api/ask -H "Content-Type: application/json" `
     -d '{\"question\":\"How does an object get promoted to the old generation?\"}'

# a question they do not answer - the service refuses instead of inventing
curl -X POST http://localhost:8085/api/ask -H "Content-Type: application/json" `
     -d '{\"question\":\"What is the capital of France?\"}'
```

| Endpoint | What it does |
| --- | --- |
| `POST /api/ask` | Answer a question from the indexed documents, with citations and similarity scores |
| `GET /api/search?q=...&topK=5` | Retrieval only — see exactly which passages the answerer was given |
| `POST /api/index` | Re-index the docs folder without a restart |
| `GET /api/documents` | Index statistics and chunk count per document |
| `GET /api/evaluate` | Run the labelled question set in `eval/questions.json` and report accuracy |
| `GET /api/evaluate/sweep` | Score every candidate similarity threshold from one retrieval pass |

---

## Measured results

Against the 16 labelled questions in `eval/questions.json`, over the three documents in `docs/`:

| Metric | Result |
| --- | --- |
| In-scope questions answered from the correct document | **11 / 12** |
| Out-of-scope questions correctly refused | **4 / 4** |
| Similarity floor | 0.14, chosen with `/api/evaluate/sweep` |
| Index | 3 documents, 18 chunks, 23 ms |
| Query latency | 0–3 ms (in-memory, exact search) |

`mvn test` runs this evaluation as a build-failing regression test, so retrieval quality cannot
silently degrade.

---

## Switching providers

The default configuration uses a local TF-IDF embedding and an extractive answerer, so the service
runs with no network access. Nothing else in the pipeline changes when you switch to a real model:

**Local model with Ollama (free, private):**

```powershell
ollama pull nomic-embed-text
ollama pull llama3.2
mvn spring-boot:run "-Dspring-boot.run.arguments=--rag.embedding-provider=ollama --rag.answer-provider=ollama --rag.min-score=0.4"
```

**Hosted model with OpenAI:**

```powershell
$env:OPENAI_API_KEY = "sk-..."
mvn spring-boot:run "-Dspring-boot.run.arguments=--rag.embedding-provider=openai --rag.answer-provider=openai --rag.min-score=0.4"
```

Re-run `GET /api/evaluate/sweep` after switching. Trained embeddings score on a different scale, so
the 0.14 floor does not carry over.

## Configuration

All settings live under `rag.*` in `src/main/resources/application.yml`:

| Property | Default | Purpose |
| --- | --- | --- |
| `docs-path` | `./docs` | Folder to index |
| `chunk-size` | `900` | Target characters per passage |
| `chunk-overlap` | `150` | Characters shared between neighbouring passages |
| `top-k` | `4` | Passages retrieved per question |
| `min-score` | `0.14` | Similarity floor below which the service refuses to answer |
| `embedding-provider` | `local` | `local`, `ollama` or `openai` |
| `answer-provider` | `extractive` | `extractive`, `ollama` or `openai` |

---

## Layout

```
src/main/java/com/harshit/docqa/
  api/RagController.java          REST endpoints
  config/                         properties and provider selection
  rag/
    DocumentLoader.java           read files from a folder
    Chunker.java                  heading-aware splitting with overlap
    Text.java                     shared tokenisation and stemming
    embed/                        EmbeddingClient + local, Ollama, OpenAI
    store/                        VectorStore + in-memory cosine search
    answer/                       Answerer + extractive, LLM, prompt building
    IndexService.java             ingest path
    RagService.java               query path, guardrail, citations
  eval/EvaluationHarness.java     labelled evaluation and threshold sweep
docs/                             the corpus (replaceable)
eval/questions.json               labelled questions
```

## Resume summary

> **Document Q&A Service (RAG)** — Java 17, Spring Boot 3, REST, Docker-ready
>
> - Built a retrieval-augmented generation service that chunks documents, embeds them into a vector
>   index, and answers natural-language questions with citations back to the source passage, keeping
>   embedding and LLM providers swappable (local, Ollama, OpenAI) behind two interfaces.
> - Added a similarity-threshold guardrail that returns an explicit "not found" instead of an
>   unsupported answer, and a labelled evaluation harness that measures it: 11/12 in-scope questions
>   answered from the correct document and 4/4 out-of-scope questions refused, with the threshold
>   chosen by an automated sweep and enforced as a build-failing regression test.
