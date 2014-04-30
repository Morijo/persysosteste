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

import com.sun.jersey.spi.container.ResourceFilters;

import br.com.exception.ModelException;
import br.com.model.StatusModel;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;
import br.com.rest.hateoas.ProdutoData;
import br.com.rest.hateoas.ResponseBatchData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.represention.ResponseBatch;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;

/**
 * Recurso produto
 * @author ricardosabatine	
 * @version 1.0.0
 * @see Produto
 */
@Path("/produto")
public class ProdutoResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public ProdutoData getProduto(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("10000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Produto.CONSTRUTOR) String fields) {

		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		ArrayList<Produto> produtos = null;
		ProdutoData produtoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			produtos = (ArrayList<Produto>) Produto.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,fields,FormatDateHelper.formatTimeZone(since),consumerKey, Produto.class,statusModel);
			tx.commit();
			produtoData = new ProdutoData(produtos, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Produto.countPorConsumerSecret(session,Produto.class,consumerKey,statusModel,since),statusModel));
			return produtoData;
		}finally{
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
			produtos = null;
			produtoData = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getProduto(@PathParam("id") Long id) {

		Produto produto = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			produto = valida(id, session);
			PreconditionsREST.checkNotNull(produto, "not found");
			return Response.ok(produto).build();
		}finally{
			produto = null;
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getProduto(@PathParam("codigo") String codigo) {

		Produto produto = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			produto = valida(codigo, session);
			PreconditionsREST.checkNotNull(produto, "not found");
			return Response.ok(produto).build();
		}finally{
			produto = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualiza(Produto produto, @PathParam("id") Long id) {

		Produto produtoAtual = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			produtoAtual = valida(id, session);
			PreconditionsREST.checkNotNull(produtoAtual, "not found");
			return atualiza(session, produto, produtoAtual);
		}finally{
			produto = null;
			produtoAtual = null;
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualiza(Produto produto, @PathParam("codigo") String codigo) {

		Produto produtoAtual = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			produtoAtual = valida(codigo, session);
			PreconditionsREST.checkNotNull(produtoAtual, "not found");
			return atualiza(session, produto, produtoAtual);
		}finally{
			produto = null;
			produtoAtual = null;
			session.close();
			session = null;
		}
	}

	private Response atualiza(Session session, Produto produto, Produto produtoAtual) {
		PreconditionsREST.checkNotNull(produtoAtual, "not found");
		produtoAtual.setNome(produto.getNome());
		produtoAtual.setCodigoBarra(produto.getCodigoBarra());
		produtoAtual.setMarca(produto.getMarca());
		produtoAtual.setCodigo(produto.getCodigo());
		produtoAtual.setStatusModel(produto.getStatusModel());
		produtoAtual.setDescricao(produto.getDescricao());
		produtoAtual.setValor(produto.getValor());
		try {
			produtoAtual.alterar(session);
			return Response.ok().entity(produtoAtual).build();

		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarProduto(Produto produto) {

		PreconditionsREST.checkNotNull(produto,"not enough items");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try{
			produto.setConsumerSecret(new ConsumerSecret("",Long.parseLong(consumerKey)));
			produto.salvar();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		return Response.ok(URI.create(String.valueOf(200))).entity(produto).build();
	}

	@Path("/batch")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarLote(ProdutoData produtoBatch) {

		PreconditionsREST.checkNotNull(produtoBatch, "not enough items");
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		List<ResponseBatch> responseBatchs = new ArrayList<ResponseBatch>();

		Session session = HibernateHelper.openSession(getClass());
		try{
			ArrayList<Produto> produtos = (ArrayList<Produto>) produtoBatch.getDados();

			int i = 0;
			for(Produto produto : produtos){
				produto.setConsumerSecret(new ConsumerSecret("",Long.parseLong(consumerKey)));
				try{
					try{
						produto.salvarAlterar(session);
						responseBatchs.add(new ResponseBatch(produto.getCodigo(), produto.getStatusModel(), produto.getId(),0));
					}catch(ModelException e){
						if(e.getCode() == 1){
							produto = (Produto) Produto.pesquisaPorCodigoConsumerSecret(Produto.class,produto.getCodigo(),consumerKey);
							if(produto != null)
								responseBatchs.add(new ResponseBatch(produto.getCodigo(), produto.getStatusModel(), produto.getId(),0));
						}else
							responseBatchs.add(new ResponseBatch(produto.getCodigo(), StatusModel.REJEITADO, -1l, 1));

					}	
					if( (i%20) == 0){
						session.flush();
						session.clear();
					}
				}catch(Exception e){
					session.close();
					session = null;
					session = HibernateHelper.openSession(getClass());
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
	public Response removeProduto(@PathParam("id") long id) {

		Produto produto = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			produto = valida(id, session);
			PreconditionsREST.checkNotNull(produto, "not found");
			produto.remover();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}catch (NullPointerException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "not found")).build());
		}finally{
			session.close();
			session = null;
		}
	}

	private Produto valida(long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id null");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Transaction tx = session.beginTransaction();
		Produto produto = (Produto) Produto.pesquisaPorIdConsumerSecret(session,Produto.class,id,consumerKey);
		tx.commit();
		return produto;
	}

	private Produto valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo null");
		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Transaction tx = session.beginTransaction();
		Produto produto = (Produto) Produto.pesquisaPorCodigoConsumerSecret(session,Produto.class,codigo,consumerKey);
		tx.commit();
		return produto;
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public ProdutoData busca(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("nome") @DefaultValue("") String nome,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<Produto> produtoLista = null;
		ProdutoData produtoData = null;

		String  consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try{		
			try {
				produtoLista = Produto.busca(consumerKey, nome, codigo, statusModel);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			PreconditionsREST.checkNotNull(produtoLista,  "not found");
			produtoData = new ProdutoData(produtoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), produtoLista.size(),0, produtoLista.size(),statusModel));
			return produtoData;
		}finally{
			produtoData = null;
			produtoLista = null;
			consumerKey = null;
		}
	}
}
