package com.example.springai.messaging;

import reactor.core.publisher.Mono;

/** Hands a workflow job to whoever executes it: this JVM (default) or a separate worker via RabbitMQ. */
public interface JobDispatcher {
    Mono<Void> dispatch(String runId, String question);
}
