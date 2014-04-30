package com.restmb.types;
import com.restmb.RestMB;
import com.restmb.RestMBClient;

public class Endereco extends RestMbType<Endereco> implements br.com.model.interfaces.IEndereco {

	public Endereco(String resourcePath) {
		super(resourcePath, Endereco.class);
	}

	public Endereco() {
		super("",Endereco.class);
	}

	@RestMB("tipo")
	private String tipo = "";

	@RestMB("logradouro")
	private String logradouro =""; 

	@RestMB("numero")
	private String numero = ""; 

	@RestMB("complemento")
	private String complemento =""; 

	@RestMB("bairro")
	private String bairro = ""; 

	@RestMB("cidade")
	private String cidade= ""; 

	@RestMB("estado")
	private String estado = ""; 

	@RestMB("cep")
	private String cep = "";

	@RestMB("obs")
	private String obs =""; 

	@RestMB("latitude")
	private Double latitude = null;

	@RestMB("longitude")
	private Double longitude = null;

	@RestMB("altura")
	private String altura="";

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAltura() {
		return altura;
	}

	public void setAltura(String altura) {
		this.altura = altura;
	}

	@Override
	public String toString() {
		return  (logradouro
				+ ", " + numero + ", " + complemento
				+ ", " + bairro + ", " + cidade + ", "
				+ estado + ", " + cep).replace(", ,", ", ");
	}

	public Endereco getEnderecoOrganizacaoHome(RestMBClient client) {
		return client.fetchObject(resourcePath, Endereco.class);
	}

}
