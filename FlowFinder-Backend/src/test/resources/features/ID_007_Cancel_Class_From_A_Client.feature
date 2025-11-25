Feature: Cancel class from a client
  As a client
  I want to cancel my registration for a specific class
  So that I free my spot and no longer appear on the attendance list

  Background:
    Given the FlowFinder system is running
    And the following clients exist:
      | clientId | name   | email            | password   | bio            | isDeleted |
      | C101     | Alice  | alice@email.com  | pass12345  | Loves salsa    | false     |
      | C102     | Bob    | bob@email.com    | pass54321  | Enjoys tango   | false     |
      | C103     | Carla  | carla@email.com  | pass11111  | Bachata lover  | false     |
    And the following instructors exist:
      | instructorId | name     | email              |
      | I201         | Natalia  | natalia@school.ca  |
    And the following dance classes exist:
      | classId | name        | genre  | description              | isPrivate |
      | DC301   | Salsa 101   | Salsa  | Basic Salsa techniques   | false     |
    And the following scheduled classes exist:
      | specificClassId | classId | instructorId | date       | startTime | endTime   | location  | capacity | isDeleted |
      | SC401           | DC301   | I201         | 2025-11-15 | 18:00     | 19:00     | Studio A  | 2        | false     |
      | SC402           | DC301   | I201         | 2025-11-15 | 19:00     | 20:00     | Studio A  | 1        | false     |
    And the following registrations exist:
      | registrationId | clientId | specificClassId | status    |
      | R501           | C102     | SC401           | ACTIVE    |
      | R502           | C101     | SC401           | ACTIVE    |
      | R503           | C103     | SC402           | CANCELED  |

  # Normal Flow
  Scenario: Successfully cancel an active registration
    Given the client "C102" has an ACTIVE registration "R501" for specific class "SC401"
    When the client "C102" requests to cancel registration "R501"
    Then the registration "R501" should be marked as CANCELED
    And the system should increase available spots for specific class "SC401" by 1
    And the message "Registration canceled successfully" should be displayed

  # Alternate Flow
  Scenario: Attempt to cancel after cutoff time
    Given the policy is that cancellations must occur at least 2 hours before the start time
    And the current time is within 2 hours of the start time for specific class "SC401"
    And the client "C101" has an ACTIVE registration "R502" for specific class "SC401"
    When the client "C101" requests to cancel registration "R502"
    Then the system should prevent the cancellation
    And the message "Cancellation window has passed" should be displayed

  # Error Flow
  Scenario: Attempt to cancel a non existent registration
    When the client "C102" requests to cancel registration "R999"
    Then the system should return an error
    And the message "Registration not found" should be displayed

  # Error Flow
  Scenario: Attempt to cancel a registration that does not belong to the client
    Given the client "C101" has an ACTIVE registration "R502" for specific class "SC401"
    When the client "C102" requests to cancel registration "R502"
    Then the system should return an error
    And the message "Registration does not belong to this client" should be displayed
