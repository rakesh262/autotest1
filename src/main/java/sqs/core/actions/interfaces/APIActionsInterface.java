package sqs.core.actions.interfaces;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.http.*;

public interface APIActionsInterface {

	/***
	 * http-get =>  read the requested-resource details
	 *
	 * get method with no parameters
	 * parameters will be defined later
	 * @return http(s) response(xml|json) for get http method
	 * 			request-pass status-line: HTTP/1.1 200 OK
	 * 			request-fail status-line: HTTP/1.1 404 Not Found
	 */
	Response get();
	
	/***
	 * http-get =>  read the requested-resource details
	 *
	 * get no parameters
	 * @param headers for the http request
	 * @param body of the http request
	 * @return http(s) response(xml|json) for get http method
	 * 			request-pass status-line: HTTP/1.1 200 OK
	 * 			request-fail status-line: HTTP/1.1 404 Not Found
	 */
	Response get(Headers headers, ResponseBody body);

	/***
	 * http-post => create|add resource
	 *
	 * post no parameters
	 * parameters will be defined later
	 * @return http(s) response(xml|json) for post http method
	 * 			request-pass status-line: HTTP/1.1 201 Created
	 * 			request-fail status-line: HTTP/1.1 405 Method Not Allowed
	 */
	Response post();

	/***
	 * http-post => create|add resource
	 *
	 * @param headers for the http request
	 * @param body of the http request
	 * @return http(s) response(xml|json) for post http method
	 * 			request-pass status-line: HTTP/1.1 201 Created
	 * 			request-fail status-line: HTTP/1.1 405 Method Not Allowed
	 */
	Response post(Headers headers, ResponseBody body);

	/***
	 * http-put => update|replace resource attribute
	 *
	 * post no parameters
	 * parameters will be defined later
	 * @return http(s) response(xml|json) for put http method
	 * 			request-pass status-line: HTTP/1.1 200 OK
	 */
	Response put();

	/***
	 * http-put => update|replace resource attribute
	 *
	 * @param headers for the http request
	 * @param body of the http request
	 * @return http(s) response(xml|json) for put http method
	 * 			request-pass status-line: HTTP/1.1 200 OK
	 */
	Response put(Headers headers, ResponseBody body);
	
	/***
	 * to update|change resource details using http(s) patch method
	 * 
	 */
	Response patch();

	/***
	 * http-delete => remove specified resource
	 *
	 * post no parameters
	 * parameters will be defined later
	 * @return http(s) response(xml|json) for delete http method
	 * 			request-pass status-line: HTTP/1.1 200 OK
	 */
	Response delete();

	/***
	 * http-delete => remove specified resource
	 *
	 * @param headers for the http request
	 * @param body of the http request
	 * @return http(s) response(xml|json) for delete http method
	 * 			request-pass status-line: HTTP/1.1 200 OK
	 */
	Response delete(Headers headers, ResponseBody body);
}
