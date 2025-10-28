Feature: Add a New Instructor

  As the application manager
  I want to add new instructors to the FlowFinder system
  So that they can be assigned to classes

  Background:
    Given the FlowFinder system is running
    And the following instructors exist:
      | name   | email              | specialty     | assignedClasses     |
      | Maria  | maria@email.com    | Salsa         | Beginner Salsa      |
      | Carlos | carlos@email.com   | Tango         | Intermediate Tango  |

  Scenario: Successful addition of a new instructor
    When a new instructor is added with name "Deniz", email "deniz@email.com", specialty "Hip Hop", and assigned classes "Hip Hop Basics"
    Then the instructor account should be created
    And they should be assigned to the selected classes
    And their hire date should be set to today
    And they should be visible in the list of available instructors

  Scenario: Addition with an email that is already in use
    Given an instructor already exists with email "maria@email.com"
    When a new instructor is added again using the same email
    Then the system should reject the addition
    And an error message should be displayed stating "Email is already in use"

  Scenario: Addition with invalid or incomplete details
    When a new instructor is added with missing name and invalid email "notanemail"
    Then the system should reject the addition
    And specific validation errors should be displayed
