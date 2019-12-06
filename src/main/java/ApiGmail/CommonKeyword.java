package ApiGmail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.Thread;

public class CommonKeyword {
	
	private static final Logger log = Logger.getLogger(CommonKeyword.class);
    private static NetHttpTransport HTTP_TRANSPORT;

    private static Gmail service;

	public CommonKeyword() {
		service = null;
		HTTP_TRANSPORT = null;
	}

	 void authentication() throws Exception {
	    HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Gmail.Builder(HTTP_TRANSPORT, GmailServiceImpl.JSON_FACTORY,
				GmailServiceImpl.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(GmailServiceImpl.APPLICATION_NAME)
                .build();
    }


	private MimeMessage createEmail(String to, String from, String subject, String bodyText)
			throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}
    
	
	/**
     * Create a message from an email.
     *
     * @param email Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    private Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
    	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	    email.writeTo(baos);
    	    String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
    	    Message message = new Message();
    	    message.setRaw(encodedEmail);
    	    return message;
    }
    
    
    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param file Path to the file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    private MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText, File file)
            throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);

        return email;
    }


	/**
	 * Create draft email.
	 *
	 * @param emailContent the MimeMessage used as email within the draft
	 * @return the created draft
	 * @throws MessagingException
	 * @throws IOException
	 */
	private Draft createDraft(MimeMessage emailContent)
            throws Exception {
		Message message = createMessageWithEmail(emailContent);
		Draft draft = new Draft();
		draft.setMessage(message);
		draft = service.users().drafts().create("me", draft).execute();

		return draft;
	}



	/**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param file Path to the file to be attached.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
	public ResponseResult sendMessage(String to, String from, String subject, String bodyText, File file) {
		try {
			authentication();
			MimeMessage email = null;
			if (file != null) {
				email = createEmailWithAttachment(to, from, subject, bodyText, file);
			} else {
				email = createEmail(to, from, subject, bodyText);
			}
			Message message = createMessageWithEmail(email);
			message = service.users().messages().send(from, message).execute();
//    	        getAttachments(message.getId());
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseResult.FAILED;
		}

		return ResponseResult.SUCCESS;
	}


	/**
	 * List all Messages of the user's mailbox matching the query.
	 *
	 * @param userId User's email address. The special value "me"
	 * can be used to indicate the authenticated user.
	 * @param query String used to filter the Messages listed.
	 * @throws IOException
	 */
	public List<Message> listMessagesMatchingQuery(String userId, String query) throws Exception {
		ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(userId).setQ(query)
						.setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

		return messages;
	}


	/**
	 * List all Messages of the user's mailbox with labelIds applied.
	 *
	 * @param labelIds Only return Messages with these labelIds applied.
	 * @throws IOException
	 */
	public List<Message> listMessagesWithLabels(List<String> labelIds) throws Exception {
		ListMessagesResponse response = service.users().messages().list("me")
				.setLabelIds(labelIds).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list("me").setLabelIds(labelIds)
						.setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

		return messages;
	}

    /**
     * List latest Messages of the user's mailbox with labelIds applied.
     *
     * @param labelIds Only return Messages with these labelIds applied.
     * @throws IOException
     */
	private Message getLatestMessageWithLabels(List<String> labelIds) throws Exception {
		ListMessagesResponse response = service.users().messages().list("me").setIncludeSpamTrash(false)
				.setLabelIds(labelIds).setMaxResults(1L).execute();

		if(response.getResultSizeEstimate() == 0) {
			return null;
		}
		
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(response.getMessages());

		return messages.get(0);
	}
	
	public MimeMessage getLatestIncommingFullMessage()
		      throws Exception {
		    authentication();
			List<String> labels = new ArrayList<String>();
			labels.add("INBOX");
		    Message rawMessage = getLatestMessageWithLabels(labels);
		    
		    if (rawMessage == null) {
		    	return null;
		    }
		    
		    Message message = service.users().messages().get("me", rawMessage.getId()).setFormat("raw").execute();

		    Base64 base64Url = new Base64(true);
		    byte[] emailBytes = base64Url.decodeBase64(message.getRaw());

		    Properties props = new Properties();
		    Session session = Session.getDefaultInstance(props, null);

		    MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

		    return email;
		  }
	
	public int countIncomingEmails() throws Exception {
		List<String> labels = new ArrayList<String>();
		labels.add("INBOX");
		
		ListMessagesResponse response = service.users().messages().list("me").setIncludeSpamTrash(false)
				.setLabelIds(labels).execute();
		
		if(response.getResultSizeEstimate() == 0) {
			return 0;
		}

		List<Message> messages = new ArrayList<Message>();
		messages.addAll(response.getMessages());

		return messages.size();
	}
	
	
	private List<String> listThreadsWithIncomingLabels () throws Exception {
		List<String> labels = new ArrayList<String>();
		labels.add("INBOX");
		ListThreadsResponse response = service.users().threads().list("me").setLabelIds(labels).execute();
		List<Thread> threads = new ArrayList<Thread>();
		List<String> threadIds = new ArrayList<String>();
		while(response.getThreads() != null) {
			threads.addAll(response.getThreads());
			if(response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().threads().list("me").setLabelIds(labels)
						.setPageToken(pageToken).execute();
			} else {
				break;
			}
		}
		
		for (int i = 0; i < threads.size(); i++) {
			threadIds.add(threads.get(i).getId());
		}
		
		return threadIds;
	}
	
	/**
	 * Immediately and permanently deletes the specified thread. This operation cannot
	 * be undone. Prefer threads.trash instead.
	 *
	 * @throws IOException
	 */
	public ResponseResult deleteAllIncomingEmail(){
		try {
			authentication();
			List<String> threadIds = listThreadsWithIncomingLabels();
			for (int i = 0; i < threadIds.size(); i++) {
				service.users().threads().delete("me", threadIds.get(i)).execute();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseResult.FAILED;
		}

		return ResponseResult.SUCCESS;
	}
	

	/**
	 * List latest Inbox Messages of the user's mailbox.
	 *
	 * @throws Exception
	 */
	public Message getLatestIncomingEmail() throws Exception {
		authentication();
		List<String> labels = new ArrayList<String>();
		labels.add("INBOX");
		return getLatestMessageWithLabels(labels);

	}


	/**
	 * Get Message with given ID.
	 *
	 * @param messageId ID of Message to retrieve.
	 * @return Message Retrieved Message.
	 * @throws IOException
	 */
	public Message getMessage(String messageId)
            throws Exception {
		Message message = service.users().messages().get("me", messageId).execute();

		return message;
	}


	/**
	 * Get the attachments in a given email.
	 *
	 * @param messageId ID of Message containing attachment..
	 * @throws IOException
	 */
	public String getAttachments(String messageId)
            throws Exception {
        Path currentDir = Paths.get("src/test/sources/","receiveAttachments");
        String fileDir = "";
        Message message = service.users().messages().get("me", messageId).execute();
		List<MessagePart> parts = message.getPayload().getParts();
		for (MessagePart part : parts) {
			if (part.getFilename() != null && part.getFilename().length() > 0) {
				String filename = part.getFilename();
				String attId = part.getBody().getAttachmentId();
				MessagePartBody attachPart = service.users().messages().attachments().
						get("me", messageId, attId).execute();

				Base64 base64Url = new Base64(true);
				byte[] fileByteArray = base64Url.decodeBase64(attachPart.getData());
				fileDir = currentDir.toAbsolutePath() + "\\" + filename;
				FileOutputStream fileOutFile =
						new FileOutputStream(currentDir.toAbsolutePath() + "\\" + filename);
				fileOutFile.write(fileByteArray);
				fileOutFile.close();
			}
		}
		return fileDir;
	}


	private String getChecksumStringFromFile(String file) throws IOException, NoSuchAlgorithmException {
		//Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);

		MessageDigest digest = MessageDigest.getInstance("MD5");

		//Create byte array to read data in chunks
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		//Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		};

		//close the stream; We don't need it now.
		fis.close();

		//Get the hash's bytes
		byte[] bytes = digest.digest();

		//This bytes[] has bytes in decimal format;
		//Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< bytes.length ;i++)
		{
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		//return complete hash
		return sb.toString();
	}


	public boolean fileComparation(String external, String internal) throws IOException, NoSuchAlgorithmException {
		return getChecksumStringFromFile(external).equals(getChecksumStringFromFile(internal));
	}
	
	public boolean checkResponse(ResponseResult actualResponse) {
		if(actualResponse == ResponseResult.SUCCESS){
			return true;
		}
		return false;
	}



}
