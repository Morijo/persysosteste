package br.com.rest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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

import com.sun.jersey.spi.container.ResourceFilters;

import br.com.exception.ModelException;
import br.com.model.StatusModel;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.model.Material;
import br.com.rest.hateoas.MaterialData;
import br.com.rest.hateoas.ResponseBatchData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.represention.ResponseBatch;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/recurso/material")
public class MaterialResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public MaterialData lista(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Material.CONSTRUTOR) String fields) {

		String consumerKey = null;
		ArrayList<Material> materials = null;
		MaterialData materialData = null; 
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Number number = Material.countPorConsumerSecret(session,Material.class,consumerKey,statusModel,since);

			if(limit >= -1)
				limit = number.intValue();

			materials = (ArrayList<Material>) Material.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields,FormatDateHelper.formatTimeZone(since),consumerKey, Material.class,statusModel);
			materialData = new MaterialData(materials, LinkData.createLinks( httpServletRequest.getRequestURL().toString(), limit, offset, number, statusModel));
			tx.commit();
			return materialData;
		}catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			consumerKey = null;
			materials = null;
			materialData = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getMaterial(@PathParam("id") Long id) {

		Material material = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			material = valida(id, session);
			PreconditionsREST.checkNotNull(material, "not found");
			return Response.ok(material).build();
		}finally{
			material = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getMaterial(@PathParam("codigo") String codigo) {

		Material material = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			material = valida(codigo, session);
			PreconditionsREST.checkNotNull(material, "not found");
			return Response.ok(material).build();
		}finally{
			material = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualiza(@PathParam("id") Long id, Material material) {

		PreconditionsREST.checkNotNull(material, "not enough items");

		Material materialAtual = null;
		Session session = HibernateHelper.openSession(getClass());
	
		try{
			materialAtual = valida(id, session);
			return atualiza(session, material, materialAtual);
		}finally{
			materialAtual = null;
			material = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualiza(@PathParam("codigo")String codigo, Material material) {

		PreconditionsREST.checkNotNull(material, "not enough items");

		Material materialAtual = null;
		Session session = HibernateHelper.openSession(getClass());
	
		try{
			materialAtual = valida(codigo, session);
			return atualiza(session, material, materialAtual);
		}finally{
			materialAtual = null;
			material = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Session session, Material material, Material materialAtual) {
		PreconditionsREST.checkNotNull(material, "not found");
		materialAtual.setMaterial(material.getMaterial());
		materialAtual.setNome(material.getNome());
		materialAtual.setEtiqueta(material.getEtiqueta());
		materialAtual.setDescricao(material.getDescricao());
		materialAtual.setMarca(material.getMarca());
		materialAtual.setModelo(material.getModelo());
		materialAtual.setStatusModel(material.getStatusModel());
		materialAtual.setMedida(material.getMedida());  
		try {
			materialAtual.alterar(session);
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		return Response.ok().entity(materialAtual).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionar(Material material) {
		
		PreconditionsREST.checkNotNull(material, "not enough items");
		
		Session session = HibernateHelper.openSession(getClass());
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try {
			material.setConsumerId(Long.parseLong(consumerKey));
			material.salvar();
			return Response.ok(URI.create(String.valueOf(200))).entity(material).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			consumerKey = null;
			material = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/batch")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarLote(MaterialData materialBatch) {

		PreconditionsREST.checkNotNull(materialBatch, "not found");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
	
		List<ResponseBatch> responseBatchs = new ArrayList<ResponseBatch>();
		
		Session session = HibernateHelper.openSession(getClass());
		try{
			ArrayList<Material> materials = (ArrayList<Material>) materialBatch.getDados();
		
			int i = 0;
			for(Material material : materials){
				material.setConsumerSecret(new ConsumerSecret("",Long.parseLong(consumerKey)));
				try{
					material.salvar();
					responseBatchs.add(new ResponseBatch(material.getCodigo(), material.getStatusModel(), material.getId(),0));
				}catch(ModelException e){
					responseBatchs.add(new ResponseBatch(material.getCodigo(), StatusModel.REJEITADO, -1l,0));
				}	
				if( (i%20) == 0){
					session.flush();
					session.clear();
				}
				i++;
			}	
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		
		return Response.ok(URI.create(String.valueOf(200))).entity(new ResponseBatchData(responseBatchs,responseBatchs.size())).build();
	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response remove(@PathParam("id") long id) {

		Session session = HibernateHelper.openSession(getClass());
		Material material = null;
		try {
			material = valida(id, session);
			PreconditionsREST.checkNotNull(material, "not found");
			material.remover();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}catch (NullPointerException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "not found")).build());
		}finally{
			material = null;
			session.close();
			session = null;
		}
	}

	private Material valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Material material = (Material) Material.pesquisaPorIdConsumerSecret(session,Material.class,id,consumerKey);
		tx.commit();
		return material;
	}
	
	private Material valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Material material = (Material) Material.pesquisaPorCodigoConsumerSecret(session,Material.class,codigo,consumerKey);
		tx.commit();
		return material;
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public MaterialData busca(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("material") @DefaultValue("") String material,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<Material> materialLista = null;
		MaterialData materialData = null;
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try{
			try {
				materialLista =(ArrayList<Material>) Material.busca(material, codigo, statusModel, consumerKey);
				PreconditionsREST.checkNotNull(materialLista, "not found");
				materialData = new MaterialData(materialLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), materialLista.size(), 0, materialLista.size(),statusModel));
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return materialData;
		}finally{
			materialData = null;
			materialLista = null;
		}
	}
}