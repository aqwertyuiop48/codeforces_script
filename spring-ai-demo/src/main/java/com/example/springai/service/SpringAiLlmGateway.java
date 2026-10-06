package com.example.springai.service;

import com.example.springai.config.AiProperties;
import java.util.HashMap;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * One Spring AI {@link ChatClient} per provider. OpenAI and Gemini (via its OpenAI-compatible endpoint)
 * speak the same chat-completions protocol, so only base URL, path and key differ.
 */
@Component
public class SpringAiLlmGateway implements LlmGateway {

    private final Map<String, ChatClient> clients = new HashMap<>();

    public SpringAiLlmGateway(AiProperties props) {
        props.providers().forEach((name, p) -> {
            if (!p.hasKey()) {
                return;
            }
            OpenAiApi api = OpenAiApi.builder()
                    .baseUrl(p.baseUrl())
                    .apiKey(p.apiKey())
                    .completionsPath(p.completionsPath())
                    .build();
            OpenAiChatModel model = OpenAiChatModel.builder().openAiApi(api).build();
            clients.put(name, ChatClient.create(model));
        });
    }

    @Override
    public boolean isAvailable(String provider) {
        return clients.containsKey(provider);
    }

    @Override
    public Mono<String> complete(String provider, String model, String systemPrompt, String userPrompt) {
        // call() blocks, so keep it off the Netty event loop
        return Mono.fromCallable(() -> request(provider, model, systemPrompt, userPrompt).call().content())
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<String> stream(String provider, String model, String systemPrompt, String userPrompt) {
        return request(provider, model, systemPrompt, userPrompt).stream().content();
    }

    private ChatClient.ChatClientRequestSpec request(String provider, String model, String system, String user) {
        ChatClient client = clients.get(provider);
        if (client == null) {
            throw new IllegalStateException("Provider '" + provider + "' is not configured (missing API key)");
        }
        return client.prompt()
                .system(system)
                .user(user)
                .options(OpenAiChatOptions.builder().model(model).build());
    }
}
