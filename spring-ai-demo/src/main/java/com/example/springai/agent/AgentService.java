package com.example.springai.agent;

import com.example.springai.service.LlmGateway;
import com.example.springai.workflow.WorkflowProperties;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/** Tool-calling agent: the model decides which {@link ShopTools} to call, and must not invent facts. */
@Service
public class AgentService {

    static final String SYSTEM = "You are a customer support agent. Use the tools to look up order status and "
            + "policies. Never guess: if the tools return NOT FOUND or NO MATCH, say you cannot find it.";

    private final LlmGateway gateway;
    private final ShopTools tools;
    private final WorkflowProperties props;

    public AgentService(LlmGateway gateway, ShopTools tools, WorkflowProperties props) {
        this.gateway = gateway;
        this.tools = tools;
        this.props = props;
    }

    public Mono<String> ask(String question) {
        if (!gateway.isAvailable(props.provider())) {
            return Mono.just("provider '" + props.provider() + "' not configured (missing API key)");
        }
        return gateway.completeWithTools(props.provider(), props.model(), SYSTEM, question, tools);
    }
}
