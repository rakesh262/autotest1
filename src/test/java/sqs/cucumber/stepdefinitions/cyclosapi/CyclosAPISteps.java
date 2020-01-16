package sqs.cucumber.stepdefinitions.cyclosapi;

import cucumber.api.java.en.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import junit.framework.Assert;
import sqs.core.actions.api.APIRequestActions;
import sqs.core.actions.api.APITestRequests;

public class CyclosAPISteps extends APITestRequests{
	
	@Given("send request")
	public void send_request() {
		APIRequestActions.setBasePath("/{owner}/accounts");
		response = getRequest();
	}

	@When("get the response")
	public void get_the_response() {
		logger.info("get the response data");
		response.then().log().body();
	}

	@Then("verify it is a valid response")
	public void verify_it_is_a_valid_response() {
		logger.info("verify response data is valid!");
		/*verify status code*/checkStatusCode(StatusCode.OK);
		///*verify account details*/checkBodyPathUsing("type.id","10");
		
		checkBodyContainsText("7762070814178015807");
		checkBodyContainsText("Member account");
		/*SINCE IF RESPONSE IS IN AN ARRAY FORMAT
		 * NO VALIDATION HAS BEEN CREATED IN THIS FRAMEWORK TO VERIFY ARRAY DATA
		 * A METHOD TO VERIFY ARRAY DATA WILL BE SOON, UNTIL THE FOLLOWING OPTION TO BE USED
		 * */
		JsonPath resp_json = response.then().extract().jsonPath();
		Assert.assertTrue(resp_json.getString("id").contains("7762070814178015807"));
		Assert.assertTrue(resp_json.getString("type.name").contains("Member account"));	
	}

	@Given("send the payment")
	public void send_the_payment() {
		logger.info("send the post request");
		APIRequestActions.setBasePath("/{owner}/payments");
		response = postRequest();
	}

	@When("request is posted")
	public void request_is_posted() {
		logger.info("get response data");
		response.then().log().body();
	}

	@Then("verify the payment details")
	public void verify_the_payment_details() {
		logger.info("validate the payment is valid");
		checkStatusCode(StatusCode.CREATED);
	}
}
