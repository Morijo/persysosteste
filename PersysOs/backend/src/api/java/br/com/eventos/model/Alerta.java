package br.com.eventos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.sun.xml.bind.CycleRecoverable;

import br.com.evento.dao.AlertaDAO;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.usuario.model.Usuario;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="alerta")
public class Alerta extends Model<Alerta> implements Serializable, CycleRecoverable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "mensagem", length = 255)
	private String mensagem = null;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumns({    
		@JoinColumn( name = "usuarioidpk", referencedColumnName="id"),
	})
	private Usuario usuario = null;

	public Alerta(){
		super(Alerta.class);
	}

	public final static String CONSTRUTOR = "id, mensagem, statusModel, usuario.id, usuario.razaoNome";

	public Alerta(Long id, String mensagem,Integer statusModel, Long idUsuario, String razaoNome){
		super(Alerta.class);

		setId(id);
		setStatusModel(statusModel);
		this.mensagem = mensagem;
		this.usuario = new Usuario(idUsuario,null,razaoNome,null);
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public static void setLida(String consumerKey, int statusModel) throws ModelException{
		try {
			AlertaDAO.lida(Long.parseLong(consumerKey), statusModel);
		} catch (NumberFormatException e) {
			throw new ModelException(e.getMessage());
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	public static java.util.List<Alerta> listaAlerta(String consumerKey, int limit) throws ModelException{
		AlertaDAO alertaDAO = new AlertaDAO();
		try {
			return alertaDAO.listaAlerta(Long.parseLong(consumerKey), limit);
		} catch (NumberFormatException e) {
			throw new ModelException(e.getMessage());
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	public static Number numberAlerta(String consumerKey, int statusModel) throws ModelException{
		AlertaDAO alertaDAO = new AlertaDAO();
		try {
			return alertaDAO.countAlerta(Long.parseLong(consumerKey), statusModel);
		} catch (NumberFormatException e) {
			throw new ModelException(e.getMessage());
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkNotNull(usuario, "user not found");
		if(getCodigo().isEmpty()){
			setCodigo("PALE"+countPorConsumerSecret(Alerta.class,getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
