package com.example.springai.messaging;

import com.example.springai.service.ChatReply;
import reactor.core.publisher.Mono;

/**
 * Event-driven seam: every chat reply is published as an event ("llm.chat.replies"). Without the "full" profile the
 * in-process implementation stores it directly; with it, Kafka carries the event and a consumer stores it.
 */
public interface ChatEventPublisher {
    Mono<Void> publish(ChatReply reply);
}
