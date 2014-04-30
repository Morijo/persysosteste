package br.com.rest.resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
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
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.container.ResourceFilters;

import br.com.clienteobjeto.model.ClienteObjeto;
import br.com.contato.model.Contato;
import br.com.exception.ModelException;
import br.com.funcionario.model.Funcionario;
import br.com.model.StatusModel;
import br.com.oauth.model.ConsumerSecret;
import br.com.ordem.model.AgendaOrdemFuncionario;
import br.com.ordem.model.Anexo;
import br.com.ordem.model.Nota;
import br.com.ordem.model.Ordem;
import br.com.ordem.model.OrdemProcedimento;
import br.com.ordem.model.RecursoOrdem;
import br.com.ordem.model.ServicoOrdem;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.AgendaOrdemFuncionarioData;
import br.com.rest.hateoas.AnexoData;
import br.com.rest.hateoas.NotaData;
import br.com.rest.hateoas.OrdemProcedimentoData;
import br.com.rest.hateoas.OrdemServicoDataDTO;
import br.com.rest.hateoas.RecursoOrdemData;
import br.com.rest.hateoas.ResponseBatchData;
import br.com.rest.hateoas.ServicoOrdemData;
import br.com.rest.hateoas.dto.OrdemDTO;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.represention.ResponseBatch;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthTokenValidFilter;
import br.com.rest.resources.helper.ParameterRequestRest;
import br.com.servico.model.Servico;
import br.com.usuario.model.Usuario;

/**
 * Recurso Ordem de Serviço
 * 
 *Path /ordem
 *
 *@version 1.0
 *
 *@since 1.0
 *
 *@author ricardosabatine
 *
 */
@Path("/ordem")
public class OrdemServicoResource {

	@Context HttpServletRequest httpServletRequest;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthTokenValidFilter.class})
	public OrdemServicoDataDTO getOrdem(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("1000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue("") String fields,
			@QueryParam("agendada") Boolean agendada) {


		ArrayList<OrdemDTO> ordens = null;
		OrdemServicoDataDTO ordemServicoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try{
			ordens = (ArrayList<OrdemDTO>) Ordem.listaOrdem(session,offset, limit, consumerKey, agendada);
			tx.commit();
			ordemServicoData = new OrdemServicoDataDTO(ordens, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, Ordem.countPorConsumerSecret(session, Ordem.class,consumerKey,statusModel,since)));
			return ordemServicoData;
		}finally{
			tx = null;
			session = null;
			consumerKey = null;
			ordemServicoData = null;
			ordens = null;
		}
	}


	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getOrdemServico(@PathParam("id") Long id) {

		OrdemDTO ordemServico = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		try{
			ordemServico = (OrdemDTO) Ordem.getOrdem(session, id, consumerKey);
			tx.commit();
			PreconditionsREST.checkNotNull(ordemServico, "Ordem Servico not found");
			return Response.ok(ordemServico).build();
		}finally{
			ordemServico = null;
			tx = null;
			session.close();
			session = null;
			consumerKey = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaOrdemServico(Ordem ordem, @PathParam("id") long id) {
		Session session = HibernateHelper.openSession(getClass());

		PreconditionsREST.checkNotNull(ordem, "not found order");

		try{
			Transaction tx = session.beginTransaction();
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Ordem ordemServico = (Ordem) Ordem.pesquisaReturnID(session,Ordem.class,id,consumerKey);
			PreconditionsREST.checkNotNull(ordemServico, "not found order");
			Usuario usuario = validaUsuario(session);
			tx.commit();

			ordemServico.setSituacaoOrdem(ordem.getSituacaoOrdem());
			ordemServico.setStatusModel(ordem.getStatusModel());
			ordemServico.setDataAgendamento(ordem.getDataAgendamento());
			ordemServico.setAssunto(ordem.getAssunto());
			ordemServico.setFonte(ordem.getFonte());
			ordemServico.setContato(ordem.getContato());
			ordemServico.setClienteObjeto(ordem.getClienteObjeto());
			ordemServico.setDepartamento(ordem.getDepartamento());
			ordemServico.setDataConclusao(ordem.getDataConclusao());
			ordemServico.setPrioridade(ordem.getPrioridade());

			if(ordemServico.alteraOrdemCustomizado(usuario, consumerKey))
				return Response.ok(URI.create(String.valueOf(200))).entity(new Ordem(ordemServico.getId())).build();
			else
				return Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Falha ao editar")).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}/situacao")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaOrdemServicoSituacao(Ordem ordem, @PathParam("id") long id) {
		Ordem ordemServico = null;
		Session session = HibernateHelper.openSession(getClass());

		try{
			Transaction tx = session.beginTransaction();
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			ordemServico = (Ordem) Ordem.pesquisaReturnID(session,Ordem.class,id,consumerKey);
			Usuario usuario = validaUsuario(session);
			tx.commit();

			ordemServico.setSituacaoOrdem(ordem.getSituacaoOrdem());
			ordemServico.setDataConclusao(ordem.getDataConclusao());
			if(ordemServico.alteraOrdemSituacao(usuario,consumerKey))
				return Response.ok(URI.create(String.valueOf(200))).entity(new Ordem(ordemServico.getId())).build();
			else
				return Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Falha ao editar")).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removeOrdemServico(@PathParam("id") long id) {

		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		try {
			Ordem ordemServico = (Ordem) Ordem.pesquisaPorIdConsumerSecret(Ordem.class,id,consumerKey);
			PreconditionsREST.checkNotNull(ordemServico, "not found");
			ordemServico.remover();
		} catch (ModelException e) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
		}
		return javax.ws.rs.core.Response.noContent().build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionarOrdem(Ordem ordem) {

		PreconditionsREST.checkNotNull(ordem, "parameter invalid");

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Usuario usuario = validaUsuario(session);
			tx.commit();
			ordem.setConsumerId(Long.parseLong(consumerKey));
			ordem.valida();
			ordem.criarOrdem(usuario, consumerKey);
			return Response.ok(URI.create(String.valueOf(200))).entity(ordem).build();

		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}/nota")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getNotas(@PathParam("id") Long id) {
		PreconditionsREST.checkNotNull(id, "not found");
		NotaData notas = new NotaData(Nota.listaNota(id), new LinkData());
		return Response.ok(notas).build();
	}

	@Path("{id}/nota")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adcionaNota(@PathParam("id") Long id, Nota nota) {

		PreconditionsREST.checkNotNull(nota, "not found");

		Session session = HibernateHelper.openSession(getClass());

		try{
			Usuario usuario = validaUsuario(session);
			Ordem ordemServico = valida(id, session);
			PreconditionsREST.checkNotNull(ordemServico, "not found");
			String consumerKey = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			nota = Nota.criarNota(nota, session, usuario, ordemServico, consumerKey);
			return Response.ok(nota).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());

		}finally{
			session.close();
			session = null;
			nota = null;
		}
	}

	@Path("{id}/servico")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionaServico(@PathParam("id") Long id, Servico servico) {

		PreconditionsREST.checkNotNull(servico, "not found");
		Session session = HibernateHelper.openSession(getClass());
		try{
			Transaction tx = session.beginTransaction();
			Usuario usuario = validaUsuario(session);
			Ordem ordemServico = valida(id, session);
			PreconditionsREST.checkNotNull(ordemServico, "not found");
			tx.commit();

			ServicoOrdem servicoOrdem = ordemServico.adicionarServico(servico, usuario);
			return Response.ok(servicoOrdem).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}/servico")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getServicoOrdem(@PathParam("id") Long id,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel) {

		ServicoOrdemData dados = null;
		try{
			PreconditionsREST.checkNotNull(id, "not found");
			dados = new ServicoOrdemData(ServicoOrdem.listaServicoOrdem(id,statusModel), new LinkData());

			return Response.ok(dados).build();
		}finally{
			dados = null;
		}
	}

	@Path("/{idOrdem}/servico/{idServicoOrdem}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionaServico(@PathParam("idOrdem") Long idOrdem, 
			@PathParam("idServicoOrdem") Long id, ServicoOrdem servicoOrdem) {

		PreconditionsREST.checkNotNull(servicoOrdem, "parameter invalid");

		Session session = HibernateHelper.openSession(getClass());
		try{
			Transaction tx = session.beginTransaction();
			Usuario usuario = validaUsuario(session);
			Ordem ordemServico = valida(id, session);
			PreconditionsREST.checkNotNull(ordemServico, "not found");
			tx.commit();

			Ordem ordem = new Ordem(idOrdem); 
			ordem.alteraServico(servicoOrdem, usuario);
			return Response.ok(servicoOrdem).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());

		}finally{
			servicoOrdem = null;
			session.close();
			session = null;
		}
	}

	@Path("/{idOrdem}/servico/{idServicoOrdem}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removerServico(@PathParam("idOrdem") Long idOrdem, @PathParam("idServicoOrdem") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			Transaction tx = session.beginTransaction();
			Usuario usuario = validaUsuario(session);
			Ordem ordemServico = valida(id, session);
			PreconditionsREST.checkNotNull(ordemServico, "not found");
			tx.commit();

			ServicoOrdem servicoOrdem = new ServicoOrdem();
			servicoOrdem.setId(id);
			Ordem ordem = new Ordem(idOrdem); 
			ordem.removeServico(servicoOrdem, usuario);
			return Response.noContent().build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}/procedimento")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getOrdemServicoProcedimento(@PathParam("id") Long id) {

		ArrayList<OrdemProcedimento> dados = null;
		try{
			dados =  OrdemProcedimento.listaOrdemProcedimento(id);
			PreconditionsREST.checkNotNull(dados, "OrdemServico não encontrado");
			return Response.ok(new OrdemProcedimentoData(dados, null)).build();
		}finally{
			dados = null;
		}
	}

	@Path("{id}/recurso")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionaRecursoUtilizado(@PathParam("id") Long id, RecursoOrdem recursoOrdem) {

		Session session = HibernateHelper.openSession(getClass());
		try{
			Transaction tx = session.beginTransaction();
			Usuario usuario = validaUsuario(session);
			Ordem ordemServico = valida(id, session);
			PreconditionsREST.checkNotNull(ordemServico, "not found");
			tx.commit();

			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			recursoOrdem.setConsumerId(Long.parseLong(consumerKey));
			ordemServico.adicionarRecurso(recursoOrdem, usuario, consumerKey);

			return Response.ok(recursoOrdem).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());

		}finally{
			session.close();
			session = null;
		}
	}

	@Path("/recurso/batch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response adicionaRecursoUtilizadoBatch(RecursoOrdemData recursoOrdems) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String consumerKey = null;
		Usuario usuario = null;

		ArrayList<ResponseBatch> batchResponses = new ArrayList<ResponseBatch>();

		try{
			consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			usuario =validaUsuario(session);
			tx.commit();

			for(RecursoOrdem recursoOrdem : recursoOrdems.getDados()){
				PreconditionsREST.checkNotNull(recursoOrdem.getOrdemOrigem().getId(), "not found order");
				PreconditionsREST.checkNotNull(recursoOrdem.getRecurso().getId(), "not found resource ");

				String codigo = recursoOrdem.getCodigo();

				try{
					Ordem ordemServico = recursoOrdem.getOrdemOrigem();
					recursoOrdem.setDataCriacaoOrigem(recursoOrdem.getDataCriacao());
					recursoOrdem.setConsumerId(Long.parseLong(consumerKey));
					ordemServico.adicionarRecurso(recursoOrdem, usuario, consumerKey);
					batchResponses.add(new ResponseBatch(codigo, StatusModel.ATIVO, recursoOrdem.getId(),0));
				} catch (Exception e) {
					batchResponses.add(new ResponseBatch(codigo, StatusModel.REJEITADO, -1l, 1));
				}
			}
			return Response.ok(new ResponseBatchData(batchResponses, batchResponses.size())).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());

		}finally{
			tx = null;
			session.close();
			session = null;
			usuario = null;
			consumerKey = null;
		}
	}

	@Path("{id}/recurso")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response getOrdemServicoRecurso(@PathParam("id") Long id,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("1") Integer statusModel) {

		ArrayList<RecursoOrdem> dados = null;
		try{
			dados =  RecursoOrdem.listaRecursoOrdem(id,statusModel);
			PreconditionsREST.checkNotNull(dados, "not found");
			return Response.ok(new RecursoOrdemData(dados, null)).build();
		}finally{
			dados = null;
		}
	}

	@Path("/{idOrdem}/recurso/{idRecursoOrdem}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response alterarRecurso(@PathParam("idOrdem") Long idOrdem,
			@PathParam("idRecursoOrdem") Long id, RecursoOrdem recursoOrdem) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Usuario usuario;

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			PreconditionsREST.checkNotNull(recursoOrdem.getId(), "not found resource");
			usuario = validaUsuario(session);
			tx.commit();

			Ordem ordem = new Ordem(idOrdem); 
			ordem.alteraRecurso(recursoOrdem, Long.parseLong(consumerKey), usuario);

			return Response.ok(recursoOrdem).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());

		}finally{
			recursoOrdem = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/{idOrdem}/recurso/{idRecursoOrdem}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response removerRecurso(@PathParam("idOrdem") Long idOrdem,@PathParam("idRecursoOrdem") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Usuario usuario = validaUsuario(session);
			tx.commit();

			RecursoOrdem recursoOrdem = new RecursoOrdem();
			recursoOrdem.setId(id);

			Ordem ordem = new Ordem(idOrdem); 
			ordem.removeRecurso(recursoOrdem, Long.parseLong(consumerKey), usuario);

			return Response.ok(recursoOrdem).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());

		}finally{
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}/contrato")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response adicionaContrato(@PathParam("id") Long id, ClienteObjeto clienteObjeto) {

		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

		Session session = HibernateHelper.openSession(getClass());

		try{
			Transaction tx = session.beginTransaction();
			Usuario usuario = validaUsuario(session);
			Ordem ordemServico = valida(id, session);
			PreconditionsREST.checkNotNull(ordemServico, "not found");
			tx.commit();

			PreconditionsREST.checkNotNull(ordemServico, "not found");

			ordemServico.adicionarContratoCliente(clienteObjeto, usuario, consumerKey);

			return Response.ok(ordemServico).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());

		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}/contato")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response adicionaContato(@PathParam("id") Long id, Contato contato) {

		Session session = HibernateHelper.openSession(getClass());

		try{
			Transaction tx = session.beginTransaction();
			Ordem ordemServico = valida(id, session);
			PreconditionsREST.checkNotNull(ordemServico, "not found");
			tx.commit();

			ordemServico.adicionarContato(contato);
			return Response.ok(ordemServico).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());

		}finally{
			session.close();
			session = null;
		}
	}

	@Path("{id}/agendamento")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response atualizaOrdemServicoAgendamento(Ordem ordem, @PathParam("id") long id) {
		PreconditionsREST.checkNotNull(ordem, "not found");
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			Usuario usuario = validaUsuario(session);
			tx.commit();

			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			ordem.adicionarAgendamento(ordem, usuario, consumerKey);

			return Response.ok(URI.create(String.valueOf(200))).entity(new Ordem(id)).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/agendamento/{id}/cancelar")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public Response cancelarOrdemServicoAgendamento(Ordem ordem, @PathParam("id") long id) {
		PreconditionsREST.checkNotNull(ordem, "not found");
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			Usuario usuario = validaUsuario(session);
			tx.commit();
			ordem.cancelarAgendamento(ordem, usuario, consumerKey);

			return Response.ok(URI.create(String.valueOf(200))).entity(new Ordem(id)).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/agendamento")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public OrdemServicoDataDTO getOrdemAgendamento(
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("1000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam(ParameterRequestRest.FIELDS) @DefaultValue("") String fields,
			@QueryParam("datainicio") @DefaultValue("01/01/1900") Date dataInicio,
			@QueryParam("datafim") @DefaultValue("01/01/2900") Date dataFim) {

		ArrayList<OrdemDTO> ordens = null;
		OrdemServicoDataDTO ordemServicoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			ordens = (ArrayList<OrdemDTO>) Ordem.listaOrdemAgendamento(session, offset, limit,consumerKey,dataInicio,dataFim);
			tx.commit();
			ordemServicoData = new OrdemServicoDataDTO(ordens, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, ordens.size()));
			return ordemServicoData;
		}finally{
			tx = null;
			session = null;
			ordemServicoData = null;
			ordens = null;
		}
	}

	@Path("funcionario/{id}/agendamento")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AgendaOrdemFuncionarioData getAgendamentoOrdemPorFuncionario(
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("1000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam("datainicio") @DefaultValue("01/01/1900") Date dataInicio,
			@QueryParam("datafim") @DefaultValue("01/01/2900") Date dataFim,
			@PathParam("id") Long id) {

		ArrayList<AgendaOrdemFuncionario> ordens = null;
		AgendaOrdemFuncionarioData  agendaOrdemFuncionarioDataDTO  = null;
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			try {
				ordens = (ArrayList<AgendaOrdemFuncionario>) AgendaOrdemFuncionario.listaFuncinarioOrdem(id,dataInicio, dataFim, consumerKey);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			agendaOrdemFuncionarioDataDTO  = new AgendaOrdemFuncionarioData (ordens, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, ordens.size()));
			return agendaOrdemFuncionarioDataDTO;
		}finally{
			agendaOrdemFuncionarioDataDTO = null;
			ordens = null;
		}
	}

	@Path("{idOrdem}/funcionario/agendamento")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class})
	public AgendaOrdemFuncionarioData getFuncionarioPorOrdem(@PathParam("idOrdem") Long idOrdem){

		ArrayList<AgendaOrdemFuncionario> ordens = null;
		AgendaOrdemFuncionarioData  agendaOrdemFuncionarioData = null;
	
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			try{
				ordens = (ArrayList<AgendaOrdemFuncionario>) AgendaOrdemFuncionario.listaOrdemFuncinario(idOrdem, consumerKey);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
			}
			agendaOrdemFuncionarioData  = new AgendaOrdemFuncionarioData (ordens, LinkData.createLinks(httpServletRequest.getRequestURL().toString(),  ordens.size(), 0, ordens.size()));
			return agendaOrdemFuncionarioData;
		}finally{
			agendaOrdemFuncionarioData = null;
			ordens = null;
		}
	}

	@Path("/funcionario/agendamento/{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response atualizaAgendamentoAtribuicao(AgendaOrdemFuncionario agendaOrdemFuncionario, @PathParam("id") long idAgendamento) {
		AgendaOrdemFuncionario agendaOrdemFuncionarioAtual;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String cs = null;
		try{
			cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			agendaOrdemFuncionarioAtual = (AgendaOrdemFuncionario) AgendaOrdemFuncionario.pesquisaPorIdConsumerSecret(session,AgendaOrdemFuncionario.class,idAgendamento,cs);
			tx.commit();

			agendaOrdemFuncionarioAtual.setDataAlteracao(new Date());
			agendaOrdemFuncionarioAtual.setStatusModel(agendaOrdemFuncionario.getStatusModel());
			agendaOrdemFuncionarioAtual.altera();

			return Response.ok(new AgendaOrdemFuncionario()).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			tx = null;
			session.close();
			session = null;
			agendaOrdemFuncionarioAtual = null;
		}
	}

	@Path("funcionario/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public OrdemServicoDataDTO getOrdemPorFuncionario(
			@QueryParam("inicio") @DefaultValue("0") Integer inicio,
			@QueryParam("tamanho") @DefaultValue("500") Integer tamanhoPagina,
			@PathParam("id") Long id,
			@QueryParam("datainicio") @DefaultValue("01/01/1900") Date dataInicio,
			@QueryParam("datafim") @DefaultValue("01/01/2900") Date dataFim) {

		ArrayList<OrdemDTO> ordens = null;
		OrdemServicoDataDTO ordemServicoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String cs = null;

		try{
			cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			if(!Funcionario.existIdAtivoInativoPorConsumerSecret(session, Funcionario.class, id, cs)){
				PreconditionsREST.checkNotNull(null, "Funcionário não existe");
			}

			ordens = (ArrayList<OrdemDTO>) Ordem.listaOrdem(session, inicio, tamanhoPagina,cs,id,dataInicio,dataFim);
			tx.commit();
			ordemServicoData = new OrdemServicoDataDTO(ordens, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), tamanhoPagina, inicio, ordens.size()));
			return ordemServicoData;
		}finally{
			tx = null;
			session = null;
			cs = null;
			ordemServicoData = null;
			ordens = null;
		}
	}


	@Path("{id}/funcionario")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response adicionaFuncionarioExecucao(@PathParam("id") Long id,
			AgendaOrdemFuncionario agendaOrdemFuncionario) {

		Ordem ordemServico = null;
		Usuario usu = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String cs = null;

		try{
			cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));

			ordemServico = Ordem.pesquisaOrdem(id, 1, cs);
			ordemServico.setConsumerId(Long.parseLong(cs));

			PreconditionsREST.checkNotNull(id, "Ordem Serviço não encontrado");

			if(!Funcionario.existIdAtivoInativoPorConsumerSecret(session, Funcionario.class, agendaOrdemFuncionario.getFuncionario().getId(), cs)){
				PreconditionsREST.checkNotNull(null, "Funcionário não existe");
			}

			usu = (Usuario) Usuario.pesquisaAccessTokenReturnId(FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")));
			PreconditionsREST.checkNotNull(usu, "Usuário ");

			tx.commit();

			agendaOrdemFuncionario = ordemServico.adicionarFuncionarioExecucao(agendaOrdemFuncionario, usu, cs);
			agendaOrdemFuncionario.setOrdem(null);
			return Response.ok(agendaOrdemFuncionario).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());

		}finally{
			ordemServico = null;
			tx = null;
			session.close();
			session = null;
			cs = null;
		}
	}

	@POST
	@Path("/{id}/anexo")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response adicionarAnexo(@PathParam("id") Long id,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("data")  String descricao) throws IOException {

		Session session = HibernateHelper.openSession(getClass());
		byte[] imagem;
		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Ordem ordem = new Ordem(id);
			PreconditionsREST.checkNotNull(id, "Ordem Serviço not found");

			Anexo anexo = new Anexo();
			anexo.setDescricao(fileDetail.getFileName());

			try{
				BufferedImage scaledImg = null;
				BufferedImage originalImage = ImageIO.read(uploadedInputStream);
				scaledImg = Scalr.resize(originalImage, Mode.AUTOMATIC, 600, 600);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(scaledImg, "jpg", os);
				imagem = os.toByteArray();
				anexo.setImagem(imagem);
				anexo.setTamanho(imagem.length);
			}catch(Exception e){}


			anexo.setOrdem(ordem);
			anexo.setConsumerSecret(new ConsumerSecret("", Long.parseLong(consumerKey)));
			try {
				anexo.salvar();
				ordem.atualizarDatas(session);
			} catch (ModelException e) {
				PreconditionsREST.error(e.getMessage());
				e.printStackTrace();
			}

			return Response.status(200).entity(anexo).build();

		}finally{
			uploadedInputStream = null;
			imagem = null;
			session.close();
			session = null;
		}
	}

	@GET
	@Path("/anexo/{id}/imagem")
	@Produces("image/jpeg")
	public Response getAnexoImage(@PathParam("id") Long id,
			@QueryParam("x") @DefaultValue("60") Integer x,
			@QueryParam("y") @DefaultValue("60") Integer y) {

		byte[] anexo= null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			anexo =  Anexo.getImagemAnexo(session, consumerKey, id);
			tx.commit();

			InputStream file = new ByteArrayInputStream(anexo);
			BufferedImage scaledImg = null;
			BufferedImage originalImage = ImageIO.read(file);
			scaledImg = Scalr.resize(originalImage, Mode.AUTOMATIC, x.intValue(), y.intValue());
			return Response.ok().entity(scaledImg).build();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			anexo = null;
			session.close();
			session = null;
			tx = null;
		}
	}

	@GET
	@Path("/anexo/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAnexo(@PathParam("id") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			Anexo anexo = (Anexo) Anexo.pesquisaPorIdConsumerSecret(session,Anexo.class,id,consumerKey);
			tx.commit();
			return Response.ok().entity(anexo).build();

		}finally{
			session.close();
			session = null;
			tx = null;
		}
	}

	@GET
	@Path("{id}/anexo")
	@Produces(MediaType.APPLICATION_JSON)
	public AnexoData listaAnexo(@PathParam("id") Long id) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<Anexo> anexos = null;
		AnexoData anexoData = null;

		try{
			String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			anexos = Anexo.listaOrdemAnexo(session, consumerKey, id);
			tx.commit();
			anexoData = new AnexoData(anexos, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), anexos.size(), 0,anexos.size()));
			return anexoData;
		} catch (ModelException e) {
			PreconditionsREST.error(e.getMessage());
			return anexoData;
		}finally{
			tx = null;
			session = null;
			anexos = null;
			anexoData = null;
		}

	}

	private Ordem valida(Long id, Session session) {
		String consumerKey =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		Ordem ordemServico = (Ordem) Ordem.pesquisaReturnID(session,Ordem.class,id,consumerKey);
		PreconditionsREST.checkNotNull(ordemServico, "OrdemServico não encontrado");
		return ordemServico;
	}

	private Usuario validaUsuario(Session session) {
		Usuario usuario = (Usuario) Usuario.pesquisaAccessTokenReturnId(session, FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")));
		PreconditionsREST.checkNotNull(usuario, "not found user");
		return usuario;
	}
}
