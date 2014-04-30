package br.com.rest.resources.oauth;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import br.com.oauth.model.RequestToken;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthNonceRequiredFilter;

import com.sun.jersey.spi.container.ResourceFilters;

@Path("/requestToken")
public class RequestTokenResource {

    @POST
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    @ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthNonceRequiredFilter.class})
    public Response generateRequestToken(@Context HttpHeaders headers) throws Exception {
        RequestToken requestToken = new RequestToken();
        requestToken.salvar(); 
        return Response.ok().entity(requestToken.toPercentEncodedString()).build();
    }
}
