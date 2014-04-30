package com.restmb.types;

import java.math.BigDecimal;

import com.restmb.RestMB;

public class Servico extends RestMbType<Servico> implements br.com.model.interfaces.IServico{

	@RestMB
	private String titulo = ""; 
	
	@RestMB
	private String descricao = ""; 

	@RestMB
	private BigDecimal valorServico = new BigDecimal(0.0);;
	
	public Servico(){
		super("/servico", Servico.class);
	}
	
	public Servico(String resourcePath, Class<Servico> paClass) {
		super(resourcePath, paClass);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return titulo + " (Código=" + getCodigo() + ")";
	}

	@Override
	public BigDecimal getValorServico() {
		// TODO Auto-generated method stub
		return valorServico;
	}

	@Override
	public void setValorServico(BigDecimal valorServico) {
		this.valorServico = valorServico;
	}
	
	
}
