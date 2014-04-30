package com.restmb.types;

import com.restmb.RestMB;

public class ServicoOrdem extends RestMbType<ServicoOrdem> {

	@RestMB
	private String descricao;

	@RestMB
	private Servico servico;

	@RestMB("status")
	private String status = ""; 

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}	
