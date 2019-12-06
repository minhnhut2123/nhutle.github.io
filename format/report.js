$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("file:src/test/resources/features/api.feature");
formatter.feature({
  "name": "Send mail from user A to user B using gmail API",
  "description": "",
  "keyword": "Feature",
  "tags": [
    {
      "name": "@api"
    }
  ]
});
formatter.scenario({
  "name": "user A send mail to user B successfully",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@api"
    },
    {
      "name": "@tc1"
    }
  ]
});
formatter.step({
  "name": "user Sender with \"testskedulo.user1@gmail.com\" creates mail to send to user Receiver with \"testskedulo.user2@gmail.com\"",
  "keyword": "Given "
});
formatter.match({
  "location": "StepDefinitions.APITesting.user_Sender_with_creates_mail_to_send_to_user_Receiver_with(String,String) in file:/D:/WORK/Skedulo/NhutTest/target/test-classes/"
});
formatter.result({
  "error_message": "com.google.api.client.googleapis.json.GoogleJsonResponseException: 403 Forbidden\r\n{\r\n  \"code\" : 403,\r\n  \"errors\" : [ {\r\n    \"domain\" : \"global\",\r\n    \"message\" : \"Delegation denied for skedulotestuser1@gmail.com\",\r\n    \"reason\" : \"forbidden\"\r\n  } ],\r\n  \"message\" : \"Delegation denied for skedulotestuser1@gmail.com\"\r\n}\r\n\tat com.google.api.client.googleapis.json.GoogleJsonResponseException.from(GoogleJsonResponseException.java:150)\r\n\tat com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest.newExceptionOnError(AbstractGoogleJsonClientRequest.java:113)\r\n\tat com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest.newExceptionOnError(AbstractGoogleJsonClientRequest.java:40)\r\n\tat com.google.api.client.googleapis.services.AbstractGoogleClientRequest$1.interceptResponse(AbstractGoogleClientRequest.java:443)\r\n\tat com.google.api.client.http.HttpRequest.execute(HttpRequest.java:1092)\r\n\tat com.google.api.client.googleapis.services.AbstractGoogleClientRequest.executeUnparsed(AbstractGoogleClientRequest.java:541)\r\n\tat com.google.api.client.googleapis.services.AbstractGoogleClientRequest.executeUnparsed(AbstractGoogleClientRequest.java:474)\r\n\tat com.google.api.client.googleapis.services.AbstractGoogleClientRequest.execute(AbstractGoogleClientRequest.java:591)\r\n\tat ApiGmail.CommonKeyword.sendMessage(CommonKeyword.java:194)\r\n\tat StepDefinitions.APITesting.user_Sender_with_creates_mail_to_send_to_user_Receiver_with(APITesting.java:32)\r\n\tat ✽.user Sender with \"testskedulo.user1@gmail.com\" creates mail to send to user Receiver with \"testskedulo.user2@gmail.com\"(src/test/resources/features/api.feature:6)\r\n",
  "status": "failed"
});
formatter.step({
  "name": "user sends mail massage from \"testskedulo.user1@gmail.com\" to \"testskedulo.user2@gmail.com\" successfully",
  "keyword": "When "
});
formatter.match({
  "location": "StepDefinitions.APITesting.user_sends_mail_massage_from_to_successfully(String,String) in file:/D:/WORK/Skedulo/NhutTest/target/test-classes/"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "user Reseiver \"testskedulo.user2@gmail.com\" receives mail successfully",
  "keyword": "Then "
});
formatter.match({
  "location": "StepDefinitions.APITesting.user_Reseiver_receives_mail_successfully(String) in file:/D:/WORK/Skedulo/NhutTest/target/test-classes/"
});
formatter.result({
  "status": "skipped"
});
});