package com.example.springai.messaging;

import com.example.springai.service.ChatReply;
import com.example.springai.store.HistoryStore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Profile("!full")
public class InProcessChatEventPublisher implements ChatEventPublisher {
    private final HistoryStore history;

    public InProcessChatEventPublisher(HistoryStore history) {
        this.history = history;
    }

    @Override
    public Mono<Void> publish(ChatReply reply) {
        return history.save(reply);
    }
}
