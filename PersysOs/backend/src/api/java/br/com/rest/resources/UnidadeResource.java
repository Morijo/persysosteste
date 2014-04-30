package br.com.rest.resources;

import java.util.ArrayList;
import java.util.Collection;
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
import br.com.contato.model.Endereco;
import br.com.empresa.model.Departamento;
import br.com.empresa.model.Organizacao;
import br.com.empresa.model.Unidade;
import br.com.exception.ModelException;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.DepartamentoData;
import br.com.rest.hateoas.UnidadeData;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/unidade")
public class UnidadeResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public UnidadeData getHomeUnidade(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Unidade.CONSTRUTOR) String fields) {

		Collection<Unidade> unidades = null;
		UnidadeData unidadeData;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			unidades = (ArrayList<Unidade>) Unidade.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit, fields, FormatDateHelper.formatTimeZone(since), consumerKey ,Unidade.class,statusModel);
			tx.commit();
			PreconditionsREST.checkNotNull(unidades, "not found");
			unidadeData = new UnidadeData(unidades,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Unidade.countPorConsumerSecret(session, Unidade.class, consumerKey,statusModel,since),statusModel));
			return unidadeData;	
		}finally{
			unidades = null;
			unidadeData = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@SuppressWarnings("unchecked")
	@Path("/home")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public UnidadeData getUnidade(@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel) {

		UnidadeData unidadeData;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Number number = Unidade.countPorConsumerSecret(session, Unidade.class, consumerKey,statusModel,since);
			ArrayList<Unidade> unidade= (ArrayList<Unidade>) Unidade.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit, "", FormatDateHelper.formatTimeZone(since), consumerKey ,Unidade.class, statusModel);
			unidadeData = new UnidadeData(unidade,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, number, statusModel));
			tx.commit();
			return unidadeData;	
		}finally{
			unidadeData= null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Unidade getUnidade(@PathParam("id") Long id) {
		Unidade unidade  = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			unidade = valida(id, session);
			PreconditionsREST.checkNotNull(unidade, "not null");
			return unidade;
		}finally{
			unidade = null;
			session.close();
			session = null;
		}
	}
	
	@Path("codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Unidade getUnidade(@PathParam("codigo") String codigo) {
		Unidade unidade  = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			unidade = valida(codigo, session);
			PreconditionsREST.checkNotNull(unidade, "not null");
			return unidade;
		}finally{
			unidade = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}/endereco")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Endereco getUnidadeEndereco(@PathParam("id") Long id) {
		Unidade unidade =  null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			unidade = valida(id, session);
			PreconditionsREST.checkNotNull(unidade.getEndereco(), "not null");
			return unidade.getEndereco();
		}finally{
			unidade = null;
			session.close();
			session = null;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionar(Unidade unidade) {
		
		PreconditionsREST.checkNotNull(unidade, "not found");
		
		Organizacao    organizacao = null;
		Session        session  = null;
		try{
			session = HibernateHelper.openSession(getClass());
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			organizacao = Organizacao.pesquisaConsumerSecret(session,consumerKey);
			PreconditionsREST.checkNotNull(organizacao, "not null org");
			try {
				unidade.setOrganizacao(organizacao);
				unidade.setConsumerSecret(organizacao.getConsumerSecret());
				unidade.setEndereco(null);
				unidade.salvar();
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.ok().entity(unidade).build();
		}finally{
			organizacao = null;
			unidade = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alterar(@PathParam("id") Long id,Unidade unidade) {
		
		PreconditionsREST.checkNotNull(unidade, "not found");
		
		Session session = HibernateHelper.openSession(getClass());
		try{
			Unidade unidadeAtual = valida(id, session);
			PreconditionsREST.checkNotNull(unidadeAtual, "not found");
			unidadeAtual.setNome(unidade.getNome());
			unidadeAtual.setEmail(unidade.getEmail());
			unidadeAtual.setStatusModel(unidade.getStatusModel());
			try {
				unidadeAtual.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.ok().entity(unidade).build();
		}finally{
			unidade = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removerUnidade(@PathParam("id") Long id) {
		Unidade 	unidade;
		Session session = HibernateHelper.openSession(getClass());
		try{
			unidade = valida(id, session);
			PreconditionsREST.checkNotNull(unidade, "not found");
			unidade.remover();
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
		}finally{
			unidade        = null;
			session.close();
			session = null;
		}
		return Response.noContent().build();
	}

	@Path("{id}/departamento")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public DepartamentoData listaDepartamentoUnidade(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@PathParam("id") Long id) {
		ArrayList<Departamento> departamentoLista = null;
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			departamentoLista = Departamento.listaDepartamentosPorUnidade(id, consumerKey, statusModel);
			PreconditionsREST.checkNotNull(departamentoLista,"Nenhum registro encontrado");
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
		}
		return new DepartamentoData(departamentoLista);
	}

	@Path("{id}/endereco")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionartEndereco(@PathParam("id") Long id,Endereco end) {
		PreconditionsREST.checkNotNull(end, "not found");
		Unidade unidade = null;
		Session session = null;
		try{
			session = HibernateHelper.openSession(getClass());
			unidade = valida(id, session);
			PreconditionsREST.checkNotNull(unidade, "Unidade sem registro no sistema");
			end.setConsumerSecret(unidade.getConsumerSecret());
			unidade.setEndereco(end);
			try {
				end.valida();
				unidade.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.ok().entity(end).build();
		}finally{
			unidade = null;
			end = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}/endereco")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alterarHomeEndereco(@PathParam("id") Long id, Endereco end) {
		Unidade  unidade  = null;
		Endereco endAtual =null;
		Session  session  = null;
		try{
			session = HibernateHelper.openSession(getClass());
			unidade = valida(id, session);
			PreconditionsREST.checkNotNull(unidade, "not null");
			if(unidade.getEndereco() == null){
				adicionartEndereco(id, end);
			}else{
				endAtual = unidade.getEndereco();
				endAtual.setBairro(end.getBairro());
				endAtual.setCidade(end.getCidade()) ;
				endAtual.setLogradouro(end.getLogradouro());
				endAtual.setCep(end.getCep());
				endAtual.setNumero(end.getNumero());
				endAtual.setEstado(end.getEstado());
				try{
					endAtual.setComplemento(end.getComplemento());
				}catch (Exception e) {
					endAtual.setComplemento("");
				}
				try {
					endAtual.valida();
					endAtual.alterar(session);
				} catch (ModelException e) {
					PreconditionsREST.error(e.getMessage());
				}
			}    
			return Response.ok().entity(end).build();
		}finally{
			unidade = null;
			end = null;
			session.close();
			session = null;
		}
	}
	
	private Unidade valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Unidade unidade = (Unidade) Unidade.pesquisaPorIdConsumerSecret(session,Unidade.class, id, consumerKey);
		tx.commit();
		return unidade;
	}
	
	private Unidade valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Unidade unidade = (Unidade) Unidade.pesquisaPorCodigoConsumerSecret(session,Unidade.class, codigo, consumerKey);
		tx.commit();
		return unidade;
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public UnidadeData buscaUnidadeLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam("nome") @DefaultValue("") String nomeUnidade,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		ArrayList<Unidade> unidadeLista = null;
		try{
			unidadeLista = Unidade.busca(consumerKey, nomeUnidade, codigo, statusModel);
			PreconditionsREST.checkNotNull(unidadeLista, "Nenhum resultado encontrado");
			UnidadeData unidadeData = new UnidadeData(unidadeLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), unidadeLista.size(), 0, unidadeLista.size(), statusModel));
			return unidadeData;
		}finally{
			unidadeLista = null;
		}
	}
}
