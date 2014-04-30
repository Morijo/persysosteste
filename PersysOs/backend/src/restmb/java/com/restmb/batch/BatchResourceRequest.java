package com.restmb.batch;

import java.util.ArrayList;
import java.util.Collection;

import com.restmb.RestMB;

public class BatchResourceRequest {

	@SuppressWarnings("rawtypes")
	@RestMB("data")
	private Collection<?> dados = new ArrayList();

	public BatchResourceRequest() {
	}

	public BatchResourceRequest(Collection<?> dados) {
		if(dados != null) 
			this.dados = dados;
	}

	public Collection<?> getDados() {
		return dados;
	}

	public void setDados(Collection<?> dados) {
		this.dados = dados;
	}
}
