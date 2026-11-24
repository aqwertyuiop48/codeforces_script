package com.example.springai.store;

import com.example.springai.service.ChatReply;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HistoryStore {
    Mono<Void> save(ChatReply reply);

    Flux<ChatReply> recent(int limit);
}
