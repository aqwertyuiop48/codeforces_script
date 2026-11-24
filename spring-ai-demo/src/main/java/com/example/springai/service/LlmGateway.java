package com.example.springai.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Thin seam over the LLM client so the service layer can be unit tested without network calls. */
public interface LlmGateway {

    boolean isAvailable(String provider);

    Mono<String> complete(String provider, String model, String systemPrompt, String userPrompt);

    Flux<String> stream(String provider, String model, String systemPrompt, String userPrompt);

    /** Multimodal call: a text prompt plus one image (GenAI image understanding). */
    default Mono<String> describeImage(String provider, String model, String prompt, byte[] image, String mimeType) {
        return Mono.error(new UnsupportedOperationException("image input not supported"));
    }

    /** Agent call: the model may invoke the given Spring AI {@code @Tool} objects before answering. */
    default Mono<String> completeWithTools(String provider, String model, String systemPrompt, String userPrompt,
                                           Object... tools) {
        return Mono.error(new UnsupportedOperationException("tool calling not supported"));
    }
}
