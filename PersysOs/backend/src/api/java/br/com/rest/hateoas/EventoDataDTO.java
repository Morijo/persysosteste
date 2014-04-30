package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.rest.hateoas.dto.EventoDTO;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class EventoDataDTO implements RESTEntity {
   
   @XmlElement(name = "data")
   private Collection<EventoDTO> dados;
   
   @XmlElement(name = "link")
   private Collection<LinkData> links;
   
   public EventoDataDTO() { }
   
   public EventoDataDTO(Collection<EventoDTO> dados) {
	      this.dados = dados;
	   }
   
   public EventoDataDTO(Collection<EventoDTO> dados, LinkData... links) {
      this.dados = dados;
      this.links = new ArrayList<LinkData>(Arrays.asList(links));
   }
   
   @Override
   public void adicionarLink(LinkData link) {
      if (links == null)
         links = new ArrayList<LinkData>();
      links.add(link);
   }
   

	public Collection<EventoDTO> getDados() {
		return dados;
	}
	
	public void setDados(Collection<EventoDTO> dados) {
		this.dados = dados;
	}
	
	public Collection<LinkData> getLinks() {
		return links;
	}
	
	public void setLinks(Collection<LinkData> links) {
		this.links = links;
	}
	   
   
}
