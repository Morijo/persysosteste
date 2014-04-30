package br.com.rest.hateoas.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClienteDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String codigo;
	private String razaoNome;
	private String cnpjCpf = null;
	
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
	public String getRazaoNome() {
		return razaoNome;
	}
	public void setRazaoNome(String razaoNome) {
		this.razaoNome = razaoNome;
	}
	
	public String getCnpjCpf() {
		return cnpjCpf;
	}
	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}
	@Override
	public String toString() {
		return "ContratoDTO [id=" + id + ", codigo=" + codigo + ", fantasia="
				+ razaoNome + "]";
	}
	
	
	
}
