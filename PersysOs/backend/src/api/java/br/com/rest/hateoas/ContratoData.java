package br.com.rest.hateoas;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.clienteobjeto.model.Contrato;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ContratoData {
  
   @XmlElement(name = "data")
   private Collection<Contrato> dados = new ArrayList<Contrato>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public ContratoData() { }
   
   public ContratoData(Collection<Contrato> dados, LinkData links) {
	  if(dados != null) 
           this.dados = dados;
	  if(links != null){
		  this.paging = links;
	  }
   }
  
	public Collection<Contrato> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Contrato> dados) {
		this.dados = dados;
	}

	public LinkData getPaging() {
		return paging;
	}

	public void setNext(LinkData paging) {
		this.paging = paging;
	}
	
}
