package ApiGmail;

public class GmailCredentials {
	
	private String userEmail;
    
	private String clientId;
    
    private String clientSecret;
    
    private String accessToken;
    
    private String refreshToken;
    
    public GmailCredentials(String userEmail, String clientId, String clientSecret, String accessToken,
			String refreshToken) {
		super();
		this.userEmail = userEmail;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}


    public String getUserEmail() {
		return userEmail;
	}
    
	public String getClientId() {
		return clientId;
	}
	
	public String getClientSecret() {
		return clientSecret;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
}