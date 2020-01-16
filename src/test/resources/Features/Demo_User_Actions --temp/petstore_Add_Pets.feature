#demo site-1
Feature: As a customer, 
			I need to know 
			everything about your Pets

  @api @demo
  Scenario: Add a pet and its details
  	Given as a owner i add a pet to customer view
  	When the valid response is received from the server
  	Then the added pet details are verified

  @api @demo
  Scenario: Find a pet by its ID
  	Given as a customer i want a ped by id
	When the valid response is received from the server
  	Then i should get the pet description and details

  @api @demo
  Scenario: Update the pet details
  	Given as a petstore owner i want the newly added pet details
  	When the valid response is received from the server
  	Then the updated should be displayed in the system

  @api @demo
  Scenario: Find availability status of pets
  	Given as a customer i want to know the avaialbilty of pets
  	When the valid response is received from the server
  	Then i should be availability status of each pets
  	
  @api @demo
  Scenario: Delete a pet by ID
  	Given as a owner i want to delete a pet which is unvailable
  	When the valid response is received from the server
  	Then the pet should be deleted from customer views
  	
  @api @oauth
  Scenario: check OAUTH api
  	Given get access token
  	When verify api with access token
  	And check status is OK
  	Then verify api without access token
  	And check status is UNAUTHORIZED