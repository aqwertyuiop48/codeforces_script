package com.ims.services;

import com.ims.models.*;
import com.ims.exceptions.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class IssueService {
    private Map<String, Queue<Issue>> issueQueueByType = new HashMap<>();
    private Map<String, Issue> issues = new HashMap<>();

    public IssueService() {
        for (IssueType type : IssueType.values()) {
            issueQueueByType.put(type.name(), new LinkedList<>());
        }
    }

    // Add Issue
    public void addIssue(String id, String customerEmail, String description, IssueType type) {
        Issue issue = new Issue(id, customerEmail, description, type);

        issues.put(id, issue);
        issueQueueByType.get(type.name()).add(issue);
        System.out.println("Added " + issue);
        System.out.println("\n");
        getIssue();
    }

    public void getIssue(){
        System.out.println("issueQueueByType:" + issueQueueByType);
        System.out.println("issueQueueByType:" + issueQueueByType.get("PAYMENT_RELATED"));
    }

    // Assign Issue
    public void assignIssueToAgent(String issueId, Agent agent) throws IssueNotFoundException, InvalidOperationException {
        if (!issues.containsKey(issueId)) throw new IssueNotFoundException("Issue not found: " + issueId);
//        Logger.getLogger("Logger");
//        Logger logger = new Logger();
        Issue issue = issues.get(issueId);
        System.out.println("agent.getSpecializations():" + agent.getSpecializations());
        System.out.println("issue.getType():" + issue.getType());
        if (!agent.getSpecializations().contains(issue.getType())) {
            System.out.println("agent.getSpecializations():" + agent.getSpecializations());
            System.out.println("issue.getType():" + issue.getType());
            throw new InvalidOperationException("Agent " + agent.getName() + " cannot handle issue type " + issue.getType());
        }
        if (issue.getStatus() != IssueStatus.OPEN) {
            throw new InvalidOperationException("Issue " + issueId + " is not OPEN");
        }
        issue.assignAgent(agent);
        issue.setStatus(IssueStatus.IN_PROGRESS);
        issueQueueByType.get(issue.getType().name()).remove(issue);
        System.out.println("Assigned " + issue + " to agent " + agent.getName());
    }

    // Update Status
    public void updateIssueStatus(String issueId, IssueStatus status) throws IssueNotFoundException {
        if (!issues.containsKey(issueId)) throw new IssueNotFoundException("Issue not found: " + issueId);
        Issue issue = issues.get(issueId);
        issue.setStatus(status);
        System.out.println("Updated " + issue);
    }

    // Resolve Issue
    public void markIssueResolved(String issueId) throws IssueNotFoundException {
        if (!issues.containsKey(issueId)) throw new IssueNotFoundException("Issue not found: " + issueId);
        Issue issue = issues.get(issueId);
        issue.markResolved();
        System.out.println("Resolved " + issue);
        System.out.println("\n");
    }

    // Fetch Issues by Status
    public void fetchIssuesByStatus(IssueStatus status) {
        List<Issue> filtered = issues.values().stream().filter(i -> i.getStatus() == status).collect(Collectors.toList());
        System.out.println("Issues with status " + status + ":");
        System.out.println("\n");
        for (Issue issue : filtered) System.out.println(issue);

    }

    // Assign next issue from queue to agent
    public void assignNextIssueToAgent(Agent agent) {
        for (IssueType type : agent.getSpecializations()) {
            Queue<Issue> queue = issueQueueByType.get(type.name());
            if (!queue.isEmpty()) {
                Issue issue = queue.poll();
                try {
                    System.out.println("issue.getId():" + issue.getId());
                    assignIssueToAgent(issue.getId(), agent);
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
