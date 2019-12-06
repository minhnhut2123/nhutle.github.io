@selenium
Feature: Amazon shopping

  @tc1
  Scenario: Go to Amazon then select some items and proceed to checkout
    Given User goes to Amazon with url "https://www.amazon.com.au/"
    When Select "Today's Deals" menu
    And Sorts the items by "Discount - High to Low"
    And Click on button "View Deal"
    And Click on first deal item
    And Add 2 items into cart
    Then Verify 2 items added into cart
    When Go back to main page
    And Search for "AAA Batteries​"
    And Sort the items by "Featured"
    When Click on the "Best Seller" product
    And Add 5 items into the cart
    Then Verify 7 items added into cart
    When Go to Cart
    Then Verify the individual price of each item
    And Verify the subtotal price of all item
    When Edit the first item quantity to 1
    And Edit the second item quantity to 3
    Then Verify the individual price of each item
    And Verify the subtotal price of all item
    When delete the "first" item
    Then Verify the subtotal price of all item
    When Click Proceed to Checkout button
    Then Verify "Sign-In" form displays
    