Feature: Post pet
  Scenario: posting pet
    Given Kitty is available in the pet store
    When I ask for a pet using Kitty's ID
    Then I get Kitty as result

  Scenario: providing a non-existing pet ID
    When I ask for a pet using a "non-existing" ID "0"
    Then I get a 404 error

  Scenario: providing a non-numeric pet ID
    When I ask for a pet using a "non-numeric" ID "A"
    Then I get a 404 error

  Scenario: providing a negative pet ID
    When I ask for a pet using a "negative" ID "-1"
    Then I get a 404 error
