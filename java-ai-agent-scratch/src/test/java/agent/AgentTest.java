package agent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class AgentTest {
    private Agent newAgent(LlmClient llm, int steps) {
        return new Agent(llm, List.of(new CalculatorTool(), new TimeTool(), new NotesTool()), steps, false);
    }

    @Test void calculatorPrecedenceAndPower() {
        assertEquals(79.0, CalculatorTool.eval("12 * (3 + 4) - 5"));
        assertEquals(512.0, CalculatorTool.eval("2^3^2"));
        assertEquals(-3.0, CalculatorTool.eval("-1-2"));
    }

    @Test void calculatorRejectsGarbage() {
        assertThrows(RuntimeException.class, () -> CalculatorTool.eval("2 + * 3"));
        assertThrows(ArithmeticException.class, () -> CalculatorTool.eval("1/0"));
    }

    @Test void agentUsesCalculatorTool() {
        assertTrue(newAgent(new MockLlmClient(), 5).run("What is 12 * (3 + 4) - 5?").contains("79"));
    }

    @Test void agentRemembersAcrossGoals() {
        Agent a = newAgent(new MockLlmClient(), 5);
        a.run("Remember city is Paris");
        assertTrue(a.run("Recall city").contains("Paris"));
    }

    @Test void agentStopsAtMaxStepsIfLlmLoops() {
        LlmClient looping = (sys, h) -> "{\"action\":\"current_time\",\"input\":{}}";
        assertTrue(newAgent(looping, 3).run("loop forever").startsWith("Stopped"));
    }

    @Test void agentRecoversFromBadJsonAndUnknownTool() {
        int[] n = {0};
        LlmClient flaky = (sys, h) -> switch (n[0]++) {
            case 0 -> "not json at all";
            case 1 -> "{\"action\":\"teleport\",\"input\":{}}";
            default -> "```json\n{\"final\":\"ok\"}\n```";
        };
        assertEquals("ok", newAgent(flaky, 6).run("x"));
    }
}
