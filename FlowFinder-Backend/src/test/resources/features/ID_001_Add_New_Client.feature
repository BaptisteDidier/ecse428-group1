Feature: Register as a New Client

  As a potential client
  I want to register for the FlowFinder system
  So that I can become a student and participate in dance classes

  Background:
    Given the FlowFinder system is running
    And the following clients exist:
      | name  | email             | password    | bio          |
      | Alice | alice@email.com   | pass12345   | Loves salsa  |
      | Bob   | bob@email.com     | pass54321   | Enjoys tango |

  Scenario: Successful registration with valid information
    When a new client registers with name "Deniz", email "deniz@email.com", password "12345678", and bio "Enjoys hip hop"
    Then the client account should be created
    And they should be marked as a registered client
    And their registration date should be set to today
    And they should have access to the list of available classes

  Scenario: Registration with an email that is already in use
    Given a client already exists with email "alice@email.com"
    When a new client attempts to register again using the same email
    Then the system should reject the registration
    And they should be informed that the email is already in use

  Scenario: Registration with invalid or incomplete details
    When a new client registers with missing name and invalid email "notanemail"
    Then the system should reject the registration
    And they should be informed of the specific validation errors

  Scenario: Registration without optional bio
    When a new client registers with name "Tom", email "tom@email.com", password "safePass123", and no bio
    Then their account should be created
    And the bio field should be marked as incomplete
    And the client should be able to update their profile later
