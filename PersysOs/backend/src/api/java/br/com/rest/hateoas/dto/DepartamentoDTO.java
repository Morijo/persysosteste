package br.com.rest.hateoas.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.empresa.model.Unidade;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class DepartamentoDTO extends ModelDTO {

	private UnidadeDTO unidade = new UnidadeDTO();
	
	private String nomeDepartamento = null;
	
	private String descricao = null; 

	private String responsavel = null; 
	
	public UnidadeDTO getUnidade() {
		return unidade;
	}
	
	public void setUnidade(UnidadeDTO unidade) {
		this.unidade = unidade;
	}
	
	public String getNomeDepartamento() {
		return nomeDepartamento;
	}
	
	public void setNomeDepartamento(String nomeDepartamento) {
		this.nomeDepartamento = nomeDepartamento;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}
	
	public void setUnidadeId(Long id) {
		this.unidade.setId(id);
	}
	
	
}

