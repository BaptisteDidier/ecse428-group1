Feature: Add New Instructor
  As the application manager
  I want to add a new Instructor (a Person) and assign them to scheduled classes
  So that each scheduled SpecificClass has a responsible Instructor

  Background:
    Given the system is running
    And the following dance classes exist:
      | name             | genre         | description              | isPrivate |
      | Beginner Ballet  | Ballet        | Intro to basic ballet    | false     |
      | Advanced Hip Hop | Hip Hop       | Advanced hip hop drills  | false     |
      | Latin Basics     | Latin         | Salsa & bachata intro    | false     |
    And the following specific classes exist:
      | className        | date       | start   | end     | location        | limit | isDeleted |
      | Beginner Ballet  | 2025-11-03 | 18:00   | 19:00   | Studio A        | 20    | false     |
      | Advanced Hip Hop | 2025-11-05 | 19:30   | 20:45   | Studio B        | 25    | false     |
      | Latin Basics     | 2025-11-07 | 17:30   | 18:30   | Studio C        | 18    | false     |
    And the following instructors exist:
      | name         | bio                   | email             | password  | creationDate | isDeleted |
      | Sarah Connor | Contemporary lead     | sarah@flow.com    | pass12345 | 2025-10-01   | false     |

  # Happy path: create Person(Instructor) and assign to existing SpecificClass rows
  Scenario: Successfully add a new instructor and assign to two specific classes
    When I add a new instructor with:
      | name         | Emma Lee                  |
      | bio          | Specialist in Latin       |
      | email        | emma@flow.com             |
      | password     | safePass123               |
    And I set the creation date to today
    And I set isDeleted to false
    And I assign the instructor to the following specific classes:
      | className    | date       | start  |
      | Latin Basics | 2025-11-07 | 17:30  |
      | Beginner Ballet | 2025-11-03 | 18:00 |
    And I click the "Add" button
    Then a new Instructor should be created with email "emma@flow.com"
    And the Instructor "Emma Lee" should be linked as the instructor of:
      | className       | date       | start  |
      | Latin Basics    | 2025-11-07 | 17:30  |
      | Beginner Ballet | 2025-11-03 | 18:00  |
    And I should see a confirmation message "Instructor added successfully"

  # Guardrail: at least one SpecificClass must be selected (because DanceClass itself has no instructor link)
  Scenario: Failed attempt to add a new instructor without selecting any specific classes
    When I add a new instructor with:
      | name         | Mark Chen           |
      | bio          | Jazz improvisation  |
      | email        | mark@flow.com       |
      | password     | secretPass          |
    And I set the creation date to today
    And I set isDeleted to false
    But I do not select any specific class
    And I click the "Add" button
    Then I should see an error message "At least one scheduled class (SpecificClass) must be assigned"
    And the instructor should not be created

  # Validation: Person fields on Instructor creation
  Scenario: Failed attempt to add an instructor with missing required details
    When I try to add a new instructor with:
      | name         |              |
      | bio          | Some bio     |
      | email        | noemail      |
      | password     |              |
    And I set the creation date to today
    And I set isDeleted to false
    And I assign the instructor to the following specific classes:
      | className       | date       | start  |
      | Beginner Ballet | 2025-11-03 | 18:00  |
    And I click the "Add" button
    Then the system should reject the addition
    And I should see validation messages:
      | field        | message                           |
      | name         | Name is required                  |
      | email        | Enter a valid email address       |
      | password     | Password is required              |
    And the instructor should not be created

  # Uniqueness on Person.email (single-table inheritance: Instructor/Client share the same email space)
  Scenario: Prevent adding an instructor with an email already in use
    Given a Person already exists with email "sarah@flow.com"
    When I try to add a new instructor with:
      | name     | Sarah 2           |
      | bio      | Another Sarah     |
      | email    | sarah@flow.com    |
      | password | anyPass123        |
    And I set the creation date to today
    And I set isDeleted to false
    And I assign the instructor to the following specific classes:
      | className     | date       | start |
      | Latin Basics  | 2025-11-07 | 17:30 |
    And I click the "Add" button
    Then the system should reject the addition
    And I should see an error message "Email already in use by another person"

  # Optional field behavior: bio can be empty; creationDate + soft-delete defaults are set
  Scenario: Add instructor without optional bio
    When I add a new instructor with:
      | name         | Sophia Grant |
      | bio          |              |
      | email        | sophia@flow.com |
      | password     | safePass123  |
    And I set the creation date to today
    And I set isDeleted to false
    And I assign the instructor to the following specific classes:
      | className       | date       | start  |
      | Beginner Ballet | 2025-11-03 | 18:00  |
    And I click the "Add" button
    Then the instructor "Sophia Grant" should be created successfully
    And the instructor should be linked as the instructor of:
      | className       | date       | start  |
      | Beginner Ballet | 2025-11-03 | 18:00  |
    And the bio field should be empty

  # Soft-delete invariant: a newly-created instructor must not start as deleted
  Scenario: Reject creation if isDeleted is set to true at creation
    When I add a new instructor with:
      | name         | Test Deleted |
      | bio          | Any bio      |
      | email        | deleted@flow.com |
      | password     | pass         |
    And I set the creation date to today
    And I set isDeleted to true
    And I assign the instructor to the following specific classes:
      | className     | date       | start |
      | Latin Basics  | 2025-11-07 | 17:30 |
    And I click the "Add" button
    Then the system should reject the addition
    And I should see an error message "New instructors must not be created as deleted"
    And the instructor should not be created
