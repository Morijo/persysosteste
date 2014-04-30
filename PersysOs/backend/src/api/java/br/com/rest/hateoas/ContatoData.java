package br.com.rest.hateoas;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.contato.model.Contato;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ContatoData{
   
   @XmlElement(name = "data")
   private Collection<Contato> dados;
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public ContatoData() { }
   
   public ContatoData(Collection<Contato> dados) {
	      this.dados = dados;
	   }
   
   public ContatoData(Collection<Contato> dados, LinkData links) {
      this.dados = dados;
      this.paging = links;
   }
  
	public Collection<Contato> getDados() {
		return dados;
	}
	
	public void setDados(Collection<br.com.contato.model.Contato> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;;
	}
	   
   
}
