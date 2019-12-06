package StepDefinitions;
import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;

import com.google.api.services.gmail.model.Message;

import ApiGmail.CommonKeyword;
import ApiGmail.ResponseResult;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class APITesting{
	private CommonKeyword commonKeyword = new CommonKeyword();
	private final static String subject = "User testskedulo1 send to User testskedulo2";
	private final static String bodyText = "I send you screenshot for new car that attached in mail";
	private final static File file = new File("src/test/sources/sendAttachments/newCar.jpg");
	private final static String sendFile = "src/test/sources/sendAttachments/newCar.jpg";
	private final static String ReceiveDir = "src/test/sources/receiveAttachments";
	
	
	@Given("user Sender with {string} send mail with attachment to user Receiver with {string}")
	public void user_Sender_with_creates_mail_to_send_to_user_Receiver_with(String from, String to) throws Throwable{
		commonKeyword.deleteAllIncomingEmail();
		assertEquals(0, commonKeyword.countIncomingEmails());
		System.out.println("Value of from: " + from);
		System.out.println("Value of To: " + to);
		ResponseResult response = commonKeyword.sendMessage(to, from, subject, bodyText, file);
        System.out.println("Message: " + commonKeyword.checkResponse(response));
        assertEquals(ResponseResult.SUCCESS, response);
	}


	@When("user {string} receives mail from user {string} successfully")
	public void user_receives_mail_fro_user_successfully(String to, String from) throws Throwable{
        MimeMessage message = commonKeyword.getLatestIncommingFullMessage();
		assertEquals(1, commonKeyword.countIncomingEmails());
		Message latestInboxEmail = commonKeyword.getLatestIncomingEmail();
        assertEquals(from,message.getHeader("From")[0].toString());
        assertEquals(to,message.getHeader("To")[0].toString());
        assertEquals(subject,message.getHeader("Subject")[0].toString());
        assertEquals(bodyText,commonKeyword.getMessage(latestInboxEmail.getId()).getSnippet());
	}
	
	@Then("attachment is correct")
	public void attachment_is_correct() {
		Message latestInboxEmail;
		try {
			FileUtils.cleanDirectory(new File(ReceiveDir));
			latestInboxEmail = commonKeyword.getLatestIncomingEmail();
			String attachmentFile = commonKeyword.getAttachments(latestInboxEmail.getId());
			System.out.println("Attachment: " + attachmentFile);
			System.out.println("checksum is: " + commonKeyword.fileComparation(attachmentFile, sendFile));
			assertEquals(true,commonKeyword.fileComparation(attachmentFile, sendFile));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}