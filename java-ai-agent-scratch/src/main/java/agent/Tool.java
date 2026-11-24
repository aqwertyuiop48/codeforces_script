package agent;

import com.fasterxml.jackson.databind.JsonNode;

/** A capability the agent can invoke. */
public interface Tool {
    String name();
    String description();   // include the expected input JSON shape
    String run(JsonNode input) throws Exception;
}
