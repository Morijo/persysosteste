package br.com.rest.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import br.com.cliente.model.Cliente;
import br.com.clienteobjeto.model.ClienteObjeto;
import br.com.clienteobjeto.model.ClienteObjetoProduto;
import br.com.clienteobjeto.model.Contrato;
import br.com.contato.model.Contato;
import br.com.empresa.model.Unidade;
import br.com.exception.ModelException;
import br.com.model.StatusModel;
import br.com.oauth.model.AccessToken;
import br.com.ordem.model.Ordem;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;
import br.com.rest.hateoas.ClienteObjetoProdutoData;
import br.com.rest.hateoas.ContratoData;
import br.com.rest.hateoas.OrdemServicoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;
import br.com.usuario.model.Usuario;

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.spi.container.ResourceFilters;

/**
 * @author ricardosabatine
 *
 */
@Path("/contrato")
public class ClienteObjetoResource {


	@Context 
	HttpServletRequest httpServletRequest;

	@Context 
	ResourceContext resourceContext;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public ContratoData getClienteObjeto(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("10000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel) {

		ArrayList<Contrato> contrato = null;
		ContratoData contratos = null;
		Session session = HibernateHelper.currentSession();
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			try {
				contrato = Contrato.listaContrato(session, offset, limit, consumerKey, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			contratos = new ContratoData(contrato,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset,ClienteObjeto.countPorConsumerSecret(session,ClienteObjeto.class, consumerKey,statusModel,since),statusModel));
			tx.commit();
			return contratos;
		}finally{
			contrato = null;
			contratos = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	@Path("{idClienteObjeto}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getClienteObjeto(@PathParam("idClienteObjeto") Long id,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel) {

		ClienteObjeto clienteOrdem = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			clienteOrdem = (ClienteObjeto) ClienteObjeto.pesquisaPorIdConsumerSecret(session,ClienteObjeto.class,id,consumerKey,statusModel);
			tx.commit();
			PreconditionsREST.checkNotNull(clienteOrdem, "not found");
			return Response.ok().entity(clienteOrdem).build();
		}finally{
			clienteOrdem = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	@Path("/contrato/{idClienteObjeto}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getClienteObjetoContrato(@PathParam("idClienteObjeto") Long id) {

		Contrato clienteOrdem = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{

			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			clienteOrdem = (Contrato) Contrato.pesquisaPorIdConsumerSecret(session,Contrato.class,id,consumerKey);
			tx.commit();
			PreconditionsREST.checkNotNull(clienteOrdem, "not found");
			return Response.ok().entity(clienteOrdem).build();
		}finally{
			clienteOrdem = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	/**
	 * @author ricardosabatine
	 * @param contrato
	 * @return response
	 * 
	 * Recebe um contrato na requisi��o, verifica o consumer key, o cliente, o contato,
	 * e a unidade para salvar.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionaClienteObjeto(Contrato contrato) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Cliente cliente = null;
		Contato contato = null;
		Unidade unidade = null;

		try{
			//verifica o consumer key
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			//verifica se o cliente n�o � null na requisi��o
			PreconditionsREST.checkNotNull(contrato.getCliente(), "not found");

			//busca o cliente no banco
			cliente = (Cliente) Cliente.pesquisaPorIdConsumerSecret(session,Cliente.class,contrato.getCliente().getId(),consumerKey);

			//verifica se o cliente n�o � null no banco de dados
			PreconditionsREST.checkNotNull(cliente, "not found");

			if(contrato.getUnidadeGestora() != null){
				unidade = (Unidade) Unidade.pesquisaPorIdConsumerSecret(session,Unidade.class,contrato.getUnidadeGestora().getId(),consumerKey);
			}else{
				try {
					unidade = Unidade.getUnidadePadrao(session, consumerKey);
				} catch (ModelException e1) {
					PreconditionsREST.checkNotNull(unidade, "Unidade sem registro no sistema");
				}
			}

			if(contrato instanceof Contrato){
				PreconditionsREST.checkNotNull(((Contrato)contrato).getContato().getId(), "Contato sem registro no sistema");
				contato = (Contato) Contato.pesquisaPorIdConsumerSecret(session,Contato.class,((Contrato)contrato).getContato().getId(),consumerKey);

				if(contato == null){
					contato = ((Contrato)contrato).getContato();
					contato.setUsuario(cliente);
					contato.setConsumerSecret(cliente.getConsumerSecret());
					try {
						contato.salvar();
						((Contrato)contrato).setContato(contato);
					} catch (ModelException e) {
						e.printStackTrace();
					}
				}
			}

			tx.commit();

			try {
				contrato.getContato().setConsumerSecret(cliente.getConsumerSecret());
				contrato.setConsumerSecret(cliente.getConsumerSecret());
				contrato.salvar();
			} catch (ModelException e) {
				throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
			}
			return Response.ok().entity(contrato).build();
		}finally{
			contrato = null;
		}
	}

	@Path("/codigo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adiciona(Contrato contrato) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Cliente cliente = null;
		Contato contato = null;
		Unidade unidade = null;

		try{
			//verifica o consumer key
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			//verifica se o cliente n�o � null na requisi��o
			PreconditionsREST.checkNotNull(contrato.getCliente(), "not found");
			PreconditionsREST.checkNotNull(contrato.getCliente().getCodigo(), "not found");

			//busca o cliente no banco
			cliente = (Cliente) Cliente.pesquisaPorCodigoConsumerSecret(session,Cliente.class,contrato.getCliente().getCodigo(),consumerKey);
			//verifica se o cliente n�o � null no banco de dados
			PreconditionsREST.checkNotNull(cliente, "not found");

			PreconditionsREST.checkNotNull(contrato.getUnidadeGestora(), "not found");
			unidade = (Unidade) Unidade.pesquisaPorIdConsumerSecret(session,Unidade.class,contrato.getUnidadeGestora().getId(),consumerKey);
			PreconditionsREST.checkNotNull(unidade, "Unidade sem registro no sistema");

			if(contrato instanceof Contrato){
				PreconditionsREST.checkNotNull(((Contrato)contrato).getContato(), "Contato sem registro no sistema");
				contato = (Contato) Contato.pesquisaPorIdConsumerSecret(session,Contato.class,((Contrato)contrato).getContato().getId(),consumerKey);
				PreconditionsREST.checkNotNull(contato, "Contato sem registro");
			}

			tx.commit();

			try {
				contrato.setConsumerId(Long.parseLong(consumerKey));
				contrato.salvar();
			} catch (ModelException e) {
				throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
			}
			return Response.ok().entity(contrato).build();
		}finally{
			contrato = null;
		}
	}

	@Path("{idClienteObjeto}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alteraClienteObjeto(@PathParam("idClienteObjeto") Long id, Contrato clienteOrdem ) {

		Contrato clienteOrdemAtual = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			clienteOrdemAtual = (Contrato) Contrato.pesquisaPorIdConsumerSecret(Contrato.class,id,consumerKey);
			tx.commit();
			PreconditionsREST.checkNotNull(clienteOrdemAtual, "not found");
			clienteOrdemAtual.setDescricaoObjeto(clienteOrdem.getDescricaoObjeto());
			clienteOrdemAtual.setValorMensal(clienteOrdem.getValorMensal());
			clienteOrdemAtual.setSituacao(clienteOrdem.getSituacao());
			clienteOrdemAtual.setFidelidade(clienteOrdem.isFidelidade());
			clienteOrdemAtual.setProrrogavel(clienteOrdem.isProrrogavel());
			clienteOrdemAtual.setUnidadeGestora(clienteOrdem.getUnidadeGestora());
			clienteOrdemAtual.setContato(clienteOrdem.getContato());
			clienteOrdemAtual.setDataAssinatura(clienteOrdem.getDataAssinatura());
			clienteOrdemAtual.setDataVigenciaInicio(clienteOrdem.getDataVigenciaInicio());
			clienteOrdemAtual.setDataVigenciaFim(clienteOrdem.getDataVigenciaFim());
			clienteOrdemAtual.setStatusModel(clienteOrdem.getStatusModel());
			clienteOrdemAtual.alterar();

			return Response.ok().entity(clienteOrdemAtual).build();
		}catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			clienteOrdem = null;
			clienteOrdemAtual = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	public Response alterarContrato(Contrato contratoAtual, Contrato contrato){
		try {
			contratoAtual.setDataAssinatura(contrato.getDataAssinatura());
			contratoAtual.setDataVigenciaFim(contrato.getDataVigenciaFim());
			contratoAtual.setDataVigenciaInicio(contrato.getDataVigenciaInicio());
			contratoAtual.setFidelidade(contrato.isFidelidade());
			contratoAtual.setProrrogavel(contrato.isProrrogavel());
			contratoAtual.setContato(contrato.getContato());
			contratoAtual.alterar();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		return Response.ok().entity(contratoAtual).build();
	}

	@Path("{idClienteObjeto}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeClienteObjeto(@PathParam("idClienteObjeto") Long id) {

		ClienteObjeto clienteOrdem = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			clienteOrdem = (ClienteObjeto) Cliente.pesquisaPorIdConsumerSecret(ClienteObjeto.class,id,consumerKey);
			tx.commit();
			PreconditionsREST.checkNotNull(clienteOrdem, "not found");
			try {
				clienteOrdem.setSituacao("removido");
				clienteOrdem.remover();
			} catch (ModelException e) {
				throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
			}
			return Response.ok().entity(clienteOrdem).build();
		}finally{
			clienteOrdem = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	@Path("{idClienteObjeto}/ordem")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getClienteObjetoOrdem(@PathParam("idClienteObjeto") Long id) {

		List<Ordem> clienteObjetoOrdem = null;
		OrdemServicoData ordem = null;

		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			try {
				clienteObjetoOrdem = ClienteObjeto.listaContratoOrdem(id, consumerKey);
			} catch (ModelException e) {
				throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
			}
			ordem = new OrdemServicoData(clienteObjetoOrdem,LinkData.createLinks(httpServletRequest.getRequestURL().toString(),clienteObjetoOrdem.size(),0,clienteObjetoOrdem.size(),StatusModel.ATIVO));
			return Response.ok().entity(ordem).build();
		}finally{
			clienteObjetoOrdem = null;
			ordem = null;
		}
	}

	@Path("{idClienteObjeto}/produto")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getClienteObjetoItens(@PathParam("idClienteObjeto") Long id) {

		List<ClienteObjetoProduto> clienteObjetoProduto = null;
		ClienteObjetoProdutoData contratos = null;
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			try {
				clienteObjetoProduto = ClienteObjetoProduto.listaContratoProduto(id, consumerKey);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			contratos = new ClienteObjetoProdutoData(clienteObjetoProduto,LinkData.createLinks(httpServletRequest.getRequestURL().toString(),clienteObjetoProduto.size(),0,clienteObjetoProduto.size(),StatusModel.ATIVO));
			return Response.ok().entity(contratos).build();
		}finally{
			clienteObjetoProduto = null;
			contratos = null;
		}
	}

	@Path("{idClienteObjeto}/produto")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarClienteObjetoProduto(@PathParam("idClienteObjeto") Long id, ClienteObjetoProduto clienteObjetoProduto) {

		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			PreconditionsREST.checkBoolean(ClienteObjeto.existIdAtivoInativoPorConsumerSecret(session, ClienteObjeto.class, id, consumerKey),
					"Contrato sem registro no sistema");

			PreconditionsREST.checkNotNull(clienteObjetoProduto.getProduto(), "Produto sem registro no sistema");
			PreconditionsREST.checkBoolean(Produto.existIdAtivoInativoPorConsumerSecret(session, Produto.class,clienteObjetoProduto.getProduto().getId(),consumerKey), "Produto sem registro no sistema");

			clienteObjetoProduto.setUsuario((Usuario) AccessToken.retrieveUsuario(FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization"))));
			tx.commit();

			try {
				clienteObjetoProduto.setClienteOrdem(new ClienteObjeto(id,null, null));
				clienteObjetoProduto.setConsumerId(Long.parseLong(consumerKey));
				clienteObjetoProduto.salvar();
				return Response.ok().entity(clienteObjetoProduto).build();
			} catch (ModelException e) {
				throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
			}
		}finally{
			clienteObjetoProduto = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	@Path("produto/{idClienteObjetoProduto}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alterarClienteOrdemProduto(@PathParam("idClienteObjetoProduto") Long idClienteObjetoProduto, ClienteObjetoProduto clienteObjetoProduto) {

		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try {
			clienteObjetoProduto.setId(idClienteObjetoProduto);
			clienteObjetoProduto.alteraSituacao(consumerKey);
			return Response.ok().entity(clienteObjetoProduto).build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}

	}

	@Path("/produto/{idClienteObjetoProduto}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getClienteObjetoProduto(@PathParam("idClienteObjetoProduto") Long idClienteObjetoProduto) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		ClienteObjetoProduto clienteObjetoProdutoAtual = null;

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			clienteObjetoProdutoAtual = (ClienteObjetoProduto) ClienteObjetoProduto.pesquisaPorIdConsumerSecret(session, ClienteObjetoProduto.class,idClienteObjetoProduto,consumerKey);
			tx.commit();
			PreconditionsREST.checkNotNull(clienteObjetoProdutoAtual, "Item do contrato sem registro no sistema");
			return Response.ok().entity(clienteObjetoProdutoAtual).build();

		}finally{
			clienteObjetoProdutoAtual = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	@Path("/produto/{idClienteObjetoProduto}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removerClienteObjetoProduto(@PathParam("idClienteObjetoProduto") Long idClienteObjetoProduto) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		ClienteObjetoProduto clienteObjetoProdutoAtual = null;

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			clienteObjetoProdutoAtual = (ClienteObjetoProduto) ClienteObjetoProduto.pesquisaPorIdConsumerSecret(session, ClienteObjetoProduto.class,idClienteObjetoProduto,consumerKey);
			tx.commit();
			PreconditionsREST.checkNotNull(clienteObjetoProdutoAtual, "Item do contrato sem registro no sistema");

			try {
				clienteObjetoProdutoAtual.remover();
				return Response.ok().entity(clienteObjetoProdutoAtual).build();
			} catch (ModelException e) {
				throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
			}
		}finally{
			clienteObjetoProdutoAtual = null;
			session.close();
			session = null;
			tx = null;
		}
	}
}
