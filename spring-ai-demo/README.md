# spring-ai-demo

Reactive Spring Boot 3.5 + Spring AI 1.0 + LangGraph4j app: LLM chat across OpenAI and Gemini, a hallucination-resistant
agent workflow, tool calling, image understanding, event-driven persistence and an async job worker.

## What maps to what (interview cheat sheet)

| Topic | Where |
|---|---|
| Java 21, advanced backend | whole project, records, sealed seams (`LlmGateway`), `Mono`/`Flux` |
| Spring Boot + reactive | WebFlux controllers, `Flux.flatMap` fan-out (`ChatService.chatAll`), SSE streaming, R2DBC, reactive Mongo |
| LLM / GenAI integration | `SpringAiLlmGateway` (Spring AI `ChatClient`, one client for OpenAI and Gemini via its OpenAI-compatible API) |
| LangChain / LangGraph | `workflow/GroundedAnswerWorkflow`: a LangGraph4j `StateGraph` (retrieve, draft, verify, revise) |
| AI reliability, fewer hallucinations | grounding on retrieved docs, "I do not know" rule, LLM-as-judge verifier returning JSON, bounded revise loop, abstention fallback, pytest evals in `eval/` |
| Agentic framework / tool calling | `agent/AgentService` + `ShopTools` (`@Tool` functions the model decides to call) |
| GenAI image processing | `vision/VisionService`, `POST /api/vision/describe` (multimodal prompt) |
| Workflow automation | the graph itself, plus async jobs: `WorkflowJobService`, `WorkflowWorker`, run states QUEUED/RUNNING/DONE/FAILED |
| Microservices | one image, two roles: `APP_ROLE=api` and `APP_ROLE=worker` (see `docker-compose.yml`, `k8s/app.yaml`) |
| Messaging: Kafka | chat events `llm.chat.replies`: `KafkaChatEventPublisher`, `ChatEventConsumer` |
| Messaging: RabbitMQ | durable job queue `workflow.jobs`: `RabbitJobDispatcher`, `RabbitJobListener` |
| Event-driven architecture | chat reply is an event; persistence is a decoupled consumer, broker failure never fails the request |
| SQL: PostgreSQL | `PostgresHistoryStore` (R2DBC) |
| NoSQL: MongoDB | `MongoRunStore` (workflow run documents). Cassandra is not included |
| JUnit, Spock, Testcontainers | `src/test/java` (JUnit 5, StepVerifier, WebTestClient), `src/test/groovy` (Spock), `FullStackIT` (real Postgres/Mongo/Kafka/RabbitMQ) |
| PyTest / unittest | `eval/test_hallucination_eval.py` |
| CI/CD | GitHub Actions (`.github/workflows/spring-ai-demo.yml`), `Jenkinsfile`, `.gitlab-ci.yml` |
| Docker, Kubernetes | `Dockerfile`, `docker-compose.yml`, `k8s/app.yaml` |

Not covered: Micronaut, Cassandra, LangChain (Python). LangChain4j would be the Java LangChain equivalent.

## Design in one minute

- Two modes, selected by Spring profile. Default: no infrastructure, in-memory stores and in-process dispatch, so it runs anywhere with just an API key. `full`: PostgreSQL, MongoDB, Kafka and RabbitMQ.
- Interfaces (`LlmGateway`, `HistoryStore`, `RunStore`, `ChatEventPublisher`, `JobDispatcher`) separate business logic from infrastructure, so unit tests need no network and the same code runs in both modes.
- Chat flow: `GET /api/chat` calls the model, publishes a reply event, Kafka delivers it, a consumer writes it to Postgres, `GET /api/history` reads it back.
- Job flow: `POST /api/workflows` stores a QUEUED run in Mongo and puts a message on RabbitMQ, a worker runs the LangGraph workflow and updates the run, `GET /api/workflows/{id}` polls it.

## Run in GitHub Codespaces

Open the repository in a Codespace, then in the terminal:

    cd spring-ai-demo

    # 1. keys: add OPENAI_API_KEY / GEMINI_API_KEY as Codespaces secrets (GitHub Settings > Codespaces > Secrets)
    #    or export them for this terminal only
    export GEMINI_API_KEY=...        # free key from https://aistudio.google.com/apikey
    export OPENAI_API_KEY=...        # optional

    # 2. tests (no keys needed). Docker is available in the Codespace, so Testcontainers ITs run too
    mvn -B test                      # unit tests: JUnit + Spock
    mvn -B verify                    # + integration tests with real Postgres/Mongo/Kafka/RabbitMQ

    # 3a. simple mode (no infrastructure)
    mvn spring-boot:run

    # 3b. full mode: start the infrastructure, then the app with the "full" profile
    docker compose up -d
    mvn spring-boot:run -Dspring-boot.run.profiles=full

    # 3c. everything in containers: api + worker microservices
    docker compose --profile app up -d --build

Codespaces forwards port 8080 automatically; use `localhost:8080` in the terminal or the forwarded URL in a browser.
Set the workflow model with `WORKFLOW_PROVIDER` and `WORKFLOW_MODEL` (defaults: gemini, gemini-3.5-flash).

## Try it

    curl localhost:8080/api/models
    curl "localhost:8080/api/chat?provider=gemini&model=gemini-3.5-flash&prompt=how%20are%20you"
    curl "localhost:8080/api/chat/all"                                   # every model in parallel (NDJSON)
    curl -N "localhost:8080/api/chat/stream?provider=gemini&model=gemini-3.5-flash"   # SSE tokens

    # LangGraph workflow: grounded answer, check "sources" and "trace" in the response
    curl "localhost:8080/api/ask?question=How%20many%20days%20do%20I%20have%20to%20return%20an%20item"
    # out of scope question: should abstain instead of guessing
    curl "localhost:8080/api/ask?question=Who%20won%20the%20world%20cup%20in%202018"

    # async job (RabbitMQ worker in full mode)
    curl -X POST localhost:8080/api/workflows -H 'Content-Type: application/json' -d '{"question":"How long is the warranty?"}'
    curl localhost:8080/api/workflows/<id>

    # tool-calling agent
    curl "localhost:8080/api/agent?question=What%20is%20the%20status%20of%20order%20A100%3F"

    # image understanding
    curl -F "file=@photo.png;type=image/png" -F "prompt=What is in this picture?" localhost:8080/api/vision/describe

    # stored chat history (Postgres in full mode)
    curl localhost:8080/api/history

    # reliability evals (needs a key)
    pip install pytest requests && pytest -q eval/

RabbitMQ UI: `localhost:15672` (guest/guest) once `docker compose up -d` is running.

A provider with no key is reported as "not configured"; the app still starts.

## Models

Edit `src/main/resources/application.yml`.

- Gemini free tier (rate limited): gemini-3.8-flash, gemini-3.7-flash, gemini-3.6-flash, gemini-3.5-flash, gemini-3.5-flash-lite, gemini-3.1-pro.
- OpenAI has no permanent free API tier. The configured models (gpt-4o-mini, gpt-4.1-nano, gpt-5-nano) are the cheapest and work with trial or free credits. Without credit you get a 429/insufficient_quota error in the reply.
- Tool calling and image input need a model that supports them; the Gemini flash models do.

## Docker and Kubernetes

    docker build -t spring-ai-demo .
    docker run -p 8080:8080 -e GEMINI_API_KEY spring-ai-demo

Kubernetes (kind works in a Codespace): see the header of `k8s/app.yaml`.

## CI/CD

`.github/workflows/spring-ai-demo.yml` (repo root) is manually triggered (`workflow_dispatch`; the push/PR triggers are commented out). Jobs: unit tests, Testcontainers integration tests, run the app and call the API, build and smoke test the Docker image, push to GHCR on `main`.
Add repository secrets `OPENAI_API_KEY` and `GEMINI_API_KEY` (Settings > Secrets and variables > Actions).
`Jenkinsfile` and `.gitlab-ci.yml` give the same pipeline for Jenkins and GitLab.
