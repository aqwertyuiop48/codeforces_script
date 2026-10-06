package com.example.springai.web;

import com.example.springai.config.AiProperties;
import com.example.springai.service.ChatReply;
import com.example.springai.service.ChatService;
import com.example.springai.service.ChatService.ModelRef;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chat;
    private final String defaultPrompt;

    public ChatController(ChatService chat, AiProperties props) {
        this.chat = chat;
        this.defaultPrompt = props.defaultPrompt() == null ? "how are you" : props.defaultPrompt();
    }

    @GetMapping("/models")
    public List<ModelRef> models() {
        return chat.models();
    }

    @GetMapping("/chat")
    public Mono<ChatReply> chat(@RequestParam String provider,
                                @RequestParam String model,
                                @RequestParam(required = false) String prompt) {
        return chat.chat(provider, model, orDefault(prompt));
    }

    /** Asks every configured model the same question in parallel and emits replies as each one finishes. */
    @GetMapping(value = "/chat/all", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ChatReply> chatAll(@RequestParam(required = false) String prompt) {
        return chat.chatAll(orDefault(prompt));
    }

    /** Token-by-token streaming over Server-Sent Events. */
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String provider,
                               @RequestParam String model,
                               @RequestParam(required = false) String prompt) {
        return chat.stream(provider, model, orDefault(prompt));
    }

    private String orDefault(String prompt) {
        return prompt == null || prompt.isBlank() ? defaultPrompt : prompt;
    }
}
