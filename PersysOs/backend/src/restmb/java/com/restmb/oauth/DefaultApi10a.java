package com.restmb.oauth;

import com.restmb.oauth.extractors.AccessTokenExtractor;
import com.restmb.oauth.extractors.BaseStringExtractor;
import com.restmb.oauth.extractors.BaseStringExtractorImpl;
import com.restmb.oauth.extractors.HeaderExtractor;
import com.restmb.oauth.extractors.HeaderExtractorImpl;
import com.restmb.oauth.extractors.RequestTokenExtractor;
import com.restmb.oauth.extractors.TokenExtractorImpl;
import com.restmb.oauth.service.HMACSha1SignatureService;
import com.restmb.oauth.service.OAuthConfig;
import com.restmb.oauth.service.SignatureService;
import com.restmb.oauth.service.TimestampService;
import com.restmb.oauth.service.TimestampServiceImpl;
import com.restmb.oauth.service.Token;

/**
 * Default implementation of the OAuth protocol, version 1.0a
 * 
 * This class is meant to be extended by concrete implementations of the API,
 * providing the endpoints and endpoint-http-verbs.
 * 
 * If your Api adheres to the 1.0a protocol correctly, you just need to extend 
 * this class and define the getters for your endpoints.
 * 
 * If your Api does something a bit different, you can override the different 
 * extractors or services, in order to fine-tune the process. Please read the 
 * javadocs of the interfaces to get an idea of what to do.
 * 
 * @author Pablo Fernandez
 *
 */
public abstract class DefaultApi10a implements Api
{
	  /**
	   * Returns the access token extractor.
	   * 
	   * @return access token extractor
	   */
	  public AccessTokenExtractor getAccessTokenExtractor()
	  {
	    return new TokenExtractorImpl();
	  }

	  /**
	   * Returns the base string extractor.
	   * 
	   * @return base string extractor
	   */
	  public BaseStringExtractor getBaseStringExtractor()
	  {
	    return new BaseStringExtractorImpl();
	  }

	  /**
	   * Returns the header extractor.
	   * 
	   * @return header extractor
	   */
	  public HeaderExtractor getHeaderExtractor()
	  {
	    return new HeaderExtractorImpl();
	  }

	  /**
	   * Returns the request token extractor.
	   * 
	   * @return request token extractor
	   */
	  public RequestTokenExtractor getRequestTokenExtractor()
	  {
	    return new TokenExtractorImpl();
	  }

	  /**
	   * Returns the signature service.
	   * 
	   * @return signature service
	   */
	  public SignatureService getSignatureService()
	  {
	    return new HMACSha1SignatureService(); 
	  }

	  /**
	   * Returns the timestamp service.
	   * 
	   * @return timestamp service
	   */
	  public TimestampService getTimestampService()
	  {
	    return new TimestampServiceImpl();
	  }
	  
	  
	  /**
	   * Returns the URL that receives the request token requests.
	   * 
	   * @return request token URL
	   */
	  public abstract String getRequestTokenEndpoint();
	  
	  /**
	   * Returns the URL that receives the access token requests.
	   * 
	   * @return access token URL
	   */
	  public abstract String getAccessTokenEndpoint();
	  
	  /**
	   * Returns the URL where you should redirect your users to authenticate
	   * your application.
	   * 
	   * @param requestToken the request token you need to authorize
	   * @return the URL where you should redirect your users
	   */
	  public abstract String getAuthorizationUrl(Token requestToken);
	  
	   /**
	   * Returns the {@link OAuthService} for this Api
	   * 
	   * @param apiKey Key
	   * @param apiSecret Api Secret
	   * @param callback OAuth callback (either URL or 'oob')
	   * @param scope OAuth scope (optional) 
	   */
	  public OAuthService createService(OAuthConfig config)
	  {
	    return new OAuth10aServiceImpl(this, config);
	  }
	}