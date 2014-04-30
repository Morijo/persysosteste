package br.com.usuario.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.exception.ModelException;
import br.com.model.interfaces.IUsuario;
import br.com.oauth.model.AccessToken;
import br.com.oauth.model.ConsumerSecret;
import br.com.usuario.dao.UsuarioDAO;

import com.sun.xml.bind.CycleRecoverable;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="aplicacaoagente ")
@PrimaryKeyJoinColumn(name="id")
public class AplicacaoAgente  extends Usuario implements IUsuario, Serializable , CycleRecoverable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String CONSTRUTOR = "id,nomeUsuario,consumerSecret.consumerKey, accessToken.secret, accessToken.token";
	
	public AplicacaoAgente(){}
	
	public AplicacaoAgente(Long id, String nomeUsuario, Long consumerKey,
			 String secret, String token){
		
		setId(id);
		setNomeUsuario(nomeUsuario);
		setConsumerSecret(new ConsumerSecret("", consumerKey));
		setAccessToken(new AccessToken(token, secret));
	}
	
	public void salva() throws ModelException{
		try {
			this.salvar();
			createTokenUser(this);
		} catch (ModelException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	public static ArrayList<AplicacaoAgente> listaAgentes(String consumerKey){
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.listaAplicacaoAgente(Long.parseLong(consumerKey));
	}
	
	public static AplicacaoAgente pesquisaAgente(Long id, String consumerKey){
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.pesquisaAplicacaoAgente(id, Long.parseLong(consumerKey));
	}
	
	@Override
	public void valida() throws ModelException {
		super.valida();
		if(getCodigo().isEmpty())
			setCodigo("PAGE"+countPorConsumerSecret(AplicacaoAgente.class,getConsumerSecret().getConsumerKey()));
	}

}
