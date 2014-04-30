package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.recurso.model.Recurso;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class RecursoData {
  
	@XmlElement(name = "data")
   private Collection<Recurso> dados = new ArrayList<Recurso>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public RecursoData() { }
   
   public RecursoData(Collection<Recurso> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Recurso> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Recurso> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
