package com.example.springai.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springai.kb.KnowledgeBase;
import com.example.springai.kb.KnowledgeBase.Doc;
import com.example.springai.service.LlmGateway;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class GroundedAnswerWorkflowTest {

    private static final KnowledgeBase KB = new KnowledgeBase(List.of(
            new Doc("shipping", "Standard shipping takes 3 to 5 business days."),
            new Doc("warranty", "Electronics carry a 12 month warranty.")));

    /** Scripted LLM: verifier verdicts are popped from a queue; drafts and revisions are fixed strings. */
    static class ScriptedGateway implements LlmGateway {
        final Deque<String> verdicts = new ArrayDeque<>();
        final List<String> calls = new ArrayList<>();

        @Override public boolean isAvailable(String provider) { return true; }

        @Override
        public Mono<String> complete(String provider, String model, String system, String user) {
            if (system.equals(GroundedAnswerWorkflow.DRAFT_SYSTEM)) {
                calls.add("draft");
                return Mono.just("Shipping takes 3 to 5 business days [shipping]");
            }
            if (system.equals(GroundedAnswerWorkflow.VERIFY_SYSTEM)) {
                calls.add("verify");
                return Mono.just(verdicts.pop());
            }
            calls.add("revise");
            return Mono.just("Standard shipping takes 3 to 5 business days [shipping]");
        }

        @Override
        public Flux<String> stream(String provider, String model, String system, String user) {
            return Flux.empty();
        }
    }

    private final ScriptedGateway gateway = new ScriptedGateway();
    private final GroundedAnswerWorkflow workflow =
            new GroundedAnswerWorkflow(gateway, KB, new WorkflowProperties("p", "m", 2, 2));

    @Test
    void groundedOnFirstTryReturnsDraftWithSources() {
        gateway.verdicts.add("{\"grounded\": true, \"issues\": \"\"}");

        StepVerifier.create(workflow.run("how long does shipping take"))
                .assertNext(r -> {
                    assertThat(r.grounded()).isTrue();
                    assertThat(r.revisions()).isZero();
                    assertThat(r.sources()).containsExactly("shipping");
                    assertThat(r.trace()).containsExactly("retrieve:1", "draft", "verify:pass");
                })
                .verifyComplete();
    }

    @Test
    void failedVerificationTriggersRevisionLoop() {
        gateway.verdicts.add("```json\n{\"grounded\": false, \"issues\": \"wrong number of days\"}\n```");
        gateway.verdicts.add("{\"grounded\": true, \"issues\": \"\"}");

        StepVerifier.create(workflow.run("how long does shipping take"))
                .assertNext(r -> {
                    assertThat(r.grounded()).isTrue();
                    assertThat(r.revisions()).isEqualTo(1);
                    assertThat(r.trace())
                            .containsExactly("retrieve:1", "draft", "verify:fail", "revise", "verify:pass");
                })
                .verifyComplete();
    }

    @Test
    void abstainsWhenNeverGroundedAfterMaxRevisions() {
        for (int i = 0; i < 3; i++) {
            gateway.verdicts.add("{\"grounded\": false, \"issues\": \"unsupported\"}");
        }

        StepVerifier.create(workflow.run("how long does shipping take"))
                .assertNext(r -> {
                    assertThat(r.grounded()).isFalse();
                    assertThat(r.answer()).isEqualTo(GroundedAnswerWorkflow.ABSTAIN);
                    assertThat(r.revisions()).isEqualTo(2);
                    assertThat(r.sources()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void unparseableVerdictCountsAsNotGrounded() {
        assertThat(GroundedAnswerWorkflow.parseVerdict("sure, looks fine!").grounded()).isFalse();
        assertThat(GroundedAnswerWorkflow.parseVerdict("{\"grounded\": true}").grounded()).isTrue();
    }

    @Test
    void abstainsWithoutCallingTheModelWhenNothingIsRetrieved() {
        StepVerifier.create(workflow.run("who won the world cup"))
                .assertNext(r -> {
                    assertThat(r.grounded()).isFalse();
                    assertThat(r.answer()).isEqualTo(GroundedAnswerWorkflow.ABSTAIN);
                    assertThat(r.trace()).containsExactly("retrieve:0");
                })
                .verifyComplete();
        assertThat(gateway.calls).isEmpty();
    }
}
