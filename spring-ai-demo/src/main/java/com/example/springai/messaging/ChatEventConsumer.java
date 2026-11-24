package com.example.springai.messaging;

import com.example.springai.service.ChatReply;
import com.example.springai.store.HistoryStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** Kafka consumer that persists chat events into PostgreSQL (event-driven, decoupled from the HTTP request). */
@Component
@Profile("full")
public class ChatEventConsumer {
    private final HistoryStore history;
    private final ObjectMapper json;

    public ChatEventConsumer(HistoryStore history, ObjectMapper json) {
        this.history = history;
        this.json = json;
    }

    @KafkaListener(topics = KafkaChatEventPublisher.TOPIC, groupId = "history-writer")
    public void onEvent(String payload) throws Exception {
        history.save(json.readValue(payload, ChatReply.class)).block();
    }
}
