package br.com.rest.resources;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sun.jersey.spi.container.ResourceFilters;
import br.com.empresa.model.Departamento;
import br.com.empresa.model.Unidade;
import br.com.exception.ModelException;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.DepartamentoData;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/departamento")
public class DepartamentoResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public DepartamentoData listaDepartamento(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("1000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Departamento.CONSTRUTOR) String fields) {

		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		DepartamentoData departamentoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<Departamento> deparmentoLista = (ArrayList<Departamento>) Departamento.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit, fields,FormatDateHelper.formatTimeZone(since) ,consumerKey, Departamento.class,statusModel);
		tx.commit();
		departamentoData = new DepartamentoData(deparmentoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Departamento.countPorConsumerSecret(session, Departamento.class, consumerKey, statusModel,since),statusModel));
		return departamentoData;	
	}


	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Departamento getDepartamento(@PathParam("id") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			Departamento departamento = valida(id, session);
			PreconditionsREST.checkNotNull(departamento, "not found");

			if(departamento.getUnidade() != null)
				departamento.getUnidade().setEndereco(null);
			return departamento;	
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Departamento getDepartamento(@PathParam("codigo") String codigo) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			Departamento departamento = valida(codigo, session);
			PreconditionsREST.checkNotNull(departamento, "not found");
			if(departamento.getUnidade() != null)
				departamento.getUnidade().setEndereco(null);
			return departamento;	
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("/unidade/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public DepartamentoData listaDepartamentoUnidade(
			@PathParam("id") Long id,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel) {

		ArrayList<Departamento> departamentoLista = null;

		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			departamentoLista = Departamento.listaDepartamentosPorUnidade(id, consumerKey, statusModel);
			PreconditionsREST.checkNotNull(departamentoLista,"not found");
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
		}
		return new DepartamentoData(departamentoLista);

	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adiciona(Departamento departamento) {

		Unidade   unidade = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			if(departamento.getUnidade() == null) 
				try {
					unidade = Unidade.getUnidadePadrao(session, consumerKey);
				} catch (ModelException e1) {
					PreconditionsREST.checkNotNull(unidade, "not found unidade");
				}
			else{
				if(departamento.getUnidade().getId() == null) 
					try {
						unidade = Unidade.getUnidadePadrao(session, consumerKey);
					} catch (ModelException e1) {
						PreconditionsREST.checkNotNull(unidade, "not found unidade");
					}
				else
					unidade = (Unidade) Unidade.pesquisaPorIdConsumerSecret(session,Unidade.class, departamento.getUnidade().getId(), consumerKey);
			}
			tx.commit();
			PreconditionsREST.checkNotNull(unidade, "not found unidade");
			departamento.setUnidade(unidade);
			departamento.setConsumerId(Long.parseLong(consumerKey));
			try {
				departamento.salvar();
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.ok().entity(departamento).build();
		}finally{
			departamento = null;
			unidade = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alterarDepartamento(@PathParam("id") Long id, Departamento departamento) {

		PreconditionsREST.checkNotNull(departamento, "not found");
		Session session = HibernateHelper.openSession(getClass());
		try{
			Departamento departamentoAtual = valida(id, session);
			PreconditionsREST.checkNotNull(departamentoAtual, "not found");
			return atualiza(departamento, departamentoAtual, session);
		}finally{
			departamento = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Departamento departamento,
			Departamento departamentoAtual, Session session) {
		departamentoAtual.setDescricao(departamento.getDescricao());
		departamentoAtual.setEmail(departamento.getEmail());
		departamentoAtual.setNomeDepartamento(departamento.getNomeDepartamento());
		departamentoAtual.setResponsavel(departamento.getResponsavel());
		departamentoAtual.setStatusModel(departamento.getStatusModel());
		departamentoAtual.setTelefone(departamento.getTelefone());
		departamentoAtual.setRamal(departamento.getRamal());
		departamentoAtual.setTipo(departamento.getTipo());
		if(departamento.getUnidade().getId() != null)
			departamentoAtual.setUnidade(departamento.getUnidade());
		try {
			departamentoAtual.alterar(session);
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
		}
		return Response.ok().entity(departamento).build();
	}

	@Path("{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removerDepartamento(@PathParam("id") Long id) {
		Session session = HibernateHelper.openSession(getClass());
		try{
			Departamento departamento = valida(id, session);
			PreconditionsREST.checkNotNull(departamento, "not found");
			departamento.remover();
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
		}finally{
			session.close();
			session = null;
		}
		return Response.noContent().build();
	}

	private Departamento valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Departamento departamento = (Departamento) Departamento.pesquisaPorIdConsumerSecret(session,Departamento.class, id, consumerKey);
		tx.commit();
		return departamento;
	}

	private Departamento valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Departamento departamento = (Departamento) Departamento.pesquisaPorCodigoConsumerSecret(session,Departamento.class, codigo, consumerKey);
		tx.commit();
		return departamento;
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public DepartamentoData buscaUnidadeLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam("nome") @DefaultValue("") String nomeDepartamento,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		ArrayList<Departamento> departamentoLista = null;
		try{
			try {
				departamentoLista = Departamento.busca(consumerKey, nomeDepartamento, codigo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			PreconditionsREST.checkNotNull(departamentoLista, "Nenhum resultado encontrado");
			DepartamentoData departamentoData = new DepartamentoData(departamentoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), departamentoLista.size(), 0, departamentoLista.size(),statusModel));
			return departamentoData;
		}finally{
			departamentoLista = null;
		}
	}
}