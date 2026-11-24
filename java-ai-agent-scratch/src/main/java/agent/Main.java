package agent;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String key = System.getenv("ANTHROPIC_API_KEY");
        String model = System.getenv().getOrDefault("ANTHROPIC_MODEL", "claude-sonnet-5-5");
        LlmClient llm = (key == null || key.isBlank()) ? new MockLlmClient() : new AnthropicLlmClient(key, model);
        System.out.println("LLM backend: " + (llm instanceof MockLlmClient ? "mock (offline)" : model));

        Agent agent = new Agent(llm, List.of(new CalculatorTool(), new TimeTool(), new NotesTool()), 8, true);

        if (args.length > 0 && args[0].equals("--demo")) {
            String[] goals = {
                "What is 12 * (3 + 4) - 5?",
                "Remember city is Paris",
                "Recall city",
                "What time is it?"
            };
            for (String goal : goals) {
                System.out.println("\n> " + goal);
                System.out.println("Answer: " + agent.run(goal));
            }
            return;
        }

        System.out.println("Type a goal (empty line to quit).");
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("\n> ");
            if (!in.hasNextLine()) break;
            String goal = in.nextLine().trim();
            if (goal.isEmpty()) break;
            System.out.println("Answer: " + agent.run(goal));
        }
    }
}
