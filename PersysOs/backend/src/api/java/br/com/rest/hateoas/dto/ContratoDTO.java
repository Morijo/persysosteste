package br.com.rest.hateoas.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.rest.represention.JsonDateAdapter;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class ContratoDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String codigo;
	private String descricaoObjeto;
	private String situacao;

	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date   dataAssinatura;
	
	private UnidadeDTO unidade = new UnidadeDTO();
	private ClienteDTO cliente = new ClienteDTO();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getDescricaoObjeto() {
		return descricaoObjeto;
	}
	public void setDescricaoObjeto(String descricaoObjeto) {
		this.descricaoObjeto = descricaoObjeto;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public Date getDataAssinatura() {
		return dataAssinatura;
	}
	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}
	
	public ClienteDTO getCliente() {
		return cliente;
	}
	
	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}
	
	public String getFantasia() {
		return cliente.getRazaoNome();
	}
	
	public void setFantasia(String fantasia) {
		cliente.setRazaoNome(fantasia);
	}
	
	public UnidadeDTO getUnidade() {
		return unidade;
	}
	public void setUnidade(UnidadeDTO unidade) {
		this.unidade = unidade;
	}
	@Override
	public String toString() {
		return "ContratoDTO [id=" + id + ", codigo=" + codigo
				+ ", descricaoObjeto=" + descricaoObjeto + ", situacao="
				+ situacao + ", dataAssinatura=" + dataAssinatura
				+ ", cliente=" + cliente + "]";
	}
	
}
