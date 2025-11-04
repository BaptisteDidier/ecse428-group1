Feature: Remove an existing instructor
  As the application manager
  I want to remove an Instructor (a Person) from the system
  So that they can no longer manage classes

  Background:
    Given the system is running
    And the following dance classes exist:
      | name             | genre         | description           | isPrivate |
      | Beginner Ballet  | Ballet        | Intro to basic ballet | false     |
      | Latin Basics     | Latin         | Salsa & bachata intro | false     |
    And the following specific classes exist:
      | className       | date       | start  | end    | location  | limit | isDeleted |
      | Beginner Ballet | 2025-11-03 | 18:00  | 19:00  | Studio A  | 20    | false     |
      | Latin Basics    | 2025-11-07 | 17:30  | 18:30  | Studio C  | 18    | false     |
    And the following instructors exist:
      | name           | bio                | email            | password  | creationDate | isDeleted |
      | Sarah Connor   | Contemporary lead  | sarah@flow.com   | pass12345 | 2025-10-01   | false     |
      | Emma Lee       | Latin specialist   | emma@flow.com    | safePass  | 2025-10-05   | false     |
    And the following instructors are assigned to specific classes:
      | instructorEmail | className       | date       | start  |
      | sarah@flow.com  | Beginner Ballet | 2025-11-03 | 18:00  |
      | emma@flow.com   | Latin Basics    | 2025-11-07 | 17:30  |

  # Normal Flow
  Scenario: Successfully remove an instructor with no assigned specific classes
    Given the instructor "Emma Lee" has no assigned specific classes
    When the application manager remove the instructor with email "emma@flow.com"
    Then the instructor "Emma Lee" should be marked as deleted
    And the message "Instructor removed successfully" should be displayed

  # Alternate flow
  Scenario: Attempt to remove an instructor with assigned active specific classes
    Given the instructor "Sarah Connor" has assigned active specific classes
    When the application manager attempt to remove the instructor with email "sarah@flow.com"
    Then the system should prevent instructor removal
    And the message "Cannot remove instructor with active classes" should be displayed
    And the instructor "Sarah Connor" should remain active in the system
  
  # Error Flow
  Scenario: Attempt to remove a non-existent instructor
    When the application manager attempt to remove an instructor with email "nonexistent@flow.com"
    Then the system should return an error
    And the message "Instructor not found" should be displayed
