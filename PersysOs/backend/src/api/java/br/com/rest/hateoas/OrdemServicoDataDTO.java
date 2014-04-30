package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.rest.hateoas.dto.OrdemDTO;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class OrdemServicoDataDTO {
  
	@XmlElement(name = "data")
   private Collection<OrdemDTO> dados = new ArrayList<OrdemDTO>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public OrdemServicoDataDTO() { }
   
   public OrdemServicoDataDTO(Collection<OrdemDTO> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<OrdemDTO> getDados() {
		return dados;
	}
	
	public void setDados(Collection<OrdemDTO> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
