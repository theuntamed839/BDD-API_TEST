Feature: Deleting the booking
  Scenario: We will try to update a booking
    Given I have a booking
    When I get the booking by id
    Then I update the booking with new values for firstname "Ganesh" and lastname "gannu"
    Then the response code should be 200