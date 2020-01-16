package sqs.pageobjects.pages.web;

import sqs.core.actions.CommonActions;
import sqs.framework.FrameworkBase;


public class CyclosBankingPage extends CommonActions {
	
	public void assertMemberAccountDisplayed() {
		action.assertTextDisplayed("Member account");
	}
	
	public void clickPaymentToUserLink() {
	action.click("Banking_PaymentToUserLink");
	}



}