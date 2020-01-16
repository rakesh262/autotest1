package sqs.cucumber.stepdefinitions.cyclosweb;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.CucumberException;
import sqs.core.utils.TestDataHandler;
import sqs.framework.FrameworkBase;
import io.qameta.allure.Step;


import static sqs.core.utils.TestDataHandler.getData;
import static sqs.pageobjects.PageFactory.*;

/**
 * @ScriptName : Utilities
 * @Description : This class contains Commonly used Keyword for Mobile/Web
 * application automation using reports base
 * @Author : Srirangan
 * @Creation Date : 23 May 2016 @Modified Date:
 */
public class CyclosWebSteps extends FrameworkBase {

    @Given("^As an authenticated user '(.*)' with my valid credential '(.*)' I am in HomePage$")
    public void navigatingHomepage(String user, String password) {
        action.openApplication();
        cyclosLoginPage.clickSignIn();
        cyclosLoginPage.enterUsername(user);
        cyclosLoginPage.enterPassword(password);
        cyclosLoginPage.clickSignInButton();
        cyclosHomePage.assertWelcomePage();
    }

    @Given("^As an authenticated user '(.*)' with my valid credentials '(.*)' and I am in HomePage$")
    public void navigatingHomepageCSV(String user, String password) {
        action.click("Sign in Link");
        action.enterText(user, "UserName");
        action.enterText(password, "Password");
        action.click("Sign in Button");
        action.assertTextDisplayed("Welcome to the Cyclos4 Demo");
    }

    @Given("^As an authenticated user '(.*)' with my valid credential '(.*)' I am in 'Payment to user' page$")
    public void navigateToPaymentPage(String user, String password) {
        action.openApplication();
        cyclosLoginPage.clickSignIn();
        cyclosLoginPage.enterUsername(user);
        cyclosLoginPage.enterPassword(password);
        cyclosLoginPage.clickSignInButton();
        cyclosHomePage.assertWelcomePage();
        cyclosHomePage.clickHomeBanking();
        cyclosBankingPage.assertMemberAccountDisplayed();
        cyclosBankingPage.clickPaymentToUserLink();
    }


    @When("I pay '(.*)' Dollar to '(.*)' and confirm the payment$")
    public void paymentToUser(String amount, String user) {
        cyclosPaymentToUserPage.assertPaymentToUser();
        cyclosPaymentToUserPage.enterPaymentToUser(user, "PaymentToUser_User");
        cyclosPaymentToUserPage.enterAmount(amount, "PaymentToUser_Amount");
        cyclosPaymentToUserPage.clickSubmit();
        cyclosPaymentReviewPage.assertPaymentReview();
        cyclosPaymentReviewPage.clickPaymentToUserConfirm();
    }

    @Then("^I should see '(.*)' message$")
    public void validatingSuccessMessage(String message) {
        cyclosPaymentReviewPage.assertPaymentSuccessMessage(message);
        logoutApplication();
    }


    public void logoutApplication() {
        try {
            action.click("Logout");
            action.assertTextDisplayed("Cyclos 4 Demo");
        } catch (Exception e) {
            throw new CucumberException(e.getMessage());
        }
    }


    @Given("As a authenticated Cyclos user check we need to login")
    public void as_a_authenticated_Cyclos_user_check_we_need_to_login() {
        action.openApplication();
        cyclosLoginPage.clickSignIn();
        cyclosLoginPage.enterUsername(getData("MyUserID"));
        cyclosLoginPage.enterPassword(getData("MyPassword"));
        cyclosLoginPage.clickSignInButton();
    }

    @When("Logged in check whether we able to see {string}")
    public void logged_in_check_whether_we_able_to_see(String string) {
        cyclosHomePage.assertWelcomePage();
    }

    @Then("User able to click on banking")
    public void user_able_to_click_on_banking() {
        action.assertTextDisplayed("Welcome to the Cyclos4 Demo");
        cyclosHomePage.clickHomeBanking();
        cyclosBankingPage.assertMemberAccountDisplayed();

    }


}

	