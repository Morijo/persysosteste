package com.restmb.types;

import br.com.model.interfaces.IBaseConhecimento;

import com.restmb.RestMB;
import com.restmb.types.RestMbType;

public class BaseConhecimento extends RestMbType<BaseConhecimento> implements IBaseConhecimento {

	@RestMB
	private String titulo= ""; //alta baixa normal urgente
	
	@RestMB
	private String mensagem= "";

	@RestMB
	private String tipo= "";

	
	public BaseConhecimento(String resourcePath, Class<BaseConhecimento> paClass) {
		super(resourcePath, paClass);
	}


	public BaseConhecimento() {
		super("ordem/baseconhecimento", BaseConhecimento.class);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		BaseConhecimento other = (BaseConhecimento) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  titulo + " ("+tipo+")" ;
	}
	
}
