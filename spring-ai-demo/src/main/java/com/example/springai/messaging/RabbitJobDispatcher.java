package com.example.springai.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/** API side of the job queue: publishes a durable message that any worker instance can pick up. */
@Component
@Profile("full")
@ConditionalOnExpression("'${app.role:all}' != 'worker'")
public class RabbitJobDispatcher implements JobDispatcher {

    public static final String QUEUE = "workflow.jobs";

    public record JobMessage(String runId, String question) {}

    @Configuration
    @Profile("full")
    static class QueueConfig {
        @Bean
        Queue workflowJobs() {
            return new Queue(QUEUE, true);
        }
    }

    private final RabbitTemplate rabbit;
    private final ObjectMapper json;

    public RabbitJobDispatcher(RabbitTemplate rabbit, ObjectMapper json) {
        this.rabbit = rabbit;
        this.json = json;
    }

    @Override
    public Mono<Void> dispatch(String runId, String question) {
        return Mono.fromCallable(() -> {
            try {
                rabbit.convertAndSend(QUEUE, json.writeValueAsString(new JobMessage(runId, question)));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
