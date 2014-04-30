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
import br.com.ordem.model.BaseConhecimento;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.BaseConhecimentoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/ordem/baseconhecimento")
public class BaseConhecimentoResource {

	@Context HttpServletRequest httpServletRequest;
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public BaseConhecimentoData getBaseConhecimento(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(BaseConhecimento.CONSTRUTOR) String fields){

		ArrayList<BaseConhecimento> baseConhecimento = null;
		BaseConhecimentoData baseConhecimentoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try{
			baseConhecimento = (ArrayList<BaseConhecimento>) BaseConhecimento.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, 
					limit,fields,FormatDateHelper.formatTimeZone(since) ,consumerKey, BaseConhecimento.class, statusModel);
			tx.commit();
			baseConhecimentoData = new BaseConhecimentoData(baseConhecimento, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, BaseConhecimento.countPorConsumerSecret(session, BaseConhecimento.class, consumerKey,statusModel,since),statusModel));
			return baseConhecimentoData;
		}catch (Exception e){
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
			baseConhecimento = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getBaseConhecimento(@PathParam("id") Long id) {

		BaseConhecimento baseConhecimento = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			baseConhecimento = valida(id, session);	
			PreconditionsREST.checkNotNull(baseConhecimento, "not found");
			return Response.ok(baseConhecimento).build();
		}finally{
			baseConhecimento = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getBaseConhecimento(@PathParam("codigo") String codigo) {

		BaseConhecimento baseConhecimento = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			baseConhecimento = valida(codigo, session);
			PreconditionsREST.checkNotNull(baseConhecimento, "not found");
			return Response.ok(baseConhecimento).build();
		}finally{
			baseConhecimento = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaId(BaseConhecimento baseConhecimento, @PathParam("id") Long id) {

		BaseConhecimento baseConhecimentoAtual = null;
		PreconditionsREST.checkNotNull(baseConhecimento, "not enough items");
		
		Session session = HibernateHelper.openSession(getClass());
		try{
			baseConhecimentoAtual = valida(id, session);
			PreconditionsREST.checkNotNull(baseConhecimentoAtual, "not found");
			return atualiza(baseConhecimento, baseConhecimentoAtual);
		}finally{
			baseConhecimento = null;
			baseConhecimentoAtual = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaCodigo(BaseConhecimento baseConhecimento, @PathParam("codigo") String id) {

		BaseConhecimento baseConhecimentoAtual = null;
		PreconditionsREST.checkNotNull(baseConhecimento, "not enough items");
		
		Session session = HibernateHelper.openSession(getClass());
		try{
			baseConhecimentoAtual = valida(id, session);
			PreconditionsREST.checkNotNull(baseConhecimentoAtual, "not enough items");
			return atualiza(baseConhecimento, baseConhecimentoAtual);
		}finally{
			baseConhecimento = null;
			baseConhecimentoAtual = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(BaseConhecimento baseConhecimento,
			BaseConhecimento baseConhecimentoAtual) {
		baseConhecimentoAtual.setCodigo(baseConhecimento.getCodigo());
		baseConhecimentoAtual.setStatusModel(baseConhecimento.getStatusModel());
		baseConhecimentoAtual.setTitulo(baseConhecimento.getTitulo());
		baseConhecimentoAtual.setMensagem(baseConhecimento.getMensagem());
		baseConhecimentoAtual.setTipo(baseConhecimento.getTipo());
		try {
			baseConhecimentoAtual.salvarAlterar();
			return Response.ok().entity(baseConhecimentoAtual).build();

		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionar(BaseConhecimento baseConhecimento) {
		PreconditionsREST.checkNotNull(baseConhecimento, "not enough items");
		
		Session session = HibernateHelper.openSession(getClass());
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			baseConhecimento.setConsumerId(Long.parseLong(consumerKey));
			baseConhecimento.salvar();
			return Response.ok(URI.create(String.valueOf(200))).entity(baseConhecimento).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeBaseConhecimento(@PathParam("id") long id) {

		BaseConhecimento baseConhecimento = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			baseConhecimento = valida(id, session);
			PreconditionsREST.checkNotNull(baseConhecimento, "not found");
			baseConhecimento.remover();
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
	
	private BaseConhecimento valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		BaseConhecimento baseConhecimento = (BaseConhecimento) BaseConhecimento.pesquisaPorIdConsumerSecret(session,BaseConhecimento.class,id,consumerKey);
		tx.commit();
		return baseConhecimento;
	}
	
	private BaseConhecimento valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		BaseConhecimento baseConhecimento = (BaseConhecimento) BaseConhecimento.pesquisaPorCodigoConsumerSecret(session,BaseConhecimento.class,codigo,consumerKey);
		tx.commit();
		return baseConhecimento;
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public BaseConhecimentoData buscaBaseLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("titulo") @DefaultValue("") String titulo,
			@QueryParam("mensagem") @DefaultValue("") String mensagem,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<BaseConhecimento> baseConhecimentoLista = null;
		BaseConhecimentoData baseConhecimentoData = null;
	
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			PreconditionsREST.checkNotNull(baseConhecimentoLista, "not found");
			try {
				baseConhecimentoLista = BaseConhecimento.busca(consumerKey, titulo, mensagem, codigo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			baseConhecimentoData = new BaseConhecimentoData(baseConhecimentoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), baseConhecimentoLista.size(), 0, baseConhecimentoLista.size(), statusModel));
			return baseConhecimentoData;
		}finally{
			baseConhecimentoData = null;
			baseConhecimentoLista = null;
		}
	}
}
