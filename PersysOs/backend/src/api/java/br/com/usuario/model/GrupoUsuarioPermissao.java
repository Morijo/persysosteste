package br.com.usuario.model;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.sun.xml.bind.CycleRecoverable;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.usuario.dao.UsuarioDAO;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="grupousuariopermissao")
public class GrupoUsuarioPermissao extends Model<GrupoUsuarioPermissao> implements Serializable , CycleRecoverable {

	private static final long serialVersionUID = 1L;

	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "grupousuarioidpk", referencedColumnName="id"),
		})
	private GrupoUsuario grupoUsuario = null;
	
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "permissaoidpk", referencedColumnName="id"),
		})
	private Permissao permissao = null;
	
	public GrupoUsuarioPermissao(){}
	
	public GrupoUsuarioPermissao(Long id, Long idPermissao, String nomePermissao, int statusModel){
		setId(id);
		setStatusModel(statusModel);
		this.permissao = new Permissao(idPermissao, "", nomePermissao);
	}
	
	public GrupoUsuarioPermissao(Long id, Long idPermissao, String nomePermissao, Long idGrupo, Boolean administrador, int statusModel){
		setId(id);
		setStatusModel(statusModel);
		this.permissao = new Permissao(idPermissao, "", nomePermissao);
		this.grupoUsuario = new GrupoUsuario(idGrupo,"","",administrador);
	}
	
	public GrupoUsuarioPermissao(GrupoUsuario grupoUsuario, Permissao permissao){
		super(GrupoUsuarioPermissao.class);
		this.grupoUsuario = grupoUsuario;
		this.permissao = permissao;
	}
	
	public static GrupoUsuarioPermissao createGrupoUsuaroPermissao(Long grupoUsuarioId, Long permissaoId, int status){
		GrupoUsuarioPermissao grupoUsuarioPermissao = 
				new GrupoUsuarioPermissao(new GrupoUsuario(grupoUsuarioId,"","",false), new Permissao(permissaoId,"",""));
		grupoUsuarioPermissao.setStatusModel(status);
		grupoUsuarioPermissao.setCodigo(grupoUsuarioId+"_"+permissaoId);
		return grupoUsuarioPermissao;
	}
	
	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return new GrupoUsuarioPermissao();  
	}
	
	public static ArrayList<GrupoUsuarioPermissao> listaPermissaoPorGrupo(Long idGrupo){
		return UsuarioDAO.pesquisaGrupoPermissao(idGrupo);
	}
	
	public boolean alteraPermissao(){
		try {
			return UsuarioDAO.alteraPermissao(this);
		} catch (DAOException e) {
			return false;
		}
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkNotNull(grupoUsuario, "Grupo Usuario inv��lido");
		PreconditionsModel.checkNotNull(permissao, "Grupo Usuario inv��lido");
		setCodigo(permissao.getId() +"_"+grupoUsuario.getId());
	}
}
