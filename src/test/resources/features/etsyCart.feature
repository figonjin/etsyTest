@etsyCart
  Feature: Add specific products to cart

   Scenario Outline: The most expensive <product1> and any <product2> are added to cart
      Given User navigates to "https://etsy.com"
      When User searches for "<product1>"
      And User sorts by ascending prices
      And User adds the most expensive "<product1>" to the cart
      And User searches for "<product2>"
      And User adds any "<product2>" to the cart
      Then the added items should be listed in the cart

     Examples:
     | product1     | product2        |
     | Sketchbook   | Turntable Mat   |