- Design and implement an Incident Management System for a customer support platform. The system
will manage agents and their work histories, allowing them to handle various types of issues based on
their specializations.


# Compile and run the program
- cd src && javac com/ims/**/*.java && java com.ims.Main && cd ..


# Steps:

<pre>
- Step 1: Initialize Services
        - Main.java
            -> AgentService(); -> AgentService.java 
            -> IssueService(); -> IssueService.java 

- Step 2: Agent Management
          - AgentService.java 
            -> Add Agents -> addAgent(String id, String name, Set<com.ims.models.IssueType> specializations)

            - Step 7: View Agent Work History
            -> View Agent Work history -> viewAgentWorkHistory(String agentId)

            -> Agent.java
                

- Step 3: Issue Management
        - IssueService.java 
           -> IssueType
           -> add issue -> addIssue(String id, String customerEmail, String description, IssueType type)

            - Step 4: Assign Issue to Agent
           -> assign issue to agent -> assignIssueToAgent(String issueId, Agent agent)

            - Step 5: Update Issue Status
           -> update issue Status -> updateIssueStatus(String issueId, IssueStatus status)
                -> IssueStatus.java (OPEN,IN_PROGRESS,RESOLVE)

            - Step 6: Mark Issue as Resolved
           -> mark Issue as resolved -> markIssueResolved(String issueId)

            - Step 8: Fetch Issues by Status
           -> fetch issue by status -> fetchIssuesByStatus(IssueStatus status)
                -> IssueStatus.java (OPEN,IN_PROGRESS,RESOLVE)


Exceptions:
- IssueNotFoundException (Main.java)
- InvalidOperationException
- AgentNotFoundException (Main.java)

</pre>