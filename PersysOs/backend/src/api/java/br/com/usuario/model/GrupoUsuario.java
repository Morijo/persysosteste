package br.com.usuario.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;

import com.sun.xml.bind.CycleRecoverable;

import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.usuario.dao.UsuarioDAO;



@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="grupousuario")
public class GrupoUsuario extends Model<GrupoUsuario>  implements Serializable, CycleRecoverable, br.com.model.interfaces.IGrupoUsuario{

	private static final long serialVersionUID = 1L;
	@XmlTransient
	@Transient
	public static String CONSTRUTOR = "id,codigo,nome,administrador";
	
	@Column(name = "nome", length = 250)
	private String nome = "";
	
	@Column(name = "adminstrador")
	private Boolean administrador = false;
	
	@OneToMany(mappedBy = "grupoUsuario", targetEntity = Usuario.class, fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    private Collection<Usuario> usuario = new ArrayList<Usuario>();

	@XmlTransient
	@OneToMany(mappedBy = "grupoUsuario", targetEntity = GrupoUsuarioPermissao.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<GrupoUsuarioPermissao> grupoUsuarioPermissao = new ArrayList<GrupoUsuarioPermissao>();

	public GrupoUsuario(){
		super(GrupoUsuario.class);
	}
	
	public GrupoUsuario(Long id){
		super(GrupoUsuario.class);
		setId(id);
	}
	
	public GrupoUsuario(Long id, String codigo, String nome, boolean administrador){
		super(GrupoUsuario.class);
		setId(id);
		setCodigo(codigo);
		this.administrador = administrador;
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Boolean getAdministrado() {
		return administrador;
	}

	public void setAdministrado(Boolean administrador) {
		this.administrador = administrador;
	}

	public Collection<GrupoUsuarioPermissao> getGrupoUsuarioPermissao() {
		return grupoUsuarioPermissao;
	}

	public void setGrupoUsuarioPermissao(
			Collection<GrupoUsuarioPermissao> grupoUsuarioPermissao) {
		this.grupoUsuarioPermissao = grupoUsuarioPermissao;
	}
	
	public Collection<Usuario> getUsuario() {
		return usuario;
	}

	public void setUsuario(Collection<Usuario> usuario) {
		this.usuario = usuario;
	}

	public boolean altera(){
		try {
			return UsuarioDAO.alteraGrupoUsuario(this);
		} catch (DAOException e) {
			return false;
		}
	}
	
	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(nome, "Nome do grupo inválido");
		if(getCodigo().isEmpty()){
			setCodigo("PGRU"+countPorConsumerSecret(GrupoUsuario.class,getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int hashCode() {
		return getId().intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		GrupoUsuario other = (GrupoUsuario) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}
