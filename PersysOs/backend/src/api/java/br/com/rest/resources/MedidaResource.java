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
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.model.Medida;
import br.com.rest.hateoas.MedidaData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/medida")
public class MedidaResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public MedidaData getMedida(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Medida.CONSTRUTOR) String fields){

		ArrayList<Medida> medida = null;
		MedidaData medidaData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String consumerKey = null;
		try{
			consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			medida = (ArrayList<Medida>) Medida.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields,FormatDateHelper.formatTimeZone(since) ,consumerKey, Medida.class,statusModel);
			tx.commit();
			medidaData = new MedidaData(medida, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Medida.countPorConsumerSecret(session,Medida.class,consumerKey,statusModel,since),statusModel));
			return medidaData;
		}finally{
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
			medida = null;
			medidaData = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getMedida(@PathParam("id") Long id) {

		Medida medida = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			medida = valida(id, session);
			PreconditionsREST.checkNotNull(medida, "not found");
			return Response.ok(medida).build();
		}finally{
			medida = null;
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getMedida(@PathParam("codigo") String codigo) {

		Medida medida = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			medida = valida(codigo, session);
			PreconditionsREST.checkNotNull(medida, "not found");
			return Response.ok(medida).build();
		}finally{
			medida = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualiza(Medida medida, @PathParam("id") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			Medida medidaAtual = valida(id, session);
			PreconditionsREST.checkNotNull(medida, "not found");
			return atualiza(medida, medidaAtual);
		}finally{
			medida = null;
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualiza(Medida medida, @PathParam("codigo") String  codigo) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			Medida medidaAtual = valida(codigo, session);
			PreconditionsREST.checkNotNull(medida, "not found");
			return atualiza(medida, medidaAtual);
		}finally{
			medida = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Medida medida, Medida medidaAtual) {
		medidaAtual.setNome(medida.getNome());
		medidaAtual.setAbreviacao(medida.getAbreviacao());
		medidaAtual.setObservacao(medida.getObservacao());
		medidaAtual.setStatusModel(medida.getStatusModel());
		try {
			medidaAtual.alterar();
			return Response.ok().entity(medidaAtual).build();

		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarMedida(Medida medida) {
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			medida.setConsumerId(Long.parseLong(consumerKey));
			medida.salvar();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}

		return Response.ok(URI.create(String.valueOf(200))).entity(medida).build();
	}
	
	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeMedida(@PathParam("id") long id) {

		Medida medida = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			medida = valida(id, session);
			PreconditionsREST.checkNotNull(medida, "not found");
			medida.remover();
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

	private Medida valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Medida medida = (Medida) Medida.pesquisaPorIdConsumerSecret(session,Medida.class,id,consumerKey);
		tx.commit();
		return medida;
	}

	private Medida valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Medida medida = (Medida) Medida.pesquisaPorCodigoConsumerSecret(session,Medida.class,codigo,consumerKey);
		tx.commit();
		return medida;
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public MedidaData busca(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("nome") @DefaultValue("") String nomeMedida,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<Medida> medidaLista = null;
		MedidaData medidaData = null;
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		try{
			medidaLista =Medida.busca(consumerKey, nomeMedida, codigo, statusModel);
			PreconditionsREST.checkNotNull(medidaLista, "not found");
			medidaData = new MedidaData(medidaLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), medidaLista.size(), 0, medidaLista.size(),statusModel));
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
		}
		return medidaData;
	}

}
