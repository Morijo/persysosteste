package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.rest.represention.LinkData;
import br.com.servico.model.Servico;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ServicoData {
  
	@XmlElement(name = "data")
   private Collection<Servico> dados = new ArrayList<Servico>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public ServicoData() { }
   
   public ServicoData(Collection<Servico> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Servico> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Servico> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
