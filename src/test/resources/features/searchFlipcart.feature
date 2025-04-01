Feature: Search for wireless headphones and save info in csv file

  Scenario Outline: Search for wireless headphones and save info in csv file
    Given user login flipcart app
    When user closes login popup
    And user search for item "<searchItem>"
    And user selects price range "<minPrice>" and "<maxPrice>"
    And user selects rating "<brandRating>"
    And user selects headphone type "<headphoneType>"
    And user selects brand "<brand>"
    Then user is displayed with search resluts
    When user collects products info
    And user saves products info in csv file
    And user fetches products info from csv
    Then user clicks each product and checks product info "<searchItem>"

    Examples:
      | searchItem          | brandRating | headphoneType|minPrice | maxPrice | brand |
      | wireless headphones | 4★ & above  |        In the Ear      | ₹600        | ₹1000        | boAt  |