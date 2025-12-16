package com.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@Slf4j
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/com/example",
        glue = "com.example",
        plugin = {
                "pretty",
                "json:target/cucumber-reports/Cucumber.json",
                "html:target/cucumber-reports/html-report.html",
                "junit:target/cucumber-reports/Cucumber.xml"
        },
        monochrome = true
        //,tags = "@SmokeTest or @RegressionTest"
)
public class RunCucumberTest {
}
