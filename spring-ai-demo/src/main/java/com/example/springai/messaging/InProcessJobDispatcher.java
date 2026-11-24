package com.example.springai.messaging;

import com.example.springai.workflow.WorkflowWorker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Profile("!full")
public class InProcessJobDispatcher implements JobDispatcher {
    private final WorkflowWorker worker;

    public InProcessJobDispatcher(WorkflowWorker worker) {
        this.worker = worker;
    }

    @Override
    public Mono<Void> dispatch(String runId, String question) {
        // fire and forget: the caller already returned 202 and polls GET /api/workflows/{id}
        worker.process(runId, question).subscribeOn(Schedulers.boundedElastic()).subscribe();
        return Mono.empty();
    }
}
