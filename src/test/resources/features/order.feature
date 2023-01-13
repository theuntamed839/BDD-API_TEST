Feature: Order pets
  Scenario: send an order
    Given that john is interested in buying pet, get "dog" id
    Then he sends an order for the dog with quantity as "5"
    Then he should get the order id which shouldn't be null or 0
    Then he should be able to get the order details using the order id which should be same as the order sent

