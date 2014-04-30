package br.com.contato.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.sun.xml.bind.CycleRecoverable;

import br.com.contato.dao.ContatoDAO;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IContato;
import br.com.model.interfaces.IEndereco;
import br.com.model.interfaces.IUsuario;
import br.com.usuario.model.Usuario;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="contato")
public class Contato extends Model<Contato> implements Serializable, CycleRecoverable, IContato{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "tipocontato")
	private String tipoContato = null;

	@Column(name = "nome")
	private String nome= null;

	@Column(name = "email")
	private String email = null;

	@Column(name = "emailenviar")
	private boolean emailEnviar = false;
	
	@Column(name = "telefonefixo", length=15)
	private String telefoneFixo = null;

	@Column(name = "telefonemovel", length=15)
	private String telefoneMovel = null;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumns({    
		@JoinColumn( name = "enderecoidpk", referencedColumnName="id"),
	})
	@Cascade(CascadeType.ALL)
	private Endereco endereco = null;

	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "usuarioidpk", referencedColumnName="id"),
	})
	private Usuario usuario;

	public Contato(){
		super(Contato.class);
	}

	public Contato(Long id, String codigo, Long idEndereco){
		setId(id);
		setCodigo(codigo);
		setEndereco(new Endereco(idEndereco, "", ""));
	}
	
	public String getTipoContato() {
		return tipoContato;
	}
	public void setTipoContato(String tipoContato) {
		this.tipoContato = tipoContato;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(IUsuario usuario) {
		this.usuario = (Usuario) usuario;
	}

	public Endereco getEndereco() {
		if(endereco == null){
			endereco = new Endereco();
		}
		return endereco;
	}

	public void setEndereco(IEndereco endereco) {
		this.endereco = (Endereco) endereco;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailEnviar() {
		return emailEnviar;
	}

	public void setEmailEnviar(boolean emailEnviar) {
		this.emailEnviar = emailEnviar;
	}
	
	public String getTelefoneFixo() {
		return telefoneFixo;
	}

	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}

	public String getTelefoneMovel() {
		return telefoneMovel;
	}

	public void setTelefoneMovel(String telefoneMovel) {
		this.telefoneMovel = telefoneMovel;
	}

	public void altera() throws ModelException{
		ContatoDAO contatoDAO = new ContatoDAO();
		try {
			contatoDAO.altera(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	public void alteraEndereco() throws ModelException{
		ContatoDAO contatoDAO = new ContatoDAO();
		try {
			contatoDAO.alteraEndereco(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	public static Contato pesquisa(Long id, String consumerKey) throws ModelException{
		ContatoDAO contatoDAO = new ContatoDAO();
		try {
			return contatoDAO.pesquisa(id, Long.parseLong(consumerKey));
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (emailEnviar ? 1231 : 1237);
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((tipoContato == null) ? 0 : tipoContato.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contato other = (Contato) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (emailEnviar != other.emailEnviar)
			return false;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (tipoContato == null) {
			if (other.tipoContato != null)
				return false;
		} else if (!tipoContato.equals(other.tipoContato))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */


	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(nome,"Nome inv��lido");
		PreconditionsModel.checkNotNull(getConsumerSecret(), "Consumer Secret inv��lido");
	
		if(getCodigo().isEmpty()){
			setCodigo("CONT"+new Date().getTime()+""+count());
		}
		
		if(endereco != null){
			if(endereco.getCep() != null){	
				endereco.setConsumerSecret(getConsumerSecret());
				endereco.valida();
			}	
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		Contato obj = new Contato();  
		return obj;  
	}


}
