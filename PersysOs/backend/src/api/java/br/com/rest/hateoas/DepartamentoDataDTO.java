package br.com.rest.hateoas;


import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.rest.hateoas.dto.DepartamentoDTO;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class DepartamentoDataDTO{
   
   @XmlElement(name = "data")
   private Collection<DepartamentoDTO> dados;
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public DepartamentoDataDTO() { }
   
   public DepartamentoDataDTO(Collection<DepartamentoDTO> dados) {
	      this.dados = dados;
	   }
   
   public DepartamentoDataDTO(Collection<DepartamentoDTO> dados, LinkData links) {
	  this.dados = dados;
	  this.paging = links;
   }
  

	public Collection<DepartamentoDTO> getDados() {
		return dados;
	}
	
	public void setDados(Collection<DepartamentoDTO> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;;
	}
	   
   
}
