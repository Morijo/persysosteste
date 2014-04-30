
package com.restmb.types;

import br.com.model.interfaces.IEquipamento;

import com.restmb.Connection;
import com.restmb.RestMB;
import com.restmb.RestMBClient;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 24/03/2013 RestMb para Equipamento
 */

public class Equipamento extends RestMbType<Equipamento> implements IEquipamento {
  
	@RestMB("equipamento")
	private String equipamento ="";
	@RestMB("etiqueta")
	private String etiqueta = "";
	@RestMB("numeroSerie")
	private String numeroSerie = "";
    @RestMB("marca")
	private String marca = "";
	@RestMB("modelo")
	private String modelo ="";
	@RestMB("descricao")
	private String descricao = "";
  
	public Equipamento(String resourcePath, Class<Equipamento> paClass) {
		super(resourcePath, paClass);
	}
	
	public Equipamento() {
		super("recurso/equipamento", Equipamento.class);
	}
	
	
	public String getEquipamento() {
		return equipamento;
	}
	
	public void setEquipamento(String equipamento) {
		this.equipamento = equipamento;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
	
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
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

	@Override
	public String toString() {
		return equipamento + "[Cod =" + getCodigo() + "]";
	}

	public static Connection<Equipamento> listaEquipamento(RestMBClient client) {
		return client.fetchConnection("/recurso/equipamento", Equipamento.class, "data");

	}	
	
}