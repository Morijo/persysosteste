package com.restmb.oauth;

public class OAuth {

	private String tokenKey;
	private String tokenSecret;
	private String apiKey;
    private String apiSecret;
    private String url;
    private String verbHttp;
    private String verifier;
    
    private com.restmb.oauth.OAuthService service;
    
    public OAuth(String tokenKey, String tokenSecret, String apiKey,
			String apiSecret) {
		super();
		this.tokenKey = tokenKey;
		this.tokenSecret = tokenSecret;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	
    }
    
    public OAuth(String apiKey,
			String apiSecret) {
		super();
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	
    }
    
    protected com.restmb.oauth.OAuthService serviceBuild() {
    	return new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret).build();
		
	}
    
    protected com.restmb.oauth.service.Token accessToken() {
    	try{
    	return new com.restmb.oauth.service.Token( tokenKey, tokenSecret);
    	}catch(Exception e){
    		return null;
    	}
	}
    
    protected com.restmb.oauth.service.OAuthRequest obtToken() {
    	return new com.restmb.oauth.service.OAuthRequest(verbHttp, url);
    }
    
    public String getSignRequest(){
    	service = serviceBuild();
    	if(accessToken() == null){
        	return service.getAuthorization(null,obtToken());
    	}
    	return service.getAuthorization(accessToken(), obtToken());
    }
    
    public String getRequestToken(){
    	service = serviceBuild();
        return service.getRequestToken(obtToken());
    }
    
    public String getAccessToken(){
    	service = serviceBuild();
        return service.getAccessToken(obtToken(), accessToken(), verifier);
    }
 
   	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVerbHttp() {
		return verbHttp;
	}

	public void setVerbHttp(String verbHttp) {
		this.verbHttp = verbHttp;
	}
	
	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
}
