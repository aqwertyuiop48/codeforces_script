/*
Agent Management:
a. Add Agent: Register agents with their specializations
b. View Agent Work History: View issues handled by an agent
 */
package com.ims.models;

import java.util.*;

public class Agent {
    private String id;
    private String name;
    private Set<IssueType> specializations;
    private List<Issue> workHistory;

    public Agent(String id, String name, Set<IssueType> specializations) {
        this.id = id;
        this.name = name;
        this.specializations = specializations;
        this.workHistory = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Set<IssueType> getSpecializations() { return specializations; }
    public List<Issue> getWorkHistory() { return workHistory; }

    public void addToWorkHistory(Issue issue) {
        workHistory.add(issue);
    }

    @Override
    public String toString() {
        return "Agent{id='" + id + "', name='" + name + "', specializations=" + specializations + "}";
    }
}
