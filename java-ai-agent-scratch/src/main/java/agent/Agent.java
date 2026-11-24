package agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

/**
 * The agent loop (ReAct): ask the LLM what to do -> run the chosen tool ->
 * feed the observation back -> repeat until the LLM returns a final answer.
 */
public class Agent {
    private static final ObjectMapper JSON = new ObjectMapper();
    private final LlmClient llm;
    private final Map<String, Tool> tools = new LinkedHashMap<>();
    private final int maxSteps;
    private final boolean verbose;

    public Agent(LlmClient llm, List<Tool> toolList, int maxSteps, boolean verbose) {
        this.llm = llm;
        toolList.forEach(t -> tools.put(t.name(), t));
        this.maxSteps = maxSteps;
        this.verbose = verbose;
    }

    String systemPrompt() {
        StringBuilder sb = new StringBuilder(
            "You are an autonomous agent. Solve the user's goal step by step using tools.\n"
            + "Reply with ONE JSON object and nothing else, in one of two forms:\n"
            + "  {\"thought\":\"...\",\"action\":\"<tool name>\",\"input\":{...}}   -> to use a tool\n"
            + "  {\"thought\":\"...\",\"final\":\"<answer for the user>\"}        -> when done\n"
            + "After each action you will receive \"Observation: ...\". Never invent observations.\n"
            + "Tools:\n");
        tools.values().forEach(t -> sb.append("- ").append(t.name()).append(": ").append(t.description()).append('\n'));
        return sb.toString();
    }

    public String run(String goal) {
        List<Message> history = new ArrayList<>();
        history.add(new Message("user", goal));
        for (int step = 1; step <= maxSteps; step++) {
            String raw;
            try {
                raw = llm.complete(systemPrompt(), history);
            } catch (Exception e) {
                return "LLM error: " + e.getMessage();
            }
            history.add(new Message("assistant", raw));

            JsonNode d;
            try {
                d = parseJson(raw);
            } catch (Exception e) {
                history.add(new Message("user", "Observation: invalid JSON. Reply with a single JSON object only."));
                continue;
            }
            log("step " + step + " thought: " + d.path("thought").asText());
            if (d.has("final")) return d.get("final").asText();

            String name = d.path("action").asText();
            Tool tool = tools.get(name);
            String obs;
            if (tool == null) {
                obs = "unknown tool '" + name + "'. Available: " + tools.keySet();
            } else {
                try {
                    obs = tool.run(d.path("input"));
                } catch (Exception e) {
                    obs = "tool error: " + e.getMessage();
                }
            }
            log("step " + step + " action: " + name + " " + d.path("input") + " -> " + obs);
            history.add(new Message("user", "Observation: " + obs));
        }
        return "Stopped: reached max steps (" + maxSteps + ") without a final answer.";
    }

    /** LLMs sometimes wrap JSON in prose or code fences; grab the outermost {...}. */
    static JsonNode parseJson(String raw) throws Exception {
        int a = raw.indexOf('{'), b = raw.lastIndexOf('}');
        if (a < 0 || b < a) throw new IllegalArgumentException("no JSON");
        return JSON.readTree(raw.substring(a, b + 1));
    }

    private void log(String s) { if (verbose) System.out.println("  [agent] " + s); }
}
