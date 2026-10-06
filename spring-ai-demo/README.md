# spring-ai-demo

Reactive Spring Boot 3.5 + Spring AI 1.0 app that asks OpenAI and Gemini models "how are you".

- **Spring AI**: `ChatClient` + `PromptTemplate` + system prompt. Gemini is reached through its OpenAI-compatible endpoint, so one client type serves both providers.
- **Reactive**: WebFlux; `/api/chat/all` fans out to all models in parallel (`Flux.flatMap`), `/api/chat/stream` streams tokens over SSE.
- **Tests**: JUnit 5, Reactor `StepVerifier`, `WebTestClient`. They need no API keys.

## Run

    export OPENAI_API_KEY=...   # optional
    export GEMINI_API_KEY=...   # optional, free key from https://aistudio.google.com/apikey
    mvn spring-boot:run

    curl localhost:8080/api/models
    curl "localhost:8080/api/chat?provider=gemini&model=gemini-2.5-flash&prompt=how%20are%20you"
    curl "localhost:8080/api/chat/all"
    curl -N "localhost:8080/api/chat/stream?provider=gemini&model=gemini-2.5-flash"

A provider with no key is reported as "not configured"; the app still starts.

## Models

Edit `src/main/resources/application.yml`.

- Gemini free tier: gemini-3.8-flash , gemini-3.7-flash , gemini-3.6-flash , gemini-3.5-flash-lite , gemini-3.1-pro (rate limited).
- OpenAI has no permanent free API tier. The configured models (gpt-4o-mini, gpt-4.1-nano, gpt-5-nano) are the cheapest and work with trial or free credits. Without credit you get a 429/insufficient_quota error in the reply.

## Docker

    docker build -t spring-ai-demo .
    docker run -p 8080:8080 -e OPENAI_API_KEY -e GEMINI_API_KEY spring-ai-demo

## GitHub Actions

`.github/workflows/spring-ai-demo.yml` (repo root) runs on changes under `spring-ai-demo/`: unit tests, start the app and call `/api/chat/all`, then build the Docker image, smoke test it, and push to GHCR on `main`.
Add repository secrets `OPENAI_API_KEY` and `GEMINI_API_KEY` (Settings > Secrets and variables > Actions).
