Feature: Remove an existing client
  As an instructor
  I want to remove a client from the system
  So that they no longer have access to classes or the platform

  Background:
    Given the FlowFinder system is running
    And the following clients exist:
      | clientId | name   | email            | password   | bio            | isDeleted |
      | C101     | Alice  | alice@email.com  | pass12345  | Loves salsa    | false     |
      | C102     | Bob    | bob@email.com    | pass54321  | Enjoys tango   | false     |
      | C103     | Carla  | carla@email.com  | pass11111  | Bachata lover  | true      |

  # Normal Flow
  Scenario: Successfully remove an active client
    Given the client "C102" exists and is not deleted
    When the admin requests to remove client "C102"
    Then the client "C102" should be marked as deleted in the system
    And the message "Client removed successfully" should be displayed

  # Alternate Flow
  Scenario: Attempt to remove a client who is already removed
    Given the client "C103" is already deleted
    When the admin requests to remove client "C103"
    Then the system should prevent the removal
    And the message "Client is already removed" should be displayed

  # Error Flow
  Scenario: Attempt to remove a non-existent client
    When the admin requests to remove client "C999"
    Then the system should return an error
    And the message "Client not found" should be displayed
