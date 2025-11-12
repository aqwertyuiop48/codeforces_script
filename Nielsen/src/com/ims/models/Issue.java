/*
Issue management:
- Add Issue : Create new customer issues
- Assign issue to agent : Assign issues to agents based on specialization -> assignAgent (Agent agent)
 */

package com.ims.models;

import java.time.LocalDateTime;

public class Issue {
    private String id;
    private String customerEmail;
    private String description;
    private IssueType type;
    private IssueStatus status;
    private LocalDateTime createdTime;
    private LocalDateTime resolvedTime;
    private Agent assignedAgent;

    public Issue(String id, String customerEmail, String description, IssueType type) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.description = description;
        this.type = type;
        this.status = IssueStatus.OPEN;
        this.createdTime = LocalDateTime.now();
    }

    public String getId() { return id; }
    public IssueType getType() { return type; }
    public IssueStatus getStatus() { return status; }
    public Agent getAssignedAgent() { return assignedAgent; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getResolvedTime() { return resolvedTime; }

    public void setStatus(IssueStatus status) { this.status = status; }

    public void assignAgent(Agent agent) {
        this.assignedAgent = agent;
        agent.addToWorkHistory(this);
    }

    public void markResolved() {
        this.status = IssueStatus.RESOLVED;
        this.resolvedTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Issue{id='" + id + "', type=" + type + ", status=" + status +
                ", assignedAgent=" + (assignedAgent != null ? assignedAgent.getName() : "None") + "}";
    }
}
