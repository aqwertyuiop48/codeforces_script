package com.example.springai.workflow;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.workflow")
public record WorkflowProperties(String provider, String model, int maxRevisions, int topK) {}
