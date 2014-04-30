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
import br.com.ordem.model.Prioridade;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.PrioridadeData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/ordem/prioridade")
public class PrioridadeResource {

	@Context HttpServletRequest httpServletRequest;
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public PrioridadeData getPrioridade(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("1000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Prioridade.CONSTRUTOR) String fields) {

		ArrayList<Prioridade> prioridades = null;
		PrioridadeData prioridadeData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String consumerKey = null;
		try{
			consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			prioridades = (ArrayList<Prioridade>) Prioridade.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields,FormatDateHelper.formatTimeZone(since) ,consumerKey, Prioridade.class,statusModel);
			tx.commit();
			prioridadeData = new PrioridadeData(prioridades, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Prioridade.countPorConsumerSecret(session, Prioridade.class, consumerKey,statusModel,since),statusModel));
			return prioridadeData;
		}finally{
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
			prioridades = null;
			prioridadeData = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getPrioridade(@PathParam("id") Long id) {

		Prioridade prioridade = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			prioridade = valida(id, session);
			PreconditionsREST.checkNotNull(prioridade, "not found");
			return Response.ok(prioridade).build();
		}finally{
			prioridade = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getPrioridade(@PathParam("codigo") String codigo) {

		Prioridade prioridade = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			prioridade = valida(codigo, session);
			PreconditionsREST.checkNotNull(prioridade, "not found");
			return Response.ok(prioridade).build();
		}finally{
			prioridade = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualiza(Prioridade prioridade, @PathParam("id") Long id) {

		Prioridade prioridadeAtual = null;
		PreconditionsREST.checkNotNull(prioridade, "not enough items");
		Session session = HibernateHelper.openSession(getClass());
		try{
			prioridadeAtual = valida(id, session);
			return atualiza(session, prioridade, prioridadeAtual);
		}finally{
			prioridade = null;
			prioridadeAtual = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualiza(Prioridade prioridade, @PathParam("codigo") String codigo) {

		Prioridade prioridadeAtual = null;
		PreconditionsREST.checkNotNull(prioridade, "not enough items");
		Session session = HibernateHelper.openSession(getClass());
		try{
			prioridadeAtual = valida(codigo, session);
			return atualiza(session, prioridade, prioridadeAtual);
		}finally{
			prioridade = null;
			prioridadeAtual = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Session session, Prioridade prioridade,Prioridade prioridadeAtual) {
		PreconditionsREST.checkNotNull(prioridade, "not found");
		prioridadeAtual.setDescricao(prioridade.getDescricao());
		prioridadeAtual.setPrioridade(prioridade.getPrioridade());
		prioridadeAtual.setPrioridadeUrgencia(prioridade.getPrioridadeUrgencia());
		prioridadeAtual.setCodigo(prioridade.getCodigo());
		prioridadeAtual.setStatusModel(prioridade.getStatusModel());
		prioridadeAtual.setCor(prioridade.getCor());
		try {
			prioridadeAtual.alterar(session);
			return Response.ok().entity(prioridadeAtual).build();

		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarPrioridade(Prioridade prioridade) {
		PreconditionsREST.checkNotNull(prioridade, "not enough items");
		
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			prioridade.setConsumerId(Long.parseLong(consumerKey));
			prioridade.salvar();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		return Response.ok(URI.create(String.valueOf(200))).entity(prioridade).build();
	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removePrioridade(@PathParam("id") long id) {

		Prioridade prioridade = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			prioridade = valida(id, session);
			PreconditionsREST.checkNotNull(prioridade, "not found");
			prioridade.remover();
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
	
	private Prioridade valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Prioridade prioridade = (Prioridade) Prioridade.pesquisaPorIdConsumerSecret(session,Prioridade.class,id,consumerKey);
		tx.commit();
		return prioridade;
	}
	
	private Prioridade valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Prioridade prioridade = (Prioridade) Prioridade.pesquisaPorCodigoConsumerSecret(session,Prioridade.class,codigo,consumerKey);
		tx.commit();
		return prioridade;
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public PrioridadeData busca(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam("nome") @DefaultValue("") String nomePrioridade,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<Prioridade> prioridadeLista = null;
		PrioridadeData prioridadeData = null;
		
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			try {
				prioridadeLista = Prioridade.busca(consumerKey, nomePrioridade, codigo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			
			PreconditionsREST.checkNotNull(prioridadeLista, "not found");

			prioridadeData = new PrioridadeData(prioridadeLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), prioridadeLista.size(), 0, prioridadeLista.size(),statusModel));
			return prioridadeData;
		}finally{
			prioridadeData = null;
			prioridadeLista = null;
		}
	}
}
