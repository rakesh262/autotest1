#Lists accounts of the given owner with their statuses
Feature: As a Authenticated user, 
  I should be able to get account information and status
  
  @api @accounts
  Scenario: Get own accounts
	Given send request
	When get the response
	Then verify it is a valid response