package br.com.rest.resources;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import br.com.funcionario.model.Funcionario;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.EventoDataDTO;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.resources.filters.FilterHelper;
import br.com.rest.resources.filters.OAuthAccessTokenRequiredFilter;
import br.com.rest.resources.filters.OAuthAuthorizationRequiredFilter;
import br.com.rest.resources.filters.OAuthNonceRequiredFilter;

import com.sun.jersey.spi.container.ResourceFilters;

@Path("{idUsuario}/rastreamento")
public class RastreamentoRecurso {

	@PathParam("idUsuario")
	private Long idUsuario;
	
	@Context HttpServletRequest httpServletRequest;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ResourceFilters(value = {OAuthAuthorizationRequiredFilter.class, OAuthAccessTokenRequiredFilter.class, OAuthNonceRequiredFilter.class})
	public EventoDataDTO lista(@HeaderParam("If-Modified-Since") @DefaultValue("01/01/1900") Date modifiedSince,
			  @QueryParam("datainicio") @DefaultValue("0") Long datainicio,
		      @QueryParam("datafinal")  @DefaultValue("0") Long datafinal,
		      @QueryParam("tamanho")  @DefaultValue("20") Integer tamanho) {
		
		Session session = HibernateHelper.openSession(getClass());
	    Transaction tx = session.beginTransaction();
	
		
		if(datainicio <= 0){
			datainicio = FormatDateHelper.formatTimeToLong(00, 01);
		}
		
		if(datafinal <= 0){
			datafinal = FormatDateHelper.formatTimeToLong(23, 59);
		}
		Funcionario funcionario;
		ConsumerSecret cs = ConsumerSecret.retrieve(session, FilterHelper.extractValueFromKeyValuePairs("oauth_consumer_key", httpServletRequest.getHeader("Authorization")));
		tx.commit();
		try{
			funcionario = (Funcionario) Funcionario.pesquisaPorIdConsumerSecret(Funcionario.class, idUsuario, cs.getConsumerSecret());
			return new EventoDataDTO(Evento.listaEvento(session, funcionario,FormatDateHelper.formatTimeZoneUSToBR(datainicio),FormatDateHelper.formatTimeZoneUSToBR(datafinal),tamanho));
		}catch (Exception e) {
		    throw new WebApplicationException(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(new ErrorEntity("RESTException", e.getMessage())).build());
		}finally{
			funcionario = null;
			tx = null;
	    	session.close();
	    	session = null;
		}
	}
}
