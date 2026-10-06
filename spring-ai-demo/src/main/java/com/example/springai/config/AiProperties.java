package com.example.springai.config;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai")
public record AiProperties(Duration requestTimeout, String defaultPrompt, Map<String, Provider> providers) {

    public record Provider(String apiKey, String baseUrl, String completionsPath, List<String> models) {
        public boolean hasKey() {
            return apiKey != null && !apiKey.isBlank();
        }
    }
}
