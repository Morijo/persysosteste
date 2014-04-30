package br.com.eventos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.Session;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.sun.xml.bind.CycleRecoverable;

import br.com.evento.dao.EventoDAO;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.rest.hateoas.dto.EventoDTO;
import br.com.rest.represention.JsonDateAdapter;
import br.com.usuario.model.Usuario;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="evento")
public class Evento extends Model<Evento> implements Serializable, CycleRecoverable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "mensagem", length = 255)
	private String mensagem = null;
	
	@XmlJavaTypeAdapter(value=JsonDateAdapter.class)
	@Column(name = "datainicio")
	private Date  dataInicio = null;
	
	@XmlJavaTypeAdapter(value=JsonDateAdapter.class)
	@Column(name = "datafim")
	private Date  dataFim = null;
	
	@Column(name="duracao")
	private Float duracao = null;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumns({    
		@JoinColumn( name = "usuarioidpk", referencedColumnName="id"),
	})
	private Usuario usuario = null;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumn(name = "tipoeventoid")
	private TipoEvento tipoEvento = null;
	
	public Evento(){
		super(Evento.class);
	}
	
	public static String CONTRUTROR = "id,mensagem, dataInicio, dataFim, usuario.id, tipoEvento.titulo";
	
	public Evento(Long id, String mensagem, Date dataInicio, Date dataFim, Float velocidade, Long idUsuario, String tipoEventoTitulo){
		super(Evento.class);
		
		setId(id);
		this.mensagem = mensagem;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.usuario = new Usuario(idUsuario);
		this.tipoEvento = new TipoEvento(0l,tipoEventoTitulo);
	}
	
	
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Date getDataInicio() {
		return dataInicio;
	}


	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}


	public Date getDataFim() {
		return dataFim;
	}


	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Float getDuracao() {
		return duracao;
	}

	public void setDuracao(Float duracao) {
		this.duracao = duracao;
	}

	public static Evento pesquisaEventoID(Long id) throws ModelException{
		
		Evento evento = null;
		
		try{
		 	EventoDAO eventoDAO = new EventoDAO();
	    	evento = eventoDAO.pesquisaEventoID(id);
	    	eventoDAO = null;
	    	return evento;
	    }
		catch (Exception e) {
			throw new ModelException(e.getMessage());
		}	
	}
	
	 public static List<Evento> listaEvento(Usuario usuario){
		 	EventoDAO eventoDAO = new EventoDAO();
		 	return eventoDAO.listaEvento(usuario);
	 }
	
	 public static List<Evento> listaEvento(Session session,Usuario usuario, Date inicio, Date fim){
		 	EventoDAO eventoDAO = new EventoDAO();
	    	return eventoDAO.listaEvento(session, usuario, inicio, fim);
	 }
	 
	 public static List<EventoDTO> listaEvento(Session session, Usuario usuario, String inicio, String fim, Integer numResultados){
		 	EventoDAO eventoDAO = new EventoDAO();
	    	return eventoDAO.listaEvento(session, usuario, inicio, fim, numResultados);
	 }
	 
	 public static List<EventoDTO> listaEvento2(Usuario usuario, String inicio, String fim, Integer numResultados){
		 	EventoDAO eventoDAO = new EventoDAO();
	    	return eventoDAO.listaEvento(usuario, numResultados);
	 }
	 
	 public static List<EventoDTO> listaUltimoEventoPorUsuario(Session session, String consumerSecret ) {
		 EventoDAO eventoDAO = new EventoDAO();
	    	return eventoDAO.listaUltimoEventoPorUsuario(session,consumerSecret );
	 }
	 
	 public static ArrayList<Evento> lista(Long idUsuario ) {
		 EventoDAO eventoDAO = new EventoDAO();
	    	return eventoDAO.listaEventos(idUsuario);
	 }
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkNotNull(dataInicio, "date start not found");
		PreconditionsModel.checkNotNull(dataFim, "date end not found");
		PreconditionsModel.checkNotNull(usuario, "user not found");
		
		if(dataInicio.after(dataFim)){
			PreconditionsModel.checkNotNull(null, "date invalid");
    	}
			
		duracao = (float) ((dataFim.getTime() - dataInicio.getTime()) / 1000);
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
