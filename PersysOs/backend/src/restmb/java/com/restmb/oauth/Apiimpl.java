package com.restmb.oauth;

import com.restmb.oauth.DefaultApi10a;
import com.restmb.oauth.service.Token;

@SuppressWarnings("unused")
public class Apiimpl extends DefaultApi10a {
	
      private static final String AUTHORIZE_URL = "http://localhost:8080/restfull/signIn/voucher/sabatine/1" +
      		"?oauth_token=%s";

	  public Apiimpl(){
		  
		  Token token = new Token(getAccessTokenEndpoint(), getRequestTokenEndpoint());
	  }
	  
	  public String getAccessTokenEndpoint()
	  {
	    return "http://localhost:8080/restfull/accessToken";
	  }

	  public String getRequestTokenEndpoint()
	  {
	    return "http://localhost:8080/restfull/requestToken";
	  }
	  
	  public String getAuthorizationUrl(Token requestToken)
	  {
	    return String.format(AUTHORIZE_URL, requestToken.getToken());
	  }
}

