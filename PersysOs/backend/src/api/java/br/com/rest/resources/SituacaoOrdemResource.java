package br.com.rest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
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
import br.com.ordem.model.SituacaoOrdem;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.SituacaoOrdemData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/ordem/situacao")
public class SituacaoOrdemResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public SituacaoOrdemData getSituacaoOrdem(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(SituacaoOrdem.CONSTRUTOR) String fields) {

		ArrayList<SituacaoOrdem> situacaos = null;
		SituacaoOrdemData situacaoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		
		try{
			situacaos = (ArrayList<SituacaoOrdem>) SituacaoOrdem.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit, fields,
					FormatDateHelper.formatTimeZone(since),consumerKey, SituacaoOrdem.class,statusModel);
			tx.commit();
			situacaoData = new SituacaoOrdemData(situacaos, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, 
					offset, SituacaoOrdem.countPorConsumerSecret(session,SituacaoOrdem.class,consumerKey,statusModel,since),statusModel));
			return situacaoData;
		}finally{
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
			situacaos = null;
			situacaoData = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getSituacaoOrdem(@PathParam("id") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			SituacaoOrdem situacao = valida(id, session);
			PreconditionsREST.checkNotNull(situacao, "not found");
			return Response.ok(situacao).build();
		}finally{
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getSituacaoOrdem(@PathParam("codigo") String codigo) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			SituacaoOrdem situacao = valida(codigo, session);
			PreconditionsREST.checkNotNull(situacao, "not found");
			return Response.ok(situacao).build();
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaSituacaoOrdem(SituacaoOrdem situacao, @PathParam("id") Long id) {

		PreconditionsREST.checkNotNull(situacao, "not enough items");
		
		SituacaoOrdem situacaoAtual = null;

		Session session = HibernateHelper.openSession(getClass());
		try{
			situacaoAtual = valida(id, session);
			return atualiza(session, situacao, situacaoAtual);
		}finally{
			situacao = null;
			situacaoAtual = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Session session, SituacaoOrdem situacao,
			SituacaoOrdem situacaoAtual) {
		PreconditionsREST.checkNotNull(situacaoAtual, "not found");
		situacaoAtual.setDescricao(situacao.getDescricao());
		situacaoAtual.setNome(situacao.getNome());
		situacaoAtual.setCodigo(situacao.getCodigo());
		situacaoAtual.setStatusModel(situacao.getStatusModel());
		try {
			situacaoAtual.alterar(session);
			return Response.ok().entity(situacaoAtual).build();

		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarSituacaoOrdem(SituacaoOrdem situacao) {
		PreconditionsREST.checkNotNull(situacao, "not enough items");
		
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			situacao.setConsumerId(Long.parseLong(consumerKey));
			situacao.salvar();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		return Response.ok(URI.create(String.valueOf(200))).entity(situacao).build();

	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeSituacaoOrdem(@PathParam("id") long id) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			SituacaoOrdem situacao = valida(id, session);
			PreconditionsREST.checkNotNull(situacao, "not found");
			situacao.remover();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}catch (NullPointerException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Sem registro no servidor")).build());
		}finally{
			session.close();
			session = null;
		}
	}
	
	private SituacaoOrdem valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		SituacaoOrdem situacao = (SituacaoOrdem) SituacaoOrdem.pesquisaPorIdConsumerSecret(session,SituacaoOrdem.class,id,cs);
		tx.commit();
		return situacao;
	}
	
	private SituacaoOrdem valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		SituacaoOrdem situacao = (SituacaoOrdem) SituacaoOrdem.pesquisaPorCodigoConsumerSecret(session,SituacaoOrdem.class,codigo,cs);
		tx.commit();
		return situacao;
	}


	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public SituacaoOrdemData busca(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("nome") @DefaultValue("") String nomeSituacaoOrdem,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<SituacaoOrdem> situacaoLista = null;
		SituacaoOrdemData situacaoData = null;
		Session session = HibernateHelper.openSession(getClass());
		
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			try {
				situacaoLista = SituacaoOrdem.busca(consumerKey, nomeSituacaoOrdem, codigo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			PreconditionsREST.checkNotNull(situacaoLista, "not found");
			
			situacaoData = new SituacaoOrdemData(situacaoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), situacaoLista.size(), 0, situacaoLista.size(), statusModel));
			return situacaoData;
		}finally{
			situacaoData = null;
			situacaoLista = null;
			session.close();
			session = null;
		}
	}

}
