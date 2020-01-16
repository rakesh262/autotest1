#for demo
Feature: As a admin of application
  I should be able to create, update and delete a user
  
  @api1 @users
  Scenario: Get current users in the system
  	Given send a get request to system requesting list of users
  	When the valid response is received from the system
  	Then validate the users details
  
  @api1 @users
  Scenario: Create user as admin
  	Given send a post request to test server to create a user
  	When the valid response is received from the system
  	Then validate the created user is existing in test system

  @api1 @users
  Scenario: Update a user created
  	Given send request to update user details
  	When the valid response is received from the system
  	Then validate that user details
  	
  @api1 @users
  Scenario: Delete user as admin
  	Given as a admin delete the user from system
  	When the valid response is received from the system
  	Then validate that user does not existing in test system