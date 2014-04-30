package br.com.oauth.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.Session;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.sun.xml.bind.CycleRecoverable;
import br.com.exception.DAOException;
import br.com.oauth.dao.ConsumerSecretDAO;


@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="consumersecret")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConsumerSecret implements Serializable , CycleRecoverable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "consumerkey")
	private Long consumerKey;
	
	@Column(name = "consumersecret", unique=true)
	private String consumerSecret;
	
	public ConsumerSecret() {}
	
	
	public ConsumerSecret(String consumerSecret, Long consumerKey) {
	   this.consumerSecret = consumerSecret;
	   if(consumerKey == null) {
           throw new IllegalArgumentException("ConsumerKey cannot be null.");
       }
       this.consumerKey = consumerKey;
	   
	  }

	 public boolean equals(Object obj) {
	        if(obj instanceof ConsumerSecret) {
	            ConsumerSecret secret = (ConsumerSecret)obj;
	            return this.consumerSecret.equals(secret.consumerSecret);
	        }
	        return false;
	   }
	    
	 public int hashCodeConsumerSecret() {
	        return consumerSecret.hashCode();
	  }
	    
	 @Override
	public String toString() {
		return "ConsumerSecret [consumerSecret=" + consumerSecret
				+ ", consumerKey=" + consumerKey + "]";
	}
	    
	 public int hashCodeConsumerKey() {
	        return consumerKey != null ? consumerKey.hashCode() : 0;
	 }
	 
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	
	public String getConsumerSecret() {
		return consumerSecret;
	}

	public Long getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(Long consumerKey) {
		this.consumerKey = consumerKey;
	}

	public static ConsumerSecret pesquisaConsumerSecret(Session session, String token) {
		ConsumerSecretDAO consumerSecretDAO = new ConsumerSecretDAO();
		return consumerSecretDAO.pesquisaConsumerSecret(session,Long.parseLong(token));
	}
	
	public static ConsumerSecret retrieve(Session session, String consumerKey) {
		ConsumerSecretDAO consumerSecretDAO = new ConsumerSecretDAO();
		return consumerSecretDAO.pesquisaConsumerKey(session,Long.parseLong(consumerKey));
	}
        
    public static ConsumerSecret containsKey(Session session, String consumerKey) {
    	ConsumerSecretDAO consumerSecretDAO = new ConsumerSecretDAO();
		ConsumerSecret consumerSecret = consumerSecretDAO.pesquisaConsumerKey(session, Long.parseLong(consumerKey));
		return consumerSecret;
    }
    
    public void salvar(){
      	ConsumerSecretDAO consumerSecretDAO = new ConsumerSecretDAO();
    	try {
			consumerSecretDAO.save(this);
		} catch (DAOException e) {
			e.printStackTrace();
		}
    }
    
	@Override
	public Object onCycleDetected(Context arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
