package br.com.rest.resources.filters;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.oauth.model.AccessToken;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.resources.exception.PreconditionsREST;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class OAuthAccessTokenRequiredFilter implements ResourceFilter {

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return new ContainerRequestFilter() {

            @Override
            public ContainerRequest filter(ContainerRequest cr) {
            	Session session = HibernateHelper.openSession(getClass());
        		Transaction tx = session.beginTransaction();
        		try{
	                String oauthValues = FilterHelper.extractValueFromKeyValuePairs("oauth_token", cr.getHeaderValue("Authorization"));
	                PreconditionsREST.checkNotNull(oauthValues, "Token inválido");
	                if(oauthValues.isEmpty()) {
	                    throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("OAuthException","Sem oauth_token na requisi��o.")).build());
	                }
	                
	                if(!AccessToken.containsToken(session, oauthValues)) {
	                    throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("OAuthException","N�o h� token de acesso correspondente registrado.")).build());
	                }
	                tx.commit();
	                return cr;
        		}finally{
        			session = null;
        			tx = null;
        		}
            }
        };
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        // TODO Auto-generated method stub
        return null;
    }

}