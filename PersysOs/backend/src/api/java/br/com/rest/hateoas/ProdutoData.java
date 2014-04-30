package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.produto.model.Produto;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ProdutoData {
  
	@XmlElement(name = "data")
   private Collection<Produto> dados = new ArrayList<Produto>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public ProdutoData() { }
   
   public ProdutoData(Collection<Produto> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Produto> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Produto> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
