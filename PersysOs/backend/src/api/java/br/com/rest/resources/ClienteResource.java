
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
import br.com.cliente.model.Cliente;
import br.com.clienteobjeto.model.Contrato;
import br.com.exception.ModelException;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.ClienteData;
import br.com.rest.hateoas.ContratoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;
import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.spi.container.ResourceFilters;

@Path("/cliente")
public class ClienteResource {

	@Context 
	HttpServletRequest httpServletRequest;

	@Context 
	ResourceContext resourceContext;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public ClienteData getCliente(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("1000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Cliente.CONSTRUTOR) String fields) {

		ArrayList<Cliente> cliente = null;
		ClienteData clientes = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			cliente = (ArrayList<Cliente>) Cliente.pesquisaListaPorDataAlteracaEConsumerSecret(session, offset, limit, fields,FormatDateHelper.formatTimeZone(since) ,consumerKey, Cliente.class,statusModel);
			clientes = new ClienteData(cliente,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset,Cliente.countPorConsumerSecret(session, Cliente.class, consumerKey,statusModel,since),statusModel));
			tx.commit();
			return clientes;
		}finally{
			cliente = null;
			clientes = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getCliente(@PathParam("id") Long id) {

		Cliente cliente = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			cliente = valida(id, session);
			PreconditionsREST.checkNotNull(cliente, "not found");
			return Response.ok().entity(cliente).build();
		}finally{
			cliente = null;
			session.close();
			session = null;
		}
	}
	
	@Path("codigo/{codigo}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getCliente(@PathParam("codigo") String codigo) {

		Cliente cliente = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			cliente = valida(codigo, session);
			PreconditionsREST.checkNotNull(cliente, "not found");
			return Response.ok().entity(cliente).build();
		}finally{
			cliente = null;
			session.close();
			session = null;
		}
	}

	@Path("/{id}/contrato")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public ContratoData getContratoCliente(@PathParam("id") Long id,
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("100") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel){

		ArrayList<Contrato> contrato = null;
		ContratoData contratos = null;
		Session session = HibernateHelper.openSession(getClass());
		
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Transaction tx = session.beginTransaction();
			try {
				contrato = Contrato.listaContratoCliente(session, offset, limit,consumerKey,id);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			PreconditionsREST.checkNotNull(contrato, "not found");
			tx.commit();
			contratos = new ContratoData(contrato,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset,contrato.size()));
			return contratos;
		}finally{
			contrato = null;
			contratos = null;
			session.close();
			session = null;
		}
	}

	@Path("/codigo/{codigo}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaCliente(Cliente cliente, @PathParam("codigo") String codigo) {

		PreconditionsREST.checkNotNull(cliente, "not found");
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			cliente.setConsumerId(Long.parseLong(consumerKey));
			return atualiza(cliente);
		}finally{
			cliente = null;
		}
	}
	
	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaCliente(Cliente cliente, @PathParam("id") Long id) {

		PreconditionsREST.checkNotNull(cliente, "not found");
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			cliente.setConsumerId(Long.parseLong(consumerKey));
			return atualiza(cliente);
		}finally{
			cliente = null;
		}
	}
	
	private Response atualiza(Cliente clienteAtual) {
		try {
			clienteAtual.altera();
			return Response.ok().entity(clienteAtual).build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarCliente(Cliente cliente) {
		Session session = HibernateHelper.openSession(getClass());
		try {
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			cliente.setConsumerId(Long.parseLong(consumerKey));
			cliente.salvar();
			return Response.ok(URI.create(String.valueOf(200))).entity(cliente).build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTExceptien", e.getMessage())).build());
		}finally{
			cliente = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeCliente(@PathParam("id") long id) {

		Session session = HibernateHelper.openSession(getClass());
		Cliente cliente = null;
		try{
			cliente = valida(id, session);
			PreconditionsREST.checkNotNull(cliente, "not found");
			cliente.remover();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException",e.getMessage())).build());
		}finally{
			cliente = null;
			session.close();
			session = null;
		}
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ClienteData buscaOrganizacaoLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("razao") @DefaultValue("") String razaoSocial,
			@QueryParam("fantasia") @DefaultValue("") String nomeFantasia,
			@QueryParam("cnpjcpf") @DefaultValue("") String cnpj,
			@QueryParam("codigo") @DefaultValue("") String codigo) {
  
		ArrayList<Cliente> clienteLista = null;
		try{

			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			try {
				clienteLista =(ArrayList<Cliente>) Cliente.busca(razaoSocial,nomeFantasia,cnpj,codigo,statusModel,consumerKey);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			PreconditionsREST.checkNotNull(clienteLista, "not found");

			ClienteData clienteData = new ClienteData(clienteLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(),clienteLista.size(), 0, clienteLista.size(), statusModel));
			return clienteData;
		}finally{
			clienteLista = null;
		}
	}
	
	private Cliente valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "id not found");
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Transaction tx = session.beginTransaction();
		Cliente cliente = (Cliente) Cliente.pesquisaPorIdConsumerSecret(session, Cliente.class,id,consumerKey);
		tx.commit();
		return cliente;
	}
	
	private Cliente valida(String codigo, Session session) {
		PreconditionsREST.checkNotNull(codigo, "codigo not found");
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Transaction tx = session.beginTransaction();
		Cliente cliente = (Cliente) Cliente.pesquisaPorCodigoConsumerSecret(session, Cliente.class,codigo,consumerKey);
		tx.commit();
		return cliente;
	}
}
