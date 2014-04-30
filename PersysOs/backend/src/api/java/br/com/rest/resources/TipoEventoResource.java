package br.com.rest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.eventos.model.TipoEvento;
import br.com.exception.ModelException;
import br.com.model.StatusModel;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.TipoEventoData;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.LinkData;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.helper.ParameterRequestRest;

@Path("/tipoevento")
public class TipoEventoResource {

@Context HttpServletRequest httpServletRequest;
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TipoEventoData getTipoEvento(
			@QueryParam(ParameterRequestRest.SINCE) @DefaultValue("01/01/1900") Date since,
			@QueryParam(ParameterRequestRest.OFFSET) @DefaultValue("0") Integer offset,
			@QueryParam(ParameterRequestRest.LIMIT) @DefaultValue("1000") Integer limit,
			@QueryParam(ParameterRequestRest.STATUS) @DefaultValue("2") Integer statusModel,
			@QueryParam("attr") @DefaultValue("") String attr) {
		
		
		ArrayList<TipoEvento> tipoEventos = null;
		TipoEventoData tipoEventoData = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String cs = null;
		try{
			cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			tipoEventos = (ArrayList<TipoEvento>) TipoEvento.pesquisaListaPorDataAlteracaEConsumerSecret(session,offset, limit,attr,FormatDateHelper.formatTimeZone(since) ,cs, TipoEvento.class,statusModel);
			tx.commit();
			tipoEventoData = new TipoEventoData(tipoEventos, LinkData.createLinks(httpServletRequest.getRequestURL().toString(), limit, offset, TipoEvento.countPorConsumerSecret(session,TipoEvento.class,cs,statusModel,since),statusModel));
			return tipoEventoData;
		}finally{
			tx = null;
		    session.close();
		    session = null;
		    cs = null;
			tipoEventos = null;
			tipoEventoData = null;
		}
	}
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTipoEvento(@PathParam("id") Long id) {
	
		TipoEvento tipoEvento = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String cs = null;
		try{
			cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			tipoEvento = (TipoEvento) TipoEvento.pesquisaPorIdConsumerSecret(session,TipoEvento.class,id,cs);
			tx.commit();
			PreconditionsREST.checkNotNull(tipoEvento, "TipoEvento n�o encontrado");
			return Response.ok(tipoEvento).build();
		}finally{
			tipoEvento = null;
			tx = null;
		    session.close();
		    session = null;
		    cs = null;
		}
	}
	
	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response atualizaTipoEvento(TipoEvento tipoEvento, @PathParam("id") Long id) {
		
		TipoEvento tipoEventoAtual = null;
		
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String cs = null;
		try{
			cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			tipoEventoAtual = (TipoEvento) TipoEvento.pesquisaPorIdConsumerSecret(session,TipoEvento.class,id,cs);
			tx.commit();
			
			PreconditionsREST.checkNotNull(tipoEventoAtual, "TipoEvento n�o encontrado");
			tipoEventoAtual.setTitulo(tipoEvento.getTitulo());
			tipoEventoAtual.setObservacao(tipoEvento.getObservacao());
			tipoEventoAtual.setCodigo(tipoEvento.getCodigo());
			try {
				tipoEventoAtual.alterar();
				return Response.ok().entity(tipoEventoAtual).build();
				
			} catch (ModelException e) {
				throw new WebApplicationException(
						Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
			}
		}finally{
			tipoEvento = null;
			tipoEventoAtual = null;
			tx = null;
		    session.close();
		    session = null;
		    cs = null;
		}
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	public Response adicionarTipoEvento(TipoEvento tipoEvento) {
		
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		ConsumerSecret cs = null;
		try{
			cs =  ConsumerSecret.retrieve(session,FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization")));
			tx.commit();
			tipoEvento.setConsumerSecret(cs);
			tipoEvento.valida();
			tipoEvento.salvar();
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}
		
		return Response.ok(URI.create(String.valueOf(200))).entity(tipoEvento).build();
		
	}

	@Path("{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeTipoEvento(@PathParam("id") long id) {
		
		TipoEvento tipoEvento = null;
		Session session = HibernateHelper.openSession(getClass());
		Transaction tx = session.beginTransaction();
		String cs = null;
		try{
			cs =  FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
			tipoEvento = (TipoEvento) TipoEvento.pesquisaPorIdConsumerSecret(session,TipoEvento.class,id,cs);
			tx.commit();
			PreconditionsREST.checkNotNull(tipoEvento, "Funcion�rio n�o encontrado");
			tipoEvento.remover();
			return Response.noContent().build();
		} catch (ModelException e) {
			throw new WebApplicationException(
				Response.status(Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}catch (NullPointerException e) {
		throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", "Sem registro no servidor")).build());
	    }finally{
	    	cs = null;
	    	tx = null;
		    session.close();
		    session = null;
		    cs = null;
	    }
	}
	
	@SuppressWarnings("unchecked")
	@Path("/busca")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public TipoEventoData buscaUnidadeLista(@HeaderParam("If-Modified-Since") @DefaultValue("01/01/1900") Date modificado,
			  @QueryParam("inicio") @DefaultValue("0") Integer inicio,
		      @QueryParam("tamanho") @DefaultValue("50") Integer tamanhoPagina,
		      @QueryParam("titulo") @DefaultValue("") String titulo,
		      @QueryParam("codigo") @DefaultValue("") String codigo) {
		
			 ArrayList<TipoEvento> tipoEventoLista = null;
		    TipoEventoData tipoEventoData = null;
		    ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
			Session session = HibernateHelper.openSession(getClass());
			Transaction tx = session.beginTransaction();
			String cs = null;

		    try{
		    	cs = FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization"));
					   
			    if(titulo.length() >= 3){
			    	parameter.add(ParameterDAO.with("titulo",titulo,ParameterDAOHelper.ILIKE));
			    }else if(codigo.length() >= 1){
					parameter.add(ParameterDAO.with("codigo",codigo,ParameterDAOHelper.EQ));
			    }else{
			    	PreconditionsREST.error("Condi��o inv�lida");
			    }
		
		    parameter.add(ParameterDAO.with("consumerSecret", cs,ParameterDAOHelper.EQ));
		    tipoEventoLista =(ArrayList<TipoEvento>) TipoEvento.pesquisaLista(session,TipoEvento.class, parameter);
			tx.commit();
			PreconditionsREST.checkNotNull(tipoEventoLista, "Nenhum resultado encontrado");
			
			tipoEventoData = new TipoEventoData(tipoEventoLista,LinkData.createLinks(httpServletRequest.getRequestURL().toString(), tamanhoPagina, inicio, tamanhoPagina,StatusModel.ATIVO));
			return tipoEventoData;
		    }finally{
		    	tipoEventoData = null;
		    	parameter = null;
		    	tipoEventoLista = null;
		    	tx = null;
			    session.close();
			    session = null;
			    cs = null;
		    }
	}
	
}
