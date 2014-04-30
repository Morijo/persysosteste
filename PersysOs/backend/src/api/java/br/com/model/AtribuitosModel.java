package br.com.model;

public enum AtribuitosModel {

	ID("id"),
	CODIGO("codigo"),
	DATACRIACAO("dataCriacao"),
	DATAALTERACAO("dataAlteracao"),
	STATUSMODEL("statusModel");
	
	private String nomeAtributo;
	
	AtribuitosModel(String nomeAtributo){
		this.nomeAtributo = nomeAtributo;
	}
	
	public String getNomeAtributo(){
		return this.nomeAtributo;
	}
}
