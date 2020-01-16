package sqs.pageobjects.pages.web;

import sqs.core.actions.CommonActions;

public class CyclosPaymentReviewPage extends CommonActions {


	public void assertPaymentReview() {
		action.assertTextDisplayed("Payment review");
	}

	public void clickPaymentToUserConfirm() {
		action.click("PaymentToUser_Confirm");
	}

	public void assertPaymentSuccessMessage(String message) {
		action.assertTextDisplayed(message);
	}


}
