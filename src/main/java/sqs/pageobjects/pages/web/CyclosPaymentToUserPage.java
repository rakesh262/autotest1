package sqs.pageobjects.pages.web;

import sqs.core.actions.CommonActions;

public class CyclosPaymentToUserPage extends CommonActions {

	public void assertPaymentToUser() {
		action.assertTextDisplayed("Payment to user");
	}

	public void enterPaymentToUser(String user, String paymentToUser) {
		action.enterText(user, paymentToUser);
	}

	public void enterAmount(String amount, String paymentToUserAmount) {
		action.enterText(amount, paymentToUserAmount);
	}

	public void clickSubmit() {
		action.click("PaymentToUser_Submit");
	}

	public boolean clickOnVisibleText(String user) {
		return action.clickOnVisibleText(user);
	}

}
