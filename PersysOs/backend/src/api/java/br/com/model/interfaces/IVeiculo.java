package br.com.model.interfaces;

import java.math.BigDecimal;

public interface IVeiculo extends IRecurso {

	public boolean isMotorizado();
	public void setMotorizado(boolean motorizado);
	public Long getHodometroInicial();
	public void setHodometroInicial(Long hodometroInicial);
	public BigDecimal getCapacidadeTanque();
	public void setCapacidadeTanque(BigDecimal capacidadeTanque);
	public BigDecimal getConsumoPadrao();
	public void setConsumoPadrao(BigDecimal consumoPadrao);
	public String getRenavam();
	public void setRenavam(String renavam);
	public String getCor();
	public void setCor(String cor);
	public String getPlaca();
	public void setPlaca(String placa);

}
