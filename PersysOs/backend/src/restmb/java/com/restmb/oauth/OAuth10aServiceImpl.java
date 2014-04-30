package com.restmb.oauth;

import java.util.*;

import com.restmb.oauth.service.Base64Encoder;
import com.restmb.oauth.service.OAuthConfig;
import com.restmb.oauth.service.OAuthConstants;
import com.restmb.oauth.service.OAuthRequest;
import com.restmb.oauth.service.Token;
import com.restmb.util.MapUtils;

/**
 * OAuth 1.0a implementation of {@link OAuthService}
 *
 * @author Pablo Fernandez
 */
public class OAuth10aServiceImpl implements OAuthService
{
  private static final String VERSION = "1.0";

  private OAuthConfig config;
  private DefaultApi10a api;

  /**
   * Default constructor
   *
   * @param api OAuth1.0a api information
   * @param config OAuth 1.0a configuration param object
   */
  public OAuth10aServiceImpl(DefaultApi10a api, OAuthConfig config)
  {
    this.api = api;
    this.config = config;
  }


  private void addOAuthParams(OAuthRequest request, Token token)
  {
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
    request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
    request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
    request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
    if(config.hasScope()) request.addOAuthParameter(OAuthConstants.SCOPE, config.getScope());
    request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));

    config.log("appended additional OAuth parameters: " + MapUtils.toString(request.getOauthParameters()));
  }

  
  public String getAccessToken(OAuthRequest request, Token token, String verifier)
  {
	  if (!token.isEmpty())
		{
		   request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
		   request.addOAuthParameter(OAuthConstants.VERIFIER, verifier);
		}
		addOAuthParams(request, token);
	    return api.getHeaderExtractor().extract(request);
	  }

  /**
   * {@inheritDoc}
   */
  public void signRequest(Token token, OAuthRequest request)
  {
   
	 if (!token.isEmpty())
    {
      request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
    }
    config.log("setting token to: " + token);
    addOAuthParams(request, token);
    appendSignature(request);
  }

  /**
   * {@inheritDoc}
   */
  public String getVersion()
  {
    return VERSION;
  }

  /**
   * {@inheritDoc}
   */
  public String getAuthorization( Token token, OAuthRequest request)
  {
	if (!token.isEmpty())
	{
	   request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
	}
	addOAuthParams(request, token);
    return api.getHeaderExtractor().extract(request);
  }
  
  public String getRequestToken(OAuthRequest request)
  {
	 request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
	 addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
	 return api.getHeaderExtractor().extract(request);
  }

  private String getSignature(OAuthRequest request, Token token)
  {
    config.log("generating signature...");
    config.log("using base64 encoder: " + Base64Encoder.type());
    String baseString = api.getBaseStringExtractor().extract(request);
    String signature = api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.getSecret());

    config.log("base string is: " + baseString);
    config.log("signature is: " + signature);
    return signature;
  }

  public String oauthHeader(OAuthRequest request){
	  return api.getHeaderExtractor().extract(request);
  }
  
  private void appendSignature(OAuthRequest request)
  {
    switch (config.getSignatureType())
    {
      case Header:
        config.log("using Http Header signature");

        String oauthHeader = api.getHeaderExtractor().extract(request);
        request.addHeader(OAuthConstants.HEADER, oauthHeader);
        break;
      case QueryString:
        config.log("using Querystring signature");

        for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet())
        {
          request.addQuerystringParameter(entry.getKey(), entry.getValue());
        }
        break;
    }
  }

}
