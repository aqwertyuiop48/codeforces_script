package com.example.springai.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springai.config.AiProperties;
import com.example.springai.config.AiProperties.Provider;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ChatServiceTest {

    private static final AiProperties PROPS = new AiProperties(
            Duration.ofMillis(200), "how are you",
            Map.of(
                    "openai", new Provider("k", "http://x", "/c", List.of("gpt-a", "boom", "slow")),
                    "gemini", new Provider("k", "http://y", "/c", List.of("gem-a")),
                    "nokey", new Provider("", "http://z", "/c", List.of("m"))));

    /** Fake gateway: echoes the model name, fails for "boom", hangs for "slow". */
    static class FakeGateway implements LlmGateway {
        final AtomicReference<String> lastUserPrompt = new AtomicReference<>();

        @Override
        public boolean isAvailable(String provider) {
            return !provider.equals("nokey");
        }

        @Override
        public Mono<String> complete(String provider, String model, String system, String user) {
            lastUserPrompt.set(user);
            return switch (model) {
                case "boom" -> Mono.error(new IllegalStateException("upstream 500"));
                case "slow" -> Mono.never();
                default -> Mono.just("hello from " + model);
            };
        }

        @Override
        public Flux<String> stream(String provider, String model, String system, String user) {
            return Flux.just("I am ", "fine");
        }
    }

    private final FakeGateway gateway = new FakeGateway();
    private final ChatService service = new ChatService(gateway, PROPS);

    @Test
    void chatReturnsAnswerAndRendersTemplate() {
        StepVerifier.create(service.chat("openai", "gpt-a", "how are you"))
                .assertNext(r -> {
                    assertThat(r.answer()).isEqualTo("hello from gpt-a");
                    assertThat(r.error()).isNull();
                })
                .verifyComplete();
        assertThat(gateway.lastUserPrompt.get()).contains("Question: how are you").contains("40 words");
    }

    @Test
    void chatReportsMissingKeyWithoutCallingGateway() {
        StepVerifier.create(service.chat("nokey", "m", "hi"))
                .assertNext(r -> assertThat(r.error()).contains("missing API key"))
                .verifyComplete();
        assertThat(gateway.lastUserPrompt.get()).isNull();
    }

    @Test
    void chatConvertsUpstreamFailureIntoErrorReply() {
        StepVerifier.create(service.chat("openai", "boom", "hi"))
                .assertNext(r -> assertThat(r.error()).contains("IllegalStateException").contains("upstream 500"))
                .verifyComplete();
    }

    @Test
    void chatTimesOutSlowModels() {
        StepVerifier.create(service.chat("openai", "slow", "hi"))
                .assertNext(r -> assertThat(r.error()).contains("Timeout"))
                .verifyComplete();
    }

    @Test
    void chatAllCoversEveryConfiguredModel() {
        StepVerifier.create(service.chatAll("how are you").collectList())
                .assertNext(list -> assertThat(list)
                        .extracting(ChatReply::model)
                        .containsExactlyInAnyOrder("gpt-a", "boom", "slow", "gem-a", "m"))
                .verifyComplete();
    }

    @Test
    void modelsFlagsUnconfiguredProviders() {
        assertThat(service.models()).contains(
                new ChatService.ModelRef("openai", "gpt-a", true),
                new ChatService.ModelRef("nokey", "m", false));
    }

    @Test
    void streamEmitsChunks() {
        StepVerifier.create(service.stream("gemini", "gem-a", "hi"))
                .expectNext("I am ", "fine")
                .verifyComplete();
    }

    @Test
    void streamFailsForUnconfiguredProvider() {
        StepVerifier.create(service.stream("nokey", "m", "hi"))
                .expectErrorMessage("provider not configured (missing API key)")
                .verify();
    }
}
