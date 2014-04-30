package br.com.model.interfaces;

public interface IChip extends IModel {

	public Long getDddNumero();
	public void setDddNumero(Long dddNumero);
	public String getOperadora();
	public void setOperadora(String operadora) ;
	public Boolean getPrincipal() ;
	public void setPrincipal(Boolean principal);
	public Boolean getLigacao();
	public void setLigacao(Boolean ligacao);
}

