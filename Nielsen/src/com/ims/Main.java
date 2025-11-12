package com.ims;

import com.ims.models.*;
import com.ims.services.*;
import com.ims.exceptions.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("""
            ================================================================================
            Step 1: Initialize Services
            """);
        AgentService agentService = new AgentService();
        //IssueService also initializes queues for each issue type to manage pending issues in FIFO order.
        IssueService issueService = new IssueService();

        try {
                //  (SPF : Seller Protection Fund)
                System.out.println("""
            ================================================================================
            Step 2: Add Agents
            """);
                //String csv = args[3];
                //String[] values = csv.split(",");
                //Set<String> hashSet = new HashSet<String>(Arrays.asList(values));
                agentService.addAgent("A1", "Raghu", new HashSet<>(Arrays.asList(IssueType.PAYMENT_RELATED, IssueType.SPF)));
                agentService.addAgent("A2", "Sita", new HashSet<>(Arrays.asList(IssueType.ORDER_PROCESSING)));


                System.out.println("""
                        ================================================================================
                        Step 3: Add Issues, Assign Issue to Agent
                        """);
                issueService.addIssue("IN1", "user@example.com", "Payment not processed", IssueType.PAYMENT_RELATED);
                issueService.addIssue("IN2", "user2@example.com", "Order delayed", IssueType.ORDER_PROCESSING);
                issueService.addIssue("IN3", "user3@example.com", "Onboarding issues", IssueType.ONBOARDING_ISSUES);

                issueService.updateIssueStatus("IN1", IssueStatus.IN_PROGRESS);
                // Step 4: Assign Issue to Agent
                Agent agent1 = agentService.getAgent("A1");
                Agent agent2 = agentService.getAgent("A2");
                //Agent agent3 = agentService.getAgent("A3"); // AgentNotFoundException
                issueService.assignNextIssueToAgent(agent1); // assigning issues to agents in FIFO
                issueService.assignNextIssueToAgent(agent2);
                //issueService.assignNextIssueToAgent(agentService.getAgent("A3")); // AgentNotFoundException

                System.out.println("""
                        ================================================================================
                        Step 4: Fetch Issues by Status:
                        """);
                issueService.fetchIssuesByStatus(IssueStatus.OPEN);

                System.out.println("""
                        ================================================================================
                        Step 5: Update Issue Status
                        """);
                issueService.updateIssueStatus("IN1", IssueStatus.IN_PROGRESS);
                issueService.updateIssueStatus("IN2", IssueStatus.IN_PROGRESS);
                issueService.updateIssueStatus("IN2", IssueStatus.IN_PROGRESS);
                //issueService.updateIssueStatus("IN3", IssueStatus.IN_PROGRESS); // IssueNotFoundException

                System.out.println("""
                        ================================================================================
                        Step 6: Mark Issue as Resolved
                        """);
                issueService.markIssueResolved("IN1");
                issueService.markIssueResolved("IN2");
                //issueService.markIssueResolved("IN3"); // IssueNotFoundException

                System.out.println("""
                        ================================================================================
                        Step 7: View Agent Work History
                        """);
                agentService.viewAgentWorkHistory("A1");
                agentService.viewAgentWorkHistory("A2");

                System.out.println("""
                        ================================================================================
                        Step 8: Fetch Issues by Status
                        """);
                issueService.fetchIssuesByStatus(IssueStatus.OPEN);
                issueService.fetchIssuesByStatus(IssueStatus.IN_PROGRESS);
                issueService.fetchIssuesByStatus(IssueStatus.RESOLVED);
                // Assign issue to agent after the issue is resolved (assignedAgent=None)
                issueService.addIssue("IN3", "user1@example.com", "Order delayed", IssueType.ORDER_PROCESSING);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("\n");
        }
    }
}
