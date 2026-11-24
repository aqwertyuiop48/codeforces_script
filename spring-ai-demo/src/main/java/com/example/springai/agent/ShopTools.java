package com.example.springai.agent;

import com.example.springai.kb.KnowledgeBase;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/** Functions the agent may call (OpenAI-style tool calling). Keep them small, deterministic and side-effect free. */
@Component
public class ShopTools {

    private static final Map<String, String> ORDERS = Map.of(
            "A100", "SHIPPED, arriving in 2 days",
            "A200", "PROCESSING, not yet shipped",
            "A300", "DELIVERED 5 days ago");

    private final KnowledgeBase kb;

    public ShopTools(KnowledgeBase kb) {
        this.kb = kb;
    }

    @Tool(description = "Look up the current status of an order by its id, for example A100")
    public String orderStatus(@ToolParam(description = "the order id") String orderId) {
        return ORDERS.getOrDefault(orderId.trim().toUpperCase(), "NOT FOUND");
    }

    @Tool(description = "Search the company policy knowledge base (returns, shipping, warranty)")
    public String searchPolicies(@ToolParam(description = "search keywords") String query) {
        String hits = kb.search(query, 2).stream()
                .map(d -> "[" + d.id() + "] " + d.text())
                .collect(Collectors.joining("\n"));
        return hits.isEmpty() ? "NO MATCH" : hits;
    }

    @Tool(description = "Get the current UTC time")
    public String currentTime() {
        return Instant.now().toString();
    }
}
