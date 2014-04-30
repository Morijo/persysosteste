package com.restmb.types;

import br.com.model.interfaces.ISituacaoOrdem;

import com.restmb.RestMB;
import com.restmb.types.RestMbType;

public class SituacaoOrdem extends RestMbType<SituacaoOrdem> implements ISituacaoOrdem {

	@RestMB
	private String nome= ""; //alta baixa normal urgente

	@RestMB
	private String  publico =""; //sim ou nao
	
	@RestMB
	private String descricao= "";

	

	public SituacaoOrdem() {
		super("ordem/situacao", SituacaoOrdem.class);
	}

	public SituacaoOrdem(String resourcePath, Class<SituacaoOrdem> paClass) {
		super(resourcePath, paClass);
		}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPublico() {
		return publico;
	}

	public void setPublico(String publico) {
		this.publico = publico;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getId() == null) ? 0 : getId().hashCode());
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
		SituacaoOrdem other = (SituacaoOrdem) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  nome ;
	}
	
	
}
