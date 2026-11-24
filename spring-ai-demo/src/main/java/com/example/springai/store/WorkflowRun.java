package com.example.springai.store;

import com.example.springai.workflow.WorkflowResult;
import java.time.Instant;
import org.springframework.data.annotation.Id;

/** Persisted state of one asynchronous workflow job (a MongoDB document in the "full" profile). */
public record WorkflowRun(@Id String id, String question, String status, WorkflowResult result, String error,
                          Instant createdAt) {

    public static WorkflowRun queued(String id, String question) {
        return new WorkflowRun(id, question, "QUEUED", null, null, Instant.now());
    }

    public WorkflowRun withStatus(String newStatus) {
        return new WorkflowRun(id, question, newStatus, result, error, createdAt);
    }

    public WorkflowRun done(WorkflowResult r) {
        return new WorkflowRun(id, question, "DONE", r, null, createdAt);
    }

    public WorkflowRun failed(String message) {
        return new WorkflowRun(id, question, "FAILED", null, message, createdAt);
    }
}
