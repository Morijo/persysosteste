package br.com.model.interfaces;

import java.math.BigDecimal;

public interface IProduto extends IModel{

	public String getNome();

	public void setNome(String nome);

	public String getDescricao();

	public void setDescricao(String descricao);

	public String getMarca();

	public void setMarca(String marca);
	
	public String getCodigoBarra();

	public void setCodigoBarra(String codigoBarra);
	
	public BigDecimal getValor();

	public void setValor(BigDecimal valor);

}
