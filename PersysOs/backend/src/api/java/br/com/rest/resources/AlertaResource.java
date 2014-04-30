package br.com.rest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.sun.jersey.spi.container.ResourceFilters;

import br.com.eventos.model.Alerta;
import br.com.exception.ModelException;
import br.com.model.StatusModel;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.AlertaData;
import br.com.rest.hateoas.ResponseBatchData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.represention.ResponseBatch;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;
import br.com.usuario.model.Usuario;

/**
 * Recurso produto
 * @author ricardosabatine	
 * @version 1.0.0
 * @see Produto
 */
@Path("/alerta")
public class AlertaResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public AlertaData get(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Alerta.CONSTRUTOR) String fields) {

		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		ArrayList<Alerta> alertas = null;
		AlertaData alertaData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			alertas = (ArrayList<Alerta>) Alerta.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields,FormatDateHelper.formatTimeZone(since),consumerKey, Alerta.class,statusModel);
			tx.commit();
			alertaData = new AlertaData(alertas, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Alerta.countPorConsumerSecret(session,Alerta.class,consumerKey,statusModel,since),statusModel));
			return alertaData;
		}finally{
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
			alertas = null;
			alertaData = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response get(@PathParam("id") Long id) {

		Alerta alerta = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			alerta = valida(id, session);
			PreconditionsREST.checkNotNull(alerta, "not found");
			return Response.ok(alerta).build();
		}finally{
			alerta = null;
			session.close();
			session = null;
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionar(Alerta alerta) {

		PreconditionsREST.checkNotNull(alerta,"not enough items");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Session session = HibernateHelper.openSession(getClass());

		try{
			Usuario usuario = validaUsuario(session);
			alerta.setUsuario(usuario);
			PreconditionsREST.checkNotNull(usuario,"user not found");
			
			alerta.setConsumerSecret(new ConsumerSecret("",Long.parseLong(consumerKey)));
			alerta.salvar();
			return Response.ok(URI.create(String.valueOf(200))).entity(alerta).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			alerta = null;
			session.close();
			session = null;
		}
	}

	@Path("/batch")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarLote(AlertaData alertaBatch) {

		PreconditionsREST.checkNotNull(alertaBatch, "not enough items");
		String  cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		List<ResponseBatch> responseBatchs = new ArrayList<ResponseBatch>();

		Session session = HibernateHelper.openSession(getClass());
		try{
			ArrayList<Alerta> alertas = (ArrayList<Alerta>) alertaBatch.getDados();
			Usuario usuario = validaUsuario(session);
			PreconditionsREST.checkNotNull(usuario,"user not found");
			int i = 0;
			for(Alerta alerta : alertas){
				alerta.setUsuario(usuario);
				alerta.setConsumerSecret(new ConsumerSecret("",Long.parseLong(cs)));
				try{
					alerta.salvar(session);
					responseBatchs.add(new ResponseBatch(alerta.getCodigo(), alerta.getStatusModel(), alerta.getId(), 0));
				}catch(ModelException e){
					responseBatchs.add(new ResponseBatch(alerta.getCodigo(),StatusModel.REJEITADO, -1l, 1));
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

	private Alerta valida(long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Transaction tx = session.beginTransaction();
		Alerta alerta = (Alerta) Alerta.pesquisaPorIdConsumerSecret(session,Alerta.class,id,consumerKey);
		tx.commit();
		return alerta;
	}

	private Usuario validaUsuario(Session session) {
		Usuario usuario = (Usuario) Usuario.pesquisaAccessTokenReturnId(session, FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")));
		PreconditionsREST.checkNotNull(usuario, "not found user");
		return usuario;
	}
}
