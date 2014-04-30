package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.recurso.model.Material;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class MaterialData {
  
   @XmlElement(name = "data")
   private Collection<Material> dados = new ArrayList<Material>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public MaterialData() { }
   
   public MaterialData(Collection<Material> dados, LinkData links) {
	  if(dados != null) 
           this.dados = dados;
	  if(links != null){
		  this.paging = links;
	  }
   }
  
	public Collection<Material> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Material> dados) {
		this.dados = dados;
	}

	public LinkData getPaging() {
		return paging;
	}

	public void setNext(LinkData paging) {
		this.paging = paging;
	}
	
}
