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
import br.com.rest.hateoas.ServicoData;
import br.com.rest.hateoas.ResponseBatchData;
import br.com.rest.hateoas.ServicoProcedimentoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.represention.ResponseBatch;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;
import br.com.servico.model.Servico;
import br.com.servico.model.ServicoProcedimento;

/**
 * Recurso serivco
 * @author ricardosabatine	
 * @version 1.0.0
 * @see Servico
 */
@Path("/servico")
public class ServicoResource {

	@Context HttpServletRequest httpServletRequest;

	/**
	 * Lista todos os serviços com base em parametros
	 * @see Servico
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public ServicoData getServico(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Servico.CONSTRUTOR) String fields) {

		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		
		ArrayList<Servico> servicos = null;
		ServicoData servicoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction transaction = session.beginTransaction();
		try{
			servicos = (ArrayList<Servico>) Servico.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields,FormatDateHelper.formatTimeZone(since) ,consumerKey, Servico.class, statusModel);
			transaction.commit();
			servicoData = new ServicoData(servicos, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Servico.countPorConsumerSecret(session,Servico.class,consumerKey,statusModel,since),statusModel));
			return servicoData;
		}finally{
			transaction = null;
			session.close();
			session = null;
			consumerKey = null;
			servicos = null;
			servicoData = null;
		}
	}

	/**
	 * Seleciona um serviço com base no ID
	 * @see Servico
	 */
	@Path("/{id}/procedimento")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public ServicoProcedimentoData getServicoProcedimento(
			@PathParam("id") Long id,
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel){

		PreconditionsREST.checkNotNull(id,"not enough items");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		
		ArrayList<ServicoProcedimento> servicos = null;
		ServicoProcedimentoData servicoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			servicos = (ArrayList<ServicoProcedimento>) ServicoProcedimento.listaServicoProcedimento(id,consumerKey);
			tx.commit();
			servicoData = new ServicoProcedimentoData(servicos, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset,servicos.size(),statusModel));
			return servicoData;
		}finally{
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
			servicos = null;
			servicoData = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getServico(@PathParam("id") Long id) {

		Servico servico = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			servico = valida(id, session);
			PreconditionsREST.checkNotNull(servico, "not found");
			return Response.ok(servico).build();
		}finally{
			servico = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getServico(@PathParam("id") String codigo) {

		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		PreconditionsREST.checkNotNull(consumerKey,"not enough items");
		
		Servico servico = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			servico = valida(codigo, session);
			PreconditionsREST.checkNotNull(servico, "not found");
			return Response.ok(servico).build();
		}finally{
			servico = null;
			session.close();
			session = null;
			consumerKey = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaServico(Servico servico, @PathParam("id") Long id) {

		PreconditionsREST.checkNotNull(servico, "not found");
		
		Servico servicoAtual = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			servicoAtual = valida(id, session);
			PreconditionsREST.checkNotNull(servicoAtual, "not found");
			return atualiza(session, servico, servicoAtual);
		}finally{
			servico = null;
			servicoAtual = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaServico(Servico servico, @PathParam("codigo") String codigo) {

		Servico servicoAtual = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			servicoAtual = valida(codigo, session);
			PreconditionsREST.checkNotNull(servico, "not found");
			return atualiza(session, servico, servicoAtual);
		}finally{
			servico = null;
			servicoAtual = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Session session, Servico servico, Servico servicoAtual) {
		servicoAtual.setTitulo(servico.getTitulo());
		servicoAtual.setDescricao(servico.getDescricao());
		servicoAtual.setCodigo(servico.getCodigo());
		servicoAtual.setValorServico(servico.getValorServico());
		servicoAtual.setStatusModel(servico.getStatusModel());
		try {
			servicoAtual.alterar(session);
			return Response.ok().entity(servicoAtual).build();

		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionar(Servico servico) {

		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try{
			servico.setConsumerSecret(new ConsumerSecret("", Long.parseLong(consumerKey)));
			servico.salvar();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}

		return Response.ok(URI.create(String.valueOf(200))).entity(servico).build();
	}
	
	@Path("/batch")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarLote(ServicoData servicoBatch) {

		PreconditionsREST.checkNotNull(servicoBatch,"not enough items");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
	
		List<ResponseBatch> responseBatchs = new ArrayList<ResponseBatch>();
		
		Session session = HibernateHelper.openSession(getClass());
		try{
			ArrayList<Servico> servicos = (ArrayList<Servico>) servicoBatch.getDados();
		
			int i = 0;
			for(Servico servico : servicos){
				servico.setConsumerSecret(new ConsumerSecret("",Long.parseLong(consumerKey)));
				try{
					servico.salvar();
					responseBatchs.add(new ResponseBatch(servico.getCodigo(), 1, servico.getId(), 0));
				}catch(ModelException e){
					responseBatchs.add(new ResponseBatch(servico.getCodigo(), StatusModel.REJEITADO, -1l, 1));
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
	@Produces({ MediaType.APPLICATION_JSON})
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeServico(@PathParam("id") long id) {
		Servico servico = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			servico = valida(id, session);
			PreconditionsREST.checkNotNull(servico, "not found");
			servico.remover();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}catch (NullPointerException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "not found")).build());
		}finally{
			session.close();
			session = null;
		}
	}

	private Servico valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "not enough items");
		Transaction tx = session.beginTransaction();
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Servico servico = (Servico) Servico.pesquisaPorIdConsumerSecret(session,Servico.class,id,consumerKey);
		tx.commit();
		return servico;
	}
	
	private Servico valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "not enough items");
		Transaction tx = session.beginTransaction();
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Servico servico = (Servico) Servico.pesquisaPorCodigoConsumerSecret(session,Servico.class,codigo,consumerKey);
		tx.commit();
		return servico;
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public ServicoData busca(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("titulo") @DefaultValue("") String titulo,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<Servico> servicoLista = null;
		ServicoData servicoData = null;
		String consumerKey = null;

		try{
			consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			try {
				servicoLista =(ArrayList<Servico>) Servico.busca(consumerKey, titulo, codigo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}

			PreconditionsREST.checkNotNull(servicoLista, "not found");

			servicoData = new ServicoData(servicoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), servicoLista.size(),0, servicoLista.size(),statusModel));
			return servicoData;
		}finally{
			servicoData = null;
			servicoLista = null;
			consumerKey = null;
		}
	}
}