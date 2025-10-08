Feature: Register as a New Client

As a potential client
I want to register for the FlowFinder system
So that I can become a student and participate in dance classes

    Scenario: Successful registration with valid information
        When the client provides valid email, password, name, and bio
        Then their account should be created
        And they should be marked as a registered client
        And they should have access to the list of available classes

    Scenario: Registration with an email that is already in use
        Given a client is already registered with a valid email
        When they attempt to register again using the same email
        Then the system should reject the registration
        And they should be informed that the email is already in use

    Scenario: Registration with invalid or incomplete details
        When the client provides invalid or incomplete required registration details
        Then the system should reject the registration
        And they should be informed of the specific validation errors

    Scenario: Registration with optional profile information skipped
        When the client provides only the required registration details
        And they do not provide optional bio
        Then their account should be created
        And the system should mark the bio as incomplete
        And the client should be able to update their profile later

        