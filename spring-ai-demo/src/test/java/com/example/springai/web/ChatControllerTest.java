package com.example.springai.web;

import static org.mockito.Mockito.when;

import com.example.springai.config.AiProperties;
import com.example.springai.service.ChatReply;
import com.example.springai.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(ChatController.class)
@EnableConfigurationProperties(AiProperties.class)
class ChatControllerTest {

    @Autowired
    WebTestClient client;

    @MockitoBean
    ChatService service;

    @Test
    void chatUsesDefaultPromptWhenMissing() {
        when(service.chat("openai", "gpt-a", "how are you"))
                .thenReturn(Mono.just(ChatReply.ok("openai", "gpt-a", "great!", 5)));

        client.get().uri("/api/chat?provider=openai&model=gpt-a")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.answer").isEqualTo("great!")
                .jsonPath("$.provider").isEqualTo("openai");
    }

    @Test
    void chatAllReturnsNdjson() {
        when(service.chatAll("how are you")).thenReturn(Flux.just(
                ChatReply.ok("openai", "gpt-a", "a", 1),
                ChatReply.failed("gemini", "gem-a", "no key", 0)));

        client.get().uri("/api/chat/all")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
                .expectBodyList(ChatReply.class).hasSize(2);
    }

    @Test
    void streamReturnsServerSentEvents() {
        when(service.stream("gemini", "gem-a", "hi")).thenReturn(Flux.just("a", "b"));

        client.get().uri("/api/chat/stream?provider=gemini&model=gem-a&prompt=hi")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class).hasSize(2);
    }
}
