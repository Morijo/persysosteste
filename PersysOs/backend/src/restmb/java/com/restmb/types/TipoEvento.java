package com.restmb.types;

import com.restmb.RestMB;

public class TipoEvento extends RestMbType<TipoEvento>{

	@RestMB
	private String titulo;
	
	@RestMB
	private String observacao;

	public TipoEvento(){
		super("/tipoevento", TipoEvento.class);
	}
	
	public TipoEvento(String resourcePath, Class<TipoEvento> paClass) {
		super(resourcePath, paClass);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
