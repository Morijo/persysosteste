package br.com.model.interfaces;

public interface IGrupoUsuario extends IModel{

	
	public String getNome();

	public void setNome(String nome);
	
	public Boolean getAdministrado();

	public void setAdministrado(Boolean administrador);
	
}
