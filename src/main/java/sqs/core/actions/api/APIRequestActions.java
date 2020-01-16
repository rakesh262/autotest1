package sqs.core.actions.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import sqs.core.actions.CommonActions;
import sqs.core.actions.interfaces.APIActionsInterface;
import sqs.core.utils.APIUtils;

import static sqs.core.constants.Constants.*;

public class APIRequestActions extends CommonActions implements APIActionsInterface {
    private static Logger logger=Logger.getLogger(APIRequestActions.class);

    /*@Override
    public Response get() {
        logger.info("GET Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
        RequestSpecification req = APIUtils.setAuthenticationType(RestAssured.given());
        req = APIUtils.requestwithHeader(req, USER_DIR
                + config.getProperty(API_HEADER_DEST),scenario_name);
        return req.get();
    }*/
	@Override
    public Response get() {
        RequestSpecification req = APIUtils.setAuthenticationType(RestAssured.given());
        req = APIUtils.requestwithHeader(req, USER_DIR
                + config.getProperty(API_HEADER_DEST), scenarioName);
        String param = APIUtils.returnQueryParams(USER_DIR
                + config.getProperty(API_PARAM_DEST), scenarioName);
        if(!param.isEmpty()) {
            logger.info("GET Request is processed for "+RestAssured.baseURI+RestAssured.basePath+
                    param +".....");
            return req.get(param);
        }
        else{
            logger.info("GET Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
            return req.get();
        }
    }

    @Override
    public Response post() {
        logger.info("POST Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
        RequestSpecification request = APIUtils.setAuthenticationType(RestAssured.given());
        request = APIUtils.requestwithHeader(request, USER_DIR
                + config.getProperty(API_HEADER_DEST), scenarioName);
        request. body(APIUtils.requestBodyFile(config.getProperty(API_BODY_DEST), scenarioName));//return body as file
        
        logger.info("GET Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
        return request.post();
    }

    @Override
    public Response put() {
        logger.info("PUT Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
        RequestSpecification request = APIUtils.setAuthenticationType(RestAssured.given());
        request = APIUtils.requestwithHeader(request, USER_DIR
                + config.getProperty(API_HEADER_DEST), scenarioName);

        request.
            body(APIUtils.requestBodyFile(config.getProperty(API_BODY_DEST),
                    scenarioName));//return body as file
        String param = APIUtils.returnQueryParams(USER_DIR
                + config.getProperty(API_PARAM_DEST), scenarioName);
        if(!param.isEmpty()) {
            logger.info("GET Request is processed for "+RestAssured.baseURI+RestAssured.basePath+
                    param +".....");
            return request.put(param);
        }
        else{
            logger.info("GET Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
            return request.put();
        }
    }

    @Override
    public Response patch() {
    	 logger.info("PUT Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
         RequestSpecification request = APIUtils.setAuthenticationType(RestAssured.given());
         request = APIUtils.requestwithHeader(request, USER_DIR
                 + config.getProperty(API_HEADER_DEST), scenarioName);

         request.
             body(APIUtils.requestBodyFile(config.getProperty(API_BODY_DEST),
                     scenarioName));//return body as file
         String param = APIUtils.returnQueryParams(USER_DIR
                 + config.getProperty(API_PARAM_DEST), scenarioName);
        if(!param.isEmpty()) {
            logger.info("GET Request is processed for "+RestAssured.baseURI+RestAssured.basePath+
                    param +".....");
            return request.patch(param);
        }
        else{
            logger.info("GET Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
            return request.patch();
        }
    }

    @Override
    public Response delete() {
        logger.info("DELETE Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
        RequestSpecification request = APIUtils.setAuthenticationType(RestAssured.given());
        request = APIUtils.requestwithHeader(request, USER_DIR
                + config.getProperty(API_HEADER_DEST), scenarioName);

        logger.info("GET Request is processed for "+RestAssured.baseURI+RestAssured.basePath+".....");
        return request.delete();
    }

    public static void setBaseURI() {
        RestAssured.baseURI =  config.getProperty("api.test.url");
    }

    public static void setBaseURI(String apiURI) {
        RestAssured.baseURI =  apiURI;
    }

    public static void resetBaseURI (){
        RestAssured.baseURI = null;
    }

    public static void setBasePath(String basePathTerm){
        if(basePathTerm.contains("{")) {
            basePathTerm = APIUtils.pathVariable(basePathTerm);
        }
        RestAssured.basePath = basePathTerm;
    }

    public static void resetBasePath(){
        RestAssured.basePath = null;
    }

    /*public static void  createSearchQueryPath(String searchTerm, String jsonPathTerm, String param, String paramValue) {
        //String path = searchTerm + "/" + jsonPathTerm + "?" + param + "=" + paramValue;
    }*/

  /*  public static RequestSpecification setBasicAuth(){

        return RestAssured.given().auth().preemptive()
                .basic("Demo"
                        ,"1234").when();
    }*/
}
