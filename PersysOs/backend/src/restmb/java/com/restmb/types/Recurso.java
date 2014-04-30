package com.restmb.types;

import br.com.model.interfaces.IMedida;
import br.com.model.interfaces.IRecurso;

import com.restmb.RestMB;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 24/03/2013 RestMb para Recurso
 */
public class Recurso<T> extends RestMbType<T> implements IRecurso {

	public Recurso(String resourcePath, Class<T> paClass) {
		super(resourcePath, paClass);
	}

	public Recurso(Long id) {
		super();
		setId(id);
	}

	public Recurso() {
		super();
	}
	@RestMB
	private String type = "";
	@RestMB("nome")
	private String nome = "";
	@RestMB("marca")
	private String marca = "";
	@RestMB("modelo")
	private String modelo = "";
	@RestMB("etiqueta")
	private String etiqueta = "";
	@RestMB("descricao")
	private String descricao = "";
	@RestMB("tipoRecurso")
	private String tipoRecurso = "";
	@RestMB
	private Medida medida = new Medida();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String getMarca() {
		return marca;
	}

	@Override
	public void setMarca(String marca) {
		this.marca = marca;
	}

	@Override
	public String getModelo() {
		return modelo;
	}

	@Override
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	@Override
	public String getEtiqueta() {
		return etiqueta;
	}

	@Override
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	@Override
	public String getDescricao() {
		return descricao;
	}

	@Override
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String getTipoRecurso() {
		return tipoRecurso;
	}

	@Override
	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	@Override
	public void setMedida(IMedida medida) {
		this.medida = (Medida) medida;
	}

	@Override
	public Medida getMedida() {
		return this.medida;
	}
}
