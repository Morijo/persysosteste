package br.com.rest.resources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.container.ResourceFilters;

import br.com.exception.ModelException;
import br.com.ordem.model.Anexo;
import br.com.ordem.model.BaseConhecimento;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.AnexoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ImageHelper;
import br.com.rest.resources.helper.ParameterRequestRest;


@Path("/anexo")
public class AnexoResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public AnexoData getAnexo(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(BaseConhecimento.CONSTRUTOR) String fields){
		
		ArrayList<Anexo> anexos = null;
		AnexoData anexoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			anexos = (ArrayList<Anexo>) Anexo.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,"",FormatDateHelper.formatTimeZone(since) ,consumerKey, Anexo.class, statusModel);
			tx.commit();
			anexoData = new AnexoData(anexos, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, br.com.ordem.model.Anexo.countPorConsumerSecret(session,Anexo.class,consumerKey,statusModel,since),statusModel));
			return anexoData;
		}finally{
			tx = null;
		    session.close();
		    session = null;
			anexos = null;
			anexoData = null;
		}
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getAnexo(@PathParam("id") Long id) {

		Anexo  anexo= null;
		Session session = HibernateHelper.openSession(getClass());
	    Transaction tx = session.beginTransaction();
	 
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			anexo = (Anexo) Anexo.pesquisaPorIdConsumerSecret(session,Anexo.class,id,consumerKey);
			tx.commit();
		    return Response.ok().entity(anexo).build();
		    
		}finally{
			anexo = null;
			session.close();
	    	session = null;
	    	tx = null;
	   }
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeAnexo(@PathParam("id") Long id) {

		Anexo  anexo= null;
		Session session = HibernateHelper.openSession(getClass());
	    Transaction tx = session.beginTransaction();
	 
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			anexo = (Anexo) Anexo.pesquisaPorIdConsumerSecret(session,Anexo.class,id,consumerKey);
			tx.commit();
			anexo.remover();
		    return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(
				Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}catch (NullPointerException e) {
		throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Sem registro no servidor")).build());
	    }finally{
			anexo = null;
			session.close();
	    	session = null;
	    	tx = null;
	   }
	}
	
	@POST
	@Path("/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alteraAnexo(@PathParam("id") Long id,
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail) {
 
		Session session = HibernateHelper.openSession(getClass());
	    Transaction tx = session.beginTransaction();
		Anexo anexo = null;
		byte[] imagem;
		try{
			
			imagem =ImageHelper.writeToByte(uploadedInputStream);
		
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			anexo = (Anexo) Anexo.pesquisaPorIdConsumerSecret(session,Anexo.class,id,consumerKey);
			tx.commit();
			anexo.setDescricao(fileDetail.getFileName());
			anexo.setImagem(imagem);
			anexo.setTamanho(imagem.length);
			try {
				anexo.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
				e.printStackTrace();
			}
			
			return Response.status(200).entity(anexo).build();
		
		}finally{
			uploadedInputStream = null;
			anexo = null;
			imagem = null;
			session.close();
	    	session = null;
	    	tx = null;
		}
	}
}
