# Java AI Agent From Scratch

A tool-using AI agent in plain Java 17 with no agent framework. The only library is Jackson, for JSON.

## How it works

```
goal -> [LLM decides] -> {"action":"calculator","input":{...}} -> run tool -> "Observation: 79"
            ^                                                                    |
            +--------------------------------------------------------------------+
        ...repeat until the LLM returns {"final":"answer"} (or max steps is hit)
```

| File | Role |
|---|---|
| `Agent.java` | The ReAct loop: it parses the LLM's JSON, dispatches tools, feeds observations back, and enforces a step limit. It survives bad JSON and unknown tools. |
| `LlmClient.java` | The interface for the "brain". |
| `MockLlmClient.java` | An offline rule-based planner, so everything runs with no API key. |
| `AnthropicLlmClient.java` | A real Claude client over plain HTTP, used when `ANTHROPIC_API_KEY` is set. |
| `Tool.java` | The tool interface. |
| `CalculatorTool.java`, `TimeTool.java`, `NotesTool.java` | The tools: a safe math parser, the clock, and key/value memory. |

To add a tool, implement `Tool` and pass it to `new Agent(...)` in `Main`.

## Run in GitHub Codespaces

1. Push this repo to GitHub. Choose **Code -> Codespaces -> Create codespace on `java_`**, or whichever branch holds this folder.
2. In the Codespace terminal:

```bash
cd java-ai-agent-scratch
java -version                         # the default image ships JDK 17+ and Maven
mvn -B verify                         # compile and run the 6 unit tests
mvn -q compile exec:java -Dexec.args=--demo   # scripted demo, offline mock LLM
mvn -q compile exec:java              # interactive: type goals such as "What is 2^10 + 5?"
```

   If `mvn` or `java` is missing, run `sdk install java 17.0.12-tem && sdk install maven`.

3. Optionally, use a real LLM. Codespaces secrets are the safest place for the key. Add `ANTHROPIC_API_KEY` under GitHub, then Settings, then Codespaces, then Secrets. You can also export it for one session:

```bash
export ANTHROPIC_API_KEY=sk-ant-...
mvn -q compile exec:java -Dexec.args=--demo
```

   The default model is `claude-sonnet-5-5`. Override it with `ANTHROPIC_MODEL`.

## GitHub Actions

`.github/workflows/java-ai-agent-scratch.yml` runs on any push or PR that touches this folder, and on demand. It:

1. sets up JDK 17 with a Maven cache,
2. runs `mvn verify`,
3. runs the offline demo,
4. runs the demo against real Claude only if the `ANTHROPIC_API_KEY` repo secret exists.

To trigger it by hand, open the **Actions** tab, select `java-ai-agent-scratch`, then **Run workflow**. From a terminal, run `gh workflow run java-ai-agent-scratch.yml`.
