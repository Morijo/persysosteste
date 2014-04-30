package br.com.model.interfaces;

import java.util.Date;

public interface IModel {

	public Long getId();

	public void setId(Long id);

	public String getCodigo();

	public void setCodigo(String codigo);

	public Date getDataCriacao();

	public void setDataCriacao(Date dataCriacao);

	public Date getDataAlteracao();

	public void setDataAlteracao(Date dataAlteracao);

	public Integer getStatusModel();

	public void setStatusModel(Integer statusModel);
	
	public Boolean getPermitidoExcluir();

	public void setPermitidoExcluir(Boolean permitidoExcluir);

	public Boolean getPermitidoAlterar();

	public void setPermitidoAlterar(Boolean permitidoAlterar);
}
