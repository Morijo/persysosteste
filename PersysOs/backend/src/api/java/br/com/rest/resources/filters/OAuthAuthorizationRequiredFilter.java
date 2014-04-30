package br.com.rest.resources.filters;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.oauth.model.AccessToken;
import br.com.oauth.model.ConsumerSecret;
import br.com.oauth.model.RequestToken;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.resources.exception.PreconditionsREST;

import com.sun.jersey.oauth.server.OAuthServerRequest;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.sun.jersey.oauth.signature.OAuthSignature;
import com.sun.jersey.oauth.signature.OAuthSignatureException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class OAuthAuthorizationRequiredFilter implements ResourceFilter {

	public ContainerRequestFilter getRequestFilter() {
		return new ContainerRequestFilter() {


			@Override
			public ContainerRequest filter(ContainerRequest cr) {
				
				String authorizationHeader = cr.getHeaderValue("Authorization");
				PreconditionsREST.checkNotNull(authorizationHeader, "No consumer authorization Header");
				
				String consumerKey = extractConsumerKey(authorizationHeader);
				PreconditionsREST.checkNotNull(consumerKey, "No consumer key");
					
			    String oauthValues = FilterHelper.extractValueFromKeyValuePairs("oauth_token", cr.getHeaderValue("Authorization"));
                PreconditionsREST.checkNotNull(oauthValues, "No token");
            	
				OAuthParameters params = new OAuthParameters();
				OAuthServerRequest request = new OAuthServerRequest(cr);
				params.readRequest(request);

				Session session = HibernateHelper.openSession(getClass());
				Transaction tx = session.beginTransaction();
				ConsumerSecret consumerSecret = validateConsumerKey(session, consumerKey, cr);
				OAuthSecrets secrets = setupSecretsFor(session, authorizationHeader, consumerSecret);
				tx.commit();
				session.close();
				session = null;
				tx = null;
				
				try {
					if (!OAuthSignature.verify(request, params, secrets)) {
						throw new WebApplicationException(Response.status(401).entity(new ErrorEntity("OAuthException","Failed to verify signature")).type(MediaType.APPLICATION_JSON).build());
					}
				} catch (OAuthSignatureException e) {
					throw new WebApplicationException(e, 500);
				}
				
				return cr;
			}

			private OAuthSecrets setupSecretsFor(Session session, String authorizationHeader,ConsumerSecret consumerSecret) {

				OAuthSecrets secrets = new OAuthSecrets();
				secrets.setConsumerSecret(consumerSecret.getConsumerSecret());

				RequestToken rt = RequestToken.pesquisaRequestToken(session, extractOAuthToken(authorizationHeader));
				AccessToken at = AccessToken.pesquisaAccessToken(session, extractOAuthToken(authorizationHeader));

				if (rt != null) {
					secrets.setTokenSecret(rt.getSecret().toString());
				} else if (at != null) {
					secrets.setTokenSecret(at.getSecret());
				}
				return secrets;
			}

			private ConsumerSecret validateConsumerKey(Session session, String consumerKey, ContainerRequest cr) {
				
				ConsumerSecret consumerSecret = ConsumerSecret.containsKey(session, consumerKey);

				if (consumerSecret == null) {
					throw new WebApplicationException(Response.status(401).header("WWW-Authenticate", "OAuth realm=\"" + cr.getBaseUri().toString() + "\"").entity(
							String.format("No consumer key [%s] registered with service.", consumerKey.toString())).build());
				}else{
					return consumerSecret;
				}
				
				
			}

			private String extractOAuthToken(String header) {
				return FilterHelper.extractValueFromKeyValuePairs("oauth_token", header);
			}

			private String extractConsumerKey(String header) {
				String consumerKeyValue = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", header);
				return consumerKeyValue == null ? null : consumerKeyValue;
			}
		};
	}

	public ContainerResponseFilter getResponseFilter() {
		return null;
	}

}
