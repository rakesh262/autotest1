Feature: User Login Mobile App

  @UserPayment_summarized_mobile
  Scenario: Validate user able to make payments within sufficient amount in App
    Given As an authenticated user 'Demo' with my valid credential '$MyPassword' I am in 'Payment to user' page in app
    When I pay '1' Dollar to 'Nicola Tesla' and confirm the payment in app
    Then I should see 'The payment was successful' message in app

  @UserPayment_summarized_mobile1
  Scenario: Validate user able to make payments within sufficient amount in App data mapping example
    Given As an authenticated user 'Demo' with my valid credential '$MyPassword' I am in 'Payment to user' page in app
    When I pay '2' Dollar to 'Nicola Tesla' and confirm the payment in app
    Then I should see 'The payment was successful' message in app