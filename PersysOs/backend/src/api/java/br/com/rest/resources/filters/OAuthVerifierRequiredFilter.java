package br.com.rest.resources.filters;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.oauth.model.RequestToken;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.represention.ErrorEntity;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class OAuthVerifierRequiredFilter implements ResourceFilter {

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return new ContainerRequestFilter() {
            @Override
            public ContainerRequest filter(ContainerRequest cr) {
            	String oauthToken = FilterHelper.extractValueFromKeyValuePairs("oauth_token", cr.getHeaderValue("Authorization"));
                String verifier = FilterHelper.extractValueFromKeyValuePairs("oauth_verifier", cr.getHeaderValue("Authorization"));
               
               if(!containsValidVerifier(oauthToken,verifier)) {
                    throw new WebApplicationException(javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).entity(new ErrorEntity("OAuthException",String.format("Invalid oauth_verifier [%s]", verifier))).type(MediaType.APPLICATION_JSON).build());
                }
                return cr;
            }

            private boolean containsValidVerifier(String oauthToken, String verifier) {
            	 if(verifier != null) {
            		Session session = HibernateHelper.openSession(getClass());
	            	Transaction tx = session.beginTransaction();
	            	
            		try{
	            		RequestToken token = RequestToken.pesquisaRequestToken(session, oauthToken);
	            		tx.commit();
	          		  	return token.getVerificationCode().equals(verifier);
            		}finally{
            			session.close();
                    	session = null;
                    	tx = null;
            		} 	
                }
                return false;
            }            
        };
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

}
