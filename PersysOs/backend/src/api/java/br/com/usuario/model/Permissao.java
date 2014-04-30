package br.com.usuario.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;


@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="permissao")
public class Permissao extends Model<Permissao>  implements Serializable , CycleRecoverable {

	private static final long serialVersionUID = 1L;

	@Transient
	@XmlTransient
	public static final String construtor = "id,codigo,nome";
	
	@Column(name = "nome", length = 100)
	private String nome = null;
	
	@XmlTransient
	@OneToMany(mappedBy = "permissao", targetEntity = GrupoUsuarioPermissao.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<GrupoUsuarioPermissao> grupoUsuarioPermissao = new ArrayList<GrupoUsuarioPermissao>();
	
	public Permissao(){
		super(Permissao.class);
	}
	
	public Permissao(Long id, String codigo, String nome){
		super(Permissao.class);
		setId(id);
		setCodigo(codigo);
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Collection<GrupoUsuarioPermissao> getGrupoUsuarioPermissao() {
		return grupoUsuarioPermissao;
	}

	public void setGrupoUsuarioPermissao(
			Collection<GrupoUsuarioPermissao> grupoUsuarioPermissao) {
		this.grupoUsuarioPermissao = grupoUsuarioPermissao;
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(nome, "Permissao inv��lido");
		if(getCodigo().isEmpty()){
			setCodigo("PPER"+countPorConsumerSecret(Permissao.class,getConsumerSecret().getConsumerKey()));
		}
	}
}
