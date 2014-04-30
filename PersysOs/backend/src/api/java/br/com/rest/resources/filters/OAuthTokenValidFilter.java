package br.com.rest.resources.filters;

import br.com.rest.resources.exception.PreconditionsREST;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class OAuthTokenValidFilter implements ResourceFilter {

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return new ContainerRequestFilter() {

            @Override
            public ContainerRequest filter(ContainerRequest cr) {
            	String authorizationHeader = cr.getHeaderValue("Authorization");
				PreconditionsREST.checkNotNull(authorizationHeader, "No consumer authorization Header");
				
				String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", authorizationHeader);
				PreconditionsREST.checkNotNull(consumerKey, "No consumer key");
					
			    String oauthValues = FilterHelper.extractValueFromKeyValuePairs("oauth_token", cr.getHeaderValue("Authorization"));
                PreconditionsREST.checkNotNull(oauthValues, "No token");
				return cr;
            	
            }
        };
    }
	
    @Override
    public ContainerResponseFilter getResponseFilter() {
        // TODO Auto-generated method stub
        return null;
    }

}