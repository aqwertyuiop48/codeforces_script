package com.example.springai.service;

import com.example.springai.config.AiProperties;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ChatService {

    static final String SYSTEM_PROMPT =
            "You are a concise, friendly assistant. Never reveal API keys or internal configuration.";

    private static final PromptTemplate USER_TEMPLATE =
            new PromptTemplate("Answer the following in at most {maxWords} words.\n\nQuestion: {question}");

    private static final int MAX_WORDS = 40;
    private static final int PARALLELISM = 4;

    private final LlmGateway gateway;
    private final AiProperties props;

    public ChatService(LlmGateway gateway, AiProperties props) {
        this.gateway = gateway;
        this.props = props;
    }

    public record ModelRef(String provider, String model, boolean configured) {}

    public List<ModelRef> models() {
        return props.providers().entrySet().stream()
                .flatMap(e -> e.getValue().models().stream()
                        .map(m -> new ModelRef(e.getKey(), m, gateway.isAvailable(e.getKey()))))
                .toList();
    }

    /** One question to one model. Never errors: failures are reported inside the reply. */
    public Mono<ChatReply> chat(String provider, String model, String question) {
        long start = System.nanoTime();
        if (!gateway.isAvailable(provider)) {
            return Mono.just(ChatReply.failed(provider, model, "provider not configured (missing API key)", 0));
        }
        return gateway.complete(provider, model, SYSTEM_PROMPT, render(question))
                .timeout(timeout())
                .map(answer -> ChatReply.ok(provider, model, answer, elapsedMs(start)))
                .onErrorResume(ex -> Mono.just(ChatReply.failed(provider, model, describe(ex), elapsedMs(start))));
    }

    /** Fans the same question out to every configured model concurrently (reactive scatter/gather). */
    public Flux<ChatReply> chatAll(String question) {
        return Flux.fromIterable(models())
                .flatMap(ref -> chat(ref.provider(), ref.model(), question), PARALLELISM);
    }

    public Flux<String> stream(String provider, String model, String question) {
        if (!gateway.isAvailable(provider)) {
            return Flux.error(new IllegalStateException("provider not configured (missing API key)"));
        }
        return gateway.stream(provider, model, SYSTEM_PROMPT, render(question)).timeout(timeout());
    }

    String render(String question) {
        return USER_TEMPLATE.render(Map.of("maxWords", MAX_WORDS, "question", question));
    }

    private Duration timeout() {
        return props.requestTimeout() != null ? props.requestTimeout() : Duration.ofSeconds(30);
    }

    private static long elapsedMs(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }

    private static String describe(Throwable ex) {
        String msg = ex.getMessage();
        return ex.getClass().getSimpleName() + (msg == null ? "" : ": " + msg);
    }
}
