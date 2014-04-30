package br.com.rest.hateoas.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.rest.represention.JsonDateAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelDTO {

	private Long   id;
	
	private String codigo;
	
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataCriacao = null;
	
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAlteracao=  null;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataCriacao() {
		return dataCriacao;
	}


	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}


	public Date getDataAlteracao() {
		return dataAlteracao;
	}


	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
