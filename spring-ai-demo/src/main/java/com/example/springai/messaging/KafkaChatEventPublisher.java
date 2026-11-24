package com.example.springai.messaging;

import com.example.springai.service.ChatReply;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/** Publishes chat replies to Kafka, keyed by provider so one provider's events stay ordered. */
@Component
@Profile("full")
public class KafkaChatEventPublisher implements ChatEventPublisher {

    public static final String TOPIC = "llm.chat.replies";

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper json;

    public KafkaChatEventPublisher(KafkaTemplate<String, String> kafka, ObjectMapper json) {
        this.kafka = kafka;
        this.json = json;
    }

    @Override
    public Mono<Void> publish(ChatReply reply) {
        try {
            return Mono.fromFuture(kafka.send(TOPIC, reply.provider(), json.writeValueAsString(reply))).then();
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
