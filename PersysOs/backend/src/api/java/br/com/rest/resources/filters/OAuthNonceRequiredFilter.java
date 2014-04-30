package br.com.rest.resources.filters;

import java.util.HashSet;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.rest.represention.ErrorEntity;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class OAuthNonceRequiredFilter implements ResourceFilter {

    private static HashSet<String> receivedNonces = new HashSet<String>();

    public ContainerRequestFilter getRequestFilter() {
        return new ContainerRequestFilter() {

            @Override
            public ContainerRequest filter(ContainerRequest cr) {
                String nonce = extractNonce(cr.getHeaderValue("Authorization"));
                
                if (seenBefore(nonce)) {
                    throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).entity(new ErrorEntity("OAuthException",String.format("Nonce [%s] foi utilizado anteriormente.", nonce))).build());
                }
                
                return cr;
            }

            private boolean seenBefore(String nonce) {
                for (String str : receivedNonces) {
                    if (str.equals(nonce)) {
                        return true;
                    }
                }
                return false;
            }

            private String extractNonce(String header) {
                if (header == null || !header.contains("oauth_nonce")) {
                    return null;
                }

                int startOfNonce = header.lastIndexOf("oauth_nonce=\"");

                if (startOfNonce < 0) {
                    return null;
                }

                // Yuck! Nasty string parsing in here.
                String str = header.substring(startOfNonce + "oauth_nonce=\"".length());
                str = str.substring(0, str.indexOf("\""));

                return str;
            }

        };
    }

    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

}
