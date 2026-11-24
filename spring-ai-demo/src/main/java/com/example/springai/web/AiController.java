package com.example.springai.web;

import com.example.springai.agent.AgentService;
import com.example.springai.service.ChatReply;
import com.example.springai.store.HistoryStore;
import com.example.springai.store.WorkflowRun;
import com.example.springai.vision.VisionService;
import com.example.springai.workflow.GroundedAnswerWorkflow;
import com.example.springai.workflow.WorkflowJobService;
import com.example.springai.workflow.WorkflowResult;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Endpoints for the advanced GenAI features: grounded workflow, async jobs, tool agent, vision, history. */
@RestController
@RequestMapping("/api")
public class AiController {

    public record AskRequest(String question) {}

    private final GroundedAnswerWorkflow workflow;
    private final WorkflowJobService jobs;
    private final AgentService agent;
    private final VisionService vision;
    private final HistoryStore history;

    public AiController(GroundedAnswerWorkflow workflow, WorkflowJobService jobs, AgentService agent,
                        VisionService vision, HistoryStore history) {
        this.workflow = workflow;
        this.jobs = jobs;
        this.agent = agent;
        this.vision = vision;
        this.history = history;
    }

    /** Synchronous run of the LangGraph workflow: retrieve, draft, verify, revise. */
    @GetMapping("/ask")
    public Mono<WorkflowResult> ask(@RequestParam String question) {
        return workflow.run(question);
    }

    /** Asynchronous variant: returns 202 + job id, the job runs in-process or on a RabbitMQ worker. */
    @PostMapping("/workflows")
    public Mono<ResponseEntity<WorkflowRun>> submit(@RequestBody AskRequest req) {
        if (req.question() == null || req.question().isBlank()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "question is required"));
        }
        return jobs.submit(req.question()).map(run -> ResponseEntity.accepted().body(run));
    }

    @GetMapping("/workflows/{id}")
    public Mono<WorkflowRun> status(@PathVariable String id) {
        return jobs.status(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/agent")
    public Mono<Map<String, String>> agent(@RequestParam String question) {
        return agent.ask(question).map(answer -> Map.of("question", question, "answer", answer));
    }

    @PostMapping(value = "/vision/describe", consumes = "multipart/form-data")
    public Mono<Map<String, String>> describe(@RequestPart("file") FilePart file,
                                              @RequestParam(required = false) String prompt) {
        String type = file.headers().getContentType() == null ? "" : file.headers().getContentType().toString();
        return org.springframework.core.io.buffer.DataBufferUtils.join(file.content()).map(buf -> {
                    byte[] bytes = new byte[buf.readableByteCount()];
                    buf.read(bytes);
                    org.springframework.core.io.buffer.DataBufferUtils.release(buf);
                    return bytes;
                })
                .flatMap(bytes -> vision.describe(bytes, type, prompt))
                .map(text -> Map.of("description", text))
                .onErrorMap(IllegalArgumentException.class,
                        e -> new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @GetMapping("/history")
    public Flux<ChatReply> history(@RequestParam(defaultValue = "20") int limit) {
        return history.recent(Math.min(Math.max(limit, 1), 100));
    }
}
