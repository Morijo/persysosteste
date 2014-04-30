package br.com.oauth.model;

import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.Session;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.interfaces.IAccessToken;
import br.com.oauth.dao.AccessTokenDAO;
import br.com.usuario.model.Usuario;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="accesstoken")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class AccessToken implements IAccessToken{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id = null;
	
    private String secret = null;
    private String token = null;
    
    public AccessToken(String oauthToken) {
        token = oauthToken;
    }
    
    public AccessToken(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }
    
    public AccessToken() {}
    
    public void geraTokens(){
       secret = UUID.randomUUID().toString();
       token = UUID.randomUUID().toString();
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
        return token.toString();
    }
    
    public String getSecret() {
        return secret.toString();
    }
    
    public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean equals(Object obj) {
        if(obj instanceof AccessToken) {
            AccessToken that = (AccessToken)obj;
            return String.valueOf(secret).equals(String.valueOf(that.secret)) && String.valueOf(token).equals(String.valueOf(that.token));
        }
        return false;
    }
    
    public int hashCode() {
        return secret.hashCode() + token.hashCode();
    }

    public String asURLEncodedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("oauth_token=");
        sb.append(token.toString());
        sb.append("&oauth_token_secret=");
        sb.append(secret.toString());
        return sb.toString();
    }
    
    
    public boolean containsTokenFor(Usuario usuario) {
    	
        return true;
    }
    
    public static boolean containsToken(Session session, String oauthToken) {
    	AccessTokenDAO accessTokenDAO = new AccessTokenDAO();
    	AccessToken accessToken = accessTokenDAO.pesquisaAccessToken(session, oauthToken);
         if(accessToken != null) {
                return true;
            }
        return false;
    }
    public static AccessToken pesquisaAccessToken(Session session, String token) {
    	AccessTokenDAO accessTokenDAO = new AccessTokenDAO();
    	return accessTokenDAO.pesquisaAccessToken(session, token);
    }
    
    public static AccessToken retrieveToken(String token) {
    	AccessTokenDAO accessTokenDAO = new AccessTokenDAO();
    	AccessToken accessToken = accessTokenDAO.pesquisaToken(token);
        if(accessToken != null) {
               return accessToken;
           }
       return null;
    }
    
    public static Usuario retrieveUsuario(String token) {
    	AccessTokenDAO accessTokenDAO = new AccessTokenDAO();
    	Usuario usuario = accessTokenDAO.pesquisaTokenUsuario(token);
        if(usuario != null) {
               return usuario;
           }
       return null;
    }
    
    public static Usuario retrieveAccessToken(Long id) throws ModelException{
    	AccessTokenDAO accessTokenDAO = new AccessTokenDAO();
    	Usuario usuario;
		try {
			usuario = accessTokenDAO.pesquisaAccessToken(id);
			if(usuario == null){
				throw new ModelException("Usuário sem token");
			}
		    return usuario;
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
    }
    
    public void salvar(){
      	AccessTokenDAO accessDAO = new AccessTokenDAO();
    	try {
    		accessDAO.save(this);
		} catch (DAOException e) {
			e.printStackTrace();
		}
    }
    
    public void salvar(Session session){
      	AccessTokenDAO accessDAO = new AccessTokenDAO();
    	try {
    		accessDAO.save(session,this);
		} catch (DAOException e) {
			e.printStackTrace();
		}
    }

	@Override
	public String toString() {
		return "AccessToken [id=" + id + ", secret=" + secret + ", token="
				+ token + "]";
	}
    
    
}
