package com.flipcart.search.carina.demo;

import com.zebrunner.carina.cucumber.CucumberBaseTest;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/searchFlipcart.feature",
        glue = "com.flipcart.search.carina.demo.cucumber.steps",
        dryRun = false,
        plugin={"pretty",
                "html:target/cucumber-core-test-report",
                "pretty:target/cucumber-core-test-report.txt",
                "json:target/cucumber-core-test-report.json",
                "junit:target/cucumber-core-test-report.xml"}
)
public class CucumberWebSampleTest extends CucumberBaseTest {
    //do nothing here as everything is declared in "GSMArenaNews.feature" and steps

}
