package com.restmb.types;

import br.com.model.interfaces.IMedida;

import com.restmb.Connection;
import com.restmb.RestMB;
import com.restmb.RestMBClient;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 24/03/2013 RestMb para Medida
 */

public class Medida extends RestMbType<Medida> implements IMedida {

	
	@RestMB("nome")
	private String nome = "";
	@RestMB("observacao")
	private String observacao = "";
	@RestMB("abreviacao")
	private String abreviacao = "";
	public Medida(String resourcePath) {
		super(resourcePath, Medida.class);
	}
	
	public Medida() {
		super("/medida", Medida.class);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getAbreviacao() {
		return abreviacao;
	}

	public void setAbreviacao(String abreviacao) {
		this.abreviacao = abreviacao;
	}

	
	@Override
	public String toString() {
		return nome + " (" + abreviacao + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Medida other = (Medida) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}


	public static Connection<Medida> listaMedida(RestMBClient client) {
		return client.fetchConnection("/medida", Medida.class, "data");
	}
}
