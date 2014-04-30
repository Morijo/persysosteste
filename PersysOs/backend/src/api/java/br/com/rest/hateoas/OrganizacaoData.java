package br.com.rest.hateoas;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.empresa.model.Organizacao;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class OrganizacaoData{
   
   @XmlElement(name = "data")
   private Collection<Organizacao> dados;
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public OrganizacaoData() { }
   
   public OrganizacaoData(Collection<Organizacao> dados) {
	      this.dados = dados;
	   }
   
   public OrganizacaoData(Collection<Organizacao> dados, LinkData links) {
	  this.dados = dados;
	  this.paging = links;
   }
  

	public Collection<Organizacao> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Organizacao> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;;
	}
	   
   
}
