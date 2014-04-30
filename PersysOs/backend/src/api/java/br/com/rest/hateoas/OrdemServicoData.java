package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.ordem.model.Ordem;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class OrdemServicoData {
  
	@XmlElement(name = "data")
   private Collection<Ordem> dados = new ArrayList<Ordem>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public OrdemServicoData() { }
   
   public OrdemServicoData(Collection<Ordem> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Ordem> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Ordem> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
