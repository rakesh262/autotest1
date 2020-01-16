Feature: UserPayment

  @UserPayment
  Scenario: Validate whether user is able to make payments with sufficient funds
    Given As an authenticated user '$MyUserID' with my valid credential '$MyPassword' I am in 'Payment to user' page
    When I pay '$OneDollar' Dollar to '$PaymentUser' and confirm the payment
    Then I should see 'The payment was successful' message


  @UserPayment_InsufficientFunds
  Scenario: Validate whether user is able to make payments with sufficient funds data mapping example
    Given As an authenticated user 'MyUserID1' with my valid credential '$MyPassword1' I am in 'Payment to user' page
    When I pay '$Dollar' Dollar to 'PaymentUser1' and confirm the payment
    Then I should see 'The payment was successful' message

  @Login
  Scenario: Check whether user able to login cyclos
    Given As a authenticated Cyclos user check we need to login
    When Logged in check whether we able to see 'Welcome1'
    Then User able to click on banking

  @UserPayment_insufficient_amount
  Scenario Outline:
  Validate user able to make payments more than sufficient amount
    Given As an authenticated user 'Demo' with my valid credential '$MyPassword' I am in 'Payment to user' page
    When I pay '<Amount>' Dollar to '<Benificiary Name>' and confirm the payment
    Then I should see 'The payment was successful' message
    Examples:
      | Benificiary Name | Amount |
      | Robert           | 7000   |

  @UserPayment1
  Scenario: Validate user able to make payment
    Given As an authenticated user 'MyUserID' with my valid credential 'MyPassword' I am in HomePage
    When I click 'Home_Banking'
    And I should see text 'Member account' present on page
    And I click 'Banking_PaymentToUserLink'
    And I should see text 'Payment to user' present on page
    And I enter 'Robert' in field 'PaymentToUser_User' and wait
    And I click on 'Robert' text
    And I clear and enter '1' in field 'PaymentToUser_Amount'
    And I click 'PaymentToUser_Submit'
    Then I should see text 'Payment review' present on page
    And I click 'PaymentToUser_Confirm'
    And I should see text 'The payment was successful' present on page


  @UserPayment_summarized_mobile1
  Scenario: Validate user able to make payments within sufficient amount in App
    Given As an authenticated user 'Demo' with my valid credential '$MyPassword' I am in 'Payment to user' page in app
    When I pay '2' Dollar to 'Nicola Tesla' and confirm the payment in app
    Then I should see 'The payment was successful' message in app