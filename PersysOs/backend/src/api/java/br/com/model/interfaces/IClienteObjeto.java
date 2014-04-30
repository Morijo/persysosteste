package br.com.model.interfaces;

import java.math.BigDecimal;

public interface IClienteObjeto extends IModel {

	
	public ICliente getCliente();
	
	public void setCliente(ICliente cliente);
	
	public String getDescricaoObjeto();
	
	public void setDescricaoObjeto(String descricaoObjeto);
	
	public BigDecimal getValorTotal();
	
	public void setValorTotal(String valorTotal);

	public void setValorTotal(BigDecimal valorTotal);
	
	public BigDecimal getValorMensal();
	
	public void setValorMensal(String valorMensal);
	
	public void setValorMensal(BigDecimal valorMensal);
	
	public String getSituacao();

	public void setSituacao(String situacao);

}
