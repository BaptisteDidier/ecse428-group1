Feature: Booking a Group Class from a Client

As a Student  
I would like to book a group dance class  
So that I can participate in group dance sessions with other students  

    Background:
        Given the following users exist:
        | username | password | role     |
        | alice    | pass123  | STUDENT  |
        | bob      | pass456  | STUDENT  |
        | clara    | pass789  | INSTRUCTOR |
        
        And the following classes exist:
        | className        | description                          | instructorUsername | classType | availableSpots | schedule            |
        | Hip Hop 101      | Introduction to Hip Hop               | clara              | group     | 10              | 2025-10-10 18:00   |
        | Ballet Basics    | Beginner ballet for posture and form  | clara              | group     | 0               | 2025-10-12 14:00   |
        | Tango Private    | One-on-one tango lessons              | clara              | private   | 1               | 2025-10-11 10:00   |

    Scenario: Successfully booking a group class
        Given I am logged in as "alice" with password "pass123"
        When I navigate to the "Available Group Classes" page
        And I select the class "Hip Hop 101"
        And I click the "Book Class" button
        And I confirm the payment
        Then I should see a confirmation message "Booking confirmed for Hip Hop 101"
        And my booked classes list should include "Hip Hop 101"
        And the number of available spots for "Hip Hop 101" should decrease by 1

    Scenario: Attempting to book a group class that is full
        Given I am logged in as "bob" with password "pass456"
        When I navigate to the "Available Group Classes" page
        And I select the class "Ballet Basics"
        And I click the "Book Class" button
        Then I should see an error message "This class is fully booked"
        And I should not be charged

    Scenario: Attempting to book without being logged in
        Given I am not logged in
        When I navigate to the "Available Group Classes" page
        And I select the class "Hip Hop 101"
        And I click the "Book Class" button
        Then I should be redirected to the login page
        And I should see a message "Please log in to continue"

    Scenario: Successfully cancelling a booked group class more than 24 hours before start time
        Given I am logged in as "alice" with password "pass123"
        And I have already booked the class "Hip Hop 101"
        When I navigate to my "Booked Classes" page
        And I click "Cancel" for "Hip Hop 101"
        And the class start time is more than 24 hours away
        Then I should see a confirmation message "Booking cancelled and refunded"
        And the number of available spots for "Hip Hop 101" should increase by 1

    Scenario: Attempting to cancel less than 24 hours before start time
        Given I am logged in as "alice" with password "pass123"
        And I have already booked the class "Hip Hop 101"
        When I navigate to my "Booked Classes" page
        And I click "Cancel" for "Hip Hop 101"
        And the class start time is less than 24 hours away
        Then I should see a warning message "Late cancellation — a fee will be charged"
        And the booking should be removed from my booked classes
