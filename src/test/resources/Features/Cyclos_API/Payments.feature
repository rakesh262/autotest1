#Provides access for performing payments or scheduled payments.
Feature: As a Authenticated user, 
  Provides access for performing payments or scheduled payments.
	
   @api @payments
  Scenario: Performs a payment from the given owner
  	Given send the payment
  	When request is posted
  	Then verify the payment details