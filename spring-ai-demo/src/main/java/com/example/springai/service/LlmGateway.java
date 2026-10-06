package com.example.springai.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Thin seam over the LLM client so the service layer can be unit tested without network calls. */
public interface LlmGateway {

    boolean isAvailable(String provider);

    Mono<String> complete(String provider, String model, String systemPrompt, String userPrompt);

    Flux<String> stream(String provider, String model, String systemPrompt, String userPrompt);
}
