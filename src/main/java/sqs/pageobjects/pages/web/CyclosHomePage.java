package sqs.pageobjects.pages.web;

import sqs.core.actions.CommonActions;

public class CyclosHomePage extends CommonActions {


	public void assertWelcomePage() {
		action.assertTextDisplayed("Welcome to the Cyclos4 Demo");
	}

	public void clickHomeBanking() {
		action.click("HomeBanking");
	}
	
}
