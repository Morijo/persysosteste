package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.eventos.model.Alerta;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class AlertaData {
  
	@XmlElement(name = "data")
   private Collection<Alerta> dados = new ArrayList<Alerta>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public AlertaData() { }
   
   public AlertaData(Collection<Alerta> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Alerta> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Alerta> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
