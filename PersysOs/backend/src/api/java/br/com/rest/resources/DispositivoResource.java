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
import br.com.recurso.model.Dispositivo;
import br.com.rest.hateoas.DispositivoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/recurso/dispositivo")
public class DispositivoResource {


	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public DispositivoData getDispositivo(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue("id,IMEI,codigo,statusModel") String fields) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<Dispositivo> dispositivos = null;
		DispositivoData dispositivoData = null;

		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			dispositivos = (ArrayList<Dispositivo>) Dispositivo.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields, 
					FormatDateHelper.formatTimeZone(since),consumerKey, Dispositivo.class, statusModel);

			dispositivoData = new DispositivoData(dispositivos, LinkData.createLinks(httpServletRequest.getRequestURL().toString(),
					limit, offset, Dispositivo.countPorConsumerSecret(session,Dispositivo.class,consumerKey,statusModel,since),statusModel));

			tx.commit();
			return dispositivoData;
		}catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			tx = null;
			session.close();
			session = null;
			dispositivoData = null;
			dispositivos = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getDispositivo(@PathParam("id") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		Dispositivo dispositivo = null;
		try{
			dispositivo = valida(id, session);
			PreconditionsREST.checkNotNull(dispositivo, "not found");
			return Response.ok(dispositivo).build();

		}finally{
			dispositivo = null;
			session.close();
			session = null;
		}
	}
	
	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getDispositivo(@PathParam("codigo") String codigo) {

		Session session = HibernateHelper.openSession(getClass());
		Dispositivo dispositivo = null;
		try{
			dispositivo = valida(codigo, session);
			PreconditionsREST.checkNotNull(dispositivo, "not found");
			return Response.ok(dispositivo).build();

		}finally{
			dispositivo = null;
			session.close();
			session = null;
		}

	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaDispositivo(Dispositivo dispositivo, @PathParam("id") Long id) {

		PreconditionsREST.checkNotNull(dispositivo, "not enough items");
		
		Session session = HibernateHelper.openSession(getClass());
		Dispositivo dispositivoAtual = null;
		try{
			dispositivoAtual = valida(id, session);
			PreconditionsREST.checkNotNull(dispositivo, "not found");
			return atualiza(session, dispositivo, dispositivoAtual);
		}finally{
			dispositivo = null;
			dispositivoAtual = null;
			session.close();
			session = null;

		}
	}

	@Path("codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaDispositivo(Dispositivo dispositivo, @PathParam("codigo") String codigo) {

		PreconditionsREST.checkNotNull(dispositivo, "not enough items");
		
		Session session = HibernateHelper.openSession(getClass());
		Dispositivo dispositivoAtual = null;
		try{
			dispositivoAtual = valida(codigo, session);
			PreconditionsREST.checkNotNull(dispositivoAtual, "not found");
			return atualiza(session, dispositivo, dispositivoAtual);
		}finally{
			dispositivo = null;
			dispositivoAtual = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Session session, Dispositivo dispositivo,
			Dispositivo dispositivoAtual) {
		dispositivoAtual.setNome(dispositivo.getNome());
		dispositivoAtual.setModelo(dispositivo.getModelo());
		dispositivoAtual.setMarca(dispositivo.getMarca());
		dispositivoAtual.setDescricao(dispositivo.getDescricao());
		dispositivoAtual.setEtiqueta(dispositivo.getEtiqueta());
		dispositivoAtual.setIMEI(dispositivo.getIMEI());
		try {
			dispositivoAtual.alterar(session);
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		return Response.ok().entity(dispositivoAtual).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarDispositivo(Dispositivo dispositivo) {

		Session session = HibernateHelper.openSession(getClass());

		String consumerKey =FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try {
			dispositivo.setConsumerId(Long.parseLong(consumerKey));
			dispositivo.salvar();
			return Response.ok(URI.create(String.valueOf(201))).entity(dispositivo).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			dispositivo = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeDispositivo(@PathParam("id") long id) {

		Session session = HibernateHelper.openSession(getClass());
		Dispositivo dispositivo = null;
		try{
			dispositivo = valida(id, session);
			PreconditionsREST.checkNotNull(dispositivo, "not found");
			dispositivo.remover();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}catch (NullPointerException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "not found")).build());
		}finally{
			dispositivo = null;
			session.close();
			session = null;
		}
	}

	private Dispositivo valida(Long id, Session session) {
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Dispositivo dispositivoAtual = (Dispositivo) Dispositivo.pesquisaPorIdConsumerSecret(session, Dispositivo.class,id,consumerKey);
		tx.commit();
		return dispositivoAtual;
	}
	
	private Dispositivo valida(String codigo, Session session) {
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Dispositivo dispositivoAtual = (Dispositivo) Dispositivo.pesquisaPorCodigoConsumerSecret(session, Dispositivo.class,codigo,consumerKey);
		tx.commit();
		return dispositivoAtual;
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public DispositivoData buscaDispositvoLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("IMEI") @DefaultValue("") String numeroSerie,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		ArrayList<Dispositivo> dispositivoLista = null;
		DispositivoData dispositivoData = null;
		try{
			
			try {
				dispositivoLista = Dispositivo.busca(numeroSerie, codigo, statusModel, consumerKey);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			PreconditionsREST.checkNotNull(dispositivoLista, "not found");

   		    dispositivoData = new DispositivoData(dispositivoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), dispositivoLista.size(), 0, dispositivoLista.size(), statusModel));
			return dispositivoData;
		}finally{
			dispositivoData = null;
			dispositivoLista = null;
		}
	}
}
