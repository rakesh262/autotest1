package sqs.core.actions.interfaces;

import io.restassured.response.Response;

public interface APITestActions {

	/***
	 * to read the resource details using http(s) get method
	 * 
	 */
	Response get();
	
	/***
	 * to create|add a resource details using http(s) post method
	 * 
	 */
	Response post();
	
	/***
	 * to update|change resource details using http(s) put method
	 * 
	 */
	Response put();
	
	/***
	 * to update|change resource details using http(s) patch method
	 * 
	 */
	Response patch();
	
	/***
	 * to remove the resource details using http(s) delete method
	 * 
	 */
	Response delete();
}