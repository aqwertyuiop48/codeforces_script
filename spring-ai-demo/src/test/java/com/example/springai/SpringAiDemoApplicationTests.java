package com.example.springai;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springai.config.AiProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** The context must boot with no API keys at all, which is how CI runs the unit tests. */
@SpringBootTest(properties = {"ai.providers.openai.api-key=", "ai.providers.gemini.api-key="})
class SpringAiDemoApplicationTests {

    @Autowired
    AiProperties props;

    @Test
    void contextLoadsAndFreeModelsAreConfigured() {
        assertThat(props.providers()).containsKeys("openai", "gemini");
        assertThat(props.providers().get("gemini").models()).contains("gemini-2.5-flash", "gemini-2.5-flash-lite");
        assertThat(props.providers().get("openai").hasKey()).isFalse();
    }
}
