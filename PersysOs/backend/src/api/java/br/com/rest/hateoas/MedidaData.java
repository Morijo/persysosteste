package br.com.rest.hateoas;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.recurso.model.Medida;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class MedidaData {
  
	@XmlElement(name = "data")
   private Collection<Medida> dados = new ArrayList<Medida>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public MedidaData() { }
   
   public MedidaData(Collection<Medida> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Medida> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Medida> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
