package br.com.rest.hateoas;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.cliente.model.Cliente;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ClienteData{
  
   @XmlElement(name = "data")
   private Collection<Cliente> dados;
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public ClienteData() { }
   
   public ClienteData(Collection<Cliente> dados) {
	      this.dados = dados;
	   }
   
   public ClienteData(Collection<Cliente> dados, LinkData links) {
      this.dados = dados;
      this.paging = links;
   }
  
   	public Collection<Cliente> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Cliente> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;;
	}
	   
   
}
