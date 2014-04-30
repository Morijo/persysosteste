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
import br.com.recurso.model.Chip;
import br.com.rest.hateoas.ChipData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/recurso/chip")
public class ChipResource {


	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public ChipData getChip(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Chip.CONSTRUTOR) String fields) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<Chip> chips = null;
		ChipData chipData = null;
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		try{
			chips = (ArrayList<Chip>) Chip.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields,FormatDateHelper.formatTimeZone(since) ,consumerKey, Chip.class,statusModel);
			chipData = new ChipData(chips, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Chip.countPorConsumerSecret(session,Chip.class,consumerKey,statusModel,since),statusModel));
			tx.commit();
			return chipData;
		}catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			tx = null;
			session.close();
			session = null;
			chipData = null;
			chips = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getChip(@PathParam("id") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Chip chip = null;

		try{
			chip = valida(id, session);
			PreconditionsREST.checkNotNull(chip, "not found");
			tx.commit();
			return Response.ok(chip).build();
		}finally{
			chip = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getChip(@PathParam("codigo") String codigo) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Chip chip = null;

		try{
			chip = valida(codigo, session);
			PreconditionsREST.checkNotNull(chip, "not found");
			tx.commit();
			return Response.ok(chip).build();
		}finally{
			chip = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response atualizaChip(Chip chip, @PathParam("id") Long id) {

		PreconditionsREST.checkNotNull(chip, "not acceptable");

		Session session = HibernateHelper.openSession(getClass());
		Chip chipAtual = null;
		try{
			chipAtual = valida(id, session);
			PreconditionsREST.checkNotNull(chip, "not found");
			return atualiza(chip, chipAtual, session);
		}finally{
			chip = null;
			chipAtual = null;
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response atualizaChip(Chip chip, @PathParam("codigo") String codigo) {

		PreconditionsREST.checkNotNull(chip, "not acceptable");

		Session session = HibernateHelper.openSession(getClass());
		Chip chipAtual = null;
		try{
			chipAtual = valida(codigo, session);
			PreconditionsREST.checkNotNull(chip, "not found");
			return atualiza(chip, chipAtual, session);
		}finally{
			chip = null;
			chipAtual = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Chip chip,Chip chipAtual, Session session) {
		chipAtual.setModelo(chip.getModelo());
		chipAtual.setOperadora(chip.getOperadora());
		chipAtual.setPrincipal(chip.getPrincipal());
		chipAtual.setDddNumero(chip.getDddNumero());
		chipAtual.setLigacao(chip.getLigacao());
		try {
			chipAtual.alterar(session);
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		return Response.ok().entity(chipAtual).build();
	}


	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	public Response adicionarChip(Chip chip) {
		
		PreconditionsREST.checkNotNull(chip, "not acceptable");

		Session session = HibernateHelper.openSession(getClass());
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		try {
			chip.setConsumerId(Long.parseLong(consumerKey));
			chip.salvar();
			return Response.ok(URI.create(String.valueOf(201))).entity(chip).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		finally{
			chip = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeChip(@PathParam("id") long id) {

		Session session = HibernateHelper.openSession(getClass());
		Chip chip = null;
		try{
			chip = valida(id, session);
			PreconditionsREST.checkNotNull(chip, "not found");
			chip.remover();
			return Response.noContent().build();

		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}catch (NullPointerException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Sem registro no servidor")).build());
		}finally{
			chip = null;
			session.close();
			session = null;
		}
	}

	private Chip valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Chip chipAtual = (Chip) Chip.pesquisaPorIdConsumerSecret(session, Chip.class,id,consumerKey);
		tx.commit();
		return chipAtual;
	}
	
	private Chip valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Chip chipAtual = (Chip) Chip.pesquisaPorCodigoConsumerSecret(session, Chip.class,codigo,consumerKey);
		tx.commit();
		return chipAtual;
	}
	
	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ChipData buscaDispositvoLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("dddNumero") @DefaultValue("") String dddNumero,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<Chip> chipLista = null;
		ChipData chipData = null;
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		
		try{
			PreconditionsREST.checkNotNull(chipLista, "not found");
			try {
				chipLista = Chip.buscaChip(consumerKey, dddNumero, codigo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			chipData = new ChipData(chipLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), chipLista.size(), 0, chipLista.size(),statusModel));
			return chipData;
		}finally{
			chipData = null;
			chipLista = null;
		
		}
	}
}
