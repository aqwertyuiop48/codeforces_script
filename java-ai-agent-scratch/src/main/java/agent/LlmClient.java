package agent;

import java.util.List;

/** The "brain". Swap implementations without touching the agent loop. */
public interface LlmClient {
    String complete(String systemPrompt, List<Message> history) throws Exception;
}
