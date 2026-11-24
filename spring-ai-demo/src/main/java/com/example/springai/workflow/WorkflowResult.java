package com.example.springai.workflow;

import java.util.List;

/** Outcome of the grounded-answer graph; {@code trace} records which nodes ran, for debugging and audits. */
public record WorkflowResult(String question, String answer, boolean grounded, int revisions,
                             List<String> sources, List<String> trace) {}
