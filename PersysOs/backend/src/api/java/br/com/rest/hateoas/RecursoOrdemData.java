package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.ordem.model.RecursoOrdem;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class RecursoOrdemData {
  
	@XmlElement(name = "data")
   private Collection<RecursoOrdem> dados = new ArrayList<RecursoOrdem>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public RecursoOrdemData() { }
   
   public RecursoOrdemData(Collection<RecursoOrdem> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<RecursoOrdem> getDados() {
		return dados;
	}
	
	public void setDados(Collection<RecursoOrdem> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
