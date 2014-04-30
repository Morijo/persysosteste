package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.rest.hateoas.dto.AgendaOrdemFuncionarioDTO;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class AgendaOrdemFuncionarioDataDTO implements RESTEntity {
   
   @XmlElement(name = "data")
   private Collection<AgendaOrdemFuncionarioDTO> dados;
   
   @XmlElement(name = "link")
   private Collection<LinkData> links;
   
   public AgendaOrdemFuncionarioDataDTO() { }
   
   public AgendaOrdemFuncionarioDataDTO(Collection<AgendaOrdemFuncionarioDTO> dados) {
	      this.dados = dados;
	   }
   
   public AgendaOrdemFuncionarioDataDTO(Collection<AgendaOrdemFuncionarioDTO> dados, LinkData... links) {
      this.dados = dados;
      this.links = new ArrayList<LinkData>(Arrays.asList(links));
   }
   
   @Override
   public void adicionarLink(LinkData link) {
      if (links == null)
         links = new ArrayList<LinkData>();
      links.add(link);
   }
   

	public Collection<AgendaOrdemFuncionarioDTO> getDados() {
		return dados;
	}
	
	public void setDados(Collection<AgendaOrdemFuncionarioDTO> dados) {
		this.dados = dados;
	}
	
	public Collection<LinkData> getLinks() {
		return links;
	}
	
	public void setLinks(Collection<LinkData> links) {
		this.links = links;
	}
	   
   
}
