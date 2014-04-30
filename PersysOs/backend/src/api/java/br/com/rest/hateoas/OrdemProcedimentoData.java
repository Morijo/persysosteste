package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.ordem.model.OrdemProcedimento;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class OrdemProcedimentoData {
  
	@XmlElement(name = "data")
   private Collection<OrdemProcedimento> dados = new ArrayList<OrdemProcedimento>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public OrdemProcedimentoData() { }
   
   public OrdemProcedimentoData(Collection<OrdemProcedimento> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<OrdemProcedimento> getDados() {
		return dados;
	}
	
	public void setDados(Collection<OrdemProcedimento> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
