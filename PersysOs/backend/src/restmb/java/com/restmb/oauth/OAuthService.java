package com.restmb.oauth;

import com.restmb.oauth.service.OAuthRequest;
import com.restmb.oauth.service.Token;

/**
 * The main Scribe object. 
 * 
 * A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.  
 * 
 * @author Pablo Fernandez
 */
public interface OAuthService
{
 
  /**
   * Retrieve the access token
   * 
   * @param requestToken request token (obtained previously)
   * @param verifier verifier code
   * @return access token
   */
  public String getAccessToken(OAuthRequest request, Token token, String verifier);

  /**
   * Signs am OAuth request
   * 
   * @param accessToken access token (obtained previously)
   * @param request request to sign
   */
  public void signRequest(Token accessToken, OAuthRequest request);

  /**
   * Returns the OAuth version of the service.
   * 
   * @return oauth version as string
   */
  public String getVersion();
  
  public String getAuthorization( Token token, OAuthRequest request);
  
  public String getRequestToken(OAuthRequest request);
}
