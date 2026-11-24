package com.example.springai.store;

import reactor.core.publisher.Mono;

public interface RunStore {
    Mono<WorkflowRun> save(WorkflowRun run);

    Mono<WorkflowRun> find(String id);
}
