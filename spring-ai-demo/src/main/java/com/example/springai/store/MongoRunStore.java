package com.example.springai.store;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/** NoSQL store: a workflow run is a nested, schema-flexible document, a natural fit for MongoDB. */
@Repository
@Profile("full")
public class MongoRunStore implements RunStore {
    private static final String COLLECTION = "workflow_runs";

    private final ReactiveMongoTemplate mongo;

    public MongoRunStore(ReactiveMongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public Mono<WorkflowRun> save(WorkflowRun run) {
        return mongo.save(run, COLLECTION);
    }

    @Override
    public Mono<WorkflowRun> find(String id) {
        return mongo.findById(id, WorkflowRun.class, COLLECTION);
    }
}
