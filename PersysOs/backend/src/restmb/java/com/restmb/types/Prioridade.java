package com.restmb.types;

import br.com.model.interfaces.IPrioridade;

import com.restmb.RestMB;
import com.restmb.types.RestMbType;

public class Prioridade extends RestMbType<Prioridade> implements IPrioridade {

	@RestMB
	private String prioridade= ""; //alta baixa normal urgente

	@RestMB
	private String cor ="";

	@RestMB
	private String  publico =""; //sim ou nao
	
	@RestMB
	private Integer prioridadeUrgencia;

	@RestMB
	private String descricao= "";

	
	public Prioridade(String resourcePath, Class<Prioridade> paClass) {
		super(resourcePath, paClass);
	}


	public Prioridade() {
		super("ordem/prioridade", Prioridade.class);
	}

	public String getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(String prioridade) {
		this.prioridade = prioridade;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getPublico() {
		return publico;
	}

	public void setPublico(String publico) {
		this.publico = publico;
	}

	public Integer getPrioridadeUrgencia() {
		return prioridadeUrgencia;
	}

	public void setPrioridadeUrgencia(Integer prioridadeUrgencia) {
		this.prioridadeUrgencia = prioridadeUrgencia;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return prioridade;
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
		Prioridade other = (Prioridade) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	
	
	
}
