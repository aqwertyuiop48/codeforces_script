package com.ims.services;

import com.ims.models.Agent;
import com.ims.models.Issue;
import com.ims.exceptions.AgentNotFoundException;

import java.util.*;

public class AgentService {
    private Map<String, Agent> agents = new HashMap<>();

    // Add Agent
    public void addAgent(String id, String name, Set<com.ims.models.IssueType> specializations) {
        Agent agent = new Agent(id, name, specializations);
        agents.put(id, agent);
        System.out.println("Added " + agent);
        System.out.println("Agent:" + id + "," + name + "," + specializations);
        System.out.println("\n");
    }

    // View Agent Work History
    public void viewAgentWorkHistory(String agentId) throws AgentNotFoundException {
        if (!agents.containsKey(agentId)) throw new AgentNotFoundException("Agent not found: " + agentId);
        Agent agent = agents.get(agentId);
        System.out.println("Work history by agent ID for " + agent.getId() + ":");
        System.out.println("Work history by agent name for " + agent.getName() + ":");
        for (Issue issue : agent.getWorkHistory()) {
            System.out.println(issue);
        }
        System.out.println(getAllAgents());
        System.out.println("\n");
    }

    public Agent getAgent(String agentId) throws AgentNotFoundException {
        if (!agents.containsKey(agentId)) throw new AgentNotFoundException("Agent not found: " + agentId);
        return agents.get(agentId);
    }

    public Collection<Agent> getAllAgents() {
        return agents.values();
    }
}
