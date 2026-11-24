package com.example.springai.messaging;

import com.example.springai.workflow.WorkflowWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** Worker side of the job queue. Run with APP_ROLE=worker to get a dedicated worker microservice. */
@Component
@Profile("full")
@ConditionalOnExpression("'${app.role:all}' != 'api'")
public class RabbitJobListener {
    private final WorkflowWorker worker;
    private final ObjectMapper json;

    public RabbitJobListener(WorkflowWorker worker, ObjectMapper json) {
        this.worker = worker;
        this.json = json;
    }

    @RabbitListener(queues = RabbitJobDispatcher.QUEUE)
    public void onJob(String payload) throws Exception {
        RabbitJobDispatcher.JobMessage job = json.readValue(payload, RabbitJobDispatcher.JobMessage.class);
        worker.process(job.runId(), job.question()).block();
    }
}
