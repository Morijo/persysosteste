package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.frota.model.Veiculo;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class VeiculoData {
  
	@XmlElement(name = "data")
   private Collection<Veiculo> dados = new ArrayList<Veiculo>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public VeiculoData() { }
   
   public VeiculoData(Collection<Veiculo> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Veiculo> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Veiculo> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
