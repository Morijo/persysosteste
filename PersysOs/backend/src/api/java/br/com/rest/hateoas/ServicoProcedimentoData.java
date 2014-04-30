package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.rest.represention.LinkData;
import br.com.servico.model.ServicoProcedimento;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ServicoProcedimentoData {
  
	@XmlElement(name = "data")
   private Collection<ServicoProcedimento> dados = new ArrayList<ServicoProcedimento>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public ServicoProcedimentoData() { }
   
   public ServicoProcedimentoData(Collection<ServicoProcedimento> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<ServicoProcedimento> getDados() {
		return dados;
	}
	
	public void setDados(Collection<ServicoProcedimento> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
