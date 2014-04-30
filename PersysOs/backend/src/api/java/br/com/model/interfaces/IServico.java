package br.com.model.interfaces;

import java.math.BigDecimal;

public interface IServico extends IModel{

	public String getTitulo();
	
	public void setTitulo(String titulo);

	public String getDescricao();

	public void setDescricao(String descricao);
	
	public BigDecimal getValorServico();

	public void setValorServico(BigDecimal valorServico);

}
