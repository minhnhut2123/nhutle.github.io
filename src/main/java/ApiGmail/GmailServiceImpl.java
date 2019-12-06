package ApiGmail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.GmailScopes;

public final class GmailServiceImpl implements GmailService {

	public static final String APPLICATION_NAME = "SkeduloTest";
	public static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	public static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
	public static final String TOKENS_DIRECTORY_PATH = "src/main/resources";

	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	public final HttpTransport httpTransport;
	private GmailCredentials gmailCredentials;

    public GmailServiceImpl(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    @Override
    public void setGmailCredentials(GmailCredentials gmailCredentials) {
        this.gmailCredentials = gmailCredentials;
    }

    @Override
    public boolean sendMessage(String recipientAddress, String subject, String body) {
		return false;
       
    }

    
  @SuppressWarnings("unused")
public  static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        // Load client secrets.
        InputStream in //= GmailServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        = new FileInputStream("src/main/resources/credentials.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

}
