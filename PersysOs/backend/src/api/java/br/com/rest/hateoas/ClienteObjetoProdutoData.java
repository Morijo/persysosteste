package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.clienteobjeto.model.ClienteObjetoProduto;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ClienteObjetoProdutoData {
  
   @XmlElement(name = "data")
   private Collection<ClienteObjetoProduto> dados = new ArrayList<ClienteObjetoProduto>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public ClienteObjetoProdutoData() { }
   
   public ClienteObjetoProdutoData(Collection<ClienteObjetoProduto> dados, LinkData links) {
	  if(dados != null) 
           this.dados = dados;
	  if(links != null){
		  this.paging = links;
	  }
   }
  
	public Collection<ClienteObjetoProduto> getDados() {
		return dados;
	}
	
	public void setDados(Collection<ClienteObjetoProduto> dados) {
		this.dados = dados;
	}

	public LinkData getPaging() {
		return paging;
	}

	public void setNext(LinkData paging) {
		this.paging = paging;
	}
	
}
