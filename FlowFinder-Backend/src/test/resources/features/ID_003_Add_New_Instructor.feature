Feature: Add new instructor
    As the application manager
    I want to be able to add a new instructor in the system
    So that instructors can be assigned to classes

Scenario: Successful add a new instructor
    Given I give all the required details for the instructor
    And I have selected at least one class
    When I click the "Add" button
    Then a instructor should be added in the system
    And the instructor should be assigned  to selected classes


Scenario: Failed attempt to add a new instructor without classes
    Given I have entered all required details for the instructor
    But I have not selected any classes
    When I click the "Add" button
    Then I should see an error message "At least one class must be assigned"
    And the instructor should not be added to the system