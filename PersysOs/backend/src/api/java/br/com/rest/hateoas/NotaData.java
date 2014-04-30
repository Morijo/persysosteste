package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.ordem.model.Nota;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class NotaData {
  
	@XmlElement(name = "data")
   private Collection<Nota> dados = new ArrayList<Nota>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public NotaData() { }
   
   public NotaData(Collection<Nota> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<Nota> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Nota> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
