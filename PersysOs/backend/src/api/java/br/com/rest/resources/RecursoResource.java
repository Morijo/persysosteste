package br.com.rest.resources;

import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.exception.ModelException;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.model.Recurso;
import br.com.rest.hateoas.RecursoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/recurso")
public class RecursoResource {

	@Context HttpServletRequest httpServletRequest;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RecursoData lista(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Recurso.CONSTRUTOR) String fields) {

		String cs = null;
		ArrayList<Recurso> recursos = null;
		RecursoData recursoData = null; 
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			cs = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			PreconditionsREST.checkNotNull(cs, "Organização não encontrado");
			recursos = (ArrayList<Recurso>) Recurso.listaRecurso(fields,statusModel,cs);
			recursoData = new RecursoData(recursos, LinkData.createLinks( httpServletRequest.getRequestURL().toString(), limit, offset, Recurso.countPorConsumerSecret(session, Recurso.class, cs,statusModel, since),statusModel));
			tx.commit();
			return recursoData;
		}catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			cs = null;
			recursos = null;
			recursoData = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RecursoData buscaRecursoLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("nome") @DefaultValue("") String nomeRecurso,
			@QueryParam("codigo") @DefaultValue("") String codigo,
			@QueryParam("tipo") @DefaultValue("") String tipo) {

		ArrayList<Recurso> recursoLista = null;
		RecursoData recursoData = null;
		String cs = null;

		try{
			cs = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			try {
				recursoLista =(ArrayList<Recurso>) Recurso.busca(cs, nomeRecurso, codigo, tipo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}

			PreconditionsREST.checkNotNull(recursoLista, "not found");

			recursoData = new RecursoData(recursoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), recursoLista.size(), 0, recursoLista.size(),statusModel));
			return recursoData;
		}finally{
			recursoData = null;
			recursoLista = null;
			cs = null;
		}
	}
}