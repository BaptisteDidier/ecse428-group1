Feature: Remove my client account
  As an existing client
  I want to delete my own account from the FlowFinder system
  So that I no longer have access to classes or the platform

  Background:
    Given the FlowFinder system is running
    And the following clients exist:
      | clientId | name   | email            | password   | bio            | isDeleted |
      | C101     | Alice  | alice@email.com  | pass12345  | Loves salsa    | false     |
      | C102     | Bob    | bob@email.com    | pass54321  | Enjoys tango   | false     |
      | C103     | Carla  | carla@email.com  | pass11111  | Bachata lover  | true      |

  # Normal Flow
  Scenario: Client successfully deletes their own active account
    Given client "C102" exists and is not deleted
    When client "C102" requests to delete their account
    Then client "C102" should be marked as deleted in the system
    And the message "Your account has been deleted" should be displayed
    And the client should no longer have access to the platform

  # Alternate Flow
  Scenario: Client tries to delete an already-deleted account
    Given client "C103" is already deleted
    When client "C103" requests to delete their account
    Then the system should not perform the deletion again
    And the message "This account is already deleted" should be displayed

  # Error Flow
  Scenario: Attempt to delete a non-existent client account
    When client "C999" requests to delete their account
    Then the system should return an error
    And the message "There is no account with id C999" should be displayed
