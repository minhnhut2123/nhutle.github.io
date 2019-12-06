I.Setup Enviroment
1. Install JDK 1.8.231
2. Install Maven 3.5.4
3. Install latest Eclipse
4. Install maven plugin for Eclipse
5. Install cucumber plugin for Eclipse

I. API Testing
1. Login user A on browser (skedulotestuser1@gmail.com/P@ssword1!)
2. Login user B on browser (skedulotestuser2@gmail.com/P@ssword1!)

Note: Currently I cannot find solution to get token and refresh_token automatically so we need some manual steps when run the scenarios.
Scenarios check:
- User B delete mailbox
- User A send mail with attachment to User B
- User B get mail and check (sender mail address, subject, body content)
- User B get attachment and check sum attachment

3. Execute scenarios
- Use maven command: mvn clean test verify -Dcucumber.options="src/test/resources/features/api.feature"
- 1st time, when scenarios start it request a popup to authenticate user login with User B to delete mail
- 2nd time, popup shows to login User A to send email and attachment to user B
- 3rd time, popup shows to login User B to get the latest comming mail
- 4th timne, popup shows to login User B to get the attachment to verify expected attachment via checksum

II. WebUI Testing
- Use maven command: mvn clean test verify -Dcucumber.options="src/test/resources/features/selenium.feature"
- Every step verify I add step to take evidence screenshot then save to target/cucumber-reports/evidence

III. Report Log
- Report html is generated automatically via maven command and save in target/cucumber-reports/advanced-reports-html/cucumber-html-reports with 2 files:
+ report-feature_file-src-test-resources-features-api-feature.html
+ report-feature_file-src-test-resources-features-selenium-feature.html
