package agent;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Long-term memory: a key/value store the agent can write to and read from. */
public class NotesTool implements Tool {
    private final Map<String, String> store = new ConcurrentHashMap<>();

    public String name() { return "notes"; }
    public String description() {
        return "Memory. Input: {\"op\":\"set\",\"key\":\"k\",\"value\":\"v\"} or {\"op\":\"get\",\"key\":\"k\"}";
    }
    public String run(JsonNode in) {
        String key = in.path("key").asText();
        return switch (in.path("op").asText()) {
            case "set" -> { store.put(key, in.path("value").asText()); yield "saved " + key; }
            case "get" -> store.getOrDefault(key, "no note named " + key);
            default -> "unknown op (use set/get)";
        };
    }
}
