package com.restmb.oauth.extractors;

import com.restmb.oauth.service.OAuthRequest;

/**
 * Simple command object that generates an OAuth Authorization header to include in the request.
 * 
 * @author Pablo Fernandez
 */
public interface HeaderExtractor
{
  /**
   * Generates an OAuth 'Authorization' Http header to include in requests as the signature.
   * 
   * @param request the OAuthRequest to inspect and generate the header
   * @return the Http header value
   */
  String extract(OAuthRequest request);
}
