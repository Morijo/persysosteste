package br.com.model.interfaces;

public interface IDispositivo extends IModel {
 
	public String getEtiqueta();
	public void setEtiqueta(String etiqueta);
	public String getMarca();
	public void setMarca(String marca);
	public String getModelo();
	public void setModelo(String modelo);
	public String getDescricao();
	public void setDescricao(String descricao);
	public String getNome();
	public void setNome(String nome);
	public String getIMEI();
	public void setIMEI(String iMEI);

}
