package com.example.springai.vision;

import com.example.springai.service.LlmGateway;
import com.example.springai.workflow.WorkflowProperties;
import java.util.Set;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/** GenAI image understanding: sends an uploaded image plus a prompt to a vision-capable model. */
@Service
public class VisionService {

    static final long MAX_BYTES = 4 * 1024 * 1024;
    static final Set<String> TYPES = Set.of("image/png", "image/jpeg", "image/webp", "image/gif");

    private final LlmGateway gateway;
    private final WorkflowProperties props;

    public VisionService(LlmGateway gateway, WorkflowProperties props) {
        this.gateway = gateway;
        this.props = props;
    }

    public Mono<String> describe(byte[] image, String mimeType, String prompt) {
        if (!TYPES.contains(mimeType)) {
            return Mono.error(new IllegalArgumentException("unsupported image type: " + mimeType));
        }
        if (image.length == 0 || image.length > MAX_BYTES) {
            return Mono.error(new IllegalArgumentException("image must be between 1 byte and 4 MB"));
        }
        if (!gateway.isAvailable(props.provider())) {
            return Mono.error(new IllegalStateException("provider '" + props.provider() + "' not configured"));
        }
        String p = prompt == null || prompt.isBlank() ? "Describe this image in two sentences." : prompt;
        return gateway.describeImage(props.provider(), props.model(), p, image, mimeType);
    }
}
