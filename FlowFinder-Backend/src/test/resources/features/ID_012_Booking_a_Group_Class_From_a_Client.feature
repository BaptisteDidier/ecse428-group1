Feature: Booking a Group Class as a Client

  As a client  
  I want to book a group dance class  
  So that I can learn and participate in group dance sessions with other students  

  Background:
    Given a client exists with username "client1"
    And an instructor exists with username "instructor1"
    And a dance class exists called "danceClassName1" of genre "danceGenre1"
    And a specific class exists in "Studio A" on a future date with start time 10:00 and end time 11:00
    And the specific class has a limit of 12 participants

  Scenario: Successful registration
    When the client books the specific class
    Then the registration is successfully created
    And the client is associated with the class

  Scenario: Booking fails if client is null
    When no client information is provided
    Then the system returns an error "Client cannot be null"

  Scenario: Booking fails if class does not exist
    When the client attempts to book a non-existent class
    Then the system returns an error "Specific Class cannot be null"

  Scenario: Booking fails if client already registered
    Given the client is already registered for the specific class
    When the client attempts to book the same class again
    Then the system returns an error "Client is already registered for this class"

  Scenario: Booking fails if class is full
    Given the specific class has reached its maximum capacity
    When the client attempts to book the class
    Then the system returns an error "This class is full"
    And no new registration is created