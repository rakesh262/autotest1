package sqs.pageobjects.pages.web;

import sqs.core.actions.CommonActions;

public class CyclosLoginPage extends CommonActions {

	public void enterUsername(String myUserID) {

		action.enterText(myUserID,"UserName");
	}

	public void enterPassword(String password) {
		action.enterText(password, "Password");
	}

	public void clickSignIn() {
		action.click("SignInLink");
	}

	public void clickSignInButton() {
		action.click("SignInButton");
	}
	

}
