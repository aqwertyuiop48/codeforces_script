package com.example.springai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.example.springai.service.ChatReply;
import com.example.springai.store.HistoryStore;
import com.example.springai.store.RunStore;
import com.example.springai.store.WorkflowRun;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import com.example.springai.messaging.ChatEventPublisher;
import com.example.springai.messaging.JobDispatcher;
import com.example.springai.workflow.WorkflowWorker;

/**
 * Integration test with real PostgreSQL, MongoDB, Kafka and RabbitMQ (Testcontainers, no mocks of infrastructure).
 * Needs Docker, so it is skipped automatically when Docker is unavailable. Run with: mvn verify
 */
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(properties = {"ai.providers.openai.api-key=", "ai.providers.gemini.api-key="})
@ActiveProfiles("full")
class FullStackIT {

    @Container @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7");
    @Container @ServiceConnection
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.13-management");
    @Container @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void r2dbc(DynamicPropertyRegistry r) {
        r.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + postgres.getHost() + ":"
                + postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT) + "/" + postgres.getDatabaseName());
        r.add("spring.r2dbc.username", postgres::getUsername);
        r.add("spring.r2dbc.password", postgres::getPassword);
    }

    @Autowired ChatEventPublisher events;
    @Autowired HistoryStore history;
    @Autowired RunStore runs;
    @Autowired JobDispatcher dispatcher;

    @Test
    void kafkaEventIsConsumedAndStoredInPostgres() {
        events.publish(ChatReply.ok("gemini", "m", "hello", 12)).block();

        await().atMost(Duration.ofSeconds(30)).untilAsserted(() ->
                assertThat(history.recent(10).collectList().block())
                        .extracting(ChatReply::answer).contains("hello"));
    }

    @Test
    void rabbitJobIsProcessedByWorkerAndStoredInMongo() {
        // no API key configured, so the workflow abstains (nothing retrieved) but the full pipeline still runs
        WorkflowRun queued = WorkflowRun.queued("job-1", "who won the world cup");
        runs.save(queued).block();
        dispatcher.dispatch("job-1", "who won the world cup").block();

        await().atMost(Duration.ofSeconds(30)).untilAsserted(() -> {
            WorkflowRun run = runs.find("job-1").block();
            assertThat(run.status()).isEqualTo("DONE");
            assertThat(run.result().grounded()).isFalse();
        });
    }
}
