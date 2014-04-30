package br.com.rest.resources;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import br.com.eventos.model.Evento;
import br.com.eventos.model.Ponto;
import br.com.eventos.model.TipoEvento;
import br.com.exception.ModelException;
import br.com.funcionario.model.Funcionario;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.EventoDataDTO;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAccessTokenRequiredFilter;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthNonceRequiredFilter;
import br.com.rest.values.RESTMessageHelper;
import br.com.usuario.model.Usuario;

import com.sun.jersey.spi.container.ResourceFilters;

@Path("/evento")
public class EventoResource {

	@Context HttpServletRequest httpServletRequest;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public EventoDataDTO lista(@HeaderParam("If-Modified-Since") @DefaultValue("01/01/1900") Date modifiedSince,
			@QueryParam("datainicio") @DefaultValue("") String datainicio,
			@QueryParam("datafinal")  @DefaultValue("") String datafinal,
			@QueryParam("tamanho")  @DefaultValue("1000") Integer tamanho) {

		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Funcionario funcionario= null;
		EventoDataDTO eventoData;

		if(datainicio.isEmpty()){
			datainicio = FormatDateHelper.formatTimeToStringUS(00, 01);
		}

		if(datafinal.isEmpty()){
			datafinal = FormatDateHelper.formatTimeToStringUS(23, 59);
		}

		System.out.println(datainicio);

		try{
			funcionario = (Funcionario) Funcionario.pesquisaAccessToken(session,FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")),Funcionario.class);
			eventoData = new EventoDataDTO(Evento.listaEvento(session,funcionario,datainicio,datafinal,tamanho));
			tx.commit();
			return eventoData;
		}catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			eventoData = null;
			funcionario = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/ultimo")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Response listaUltimoEvento(@HeaderParam("If-Modified-Since") @DefaultValue("01/01/1900") Date modifiedSince){
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String cs = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
		EventoDataDTO eventoData;
		try{
			eventoData = new EventoDataDTO(Evento.listaUltimoEventoPorUsuario(session,cs));
			tx.commit();
			return Response.ok(eventoData).build();
		}catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			cs = null;
			eventoData = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Response pesquisa(@HeaderParam("If-Modified-Since") @DefaultValue("01/01/1900") Date modifiedSince, @PathParam("id") Long id) {
		Evento evento = null;
		try {
			evento = Evento.pesquisaEventoID(id);
		} catch (ModelException e1) {
			Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException",e1.getMessage())).build();
		}
		return Response.ok().entity(evento).build();
	}



	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response adicionaEventoPost(Evento evento) {
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Usuario usu = null;

		try{
			usu = (Usuario) Usuario.pesquisaAccessTokenReturnId(FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")));
			tx.commit();
			PreconditionsREST.checkNotNull(usu, RESTMessageHelper.NOTFOUND);
			evento.setUsuario(usu);
			evento.setConsumerSecret(usu.getConsumerSecret());

			if(evento.getTipoEvento() == null)
				PreconditionsREST.checkNotNull(null, "Tipo evento n�o dispon�vel");

			return adicionaEvento(session,evento);

		}finally{
			evento		= null;
			session.close();
			session = null;
			tx = null;
		}
	}

	protected Response adicionaEvento(Session session, Evento evento) {
		try {
			evento.setDuracao((float)evento.getDataFim().getTime() - evento.getDataInicio().getTime());
			evento.salvar(session);
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE) .
					type(MediaType.APPLICATION_JSON) .entity(new ErrorEntity("RESTException",e.getMessage())).build());
		}
		return Response.ok().entity(evento).build();
	}

	@Path("/atendimento")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Response adicionarEventoAtendimento(Evento evento) {
		Funcionario funcionario;
		TipoEvento  tipoEvento;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			funcionario = (Funcionario) Funcionario.pesquisaAccessToken(FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")),Funcionario.class);
			PreconditionsREST.checkNotNull(funcionario, "Sem registro de funcionario no sistema");

			tipoEvento = TipoEvento.pesquisaTipoEvento("atendimento",ConsumerSecret.retrieve(session, FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"))));
			PreconditionsREST.checkNotNull(tipoEvento, "Tipo evento n�o dispon�vel");
			tx.commit();
			evento.setUsuario(funcionario);

			try {
				evento.salvar();
			} catch (ModelException e) {
				throw new WebApplicationException(
						Response.status(Status.NOT_ACCEPTABLE) .
						type(MediaType.APPLICATION_JSON) .entity(new ErrorEntity("RESTException",e.getMessage())).build());
			}
			return Response.ok().entity(evento).build();
		}finally{
			funcionario = null;
			tipoEvento  = null;
			evento		= null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/andamento")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Response adicionarEventoEmAndamento(Evento evento) {

		Funcionario funcionario;
		TipoEvento  tipoEvento;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			funcionario = (Funcionario) Funcionario.pesquisaAccessToken(session, FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")),Funcionario.class);
			PreconditionsREST.checkNotNull(funcionario, "Sem registro de funcionario no sistema");

			tipoEvento = TipoEvento.pesquisaTipoEvento("andamento",ConsumerSecret.retrieve(session,FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"))));
			PreconditionsREST.checkNotNull(tipoEvento, "Tipo evento n�o dispon�vel");
			tx.commit();
			try {
				evento.salvar();
			} catch (ModelException e) {
				throw new WebApplicationException(
						Response.status(Status.NOT_ACCEPTABLE) .
						type(MediaType.APPLICATION_JSON) .entity(new ErrorEntity("RESTException",e.getMessage())).build());
			}
			return Response.ok().entity(evento).build();
		}finally{
			funcionario = null;
			tipoEvento  = null;
			evento		= null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/livre")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Response adicionarEventoEmLivre(Evento evento) {

		Funcionario funcionario;
		TipoEvento  tipoEvento;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			funcionario = (Funcionario) Funcionario.pesquisaAccessToken(session,FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")),Funcionario.class);
			PreconditionsREST.checkNotNull(funcionario, "Sem registro de funcionario no sistema");

			tipoEvento = TipoEvento.pesquisaTipoEvento("livre",ConsumerSecret.retrieve(session, FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"))));
			PreconditionsREST.checkNotNull(tipoEvento, "Tipo evento n�o dispon�vel");
			tx.commit();
			try {
				evento.salvar();
			} catch (ModelException e) {
				throw new WebApplicationException(
						Response.status(Status.NOT_ACCEPTABLE) .
						type(MediaType.APPLICATION_JSON) .entity(new ErrorEntity("RESTException",e.getMessage())).build());
			}
			return Response.ok().entity(evento).build();
		}finally{
			funcionario = null;
			tipoEvento  = null;
			evento		= null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/login")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Response adicionarEventoLogin(Evento evento) {

		Funcionario funcionario;
		TipoEvento  tipoEvento;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			funcionario = (Funcionario) Funcionario.pesquisaAccessToken(session, FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")),Funcionario.class);
			PreconditionsREST.checkNotNull(funcionario, "Sem registro de funcionario no sistema");

			tipoEvento = TipoEvento.pesquisaTipoEvento("login",ConsumerSecret.retrieve(session, FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"))));
			PreconditionsREST.checkNotNull(tipoEvento, "Tipo evento n�o dispon�vel");
			tx.commit();
			try {
				evento.salvar();
			} catch (ModelException e) {
				throw new WebApplicationException(
						Response.status(Status.NOT_ACCEPTABLE) .
						type(MediaType.APPLICATION_JSON) .entity(new ErrorEntity("RESTException",e.getMessage())).build());
			}
			return Response.ok().entity(evento).build();
		}finally{
			funcionario = null;
			tipoEvento  = null;
			evento		= null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("/logout")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Response adicionarEventoLogOut(Evento evento) {
		Funcionario funcionario;
		TipoEvento  tipoEvento;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();

		try{
			funcionario = (Funcionario) Funcionario.pesquisaAccessToken(session,FilterHelper.extractValueFromKeyValuePairs("oauth_token", httpServletRequest.getHeader("Authorization")),Funcionario.class);
			PreconditionsREST.checkNotNull(funcionario, "Sem registro de funcionario no sistema");

			tipoEvento = TipoEvento.pesquisaTipoEvento("logout",ConsumerSecret.retrieve(session, FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"))));
			PreconditionsREST.checkNotNull(tipoEvento, "Tipo evento n�o dispon�vel");
			tx.commit();
			try {
				evento.salvar();
			} catch (ModelException e) {
				throw new WebApplicationException(
						Response.status(Status.NOT_ACCEPTABLE) .
						type(MediaType.APPLICATION_JSON) .entity(new ErrorEntity("RESTException",e.getMessage())).build());
			}
			return Response.ok().entity(evento).build();
		}finally{
			funcionario = null;
			tipoEvento  = null;
			evento		= null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public Response alterarEvento(Ponto ponto, @PathParam("id") Long id) {
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		Evento evento = null;

		try {
			evento = Evento.pesquisaEventoID(id);
		} catch (ModelException e1) {
			Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException",e1.getMessage())).build();
		}
		tx.commit();

		evento.setDataFim(ponto.getDataRegistro());

		try {
			ponto.setEvento(evento);
			ponto.salvar();
			evento.alterar();
			return Response.ok().entity(ponto).build();
		} catch (ModelException e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE) .
					type(MediaType.APPLICATION_JSON) .entity(new ErrorEntity("RESTException",e.getMessage())).build());
		}finally{
			ponto = null;
			evento = null;
			session.close();
			session = null;
			tx = null;
		}
	}
}
