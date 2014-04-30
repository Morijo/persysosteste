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
import br.com.recurso.model.Equipamento;
import br.com.rest.hateoas.EquipamentoData;
import br.com.rest.hateoas.ResponseBatchData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.represention.ResponseBatch;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/recurso/equipamento")
public class EquipamentoResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public EquipamentoData listaEquipamento(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Equipamento.CONSTRUTOR) String fields) {

		ArrayList<Equipamento> equipamento = null;
		EquipamentoData equipamentoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		try{
			equipamento = (ArrayList<Equipamento>) Equipamento.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit, fields,
					FormatDateHelper.formatTimeZone(since) ,consumerKey, Equipamento.class,statusModel);
			equipamentoData = new EquipamentoData(equipamento, LinkData.createLinks("/recurso/equipamento", limit, 
					offset, Equipamento.countPorConsumerSecret(session,Equipamento.class,consumerKey,statusModel,since),statusModel));
			tx.commit();
			return equipamentoData;
		}catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			equipamentoData =null;
			equipamento = null;
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getEquipamento(@PathParam("id") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		Equipamento equipamento = null;
		try{
			equipamento = valida(id, session);
			PreconditionsREST.checkNotNull(equipamento, "not found");
			return Response.ok(equipamento).build();

		}catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Sem registro no servidor")).build());
		}finally{
			equipamento = null;
			session.close();
			session = null;
		}		
	}
	
	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getEquipamento(@PathParam("codigo") String codigo) {

		Session session = HibernateHelper.openSession(getClass());
		Equipamento equipamento = null;
		try{
			equipamento = valida(codigo, session);
			PreconditionsREST.checkNotNull(equipamento, "not found");
			return Response.ok(equipamento).build();

		}catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Sem registro no servidor")).build());
		}finally{
			equipamento = null;
			session.close();
			session = null;
		}		
	}

	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizaEquipamento(Equipamento equipamento, @PathParam("id") Long id) {

		PreconditionsREST.checkNotNull(equipamento, "not acceptable");

		Session session = HibernateHelper.openSession(getClass());
		Equipamento equipamentoAtual = null;
		try{
			equipamentoAtual = valida(id, session);
			PreconditionsREST.checkNotNull(equipamento, "not found");
			return atualiza(session, equipamento, equipamentoAtual);
		}finally{
			equipamentoAtual = null;
			equipamento = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizaEquipamento(Equipamento equipamento, @PathParam("codigo") String codigo) {

		PreconditionsREST.checkNotNull(equipamento, "not acceptable");

		Session session = HibernateHelper.openSession(getClass());
		Equipamento equipamentoAtual = null;
		try{
			equipamentoAtual = valida(codigo, session);
			PreconditionsREST.checkNotNull(equipamento, "not found");
			return atualiza(session, equipamento, equipamentoAtual);
		}finally{
			equipamentoAtual = null;
			equipamento = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Session session, Equipamento equipamento,
			Equipamento equipamentoAtual) {
		equipamentoAtual.setEquipamento(equipamento.getEquipamento());
		equipamentoAtual.setNumeroSerie(equipamento.getNumeroSerie());
		equipamentoAtual.setEquipamento(equipamento.getEquipamento());
		equipamentoAtual.setEtiqueta(equipamento.getEtiqueta());
		equipamentoAtual.setDescricao(equipamento.getDescricao());
		equipamentoAtual.setMarca(equipamento.getMarca());
		equipamentoAtual.setModelo(equipamento.getModelo());
		equipamentoAtual.setStatusModel(equipamento.getStatusModel());
		try {
			equipamentoAtual.alterar(session);
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		return Response.ok().entity(equipamentoAtual).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	public Response adicionarEquipamento(Equipamento equipamento) {

		PreconditionsREST.checkNotNull(equipamento, "not acceptable");

		Session session = HibernateHelper.openSession(getClass());
		try{
			String consumerKey =FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			equipamento.setMedida(null);
			equipamento.setConsumerId(Long.parseLong(consumerKey));
			equipamento.salvar();
			return Response.ok(URI.create(String.valueOf(201))).entity(equipamento).build();

		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			equipamento = null;
			session.close();
			session = null;
		}
	}

	@Path("/batch")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarMaterialLote(EquipamentoData equipamentoBatch) {

		PreconditionsREST.checkNotNull(equipamentoBatch, "Parametro invalido");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
	
		List<ResponseBatch> responseBatchs = new ArrayList<ResponseBatch>();
		
		Session session = HibernateHelper.openSession(getClass());
		try{
			ArrayList<Equipamento> equipamentos = (ArrayList<Equipamento>) equipamentoBatch.getDados();
		
			int i = 0;
			for(Equipamento equipamento : equipamentos){
				equipamento.setConsumerSecret(new ConsumerSecret("",Long.parseLong(consumerKey)));
				try{
					equipamento.salvar();
					responseBatchs.add(new ResponseBatch(equipamento.getCodigo(), equipamento.getStatusModel(), equipamento.getId(),0));
				}catch(ModelException e){
					responseBatchs.add(new ResponseBatch(equipamento.getCodigo(), StatusModel.REJEITADO, null, 1));
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
	public Response removeEquipamento(@PathParam("id") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		Equipamento equipamento = null;
		try{
			equipamento = valida(id, session);
			equipamento.remover();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}catch (NullPointerException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Sem registro no servidor")).build());
		}finally{
			equipamento = null;
			session.close();
			session = null;
		}
	}

	private Equipamento valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Equipamento equipamento = (Equipamento) Equipamento.pesquisaPorIdConsumerSecret(session,Equipamento.class,id,consumerKey);
		tx.commit();
		return equipamento;
	}

	private Equipamento valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Equipamento equipamento = (Equipamento) Equipamento.pesquisaPorCodigoConsumerSecret(session,Equipamento.class,codigo,consumerKey);
		tx.commit();
		return equipamento;
	}
	
	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public EquipamentoData buscaEquipamentoLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("numeroSerie") @DefaultValue("") String numeroSerie,
			@QueryParam("nome") @DefaultValue("") String equipamento,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		EquipamentoData equipamentoData = null;
		ArrayList<Equipamento> equipamentoLista = null;

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			try {
				equipamentoLista = Equipamento.buscaEquipamento(consumerKey, numeroSerie, equipamento, codigo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			PreconditionsREST.checkNotNull(equipamentoLista, "not found");

			equipamentoData = new EquipamentoData(equipamentoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), equipamentoLista.size(), 0, equipamentoLista.size(),StatusModel.ATIVO));
			return equipamentoData;
		}finally{
			equipamentoData = null;
			equipamentoLista = null;
		}
	}
}
