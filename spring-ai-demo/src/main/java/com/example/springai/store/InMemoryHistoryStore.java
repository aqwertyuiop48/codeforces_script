package com.example.springai.store;

import com.example.springai.service.ChatReply;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Profile("!full")
public class InMemoryHistoryStore implements HistoryStore {
    private final List<ChatReply> replies = new CopyOnWriteArrayList<>();

    @Override
    public Mono<Void> save(ChatReply reply) {
        replies.add(reply);
        return Mono.empty();
    }

    @Override
    public Flux<ChatReply> recent(int limit) {
        List<ChatReply> copy = new ArrayList<>(replies);
        Collections.reverse(copy);
        return Flux.fromIterable(copy).take(limit);
    }
}
