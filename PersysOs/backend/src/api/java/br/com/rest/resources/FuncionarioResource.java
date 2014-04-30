package br.com.rest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.spi.container.ResourceFilters;

import br.com.empresa.model.Departamento;
import br.com.exception.ModelException;
import br.com.funcionario.model.Funcionario;
import br.com.model.StatusModel;
import br.com.model.interfaces.IGrupoUsuario;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.FuncionarioData;
import br.com.rest.hateoas.ResponseBatchData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.represention.ResponseBatch;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;
import br.com.usuario.model.GrupoUsuario;

@Path("/funcionario")
public class FuncionarioResource {

	@Context 
	HttpServletRequest httpServletRequest;

	@Context 
	ResourceContext resourceContext;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public FuncionarioData getFuncionario (
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Funcionario.CONSTRUTOR) String fields) {

		ArrayList<Funcionario> funcionario = null;
		FuncionarioData funcionarios = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			funcionario = (ArrayList<Funcionario>) Funcionario.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields,since,consumerKey, Funcionario.class,statusModel);
			tx.commit();
			funcionarios = new FuncionarioData(funcionario,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset,Funcionario.countPorConsumerSecret(session,Funcionario.class, consumerKey,statusModel,since),statusModel));
			return funcionarios;
		}finally{
			funcionario = null;
			funcionarios = null;
			tx = null;
			session.close();
			session = null;
		}

	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getFuncionario(@PathParam("id") Long id) {
		try{
			Funcionario funcionario = valida(id);
			PreconditionsREST.checkNotNull(funcionario, "not found");
			return Response.ok().entity(funcionario).build();
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("Empregado", e.getMessage())).build());
		}
	}

	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getFuncionario(@PathParam("codigo") String codigo) {

		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Funcionario funcionario = (Funcionario) Funcionario.pesquisaFuncionarioPorCodigo(codigo, consumerKey);
			PreconditionsREST.checkNotNull(funcionario, "not found");
			return Response.ok().entity(funcionario).build();
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("Empregado", e.getMessage())).build());
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualiza(Funcionario funcionario, @PathParam("id") Long id) {

		PreconditionsREST.checkNotNull(funcionario, "not found");

		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		funcionario.setConsumerId(Long.parseLong(consumerKey));
		try{
			try {
				funcionario.alteraFuncionario();
				return Response.ok().entity(funcionario).build();
			} catch (ModelException e) {
				throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("Empregado", e.getMessage())).build());
			}
		}finally{
			funcionario = null;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionar(Funcionario funcionario) {

		PreconditionsREST.checkNotNull(funcionario, "not found");

		Session session = HibernateHelper.openSession(getClass());
		try {
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			
			if(funcionario.getGrupoUsuario() == null){
				if(funcionario.getGrupoUsuario().getCodigo().isEmpty())
					funcionario.setGrupoUsuario((IGrupoUsuario) GrupoUsuario.pesquisaPorCodigoConsumerSecret(GrupoUsuario.class, "PGRU0", consumerKey));
			}
			
			funcionario.setGrupoUsuario(new GrupoUsuario(1l));
			
			//verifica departamento
			Departamento departamento = (Departamento) funcionario.getDepartamento();
			if(departamento != null){
				if(departamento.getId() != null)
					departamento = (Departamento) Departamento.pesquisaPorIdConsumerSecret(Departamento.class,departamento.getId(),consumerKey);
				else if(!departamento.getCodigo().isEmpty()){
					departamento = (Departamento) Departamento.pesquisaPorCodigoConsumerSecret(Departamento.class,departamento.getCodigo(),consumerKey);
				}else{
					departamento = (Departamento) Departamento.pesquisaPorCodigoConsumerSecret(Departamento.class,"PDEP0",consumerKey);
				}
				funcionario.setDepartamento(departamento);
			}
			
			funcionario.setConsumerId(Long.parseLong(consumerKey));
			funcionario.salvar();
			try{
				Funcionario.recuparaSenhaPorId(funcionario.getId(), consumerKey);
			} catch (Exception e) {}
			return Response.ok(URI.create(String.valueOf(200))).entity(funcionario).build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("Empregado", e.getMessage())).build());
		}finally{
			funcionario = null;
			session.close();
			session = null;
		}
	}

	@Path("/batch")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarLote(FuncionarioData funcionarioBatch) {

		PreconditionsREST.checkNotNull(funcionarioBatch, "not found");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		List<ResponseBatch> responseBatchs = new ArrayList<ResponseBatch>();

		Session session = HibernateHelper.openSession(getClass());
		try{
			ArrayList<Funcionario> funcionarios = (ArrayList<Funcionario>) funcionarioBatch.getDados();

			int i = 0;
			for(Funcionario funcionario : funcionarios){
				funcionario.setConsumerSecret(new ConsumerSecret("",Long.parseLong(consumerKey)));
				try{
					funcionario.salvar();
					try{
						Funcionario.recuparaSenhaPorId(funcionario.getId(), consumerKey);
					} catch (ModelException e) {}
					responseBatchs.add(new ResponseBatch(funcionario.getCodigo(), 1, funcionario.getId(),0));
				}catch(ModelException e){
					responseBatchs.add(new ResponseBatch(funcionario.getCodigo(), StatusModel.REJEITADO, -1l, 1));
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

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response remove(@PathParam("id") long id) {

		try {
			Funcionario funcionario =valida(id);
			PreconditionsREST.checkNotNull(funcionario, "not found");
			funcionario.remover();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("Empregado",e.getMessage())).build());
		}
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public FuncionarioData busca( 
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam("nome") @DefaultValue("") String nome,
			@QueryParam("cpf") @DefaultValue("") String cpf,
			@QueryParam("rg") @DefaultValue("") String rg,
			@QueryParam("nomeusuario") @DefaultValue("") String nomeUsuario) {

		ArrayList<Funcionario> funcionarioLista = null;
		try{

			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			try {
				funcionarioLista =(ArrayList<Funcionario>) Funcionario.busca(nome,cpf,rg,nomeUsuario,statusModel,consumerKey);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			PreconditionsREST.checkNotNull(funcionarioLista, "not found");

			FuncionarioData funcionarioData = new FuncionarioData(funcionarioLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), funcionarioLista.size(), 0, funcionarioLista.size(), statusModel));
			return funcionarioData;
		}finally{
			funcionarioLista = null;
		}
	}
	
	private Funcionario valida(Long id) {
		Funcionario funcionarioAtual;
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		funcionarioAtual = (Funcionario) Funcionario.pesquisaFuncionario(id,consumerKey);
		return funcionarioAtual;
	}
}
