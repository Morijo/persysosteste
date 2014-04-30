package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.rest.hateoas.dto.AnexoDTO;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class AnexoDataDTO {
  
	@XmlElement(name = "data")
   private Collection<AnexoDTO> dados = new ArrayList<AnexoDTO>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public AnexoDataDTO() { }
   
   public AnexoDataDTO(Collection<AnexoDTO> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<AnexoDTO> getDados() {
		return dados;
	}
	
	public void setDados(Collection<AnexoDTO> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
