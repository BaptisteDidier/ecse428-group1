Feature: Add a new class

As an Instructor
I would like to add a new class
So that students can sign up to take the class

    Background:
        Given the following users exist:
        | username   | password | role       |
        | instructor | pass123  | INSTRUCTOR |
        And the following classes exist:
        | className         | description                                          | instructorUsername | | classType |
        | Salsa 101         | Basic Salsa Techniques                               | instructor         | | private   |
        | Brekdancing 252   | Introduction to intermediate breakdancing techniques | instructor         | | group     |
    
    Scenario: Successfully adding a new class
        Given I am logged in as "instructor" with password "pass123"
        And the class name "Tap Dancing 101" is provided
        And the description "Basic Tap Dancing" is provided
        And the class type "private" is provided
        Then a confirmation message "Class added successfully" should be displayed
        And the class list should include "Tap Dancing 101"
    
    Scenario: Attempting to add a class with a duplicate name
        Given I am logged in as "instructor" with password "pass123"
        When I navigate to the "Add New Class" page
        And the class name "Salsa 101" is provided
        And the description "Basic Salsa-ing" is provided
        And the class type "group" is provided
        Then the error message "Class name already exists" should be displayed
    
    Scenario: Attempting to add a class without a name
        Given I am logged in as "instructor" with password "pass123"
        And no class name field is provided
        And the description "Basic Conga Lines" is provided
        And the class type "private" is provided
        Then the error message "Class name is required" should be displayed
    
    Scenario: Attempting to add a class without a description
        Given I am logged in as "instructor" with password "pass123"
        And the class name "Ballet 438" is provided
        And no description is provided
        And the class type "group" is provided
        Then the error message "Description is required" should be displayed

    Scenario: Attempting to add a class without selecting a class type
        Given I am logged in as "instructor" with password "pass123"
        And the class name "Hip Hop 101" is provided
        And the description "Introduction to Hip Hop" is provided
        And no class type is provided
        Then the error message "Class type is required" should be displayed