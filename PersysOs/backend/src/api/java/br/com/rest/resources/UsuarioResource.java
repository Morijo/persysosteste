package br.com.rest.resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.container.ResourceFilters;

import br.com.cliente.model.Cliente;
import br.com.contato.model.Contato;
import br.com.exception.ModelException;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.ContatoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.usuario.model.Usuario;

@Path("/usuario")
public class UsuarioResource {

	@Context HttpServletRequest httpServletRequest;

	@Context 
	ResourceContext resourceContext;

	@Path("{id}/contato")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public ContatoData getContatos(@PathParam("id") Long id) {

		Usuario usuario = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			usuario = (Usuario) Usuario.pesquisaPorIdConsumerSecret(session, Usuario.class,id,consumerKey);
			tx.commit();
			PreconditionsREST.checkNotNull(usuario, "not found");
			ContatoResource cr = resourceContext.getResource(ContatoResource.class);
			return cr.getContato(usuario);
		}finally{
			usuario = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	@Path("{id}/contato")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarContatos(@PathParam("id") Long id, Contato contato) {

		ContatoResource cr = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Usuario usuario = (Usuario) Cliente.pesquisaPorIdConsumerSecret(session,Usuario.class,id,consumerKey);
			tx.commit();
			PreconditionsREST.checkNotNull(usuario, "not found");
			contato.setConsumerSecret(usuario.getConsumerSecret());
			cr = resourceContext.getResource(ContatoResource.class);
			return cr.adicionarContato(usuario, contato, consumerKey);
		}finally{
			cr = null;
			contato = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	@Path("senha")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecuperaSenha(@QueryParam("id") @DefaultValue("-1") Long id,
			@QueryParam("email") @DefaultValue("") String email) {

		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		if(id != -1 ){
			try {
				Usuario usuario = Usuario.recuparaSenhaPorId(id,consumerKey);
				return Response.ok(URI.create(String.valueOf(200))).entity(usuario).build();
			} catch (ModelException e) {
				return Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build();
			}
		}else if( !email.isEmpty()){
			try {
				Usuario.recuparaSenhaPorEmail(email);
				return Response.ok(URI.create(String.valueOf(200))).entity(new Usuario(id)).build();
			} catch (ModelException e) {
				return Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build();
			}
		}else{
			return Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Parametros inválidos")).build();
		}
	}

	@POST
	@Path("/{id}/imagem")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(@PathParam("id") Long id,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		byte[] imagem = null;

		try{
			BufferedImage originalImage = ImageIO.read(uploadedInputStream);
			BufferedImage scaledImg = Scalr.resize(originalImage, Mode.AUTOMATIC, 600, 600);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(scaledImg, "jpg", os);
			imagem = os.toByteArray();
		} catch (IOException e1) {
			PreconditionsREST.error(e1.getMessage());
		}

		try {
			Usuario.alteraImagem(imagem, id);
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
		}finally{
			uploadedInputStream = null;
		}
		return Response.status(200).build();
	}

	@GET
	@Path("/{id}/imagem")
	@Produces("image/jpeg")
	public Response getImage(@PathParam("id") Long id,
			@QueryParam("x") @DefaultValue("60") Integer x,
			@QueryParam("y") @DefaultValue("60") Integer y) {

		try{
			byte[] imagem = Usuario.carregaImage(id);
			InputStream file = new ByteArrayInputStream(imagem);
			BufferedImage scaledImg = null;
			BufferedImage originalImage = ImageIO.read(file);
			scaledImg = Scalr.resize(originalImage, Mode.AUTOMATIC, x.intValue(), y.intValue());
			return Response.ok().entity(scaledImg).build();
		}catch(Exception e){
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException",e.getMessage())).build());
		}
	}

}

