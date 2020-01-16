package sqs.cucumber.stepdefinitions.cyclosapp;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.CucumberException;
import sqs.framework.FrameworkBase;

/**
 * @ScriptName : Utilities
 * @Description : This class contains Commonly used Keyword for Mobile/Web
 *              application automation using reports base
 * @Author : Srirangan
 * @Creation Date : 23 May 2016 @Modified Date:
 */
public class CyclosAppSteps extends FrameworkBase {
	String login="Login";

	@Given("^As an authenticated user '(.*)' with my valid credential '(.*)' I am in 'Payment to user' page in app$")
	public void navigateToPaymentPage(String user, String password)  {
		action.enterText("https://demo.cyclos.org/", "CyclosURL");
		action.dismissKeyBoard();
		action.click("Submit");
		action.waitForElementToDisplay(login);
		action.enterText(user, "LoginName");
		action.enterText(password, "Password");
		action.dismissKeyBoard();
		action.click(login);
		action.click("Payment");
		System.out.println("after paymnet client ");
		action.click("ToUser");
		System.out.println("after to user");
	}


	@When("I pay '(.*)' Dollar to '(.*)' and confirm the payment in app$")
	public void paymentToUser(String amount, String paymentUser)  {
		action.enterText(paymentUser, "Keywords");
		action.click("FIND");
		action.click(paymentUser);
		action.waitForElementToDisplay("Amount in Units");
		action.enterText(amount, "Amount in Units");
		action.dismissKeyBoard();
		action.click("MAKE PAYMENT");
		action.click("CONFIRM");
	}


	@Then("^I should see 'The payment was successful' message in app$")
	public void validatingSuccessMessage()   {
		action.isElementExists("The payment was successful");
		logoutApplication();
	}

	public void logoutApplication() {
		try {
			action.click("Mobilelogout");
			action.click("YES");
			action.waitForElementToDisplay("Login");

		} catch (Exception exception) {
			throw new CucumberException(exception.getMessage());
		}
	}
}

	