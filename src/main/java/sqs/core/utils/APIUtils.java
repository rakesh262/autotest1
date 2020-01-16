package sqs.core.utils;

import io.restassured.authentication.CertificateAuthSettings;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import sqs.core.constants.Constants;
import sqs.framework.FrameworkData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class APIUtils extends FrameworkData{
	
	protected static CSVParser csvParser = null;
	protected static RequestSpecification req;
	private static Logger logger=Logger.getLogger(APIUtils.class);
	/*set Auth type as Basic (or) Digest (or) OAuth
	 *
	 *@param HTTP-request
	 *@return HTTP-request with authorization | without authorization
	 *
	 * */
	public static RequestSpecification setAuthenticationType(RequestSpecification request) {
		String authType = config.getProperty("api.auth.type");
		String userName= TestDataHandler.getData("$MyUserID");
		String password = TestDataHandler.getData("$MyPassword");
		String certFile = TestDataHandler.getData("$certificate_path");
		String certPass = TestDataHandler.getData("$certificate_password");
		String accessToken = TestDataHandler.getData("$access-token");
		
		//Set authentication type to specified type
		switch(authType.toLowerCase()) {
			case "none":
				break;
			case "basic":
				request = request.auth().preemptive().basic(userName, password).when();
				break;
			case "digest":
				request = request.auth().digest(userName, password).when();
				break;
			case "form":
				request = request.auth().form(userName, password).when();
				break;
			case "oauth":
				request = request.auth().oauth2(accessToken).when();
				break;
			case "certificate":
				request = request.auth().certificate(certFile, certPass,
						CertificateAuthSettings.certAuthSettings().allowAllHostnames());
				break;
			default:
				break;
		}
		
		return request;
	}
	
		
	/*set params in basePath to the user value
	 *
	 *@param base-path with params
	 *@return base-path with user-value
	 *
	 * */
	public static String pathVariable(String basePath) {
		String pathVar ;
		String pathVarValue ;
		
		while(basePath.contains("{")){
			pathVar = basePath.substring(basePath.indexOf('{'), basePath.indexOf('}')+1);
			pathVarValue = TestDataHandler.getData("$"+pathVar.substring(1, (pathVar.length()-1)));
			basePath = basePath.replace(pathVar, pathVarValue);
		}
		
		return basePath;
	}


		/*
		 * returns query parameters for api calling
		 *
		 * @param 1. the csv file that contains query parameters
		 * @param 2. lookup feature name
		 *
		 * @return String: the query parameters for api call
		 * */
	public static String returnQueryParams(String csvFile, String feature) {
		StringBuilder queryParams = new StringBuilder();
		String qryParams = "";
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFile))) {
			CSVParser field = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());

			for (CSVRecord data : field.getRecords()) {
				if (data.get(Constants.FEATURE).equalsIgnoreCase(feature)) {
					Set<String> getKeys = data.toMap().keySet();
					for (String key : getKeys) {
						if ((!key.equalsIgnoreCase(Constants.FEATURE)) && (!(data.get(key)).isEmpty())) {
							if (config.getProperty("api.default.param").isEmpty())
								queryParams.append(key + "=" + data.get(key) + "&");
							else
								queryParams.append(config.getProperty("api.default.param")
										+ "=" + data.get(key) + "&");
						}
					}
				}
			}
		} catch (IOException exception) {
			logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName(), exception);
		}

		qryParams = queryParams.toString();

		if (qryParams.endsWith("&"))
			qryParams = qryParams.substring(0, qryParams.length() - 1);
		if (qryParams.equals("?"))
			qryParams = "";

		return qryParams;
	}

		/*
		 * appends header to api call
		 *
		 * @param 1. input request call
		 *        2. the csv file that contains headers and its value
		 *        3. lookup feature name
		 */
		public static RequestSpecification requestwithHeader(RequestSpecification request, String csvFile, String feature) {

			req = request;

			try(Reader reader = Files.newBufferedReader(Paths.get(csvFile)))  {
				csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());

				for(CSVRecord data : csvParser.getRecords()) {
					if(data.get(Constants.FEATURE).equalsIgnoreCase(feature)) {
						Set<String> getKeys = data.toMap().keySet();
						for(String key : getKeys) {

							if((!key.equalsIgnoreCase(Constants.FEATURE))&&(!( data.get(key)).isEmpty()))
								req=req.header(key, data.get(key)).when();
						}
					}
				}
				}catch (IOException exception) {
				logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() , exception);
				}

			return req;
		}

		/*
		 * read string request body and return as text
		 *
		 * @param 1. input request call
		 *        2. the csv file that contains body
		 *        3. lookup feature name
		 *
		 * @return request body as String
		 */
		public static String returnRequestBody(RequestSpecification request, String csvFile,
				String feature) {
			String filepath = "";
			String body = "";
			StringBuilder bodyStrBuilder= new StringBuilder();
			try (Reader reader = Files.newBufferedReader(Paths.get(csvFile))) {
				CSVParser requestBody = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
				for (CSVRecord data : requestBody.getRecords()) {
					if (data.get(Constants.FEATURE).equalsIgnoreCase(feature)) {
						Set<String> getKeys = data.toMap().keySet();
						for (String key : getKeys) {
							if (key.equalsIgnoreCase("bodyFilepath"))
								filepath = Constants.USER_DIR + data.get(key);
						}
					}
				}
				File file = new File(filepath);

				try(BufferedReader br = new BufferedReader(new FileReader(file))) {
					String st;
					while ((st = br.readLine()) != null)
						bodyStrBuilder.append(st);
				}

			} catch (IOException exception) {
				logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName(), exception);
			}
			body=bodyStrBuilder.toString();

			return body;
		}

		public static File requestBodyFile(String csvFile, String feature) {
			File bodyFile = null;
			try (Reader reader = Files.newBufferedReader(Paths.get(csvFile))) {
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
				for (CSVRecord data : csvParser.getRecords()) {
					if (data.get(Constants.FEATURE).equalsIgnoreCase(feature)) {
						Set<String> getKeys = data.toMap().keySet();
						for (String key : getKeys) {
							if (key.equalsIgnoreCase("bodyFilepath")) {
								bodyFile = new File(Constants.USER_DIR + data.get(key));
							}
						}
					}
				}


			} catch (IOException exception) {
				logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName(), exception);
			}
			return bodyFile;
		}

		/*
		 * set api request content-type to xml|json
		 *
		 * @param 1. input request
		 *        2. content-type as string
		 *
		 * @return req with content-type set to xml (or) json
		 */
		public static RequestSpecification setContentType(RequestSpecification req, String type) {
			switch (type.toLowerCase()) {
				case "application/xml":
					req = req.contentType(ContentType.XML);
					break;
				case "application/json":
					req = req.contentType(ContentType.JSON);
					break;
				default:
					break;
			}
			return req;
		}
		
		/*set Auth type as Basic (or) Digest (or) OAuth
		 *this was check oauth api, if not necessary can be deprecated
		 *@param HTTP-request
		 *@return HTTP-request with authorization | without authorization
		 *
		 * */
		public static RequestSpecification setAuthenticationType(RequestSpecification request, String authType) {

			String userName=TestDataHandler.getData("$MyUserID");
			String password = TestDataHandler.getData("$MyPassword");
			String certFile = TestDataHandler.getData("$certificate_path");
			String certPass = TestDataHandler.getData("$certificate_password");
			String accessToken = TestDataHandler.getData("$access-token");
			
			//Set authentication type to specified type
			switch(authType.toLowerCase()) {
				case "none":
					break;
				case "basic":
					request = request.auth().preemptive().basic(userName, password).when();
					break;
				case "digest":
					request = request.auth().digest(userName, password).when();
					break;
				case "form":
					request = request.auth().form(userName, password).when();
					break;
				case "oauth":
					request = request.auth().oauth2(accessToken).when();
					break;
				case "certificate":
					request = request.auth().certificate(certFile, certPass,
							CertificateAuthSettings.certAuthSettings().allowAllHostnames());
					break;
				default:
					break;
			}
			
			return request;
		}
}
