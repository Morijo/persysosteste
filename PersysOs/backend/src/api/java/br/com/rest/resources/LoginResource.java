package br.com.rest.resources;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.exception.ModelException;
import br.com.oauth.model.RequestToken;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.represention.ErrorEntity;
import br.com.usuario.model.Usuario;


@Path("/login")
public class LoginResource {

	public static final String SENHA_KEY = "senha";
	public static final String USUARIO_KEY = "nome";

    @Context
    private UriInfo uriInfo;

    public LoginResource() {}

    public LoginResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }
		
	 @POST
	 @Path("/movel")
	 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	 public Response signIn(@QueryParam("oauth_token") String oauthTokenClaim, MultivaluedMap<String, String> map) {
		 Session session= null;
		 Transaction tx  = null;
   	 
		 try{
	    	  
		 	if (!isCallback(map)) {
	            return Response.seeOther(uriInfo.getRequestUri()).build();
	        }
		 	
		 	session = HibernateHelper.openSession(getClass());
     	    tx = session.beginTransaction();
     	  	RequestToken token = RequestToken.pesquisaRequestToken(session, oauthTokenClaim);
		 	
		    String senha = map.getFirst(SENHA_KEY);
	        String nome  = map.getFirst(USUARIO_KEY);

	        if (oauthTokenClaim == null || oauthTokenClaim.length() == 0) {
	            return Response.status(Status.BAD_REQUEST).entity(new ErrorEntity("OAuthException",String.format("Necessario um token de requisi��o para [%s]", uriInfo.getRequestUri()))).type(MediaType.APPLICATION_JSON).build();

	        }

	        if (token == null) {
	            return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("OAuthException",String.format("N�o h� token de requisi��o para [%s]", uriInfo.getRequestUri()))).build();
	        }

	      
	        if(senha == null ||senha.length() < 1) {
	            return Response.status(Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("OAuthException",String.format("Falha: Senha inválida [%s]", uriInfo.getRequestUri().toString()))).build();
	        }

	        Usuario usuario = authenticated(session, nome, senha);
	        tx.commit();
	        
	        if (usuario != null) {
	        
	        	token.createVerificationCode();
	            token.setUsuario(usuario);
	        	try {
					token.alterar(session);
				} catch (ModelException e) {
				    return Response.status(Status.SERVICE_UNAVAILABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("OAuthException",String.format("Falha ao alterar token [%s]" + e.getMessage(), uriInfo.getRequestUri().toString()))).build();
			    }
	            return Response.ok().entity("oauth_token:"+oauthTokenClaim+", oauth_verifier:" + token.getVerificationCode()).build();

	        } else {
	            return Response.status(Status.UNAUTHORIZED).entity(new ErrorEntity("Login","Usuário ou Senha inválido")).type(MediaType.APPLICATION_JSON).build();
	        }
	      }finally{
	    		tx=null;
			 	session.close();
			 	session = null;
		  }
	    }
	 
	  private boolean isCallback(MultivaluedMap<String, String> map) {
	        return map.containsKey(SENHA_KEY);
	    }

	    private Usuario authenticated(Session session, String nome, String senha) {
	    	return Usuario.autenticaUsuario(session, nome, senha);
	    }
}
