package com.example.springai.store;

import com.example.springai.service.ChatReply;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** SQL store: append-only chat audit log in PostgreSQL through the reactive R2DBC driver. */
@Repository
@Profile("full")
public class PostgresHistoryStore implements HistoryStore {
    private final DatabaseClient db;

    public PostgresHistoryStore(DatabaseClient db) {
        this.db = db;
    }

    @Override
    public Mono<Void> save(ChatReply r) {
        DatabaseClient.GenericExecuteSpec spec = db.sql(
                        "INSERT INTO chat_history(provider, model, answer, error, latency_ms) VALUES (:p, :m, :a, :e, :l)")
                .bind("p", r.provider()).bind("m", r.model()).bind("l", r.latencyMs());
        spec = r.answer() == null ? spec.bindNull("a", String.class) : spec.bind("a", r.answer());
        spec = r.error() == null ? spec.bindNull("e", String.class) : spec.bind("e", r.error());
        return spec.then();
    }

    @Override
    public Flux<ChatReply> recent(int limit) {
        return db.sql("SELECT provider, model, answer, error, latency_ms FROM chat_history ORDER BY id DESC LIMIT :n")
                .bind("n", limit)
                .map((row, md) -> new ChatReply(row.get("provider", String.class), row.get("model", String.class),
                        row.get("answer", String.class), row.get("error", String.class),
                        row.get("latency_ms", Long.class)))
                .all();
    }
}
