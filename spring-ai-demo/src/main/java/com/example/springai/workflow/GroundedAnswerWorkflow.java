package com.example.springai.workflow;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

import com.example.springai.kb.KnowledgeBase;
import com.example.springai.kb.KnowledgeBase.Doc;
import com.example.springai.service.LlmGateway;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.state.AgentState;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Agentic, hallucination-resistant answering as a LangGraph state machine:
 *
 * <pre>
 * START -> retrieve --(no docs)--> END            (abstain instead of guessing)
 *             |
 *             v
 *           draft -> verify --(grounded or out of retries)--> END
 *                      ^  |
 *                      |  v (not grounded)
 *                      +- revise
 * </pre>
 *
 * Reliability techniques: grounding on retrieved documents, a "say I do not know" instruction,
 * LLM-as-judge verification returning strict JSON, a bounded self-correction loop, and an abstention fallback.
 */
@Component
public class GroundedAnswerWorkflow {

    static final String DRAFT_SYSTEM = "You answer ONLY from the provided context. "
            + "If the context does not contain the answer, reply exactly: I do not know. "
            + "Cite the document ids in square brackets.";
    static final String VERIFY_SYSTEM = "You are a strict fact checker. Decide whether every claim in the ANSWER "
            + "is supported by the CONTEXT. Reply with JSON only: {\"grounded\": true|false, \"issues\": \"...\"}";
    static final String REVISE_SYSTEM = "Rewrite the ANSWER so that it only contains claims supported by the CONTEXT, "
            + "fixing the listed ISSUES. If nothing is supported, reply exactly: I do not know.";
    static final String ABSTAIN = "I do not know. I could not find a verified answer in the knowledge base.";

    private static final ObjectMapper JSON = new ObjectMapper();

    private final LlmGateway gateway;
    private final KnowledgeBase kb;
    private final WorkflowProperties props;
    private final CompiledGraph<AgentState> graph;

    public GroundedAnswerWorkflow(LlmGateway gateway, KnowledgeBase kb, WorkflowProperties props) {
        this.gateway = gateway;
        this.kb = kb;
        this.props = props;
        this.graph = build();
    }

    public Mono<WorkflowResult> run(String question) {
        if (!gateway.isAvailable(props.provider())) {
            return Mono.just(new WorkflowResult(question, "provider '" + props.provider()
                    + "' not configured (missing API key)", false, 0, List.of(), List.of("skipped")));
        }
        // Graph execution and the LLM calls inside nodes are blocking: keep them off the event loop.
        return Mono.fromCallable(() -> execute(question)).subscribeOn(Schedulers.boundedElastic());
    }

    private WorkflowResult execute(String question) {
        AgentState end = graph.invoke(Map.<String, Object>of("question", question))
                .orElseThrow(() -> new IllegalStateException("workflow produced no state"));
        boolean grounded = end.value("grounded", false);
        String answer = grounded ? end.value("answer", ABSTAIN) : ABSTAIN;
        List<String> sources = grounded ? end.<List<String>>value("sources", List.of()) : List.of();
        return new WorkflowResult(question, answer, grounded, end.value("revisions", 0), sources,
                end.<List<String>>value("trace", List.of()));
    }

    private CompiledGraph<AgentState> build() {
        try {
            StateGraph<AgentState> g = new StateGraph<>(AgentState::new);
            g.addNode("retrieve", node_async(this::retrieve));
            g.addNode("draft", node_async(this::draft));
            g.addNode("verify", node_async(this::verify));
            g.addNode("revise", node_async(this::revise));

            g.addEdge(START, "retrieve");
            g.addConditionalEdges("retrieve",
                    edge_async(s -> s.<List<String>>value("sources", List.of()).isEmpty() ? "none" : "found"),
                    Map.of("none", END, "found", "draft"));
            g.addEdge("draft", "verify");
            g.addConditionalEdges("verify",
                    edge_async(s -> s.value("grounded", false) || s.value("revisions", 0) >= props.maxRevisions()
                            ? "done" : "revise"),
                    Map.of("done", END, "revise", "revise"));
            g.addEdge("revise", "verify");
            return g.compile();
        } catch (GraphStateException e) {
            throw new IllegalStateException("Invalid workflow graph", e);
        }
    }

    // ---- nodes: each returns only the state keys it changes ----

    private Map<String, Object> retrieve(AgentState s) {
        String q = s.value("question", "");
        List<Doc> docs = kb.search(q, props.topK());
        StringBuilder context = new StringBuilder();
        docs.forEach(d -> context.append("[").append(d.id()).append("] ").append(d.text()).append("\n"));
        return Map.of(
                "sources", docs.stream().map(Doc::id).toList(),
                "context", context.toString(),
                "trace", trace(s, "retrieve:" + docs.size()));
    }

    private Map<String, Object> draft(AgentState s) {
        String answer = ask(DRAFT_SYSTEM, "CONTEXT:\n" + s.value("context", "") + "\nQUESTION: " + s.value("question", ""));
        return Map.of("answer", answer, "trace", trace(s, "draft"));
    }

    private Map<String, Object> verify(AgentState s) {
        String reply = ask(VERIFY_SYSTEM, "CONTEXT:\n" + s.value("context", "") + "\nANSWER: " + s.value("answer", ""));
        Verdict v = parseVerdict(reply);
        return Map.of("grounded", v.grounded(), "issues", v.issues(),
                "trace", trace(s, "verify:" + (v.grounded() ? "pass" : "fail")));
    }

    private Map<String, Object> revise(AgentState s) {
        String answer = ask(REVISE_SYSTEM, "CONTEXT:\n" + s.value("context", "") + "\nANSWER: " + s.value("answer", "")
                + "\nISSUES: " + s.value("issues", ""));
        return Map.of("answer", answer, "revisions", s.value("revisions", 0) + 1, "trace", trace(s, "revise"));
    }

    private String ask(String system, String user) {
        return gateway.complete(props.provider(), props.model(), system, user).block();
    }

    private static List<String> trace(AgentState s, String step) {
        List<String> t = new ArrayList<>(s.<List<String>>value("trace", List.of()));
        t.add(step);
        return t;
    }

    record Verdict(boolean grounded, String issues) {}

    /** Tolerates code fences or chatter around the JSON; anything unparseable counts as NOT grounded. */
    static Verdict parseVerdict(String reply) {
        try {
            int a = reply.indexOf('{');
            int b = reply.lastIndexOf('}');
            JsonNode n = JSON.readTree(reply.substring(a, b + 1));
            return new Verdict(n.path("grounded").asBoolean(false), n.path("issues").asText(""));
        } catch (Exception e) {
            return new Verdict(false, "verifier returned unparseable output");
        }
    }
}
