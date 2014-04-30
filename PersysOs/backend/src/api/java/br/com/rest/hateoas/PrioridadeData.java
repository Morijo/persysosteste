package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.ordem.model.Prioridade;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class PrioridadeData {
  
	@XmlElement(name = "data")
   private Collection<Prioridade> dados = new ArrayList<Prioridade>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public PrioridadeData() { }
   
   public PrioridadeData(Collection<Prioridade> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Prioridade> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Prioridade> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
