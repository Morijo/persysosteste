package br.com.oauth.model;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.Session;
import com.sun.xml.bind.CycleRecoverable;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.oauth.dao.RequestTokenDAO;
import br.com.usuario.model.Usuario;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="requesttoken")
public class RequestToken implements Serializable , CycleRecoverable{


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id = null;
    private String secret = UUID.randomUUID().toString();
    private String token = UUID.randomUUID().toString();
    private String verificationCode;
    
    @OneToOne
	@JoinColumns({    
		@JoinColumn( name = "usuarioidpk", referencedColumnName="id"),
	})
	private Usuario usuario;

    
    public RequestToken() {}
    
    public RequestToken(String token, Long id) {
    	this.token = token;
    	this.usuario = new Usuario(id);
    }
  
    public String getToken() {
        return token;
    }
   
    public String getSecret() {
        return secret;
    }
    
    public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIdUsuario() {
		return usuario.getId();
	}

	public void setIdUsuario(Long id) {
		usuario = new Usuario(id);
	}
	
	public void createVerificationCode(){
		Random num = new Random();
		verificationCode = String.format("%4d",num.nextInt(1000)+1000);
	}
	
	public String toPercentEncodedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("oauth_token=" + token.toString() + "&");
        sb.append("oauth_token_secret=" + secret.toString()  + "&");
        sb.append("oauth_callback_confirmed=true");
        return sb.toString();
    }

	public boolean containsTokenFor(Usuario usuario) {
	      return true;
	}
	
	public static RequestToken retrieveToken(String token) {
		RequestTokenDAO requestTokenDAO = new RequestTokenDAO();
    	RequestToken requestToken = requestTokenDAO.pesquisaToken(token);
        if(requestToken != null) {
               return requestToken;
           }
       return null;    
	}
	
	public static RequestToken retrieveToken(Session session, String token) {
		RequestTokenDAO requestTokenDAO = new RequestTokenDAO();
    	RequestToken requestToken = requestTokenDAO.pesquisaToken(session, token);
        if(requestToken != null) {
               return requestToken;
           }
       return null;    
	}
	
	public static boolean containsToken(String token) {
		RequestTokenDAO requestTokenDAO = new RequestTokenDAO();
    	RequestToken requestToken = requestTokenDAO.pesquisaToken(token);
        if(requestToken != null) {
               return true;
           }
       return false;    
	}
	
	public static RequestToken pesquisaRequestToken(Session session, String token) {
		RequestTokenDAO requestTokenDAO = new RequestTokenDAO();
    	return requestTokenDAO.pesquisaRequestToken(session, token);
   }
	
	public static boolean remover(String token) {
		RequestTokenDAO requestTokenDAO = new RequestTokenDAO();
    	return requestTokenDAO.removerToken(token);
     }
	
	 public void salvar(){
	      	RequestTokenDAO requestDAO = new RequestTokenDAO();
	    	try {
	    		requestDAO.save(this);
			} catch (DAOException e) {
				e.printStackTrace();
			}
	    }

	 public void alterar(Session session) throws ModelException{
	      	RequestTokenDAO requestDAO = new RequestTokenDAO();
	    	try {
	    		requestDAO.alterar(session, this);
			} catch (DAOException e) {
				throw new ModelException(e.getMessage());
			}
	    }
	 
	 public void remover() throws ModelException{
	      	RequestTokenDAO requestDAO = new RequestTokenDAO();
	    	try {
	    		requestDAO.delete(this);
			} catch (DAOException e) {
				throw new ModelException(e.getMessage());
			}
	    }
	
	@Override
	public String toString() {
		return "RequestToken [id=" + id + ", secret=" + secret + ", token=" + token
				+ ", verificationCode=" + verificationCode + ", usuario="
				+ usuario + "]";
	}
	
	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}