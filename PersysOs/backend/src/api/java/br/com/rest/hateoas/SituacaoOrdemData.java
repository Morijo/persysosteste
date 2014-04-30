package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.ordem.model.SituacaoOrdem;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class SituacaoOrdemData {
  
	@XmlElement(name = "data")
   private Collection<SituacaoOrdem> dados = new ArrayList<SituacaoOrdem>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public SituacaoOrdemData() { }
   
   public SituacaoOrdemData(Collection<SituacaoOrdem> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<SituacaoOrdem> getDados() {
		return dados;
	}
	
	public void setDados(Collection<SituacaoOrdem> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
