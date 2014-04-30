package br.com.rest.hateoas;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.funcionario.model.Funcionario;
import br.com.rest.represention.LinkData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class FuncionarioData{
  
   @XmlElement(name = "data")
   private Collection<Funcionario> dados;
   
   @XmlElement(name = "paging")
   private LinkData paging;
   
   public FuncionarioData() { }
   
   public FuncionarioData(Collection<Funcionario> dados) {
	      this.dados = dados;
	   }
   
   public FuncionarioData(Collection<Funcionario> dados, LinkData links) {
      this.dados = dados;
      this.paging = links;
   }
  
   	public Collection<Funcionario> getDados() {
		return dados;
	}
	
	public void setDados(Collection<Funcionario> dados) {
		this.dados = dados;
	}
	
	public LinkData getPaging() {
		return paging;
	}
	
	public void setPaging(LinkData paging) {
		this.paging = paging;;
	}
	   
   
}
