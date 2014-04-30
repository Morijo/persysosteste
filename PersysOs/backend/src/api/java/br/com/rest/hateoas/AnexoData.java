package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.ordem.model.Anexo;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class AnexoData {
  
	@XmlElement(name = "data")
   private Collection<Anexo> dados = new ArrayList<Anexo>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public AnexoData() { }
   
   public AnexoData(Collection<Anexo> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Anexo> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Anexo> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
