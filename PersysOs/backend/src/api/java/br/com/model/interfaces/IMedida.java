package br.com.model.interfaces;

public interface IMedida extends IModel{

	public String getObservacao();
	
	public String getNome();

	public void setNome(String nome);

	public void setObservacao(String observacao);
	
	public String getAbreviacao();

	public void setAbreviacao(String abreviacao);

}
