package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.recurso.model.Equipamento;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class EquipamentoData {
   
   @XmlElement(name = "data")
   private Collection<Equipamento> dados = new ArrayList<Equipamento>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public EquipamentoData() { }
   
   public EquipamentoData(Collection<Equipamento> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
   
	public Collection<Equipamento> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Equipamento> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setLinks(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
