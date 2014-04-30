package com.restmb.types;

import java.util.Date;

import com.restmb.DefaultJsonMapper;
import com.restmb.RestMB;
import com.restmb.RestMBClient;
import com.restmb.oauth.service.ParameterList;

public class Ponto {

	@RestMB("latitude")
	private Double latitude = null;
	
	@RestMB("longitude")
	private Double longitude = null;
	
	@RestMB("velocidade")
	private Double velocidade = null;
	
	@RestMB("altura")
	private String altura = null;

	@RestMB("dataRegistro")
	private Date  dataRegistro = null;

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

	public Double getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(Double velocidade) {
		this.velocidade = velocidade;
	}

	public String getAltura() {
		return altura;
	}

	public void setAltura(String altura) {
		this.altura = altura;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}
	
	public void salvar(RestMBClient client, Long id){
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		System.out.println(json.toJson(this));
		Ponto m = client.publishChanges("/evento/"+id,Ponto.class,json.toJson(this),headers);
		System.out.println(json.toJson(m));
	
	}

	@Override
	public String toString() {
		return "Ponto [latitude=" + latitude + ", longitude=" + longitude
				+ ", velocidade=" + velocidade + ", altura=" + altura
				+ ", dataRegistro=" + dataRegistro + "]";
	}
	
	
}
