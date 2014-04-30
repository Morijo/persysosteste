package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.ordem.model.ServicoOrdem;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ServicoOrdemData {
  
   @XmlElement(name = "data")
   private Collection<ServicoOrdem> dados = new ArrayList<ServicoOrdem>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public ServicoOrdemData() { }
   
   public ServicoOrdemData(Collection<ServicoOrdem> dados, LinkData links) {
	  if(dados != null) 
           this.dados = dados;
	  if(links != null){
		  this.paging = links;
	  }
   }
  
	public Collection<ServicoOrdem> getDados() {
		return dados;
	}
	
	public void setDados(Collection<ServicoOrdem> dados) {
		this.dados = dados;
	}

	public LinkData getPaging() {
		return paging;
	}

	public void setNext(LinkData paging) {
		this.paging = paging;
	}
	
}
