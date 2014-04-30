package br.com.rest.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.container.ResourceFilters;

import br.com.contato.model.Contato;
import br.com.eventos.model.Evento;
import br.com.exception.ModelException;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.ContatoData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAccessTokenRequiredFilter;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthNonceRequiredFilter;
import br.com.rest.resources.helper.ImageHelper;
import br.com.rest.values.RESTMessageHelper;
import br.com.usuario.model.Usuario;

@Path("/home")
public class UsuarioHomeResource {

	@Context HttpServletRequest httpServletRequest;

	@Context 
	ResourceContext resourceContext;

	protected Usuario getUsuario(){
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Usuario usu = null;
		try{
			usu = Usuario.pesquisaAccessToken(session,FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")));
			tx.commit();
			return usu;
		}finally{
			tx = null;
			session.close();
			session = null;
			usu = null;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getUsuarioHome() {

		Usuario usuario = null;
		try{
			usuario = getUsuario();
			PreconditionsREST.checkNotNull(usuario, RESTMessageHelper.NOTFOUND);
			return Response.ok().entity(usuario).build();
		}finally{
			usuario = null;
		}	
	}

	@GET
	@Path("/dashboard")
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getUsuarioHomeDashboard() {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Usuario usu = null;
		try{
			usu = Usuario.pesquisaAccessTokenDashboard(session,FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")));
			tx.commit();
			return Response.ok().entity(usu).build();
		}finally{
			tx = null;
			session.close();
			session = null;
			usu = null;
		}
	}

	@PUT
	@Path("/dashboard")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alteraUsuarioHomeDashboard(Usuario usuario) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			Usuario usu = getUsuario();
			usu.setDashboardNome(usuario.getDashboardNome());
			usu.setNota(usuario.getNota());
			try {
				usu.alteraDashboard();
				return Response.ok().entity(usu).build();
			} catch (ModelException e) {
				return Response.ok().build();
			}
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("/contato")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public ContatoData getContato() {

		Usuario usu = null;

		try{
			usu = getUsuario();
			PreconditionsREST.checkNotNull(usu, RESTMessageHelper.NOTFOUND);
			ContatoResource cr = resourceContext.getResource(ContatoResource.class);
			return cr.getContato(usu);
		}finally{
			usu = null;
		}	
	}

	@Path("/contato")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarContatos(Contato contato) {

		ContatoResource cr = null;
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Usuario usuario = getUsuario();
			PreconditionsREST.checkNotNull(usuario, RESTMessageHelper.NOTFOUND);
			contato.setConsumerSecret(usuario.getConsumerSecret());
			cr = resourceContext.getResource(ContatoResource.class);
			return cr.adicionarContato(usuario, contato, consumerKey);
		}finally{
			cr = null;
			contato = null;
		}
	}

	@POST
	@Path("/imagem")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		Usuario usu = null;

		try{
			usu = getUsuario();
			PreconditionsREST.checkNotNull(usu, RESTMessageHelper.NOTFOUND);
			usu.setImagemPerfil(ImageHelper.writeToByte(uploadedInputStream));
			try {
				usu.alterar();
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
				e.printStackTrace();
			}
			return Response.status(200).entity(usu).build();

		}finally{
			uploadedInputStream = null;
			usu = null;
		}
	}

	@GET
	@Path("/imagem")
	@Produces("image/png")
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getImage(@QueryParam("x") @DefaultValue("60") Integer x,
			@QueryParam("y") @DefaultValue("60") Integer y) {

		byte[] imagem = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			imagem = Usuario.carregaImage(session,FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")));
			tx.commit();
			if(imagem == null){
				byte []buffer = new byte[1024];  
				InputStream is = this.getClass().getResourceAsStream( "../reindeer/Icons/delete.png" );  
				ByteArrayOutputStream out = new ByteArrayOutputStream(); 
				try {
					while (is.read( buffer ) != -1)  {  
						out.write( buffer );  
					}
				} catch (IOException e) {} 
				return Response.ok().entity(new ByteArrayInputStream(out.toByteArray())).build();
			}

			CacheControl control = new CacheControl();
			control.setMaxAge(60);

			return Response.ok().entity(new ByteArrayInputStream(ImageHelper.imageXY(imagem, x, y))).cacheControl(control).build();

		}finally{
			imagem = null;
		}
	}

	@Path("/evento")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Response adicionarEvento(Evento evento) {
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Usuario usu = null;
		EventoResource cr = null;
		try{
			usu = getUsuario();
			PreconditionsREST.checkNotNull(usu, RESTMessageHelper.NOTFOUND);
			evento.setConsumerSecret(usu.getConsumerSecret());
			evento.setUsuario(usu);
			cr = resourceContext.getResource(EventoResource.class);
			return cr.adicionaEvento(session, evento);
		}finally{
			tx.commit();
			usu = null;
			cr = null;
			usu = null;
			session.close();
			session = null;
		}
	}
}
