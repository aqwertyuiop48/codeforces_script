package com.example.springai.workflow;

import com.example.springai.store.RunStore;
import com.example.springai.store.WorkflowRun;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/** Executes one queued job: RUNNING -> DONE / FAILED, persisting every transition. */
@Component
public class WorkflowWorker {
    private final GroundedAnswerWorkflow workflow;
    private final RunStore runs;

    public WorkflowWorker(GroundedAnswerWorkflow workflow, RunStore runs) {
        this.workflow = workflow;
        this.runs = runs;
    }

    public Mono<Void> process(String runId, String question) {
        return runs.find(runId)
                .switchIfEmpty(Mono.defer(() -> runs.save(WorkflowRun.queued(runId, question))))
                .flatMap(r -> runs.save(r.withStatus("RUNNING")))
                .flatMap(r -> workflow.run(question)
                        .flatMap(res -> runs.save(r.done(res)))
                        .onErrorResume(ex -> runs.save(r.failed(ex.getClass().getSimpleName() + ": " + ex.getMessage()))))
                .then();
    }
}
