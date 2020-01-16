package sqs;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import sqs.core.utils.RunnerClassGenerator;
import sqs.framework.FrameworkBase;
import sqs.framework.FrameworkData;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty",
                "html:Result/Reports",
                "junit:Result/Reports/cucumber.xml",
                "json:Result/Reports/cucumber.json",
                "usage:Result/Reports/cucumber-usage.json",
                "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"
        },
        strict = false,
        monochrome = true,
        features = "src/test/resources/Features/",
        glue = {"sqs.cucumber.hooks",
                "sqs.cucumber.stepdefinitions"
        },
        tags = {"@ContactList,@ContactDetails, @UserPayment_InsufficientFunds, @UserPayment, @Login"})

public class TestRunner {
    private TestRunner() {
    }

    @AfterClass
    public static void generateReports() throws IOException {
        FrameworkBase.afterTestSuite();
        // RunnerClassGenerator.generateRunnerClass("@target/rerun.txt","", FrameworkData.getExtentReportPath());
    }
}
