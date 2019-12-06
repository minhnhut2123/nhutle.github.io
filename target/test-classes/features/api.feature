@api
Feature: Send mail from user A to user B using gmail API
  
  @tc1
  Scenario: user A send mail to user B successfully
    Given user Sender with "skedulotestuser1@gmail.com" send mail with attachment to user Receiver with "skedulotestuser2@gmail.com"
    When user "skedulotestuser2@gmail.com" receives mail from user "skedulotestuser1@gmail.com" successfully
    Then attachment is correct

