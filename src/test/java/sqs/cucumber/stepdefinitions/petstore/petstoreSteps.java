package sqs.cucumber.stepdefinitions.petstore;

import cucumber.api.java.en.*;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import sqs.core.actions.api.APIRequestActions;
import sqs.core.actions.api.APITestRequests;
import sqs.core.actions.api.APITestRequests.StatusCode;
import sqs.core.utils.APIUtils;

public class petstoreSteps extends APITestRequests{
    @Given("as a owner i add a pet to customer view")
    public void asAOwnerIAddAPetToCustomerView() {
        logger.info("______________________________________________________________________________________start petstore API test________________________________________________");
        logger.info("_______________________________________________________________________________________post___________________________________________________________");

        ///*no need to set it's default in property*/APIRequestActions.setBaseURI("https://petstore.swagger.io/v2");
        APIRequestActions.setBasePath("/pet");
        response = postRequest();

    }
    
    @When("the valid response is received from the server")
	public void theValidResponseIsReceivedFromTheSystem() {
    	response.then().log().status();
		response.then().log().body();
		logger.info("Verify the request is successful");
		checkStatusCode(StatusCode.OK);
    }
    
    @Then("the added pet details are verified")
    public void theAddedPetDetailsAreVerified() {
        //verify the pet is added using http-get request
    	APIRequestActions.setBasePath("/pet/{petId}");
    	response = getRequest();
       checkStatusCode(StatusCode.OK);
    }

    @Given("as a customer i want a ped by id")
    public void asACustomerIWantAPedById() {
        logger.info("_______________________________________________________________________________________get____________________________________________________________");
      ///*no need to set it's default in property*/APIRequestActions.setBaseURI("https://petstore.swagger.io/v2");
        APIRequestActions.setBasePath("/pet/{petId}");
        response = getRequest();
    }

    @Then("i should get the pet description and details")
    public void iShouldGetThePetDescriptionAndDetails() {
        logger.info("verify the valid session id is returned");
        checkBodyContainsText("10");
        
        /*verify pet id*/checkBodyPathUsing("Pet.id","10");
        /*verify pet name*/checkBodyPathUsing("Pet.name","doggie");
        logger.info("______________________________________________________________________________________________________________________________________________________");
    }

    @Given("as a petstore owner i want the newly added pet details")
    public void asAPetstoreOwnerIWantTheNewlyAddedPetDetails() {
        logger.info("_______________________________________________________________________________________put____________________________________________________________");

      ///*no need to set it's default in property*/APIRequestActions.setBaseURI("https://petstore.swagger.io/v2");
        APIRequestActions.setBasePath("/pet");
        response = putRequest();
       checkStatusCode(200);
    }

    @Then("the updated should be displayed in the system")
    public void theUpdatedShouldBeDisplayedInTheSystem() {
        logger.info("verify the valid session id is returned");
        logger.info(response.then().log().body());
        checkBodyContainsText("doggie");
        
        /*verify pet name*/checkBodyPathUsing("Pet.name","doggie");
        logger.info("______________________________________________________________________________________________________________________________________________________");
    }

    @Given("as a owner i want to delete a pet which is unvailable")
    public void asAOwnerIWantToDeleteAPetWhichIsUnvailable() {
        logger.info("_______________________________________________________________________________________________delete_________________________________________________");
      ///*no need to set it's default in property*/APIRequestActions.setBaseURI("https://petstore.swagger.io/v2");
        APIRequestActions.setBasePath("/pet/{petId}");
        response = deleteRequest();
    }

    @Then("the pet should be deleted from customer views")
    public void thePetShouldBeDeletedFromCustomerViews() {
        logger.info("check resource is deleted");
        APIRequestActions.setBaseURI("https://petstore.swagger.io/v2");
        APIRequestActions.setBasePath("/pet/{petId}");
        response = getRequest();
        /*verify pet is deleted*/checkStatusCode(StatusCode.NOT_FOUND);
        logger.info("______________________________________________________________________________________________________________________________________________________");
    }

    @Given("as a customer i want to know the avaialbilty of pets")
    public void asACustomerIWantToKnowTheAvaialbiltyOfPets() {
        logger.info("_______________________________________________________________________________________get____________________________________________________________");

      ///*no need to set it's default in property*/APIRequestActions.setBaseURI("https://petstore.swagger.io/v2");
        APIRequestActions.setBasePath("/pet/findByStatus");
        response = getRequest();
    }

    @Then("i should be availability status of each pets")
    public void iShouldBeAvailabilityStatusOfEachPets() {
        logger.info("verify the valid session id is returned");
      ///*no need to set it's default in property*/APIRequestActions.setBaseURI("https://petstore.swagger.io/v2");
        APIRequestActions.setBasePath("/pet/{petId}");
        response = getRequest();
        /*verify pet is deleted*/checkStatusCode(StatusCode.NOT_FOUND);
        APIRequestActions.setBaseURI("https://petstore.swagger.io/v2");
        APIRequestActions.setBasePath("/pet/1");
        response = getRequest();
        /*verify pet is available*/checkStatusCode(StatusCode.OK);
        logger.info("______________________________________________________________________________________________________________________________________________________");
        logger.info("________________________________________________________end petstore api test___________________________________________________________________________________________");
    }
    
    @Given("send a get request to system requesting list of users")
	public void sendAGetRequestToSystemRequestingListOfUsers() {
		logger.info("______________________________________________________________________________________start users api test___________________________________________________________");
		logger.info("_______________________________________________________________________________________get____________________________________________________________");
		
        APIRequestActions.setBaseURI("https://fakerestapi.azurewebsites.net/api");
		APIRequestActions.setBasePath("/Users");
		response = getRequest();
	}

	@When("the valid response is received from the system")
	public void theValidResponseIsReceivedFromTheSystem1() {
		response.then().log().status();
		response.then().log().body();
		logger.info(response.then().log().body());
		logger.info("Verify the request is successful");
		checkStatusCode(StatusCode.OK);//this is overloaded with method either enum|string as parameter.
	}

	@Then("validate the users details")
	public void validateTheUsersDetails() {
		/*verify in content*/checkBodyContainsText("Password1");
		/*verify ID*/checkBodyPathUsing("ID",1);
		/*verify name*/checkBodyPathUsing("UserName","User 1");
		logger.info("______________________________________________________________________________________________________________________________________________________");
	}

	@Given("send a post request to test server to create a user")
	public void sendAPostRequestToTestServerToCreateAUser() {
		logger.info("_______________________________________________________________________________________post___________________________________________________________");
        APIRequestActions.setBaseURI("https://fakerestapi.azurewebsites.net/api");

		APIRequestActions.setBasePath("/Users");
		response = postRequest();
	}

	@Then("validate the created user is existing in test system")
	public void validateTheCreatedUserIsExistingInTestSystem() {
		APIRequestActions.setBasePath("/Users/{USER_ID}");
		response = getRequest();
		checkStatusCode("ok");//this is overloaded with method either enum|string as parameter.
		logger.info("______________________________________________________________________________________________________________________________________________________");
	}

	@Given("as a admin delete the user from system")
	public void asAAdminDeleteTheUserFromSystem() {
		logger.info("_______________________________________________________________________________________________delete_________________________________________________");
        APIRequestActions.setBaseURI("https://fakerestapi.azurewebsites.net/api");

		APIRequestActions.setBasePath("/Users/{ID}");
		response = deleteRequest();
	}

	@Then("validate that user does not existing in test system")
	public void validateThatUserDoesNotExistingInTestSystem() {
		logger.info("\tverify the system message");
		APIRequestActions.setBaseURI("https://fakerestapi.azurewebsites.net/api");

		APIRequestActions.setBasePath("/Users/{ID}");
		response = getRequest();
		/*verify user is deleted*/checkStatusCode(StatusCode.NOT_FOUND);
		logger.info("______________________________________________________________________________________________________________________________________________________");
	}

	@Given("send request to update user details")
	public void sendRequestToUpdateUserDetails() {
		logger.info("_______________________________________________________________________________________put____________________________________________________________");
        APIRequestActions.setBaseURI("https://fakerestapi.azurewebsites.net/api");

		APIRequestActions.setBasePath("/Users/{USER_ID}");
		response = putRequest();
	}

	@Then("validate that user details")
	public void validateThatUserDetails() {
		logger.info("\tverify the updated details of user");
		APIRequestActions.setBaseURI("https://fakerestapi.azurewebsites.net/api");

		APIRequestActions.setBasePath("/Users/{USER_ID}");
		response = getRequest();
		/*verify user is available*/checkStatusCode(StatusCode.OK);
		/*verify user name is changed*/checkBodyPathUsing("UserName","User 1");
		/*verify user name is changed*/checkBodyPathUsing("Password","Password1");
		logger.info("______________________________________________________________________________________________________________________________________________________");
		logger.info("________________________________________________________end API test_________________________________________________________________________________");
	}
    
	@Given("get access token")
	public void get_access_token() {
		logger.info("______________________________________________________________________________________start oauth api test___________________________________________________________");
		APIRequestActions.setBaseURI("https://api.imgur.com/");

		APIRequestActions.setBasePath("/3/account/me/images");
		
	}

	@When("verify api with access token")
	public void verify_api_with_access_token() {
		RequestSpecification req = APIUtils.setAuthenticationType(RestAssured.given(),"oauth");
		response = req.get();
//		response.then().log().body();
		logger.info(response.then().log().body());
	}

	@When("check status is OK")
	public void check_status_is_OK() {
	   checkStatusCode(StatusCode.OK);
		logger.info("______________________________________________________________________________________________________________________________________________________");
	}

	@Then("verify api without access token")
	public void verify_api_without_access_token() {
		logger.info("______________________________________________________________________________________________________________________________________________________");
	    RequestSpecification req = APIUtils.setAuthenticationType(RestAssured.given(),"none");
	    response = req.get();
//		response.then().log().body();
		logger.info(response.then().log().body());
	}

	@Then("check status is UNAUTHORIZED")
	public void check_status_is_UNAUTHORIZED() {
		checkStatusCode(StatusCode.UNAUTHORIZED);
		logger.info("______________________________________________________________________________________________________________________________________________________");
	}
}
