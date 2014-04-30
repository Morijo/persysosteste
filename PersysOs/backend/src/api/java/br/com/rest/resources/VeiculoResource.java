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
import br.com.exception.ModelException;
import br.com.frota.model.Veiculo;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;
import br.com.rest.hateoas.VeiculoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/recurso/veiculo")
public class VeiculoResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public VeiculoData getVeiculo(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Produto.CONSTRUTOR) String fields) {

		ArrayList<Veiculo> Veiculos = null;
		VeiculoData VeiculoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		
		try{
			Veiculos = (ArrayList<Veiculo>) Veiculo.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields,
					FormatDateHelper.formatTimeZone(since) ,consumerKey, Veiculo.class,statusModel);
			tx.commit();
			VeiculoData = new VeiculoData(Veiculos, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), 
					limit, offset, Veiculo.countPorConsumerSecret(session,Veiculo.class,consumerKey,statusModel,since)));
			return VeiculoData;
		}finally{
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
			Veiculos = null;
			VeiculoData = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVeiculo(@PathParam("id") Long id) {

		Veiculo veiculo = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			veiculo = valida(id, session);
			PreconditionsREST.checkNotNull(veiculo, "not found");
			return Response.ok(veiculo).build();
		}finally{
			veiculo = null;
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVeiculo(@PathParam("codigo") String codigo) {

		Veiculo veiculo = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			veiculo = valida(codigo, session);
			PreconditionsREST.checkNotNull(veiculo, "not found");
			return Response.ok(veiculo).build();
		}finally{
			veiculo = null;
			session.close();
			session = null;
		}
	}	

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response atualizaVeiculo(Veiculo veiculo, @PathParam("id") Long id) {
		PreconditionsREST.checkNotNull(veiculo, "not acceptable");

		Veiculo veiculoAtual = null;

		Session session = HibernateHelper.openSession(getClass());
		try{
			veiculoAtual = valida(id, session);
			PreconditionsREST.checkNotNull(veiculoAtual, "not found");
			return atualiza(session, veiculo, veiculoAtual);
		}finally{
			veiculo = null;
			veiculoAtual = null;
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response atualizaVeiculo(Veiculo veiculo, @PathParam("id") String codigo) {

		PreconditionsREST.checkNotNull(veiculo, "not acceptable");

		Veiculo veiculoAtual = null;

		Session session = HibernateHelper.openSession(getClass());
		try{
			veiculoAtual = valida(codigo, session);
			PreconditionsREST.checkNotNull(veiculoAtual, "not found");
			return atualiza(session, veiculo, veiculoAtual);
		}finally{
			veiculo = null;
			veiculoAtual = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Session session, Veiculo veiculo, Veiculo veiculoAtual) {
		veiculoAtual.setNome(veiculo.getNome());
		veiculoAtual.setPlaca(veiculo.getPlaca());
		veiculoAtual.setMarca(veiculo.getMarca());
		veiculoAtual.setCodigo(veiculo.getCodigo());
		veiculoAtual.setStatusModel(veiculo.getStatusModel());
		veiculoAtual.setDescricao(veiculo.getDescricao());
		try {
			veiculoAtual.alterar(session);
			return Response.ok().entity(veiculoAtual).build();

		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	public Response adicionarVeiculo(Veiculo veiculo) {
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			veiculo.setMedida(null);
			veiculo.setConsumerId(Long.parseLong(consumerKey));
			veiculo.salvar();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}

		return Response.ok(URI.create(String.valueOf(200))).entity(veiculo).build();

	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeVeiculo(@PathParam("id") long id) {

		Veiculo veiculo = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			veiculo = valida(id, session);
			PreconditionsREST.checkNotNull(veiculo, "not found");
			veiculo.remover();
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

	private Veiculo valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Veiculo veiculo = (Veiculo) Veiculo.pesquisaPorIdConsumerSecret(session,Veiculo.class,id,consumerKey);
		tx.commit();
		return veiculo;
	}
	
	private Veiculo valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Veiculo veiculo = (Veiculo) Veiculo.pesquisaPorCodigoConsumerSecret(session,Veiculo.class,codigo,consumerKey);
		tx.commit();
		return veiculo;
	}
	
	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public VeiculoData buscaUnidadeLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("placa") @DefaultValue("") String nomeVeiculo,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<Veiculo> veiculoLista = null;
		VeiculoData veiculoData = null;
		String cs = null;

		try{		
			cs = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			try {
				veiculoLista = Veiculo.buscaVeiculo(cs,nomeVeiculo, codigo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}

			PreconditionsREST.checkNotNull(veiculoLista, "Nenhum resultado encontrado");

			veiculoData = new VeiculoData(veiculoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), veiculoLista.size(), 0, veiculoLista.size()));
			return veiculoData;
		}finally{
			veiculoData = null;
			veiculoLista = null;
			cs = null;
		}
	}
}
