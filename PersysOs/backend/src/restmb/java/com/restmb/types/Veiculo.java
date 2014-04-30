package com.restmb.types;

import java.math.BigDecimal;

import com.restmb.RestMB;

import br.com.model.interfaces.IVeiculo;

public class Veiculo extends Recurso<Veiculo> implements IVeiculo {

	@RestMB
	private String placa="";

	@RestMB
	private String renavam="";

	@RestMB
	private String cor="";

	@RestMB
	private boolean motorizado = false;

	@RestMB
	private Long hodometroInicial= 0l;

	@RestMB
	private BigDecimal capacidadeTanque;

	@RestMB
	private BigDecimal consumoPadrao;

	public Veiculo(String resourcePath, Class<Veiculo> paClass) {
		super(resourcePath, paClass);
	}

	public Veiculo(){
		super("/recurso/veiculo", Veiculo.class);
	}

	public Veiculo(Long id, String codigo, String nome, String placa) {
		super("/recurso/veiculo", Veiculo.class);
		setId(id);
		setCodigo(codigo);
		setNome(nome);
		this.placa = placa;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public boolean isMotorizado() {
		return motorizado;
	}

	public void setMotorizado(boolean motorizado) {
		this.motorizado = motorizado;
	}

	public Long getHodometroInicial() {
		return hodometroInicial;
	}

	public void setHodometroInicial(Long hodometroInicial) {
		this.hodometroInicial = hodometroInicial;
	}

	public BigDecimal getCapacidadeTanque() {
		return capacidadeTanque;
	}

	public void setCapacidadeTanque(BigDecimal capacidadeTanque) {
		this.capacidadeTanque = capacidadeTanque;
	}

	public BigDecimal getConsumoPadrao() {
		return consumoPadrao;
	}

	public void setConsumoPadrao(BigDecimal consumoPadrao) {
		this.consumoPadrao = consumoPadrao;
	}

	public String getRenavam() {
		return renavam;
	}

	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}
}
