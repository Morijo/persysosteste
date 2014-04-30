package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.eventos.model.TipoEvento;
import br.com.rest.represention.LinkData;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class TipoEventoData {
  
	@XmlElement(name = "data")
   private Collection<TipoEvento> dados = new ArrayList<TipoEvento>();
   
   @XmlElement(name = "paging")
   private LinkData paging;
    
   public TipoEventoData() { }
   
   public TipoEventoData(Collection<TipoEvento> dados, LinkData paging) {
	  if(dados != null) 
           this.dados = dados;
      this.paging = paging;
   }
  
	public Collection<TipoEvento> getDados() {
		return dados;
	}
	
	public void setDados(Collection<TipoEvento> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;
	}
	   
   
}
