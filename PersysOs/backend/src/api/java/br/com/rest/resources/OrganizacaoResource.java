package br.com.rest.resources;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.container.ResourceFilters;
import br.com.contato.model.Endereco;
import br.com.empresa.model.Organizacao;
import br.com.empresa.model.Unidade;
import br.com.exception.ModelException;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.OrganizacaoData;
import br.com.rest.hateoas.UnidadeData;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthNonceRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ImageHelper;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/organizacao")
public class OrganizacaoResource {

	@Context HttpServletRequest httpServletRequest;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public OrganizacaoData getLista(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("1000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue(Organizacao.CONSTRUTOR) String fields) {

		OrganizacaoData organizacaoData = null;
		ArrayList<Organizacao> organizacaoLista = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			organizacaoLista = (ArrayList<Organizacao>) Organizacao.pesquisaListaPorDataAlteracao(session, offset, limit,fields,FormatDateHelper.formatTimeZone(since), Organizacao.class);
			tx.commit();
			organizacaoData = new OrganizacaoData(organizacaoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Organizacao.count(session, Organizacao.class)));
			return organizacaoData;
		}finally{
			organizacaoData = null;
			organizacaoLista = null;
			session.close();
			tx = null;
			session = null;
		}	
	}

	@Path("/home")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Organizacao getOrganizacao() {
		Organizacao    org = null;
		Session session = HibernateHelper.openSession(getClass());

		try{
			org = valida(session);
			PreconditionsREST.checkNotNull(org, "not found");
			return org;
		}finally{
			session.close();
			session  = null;
			org = null;
		}
	}

	@Path("/home/unidade")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Collection<Unidade> getHomeUnidade() {
		Collection<Unidade> unidades;
		Session session = HibernateHelper.openSession(getClass());
		try{
			Organizacao org = valida(session);
			PreconditionsREST.checkNotNull(org, "not found");
			unidades = org.getUnidade();
			PreconditionsREST.checkNotNull(unidades, "not found");
			return unidades;	
		}finally{
			session.close();
			session = null;
			unidades = null;
		}
	}

	@Path("/home/endereco")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Endereco getHomeEndereco() {
		Session session = HibernateHelper.openSession(getClass());

		try{
			Organizacao org = valida(session);
			PreconditionsREST.checkNotNull(org, "not found");
			Endereco end = org.getEndereco();
			PreconditionsREST.checkNotNull(end, "not found");
			return end;	
		}finally{
			session.close();
			session = null; 
		}
	}

	@Path("/home/endereco")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionartHomeEndereco(Endereco end) {
		Session session = HibernateHelper.openSession(getClass());

		try{
			Organizacao org = valida(session);
			PreconditionsREST.checkNotNull(org, "not found");
			end.setConsumerSecret(org.getConsumerSecret());
			try {
				end.valida();
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			org.setEndereco(end);
			try {
				org.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.ok().entity(end).build();
		}finally{
			end = null;
			session.close();
			session = null; 
		}
	}

	@Path("/home/endereco")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Endereco alterarHomeEndereco(Endereco end) {
		Session session = HibernateHelper.openSession(getClass());
		try{
			Organizacao org = valida(session);
			Endereco endAtual = alteraEndereco(end, org);
			try {
				endAtual.valida();
				org.setEndereco(endAtual);
				org.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return endAtual;
		}finally{
			end = null;
			session.close();
			session = null; 
		}
	}

	@Path("/home")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alterarHomeOrganizacao(Organizacao organizacao) {
		Session session = HibernateHelper.openSession(getClass());
	
		try{
			try {
				organizacao.valida();
				Organizacao org =valida(session); 
				PreconditionsREST.checkNotNull(org, "not found");
				alteraOrganizacao(organizacao, org);
				org.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.ok().entity(organizacao).build();
		}finally{
			organizacao = null;
			session.close();
		}
	}

	@Path("/home/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removerHomeOrganizacao(@PathParam("id") Long id) {
		Session session = HibernateHelper.openSession(getClass());
		try{
			try {
				Organizacao organizacao = valida(session);
				PreconditionsREST.checkNotNull(organizacao, "not found");
				organizacao.removerLogico();
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.noContent().build();
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Organizacao getOrganizacao(@PathParam("id") Long id) {
		Session session = HibernateHelper.openSession(getClass());
		try{
			Organizacao organizacao = valida(session);
			PreconditionsREST.checkNotNull(organizacao, "not found");
			return organizacao;
		}
		finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}/unidade")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public UnidadeData getUnidade(@PathParam("id") Long id) {
		UnidadeData unidadeData;
		Session session = HibernateHelper.openSession(getClass());
		try{

			Organizacao organizacao =valida(session);
			PreconditionsREST.checkNotNull(organizacao, "not found");
			unidadeData = new UnidadeData(organizacao.getUnidade(),LinkData.criarLinks(organizacao.getUnidade().size()));
			return unidadeData;	
		}finally{
			unidadeData = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}/endereco")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Endereco getEndereco(@PathParam("id") Long id) {
		Session session = HibernateHelper.openSession(getClass());
			try{
			Organizacao organizacao = valida(session);
			PreconditionsREST.checkNotNull(organizacao, "not found");
			Endereco end = organizacao.getEndereco();
			PreconditionsREST.checkNotNull(end, "not found endereco");
			return end;	
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{idorganizacao}/endereco")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarEndereco(@PathParam("idorganizacao") Long id, Endereco end) {
		Session session = HibernateHelper.openSession(getClass());
		try{
			Organizacao organizacao = valida(session);
			PreconditionsREST.checkNotNull(organizacao, "not found");
			try {
				end.setConsumerSecret(organizacao.getConsumerSecret());
				end.valida();
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			organizacao.setEndereco(end);
			try {
				organizacao.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.ok().entity(end).build();
		}finally{
			end = null;
			session.close();
			session = null;
		}
	}

	@Path("/{idorganizacao}/endereco")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Endereco alterarEndereco(@PathParam("idorganizacao") Long id, Endereco end) {
		PreconditionsREST.checkNotNull(end,"not found address");

		Session session = HibernateHelper.openSession(getClass());
		try{
			Organizacao organizacao = valida(session);
			Endereco endAtual = alteraEndereco(end, organizacao);
			try {
				endAtual.valida();
				organizacao.setEndereco(endAtual);
				organizacao.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return endAtual;
		}finally{
			end = null;
			session.close();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarOrganizacao(Organizacao organizacao) {
		try{
			PreconditionsREST.checkNotNull(organizacao,"not found address");

			try {
				organizacao.setEndereco(null);
				organizacao.salvar();
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.ok().entity(organizacao).build();
		}finally{
			organizacao = null;
		}
	}

	@Path("{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alterarOrganizacao(Organizacao organizacao,@PathParam("id") Long id) {
		Session session = HibernateHelper.openSession(getClass());
		PreconditionsREST.checkEmptyString(organizacao.getRazaoSocial(),"Razão Social invalida");
		PreconditionsREST.checkEmptyString(organizacao.getNomeFantasia(),"Nome Fantasia invalido");

		try{
			PreconditionsREST.checkNotNull(organizacao,"not found address");
			Organizacao organizacaoAtual = valida(session);
			alteraOrganizacao(organizacao, organizacaoAtual);
			try {
				organizacaoAtual.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			return Response.ok().entity(organizacao).build();
		}finally{
			organizacao = null;
			session.close();
			session = null; 
		}
	}

	@Path("{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removerOrganizacao(@PathParam("id") Long id) {
		
		Organizacao organizacao = null;
		Session session = HibernateHelper.openSession(getClass());
		try{
			organizacao = valida(session);
			PreconditionsREST.checkNotNull(organizacao, "not found");
			organizacao.removerLogico();
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
		}finally{
			organizacao = null;
			session.close();
			session = null;
		}
		return Response.noContent().build();
	}
	
	@POST
	@Path("/{id}/logo")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response uploadFile(@PathParam("id") Long id,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			Organizacao organizacao = valida(id, session);
			PreconditionsREST.checkNotNull(organizacao, "not found");
			organizacao.setImagem(ImageHelper.writeToByte(uploadedInputStream));
			try {
				organizacao.alterar(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
				e.printStackTrace();
			}
			return Response.status(200).entity(organizacao.getId()).build();
		}finally{
			uploadedInputStream = null;
			session.close();
			session = null;
		}
	}

	@GET
	@Path("{id}/logo")
	@Produces("image/png")
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getImage(@PathParam("id") Long id,
			@QueryParam("x") @DefaultValue("60") Integer x,
			@QueryParam("y") @DefaultValue("60") Integer y) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			Organizacao organizacao = valida(id, session);
			PreconditionsREST.checkNotNull(organizacao, "not found");
			return Response.ok().entity(new ByteArrayInputStream(ImageHelper.imageXY(organizacao.getImagem(), x, y))).build();
		}catch (Exception e) {
			PreconditionsREST.error(e.getMessage());
			return null;
		}    
		finally{
			session.close();
			session = null;
		}
	}

	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public OrganizacaoData buscaOrganizacaoLista(
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel,
			@QueryParam("nomefantasia") @DefaultValue("") String nomeFantasia,
			@QueryParam("cnpj") @DefaultValue("") String cnpj,
			@QueryParam("razaosocial") @DefaultValue("") String razaoSocial,
			@QueryParam("codigo") @DefaultValue("") String codigo) {

		ArrayList<Organizacao> organizacaoLista= null;
		try {
			organizacaoLista = Organizacao.busca(nomeFantasia, cnpj, razaoSocial, codigo, statusModel);
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
		}
		OrganizacaoData organizacaoData = new OrganizacaoData(organizacaoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(),organizacaoLista.size(), 0, organizacaoLista.size()));
		return organizacaoData;
	}

	private Organizacao valida(Long id, Session session) {
		PreconditionsREST.checkNotNull(id, "codigo null");
		Organizacao organizacao = Organizacao.pesquisaOrganizacaoID(id);
		return organizacao;
	}
	
	private Organizacao valida(Session session) {
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		PreconditionsREST.checkNotNull(consumerKey, "codigo null");
		Transaction tx = session.beginTransaction();
		Organizacao organizacao = Organizacao.pesquisaConsumerSecret(session,consumerKey);
		tx.commit();
		return organizacao;
	}

	private Endereco alteraEndereco(Endereco endereco, Organizacao organizacao) {
		Endereco endAtual;
		endAtual = organizacao.getEndereco();
		PreconditionsREST.checkNotNull(endAtual,"not found address");

		endAtual.setBairro(endereco.getBairro());
		endAtual.setCidade(endereco.getCidade()) ;
		endAtual.setLogradouro(endereco.getLogradouro());
		endAtual.setCep(endereco.getCep());
		endAtual.setNumero(endereco.getNumero());
		endAtual.setEstado(endereco.getEstado());
		endAtual.setComplemento(endereco.getComplemento());
		return endAtual;
	}

	private void alteraOrganizacao(Organizacao organizacaoNova, Organizacao organizacaoAtual) {
		organizacaoAtual.setRazaoSocial(organizacaoNova.getRazaoSocial());
		organizacaoAtual.setNomeFantasia(organizacaoNova.getNomeFantasia());
		organizacaoAtual.setCnaeFiscal(organizacaoNova.getCnaeFiscal());
		organizacaoAtual.setEmail(organizacaoNova.getEmail());
		organizacaoAtual.setInscricaoEstadual(organizacaoNova.getInscricaoEstadual());
		organizacaoAtual.setInscricaoEstadualSubstTributario(organizacaoNova.getInscricaoEstadualSubstTributario());
		organizacaoAtual.setInscricaoMunicipal(organizacaoNova.getInscricaoMunicipal());
		organizacaoAtual.setRegimeTributario(organizacaoNova.getRegimeTributario());
		organizacaoAtual.setTelefone(organizacaoNova.getTelefone());
	}
}
