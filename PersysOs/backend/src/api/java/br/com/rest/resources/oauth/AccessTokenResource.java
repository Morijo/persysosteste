package br.com.rest.resources.oauth;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.exception.ModelException;
import br.com.oauth.model.AccessToken;
import br.com.oauth.model.RequestToken;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthNonceRequiredFilter;
import br.com.rest.resources.filters.OAuthVerifierRequiredFilter;
import br.com.usuario.model.Usuario;

import com.sun.jersey.spi.container.ResourceFilters;

@Path("/accessToken")
public class AccessTokenResource {

	@POST
	@Path("/movel")
	@Produces(MediaType.APPLICATION_FORM_URLENCODED)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthNonceRequiredFilter.class, OAuthVerifierRequiredFilter.class})
	public Response createAccessToken(@Context HttpServletRequest httpServletRequest) {

		String authorizationHeader = httpServletRequest.getHeader("Authorization");

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			RequestToken requestToken = RequestToken.retrieveToken(session,extractOAuthToken(authorizationHeader));
			Usuario usuario = requestToken.getUsuario();
			PreconditionsREST.checkNotNull(usuario, "Usuário não existe");
			
			AccessToken accessToken;
			try{
				accessToken = (AccessToken) AccessToken.retrieveAccessToken(usuario.getId()).getAccessToken();
			}catch (ModelException e) {
				try {
					accessToken = Usuario.createTokenUser(session, usuario);
				}catch (ModelException e2) {
					throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity(new ErrorEntity("OAuthException","Usuario n�o encontrado")).type(MediaType.APPLICATION_JSON).build());
				}
			}
			tx.commit();
			return Response.ok().entity(accessToken.asURLEncodedString()).build();

		}finally{
			session.close();
			session = null;
			tx = null;
		}
	}

	private String extractOAuthToken(String header) {
		return FilterHelper.extractValueFromKeyValuePairs("oauth_token", header);
	}

	@SuppressWarnings("unused")
	private String extractOAuthTokenSecret(String header) {
		return FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", header);
	}
}
