package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.recurso.model.Chip;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ChipData {
  
   @XmlElement(name = "data")
   private Collection<Chip> dados = new ArrayList<Chip>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public ChipData() { }
   
   public ChipData(Collection<Chip> dados, LinkData links) {
	  if(dados != null) 
           this.dados = dados;
	  if(links != null){
		  this.paging = links;
	  }
   }
  
	public Collection<Chip> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Chip> dados) {
		this.dados = dados;
	}

	public LinkData getPaging() {
		return paging;
	}

	public void setNext(LinkData paging) {
		this.paging = paging;
	}
	
}
