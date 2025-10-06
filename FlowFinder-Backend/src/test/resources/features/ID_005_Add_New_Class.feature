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
        When I navigate to the "Add New Class" page
        And I fill in the class name with "Tap Dancing 101"
        And I fill in the description with "Basic Tap Dancing"
        And I fill in the class type with "private" 
        And I submit the form
        Then I should see a confirmation message "Class added successfully"
        And the class list should include "Tap Dancing 101"
    
    Scenario: Attempting to add a class with a duplicate name
        Given I am logged in as "instructor" with password "pass123"
        When I navigate to the "Add New Class" page
        And I fill in the class name with "Salsa 101"
        And I fill in the description with "Basic Salsa-ing"
        And I fill in the class type with "group"
        And I submit the form
        Then I should see an error message "Class name already exists"
    
    Scenario: Attempting to add a class without a name
        Given I am logged in as "instructor" with password "pass123"
        When I navigate to the "Add New Class" page
        And I leave the class name field empty
        And I fill in the description with "Basic Conga Lines"
        And I fill in the class type with "private"
        And I submit the form
        Then I should see an error message "Class name is required"
    
    Scenario: Attempting to add a class without a description
        Given I am logged in as "instructor" with password "pass123"
        When I navigate to the "Add New Class" page
        And I fill in the class name with "Ballet 438"
        And I leave the description field empty
        And I fill in the class type with "group"
        And I submit the form
        Then I should see an error message "Description is required"

    Scenario: Attempting to add a class without selecting a class type
        Given I am logged in as "instructor" with password "pass123"
        When I navigate to the "Add New Class" page
        And I fill in the class name with "Hip Hop 101"
        And I fill in the description with "Introduction to Hip Hop"
        And I leave the class type field empty
        And I submit the form
        Then I should see an error message "Class type is required"