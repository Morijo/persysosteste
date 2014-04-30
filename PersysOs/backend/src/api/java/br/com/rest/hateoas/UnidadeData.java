package br.com.rest.hateoas;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.empresa.model.Unidade;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class UnidadeData{
  
   @XmlElement(name = "data")
   private Collection<Unidade> dados;
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public UnidadeData() { }
   
   public UnidadeData(Collection<Unidade> dados) {
	      this.dados = dados;
	   }
   
   public UnidadeData(Collection<Unidade> dados, LinkData links) {
      this.dados = dados;
      this.paging = links;
   }
  
  	public Collection<Unidade> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Unidade> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;;
	}
	   
   
}
