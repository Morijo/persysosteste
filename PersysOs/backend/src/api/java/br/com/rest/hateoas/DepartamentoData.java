package br.com.rest.hateoas;


import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.empresa.model.Departamento;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class DepartamentoData{
   
   @XmlElement(name = "data")
   private Collection<Departamento> dados;
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public DepartamentoData() { }
   
   public DepartamentoData(Collection<Departamento> dados) {
	      this.dados = dados;
	   }
   
   public DepartamentoData(Collection<Departamento> dados, LinkData links) {
	  this.dados = dados;
	  this.paging = links;
   }
  

	public Collection<Departamento> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Departamento> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;;
	}
	   
   
}
