package com.restmb.types;

import java.math.BigDecimal;

import com.restmb.RestMB;

public class RecursoOrdem extends RestMbType<RecursoOrdem>{

	@RestMB
	private BigDecimal quantidadeConsumida  = new BigDecimal("0");
	
	@RestMB
	private String obs = ""; 
	
	@RestMB
	private Recurso<?> recurso;

	public RecursoOrdem(){
		super();
	}
	
	public BigDecimal getQuantidadeConsumida() {
		return quantidadeConsumida;
	}

	public void setQuantidadeConsumida(BigDecimal quantidadeConsumida) {
		this.quantidadeConsumida = quantidadeConsumida;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Recurso<?> getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso<?> recurso) {
		this.recurso = recurso;
	}
	
}
