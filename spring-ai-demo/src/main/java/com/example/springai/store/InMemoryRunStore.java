package com.example.springai.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Profile("!full")
public class InMemoryRunStore implements RunStore {
    private final Map<String, WorkflowRun> runs = new ConcurrentHashMap<>();

    @Override
    public Mono<WorkflowRun> save(WorkflowRun run) {
        runs.put(run.id(), run);
        return Mono.just(run);
    }

    @Override
    public Mono<WorkflowRun> find(String id) {
        return Mono.justOrEmpty(runs.get(id));
    }
}
