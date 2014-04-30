package br.com.rest.resources;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.spi.container.ResourceFilters;
import br.com.contato.model.Contato;
import br.com.contato.model.Endereco;
import br.com.exception.ModelException;
import br.com.model.StatusModel;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.ContatoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.usuario.model.Usuario;

@Path("/contato")
public class ContatoResource {

	@Context 
	HttpServletRequest httpServletRequest;

	@Context 
	ResourceContext resourceContext;

	private Contato validaContrato(Long id, Session session) {
		Contato contatoAtual;
		String cs = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		contatoAtual = (Contato) Contato.pesquisaPorIdConsumerSecret(session,Contato.class,id,cs);
		PreconditionsREST.checkNotNull(contatoAtual, "Contato não encontrado");
		return contatoAtual;
	}

	protected ContatoData getContato(Usuario usuario) {

		ArrayList<Contato> contato = null;
		ContatoData contatos = null;

		try{
			contato = new ArrayList<Contato>(usuario.getContato());
			PreconditionsREST.checkNotNull(contato, "not found");
			contatos = new ContatoData(contato,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), contato.size(), 0,contato.size(),StatusModel.ATIVO));
			return contatos;
		}finally{
			contato = null;
			contatos = null;
		}
	}

	@Path("{idContato}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public Response getContato(@PathParam("idContato") Long id) {
		String cs = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Contato contato;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			contato = (Contato) Contato.pesquisaPorIdConsumerSecret(session,Contato.class, id, cs);
			PreconditionsREST.checkNotNull(contato, "not found");
			tx.commit();
			return Response.ok().entity(contato).build();
		}finally{
			contato = null;
			cs = null;
			session.close();
			tx = null;
		}
	}

	@Path("{idContato}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaContato(@PathParam("idContato") Long id, Contato contato) {

		PreconditionsREST.checkNotNull(contato, "Contato inválid");
		PreconditionsREST.checkNotNull(contato.getId(), "Contato inválid");
		try {
			if(contato.getEndereco() != null)
				if(contato.getEndereco().getCep() != null && !contato.getEndereco().getCep().isEmpty())
					atualizaEndereco(id, contato.getEndereco());
			contato.altera();
			return Response.ok().entity(contato).build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	@Path("{idContato}/endereco")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaContatoEndereco(@PathParam("idContato") Long id, Endereco endereco) {
		try{
			Endereco enderecoAtual = atualizaEndereco(id, endereco);
			return Response.ok().entity(enderecoAtual).build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	private Endereco atualizaEndereco(Long id, Endereco endereco) throws ModelException {
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Contato contatoAtual = null;
		try {
			contatoAtual = Contato.pesquisa(id, consumerKey);
		} catch (ModelException e1) {
			PreconditionsREST.checkNull(contatoAtual, "Contato not found");
		}
		Endereco enderecoAtual = contatoAtual.getEndereco();
		if(enderecoAtual.getId() == null){
			endereco.setConsumerId(Long.parseLong(consumerKey));
			endereco.salvar();
			contatoAtual.setEndereco(endereco);
			contatoAtual.alteraEndereco();
			return endereco;
		}
		else{
			endereco.setId(enderecoAtual.getId());
			endereco.altera();
			return endereco;
		}
	}


	@Path("{idContato}/endereco")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarContatoEndereco(@PathParam("idContato") Long id, Endereco endereco) {
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Contato  contatoAtual  = null;

		try{	
			try {
				contatoAtual = Contato.pesquisa(id, consumerKey);
			} catch (ModelException e) {
				PreconditionsREST.error("not found");
			}
			PreconditionsREST.checkNull(contatoAtual.getEndereco().getId(), "Endereço já existe");
			return adicionaEndereco(endereco, consumerKey, contatoAtual);
		}finally{
			contatoAtual = null;
			endereco = null;
			endereco = null;
			consumerKey = null;
		}
	}

	private Response adicionaEndereco(Endereco endereco, String consumerKey,
			Contato contatoAtual) {
		try {
			endereco.setConsumerId(Long.parseLong(consumerKey));
			endereco.valida();
			contatoAtual.setConsumerId(Long.parseLong(consumerKey));
			contatoAtual.setEndereco(endereco);
			contatoAtual.alterar();
			return Response.ok().entity(endereco).build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}

	@Path("{idContato}/endereco")
	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removerContatoEndereco(@PathParam("idContato") Long id) {

		Contato  contatoAtual  = null;
		Endereco endereco = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			contatoAtual = validaContrato(id, session);
			tx.commit();
			endereco = contatoAtual.getEndereco();
			PreconditionsREST.checkNotNull(endereco, "not found");

			try {
				endereco.removerFisico();
				contatoAtual.setEndereco(null);
				contatoAtual.alterar();
				return Response.noContent().build();
			} catch (ModelException e) {
				throw new WebApplicationException(Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
			}
		}finally{
			contatoAtual = null;
			endereco = null;
			session.close();
			tx = null;
			session = null;
		}
	}

	@Path("{idUsuario}")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionar(@PathParam("idUsuario") Long idUsuario, Contato contato) {
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Usuario usuario = (Usuario) Usuario.pesquisaPorIdConsumerSecret(Usuario.class, idUsuario,consumerKey);
			tx.commit();
			PreconditionsREST.checkNotNull(usuario, "not found");
			contato.setConsumerSecret(usuario.getConsumerSecret());
			return adicionarContato(usuario, contato, consumerKey);
		}finally{
			session.close();
			tx = null;
			session = null;
		}
	}

	protected Response adicionarContato(Usuario usuario,Contato contato, String consumerKey) {
		contato.setUsuario(usuario);
		try {
			contato.setStatusModel(StatusModel.ATIVO);
			if(contato.getEndereco().getCep() != null && !contato.getEndereco().getCep().isEmpty()){
				contato.getEndereco().setConsumerId(Long.parseLong(consumerKey));
				contato.getEndereco().salvar();
			}else
				contato.setEndereco(null);

			contato.setConsumerId(Long.parseLong(consumerKey));
			contato.salvar();

			return Response.ok().entity(contato).build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
	}	

	@Path("{idContato}")
	@DELETE
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeContato(@PathParam("idContato") long id) {
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Contato  contatoAtual = Contato.pesquisa(id, consumerKey);
			contatoAtual.removerFisico();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException",e.getMessage())).build());
		}
	}
}
