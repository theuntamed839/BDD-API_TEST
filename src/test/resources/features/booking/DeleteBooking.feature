Feature: Deleting the booking
  Scenario: We will try to delete a booking
    Given I have a booking
    When I get the booking by id
    Then I delete the booking
    Then the response code should be 201