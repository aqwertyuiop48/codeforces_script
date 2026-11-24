package agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Offline rule-based "LLM" so the whole loop runs with no API key (also used in CI tests). */
public class MockLlmClient implements LlmClient {
    private static final ObjectMapper J = new ObjectMapper();
    private static final Pattern MATH = Pattern.compile("\\d[\\d\\s+\\-*/().^]*[+\\-*/^][\\d\\s+\\-*/().^]*\\d");
    private static final Pattern REMEMBER = Pattern.compile("remember (\\w+) is (.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern RECALL = Pattern.compile("recall (\\w+)", Pattern.CASE_INSENSITIVE);

    public String complete(String system, List<Message> h) {
        String last = h.get(h.size() - 1).content();
        if (last.startsWith("Observation: ")) return fin("Done. Result: " + last.substring(13));

        Matcher m;
        if ((m = REMEMBER.matcher(last)).find())
            return act("User wants me to store a note", "notes", J.createObjectNode()
                .put("op", "set").put("key", m.group(1)).put("value", m.group(2).trim()));
        if ((m = RECALL.matcher(last)).find())
            return act("Look up a note", "notes", J.createObjectNode().put("op", "get").put("key", m.group(1)));
        if ((m = MATH.matcher(last)).find())
            return act("This is arithmetic", "calculator", J.createObjectNode().put("expression", m.group().trim()));
        if (last.toLowerCase().contains("time"))
            return act("Need the clock", "current_time", J.createObjectNode());
        return fin("Offline mock can't plan that. Set ANTHROPIC_API_KEY to use a real LLM.");
    }

    private static String act(String thought, String tool, ObjectNode input) {
        ObjectNode o = J.createObjectNode().put("thought", thought).put("action", tool);
        o.set("input", input);
        return o.toString();
    }

    private static String fin(String answer) {
        return J.createObjectNode().put("thought", "I have what I need").put("final", answer).toString();
    }
}
