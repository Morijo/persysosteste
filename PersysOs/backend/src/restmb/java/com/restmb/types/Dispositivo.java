package com.restmb.types;

import br.com.model.interfaces.IDispositivo;

import com.restmb.Connection;
import com.restmb.RestMB;
import com.restmb.RestMBClient;
/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 24/03/2013 RestMb para Dipositivo 
 */
public class Dispositivo extends RestMbType<Dispositivo> implements IDispositivo {

	@RestMB("etiqueta")
	private String etiqueta = "";
	@RestMB("marca")
	private String marca = "";
	@RestMB("modelo")
	private String modelo = "";
	@RestMB("descricao")
	private String descricao = "";
	@RestMB("nome")
	private String nome = "";
	@RestMB("principal")
	private Boolean principal = null;
	@RestMB("IMEI")
	private String IMEI = "";

	public Dispositivo(String resourcePath, Class<Dispositivo> paClass) {
		super(resourcePath, paClass);
	}
	
	public Dispositivo() {
		super("recurso/dispositivo", Dispositivo.class);
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
	
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Boolean getPrincipal() {
		return principal;
	}
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}
	
	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	@Override
	public String toString() {
		return nome + "[Cod =" + getCodigo() + "]";
	}

	public static Connection<Dispositivo> listaDispositivo(RestMBClient client) {
		return client.fetchConnection("/recurso/dispositivo", Dispositivo.class, "data");

	}	

}
