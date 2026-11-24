package com.example.springai.workflow;

import com.example.springai.messaging.JobDispatcher;
import com.example.springai.store.RunStore;
import com.example.springai.store.WorkflowRun;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/** Accepts a workflow request: persists a QUEUED run, dispatches the job, returns immediately. */
@Service
public class WorkflowJobService {
    private final RunStore runs;
    private final JobDispatcher dispatcher;

    public WorkflowJobService(RunStore runs, JobDispatcher dispatcher) {
        this.runs = runs;
        this.dispatcher = dispatcher;
    }

    public Mono<WorkflowRun> submit(String question) {
        WorkflowRun run = WorkflowRun.queued(UUID.randomUUID().toString(), question);
        return runs.save(run).flatMap(saved -> dispatcher.dispatch(saved.id(), question).thenReturn(saved));
    }

    public Mono<WorkflowRun> status(String id) {
        return runs.find(id);
    }
}
