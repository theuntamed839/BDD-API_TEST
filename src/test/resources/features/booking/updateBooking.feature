Feature: update Booking
  Scenario: update booking using api
    Given that token is generated
    When user sends a PUT request to booking with id as "1" following payload first_name as "John", last_name as "Doe", total_price as "100", deposit_paid as "true", checkin as "2020-01-01", checkout as "2020-01-02"
    Then the response status code should be 200

  Scenario: update booking using api with a invalid token
    Given that token is generated which is invalid
    When user sends a PUT request to booking with id as "1" following payload first_name as "John", last_name as "Doe", total_price as "100", deposit_paid as "true", checkin as "2020-01-01", checkout as "2020-01-02"
    Then the response status code should be 403

  Scenario: update booking using api with a invalid id
    Given that token is generated
    When user sends a PUT request to booking with id as "-1100" following payload first_name as "John", last_name as "Doe", total_price as "100", deposit_paid as "true", checkin as "2020-01-01", checkout as "2020-01-02"
    Then the response status code should be 405

  Scenario: update booking using api with a invalid payload as doposit_paid not present
    Given that token is generated
    When user sends a PUT request to booking with id as "1" following payload first_name as "John", last_name as "Doe", total_price as "100", checkin as "2020-01-01", checkout as "2020-01-02"
    Then the response status code should be 400

