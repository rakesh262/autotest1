package sqs.core.actions.api;

import org.testng.Assert;
import io.restassured.response.Response;
import sqs.framework.FrameworkBase;


public class APITestRequests extends FrameworkBase{
	public static Response response;
	
	public enum StatusCode{
		CONTINUE,
		OK, CREATED, ACCEPTED, NO_CONTENT, ALREADY_REPORTED,
		MOVED_PERMANENTLY, FOUND, TEMPORARY_REDIRECT,
		BAD_REQUEST, UNAUTHORIZED, PAYMENT_REQUIRED, FORBIDDEN, 
		NOT_FOUND, METHOD_NOT_ALLOWED, NOT_ACCEPTABLE, CONFLICT,
		UNSUPPORTED_MEDIA_TYPE,
		INTERNAL_SERVER_ERROR, NOT_IMPLEMENTED, BAD_GATEWAY, 
		SERVICE_UNAVAILABLE, GATEWAY_TIMEOUT, BANDWITH_LIMIT_EXCEEDED;
	}
		
	protected Response getRequest(){
		return action.get();
	}
	
	protected Response postRequest(){		
		return action.post();
	}
	
	protected Response putRequest(){		
		return action.put();
	}
	
	protected Response deleteRequest(){		
		return action.delete();
	}
	
	protected void checkStatusCode(int codeAsNum) {
		Assert.assertEquals(response.getStatusCode(),codeAsNum);
	}
	
	protected void checkStatusCode(StatusCode codeStatus) {
		int codeNum = APIStatusCodes.getCodeNum(codeStatus);
		checkStatusCode(codeNum);
		System.out.println("'" +codeStatus.toString() +"'"+ " status is verified");
	}
	
	protected void checkStatusCode(String codeAsString) {
		int codeNum = APIStatusCodes.getCodeNum(codeAsString);
		checkStatusCode(codeNum);
		System.out.println("'" +codeAsString +"'"+ " status is verified");
	}
	
	protected void checkBodyContainsText(String verifyInResponse) {
		String resp_string = response.asString();
		Assert.assertTrue(resp_string.contains(verifyInResponse));
		logger.info("The response contains "+verifyInResponse);
	} 
	
	protected void checkBodyPathUsing(String objPath, int objValue) {
		int resp_value = response.then().extract().path(objPath);
		Assert.assertEquals(resp_value, objValue);
		logger.info("The response " +objPath +" is " +objValue);
	} 
	
	protected void checkBodyPathUsing(String objPath, String objValue) {
		String resp_value = response.then().extract().path(objPath);
		Assert.assertEquals(resp_value, objValue);
		logger.info("The response " +objPath +" is " +objValue);
	} 

}
