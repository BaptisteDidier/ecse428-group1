Feature: Remove an existing class
  As an instructor
  I want to remove a class I previously created
  So that students can no longer book or attend that class

  Background:
    Given the following classes exist in the system
      | classId | instructorUsername | className        | date       | time  | room  | status   |
      | C001     | danceQueen         | Beginner Salsa   | 2025-12-10 | 18:00 | R101  | Active   |
      | C002     | danceKing          | Hip Hop Fusion   | 2025-12-10 | 19:30 | R102  | Active   |
    And the following students are enrolled in these classes
      | classId | studentUsername |
      | C001    | studentA        |
      | C001    | studentB        |

  Scenario: Successfully remove a class with no upcoming bookings
    Given the class "C002" has no enrolled students
    When the instructor requests to remove class "C002"
    Then the class "C002" should be deleted from the system
    And the message "Class removed successfully" should be displayed

  Scenario: Attempt to remove a class that has active student bookings
    Given the class "C001" has enrolled students
    When the instructor requests to remove class "C001"
    Then the system should prevent class deletion
    And the message "Cannot remove class with active bookings" should be displayed
    And the class "C001" should remain active in the system

  Scenario: Attempt to remove a non-existent class
    When the instructor requests to remove class "C999"
    Then the system should return an error
    And the message "Class not found" should be displayed

