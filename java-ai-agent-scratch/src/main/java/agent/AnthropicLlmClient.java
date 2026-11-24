package agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/** Real LLM via the Anthropic Messages API (plain HTTP, no SDK). */
public class AnthropicLlmClient implements LlmClient {
    private static final ObjectMapper J = new ObjectMapper();
    private final HttpClient http = HttpClient.newHttpClient();
    private final String apiKey, model;

    public AnthropicLlmClient(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    public String complete(String system, List<Message> history) throws Exception {
        ObjectNode body = J.createObjectNode().put("model", model).put("max_tokens", 1024).put("system", system);
        ArrayNode msgs = body.putArray("messages");
        for (Message m : history) msgs.addObject().put("role", m.role()).put("content", m.content());

        HttpRequest req = HttpRequest.newBuilder(URI.create("https://api.anthropic.com/v1/messages"))
            .header("x-api-key", apiKey)
            .header("anthropic-version", "2023-06-01")
            .header("content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
            .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());

        StringBuilder sb = new StringBuilder();
        for (JsonNode part : J.readTree(res.body()).path("content"))
            if ("text".equals(part.path("type").asText())) sb.append(part.path("text").asText());
        return sb.toString();
    }
}
