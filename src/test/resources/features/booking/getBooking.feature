Feature: Get Booking
  Scenario: Get booking
    Given I have a booking
    When I get the booking by id
    Then the response code should be 200
    Then I should get the booking


  Scenario: Get booking with invalid id
    Given I have a booking
    When I get the booking by invalid id
    Then the response code should be 404
