package br.com.model.interfaces;

public interface IRecurso extends IModel {

	public String getMarca();

	public void setMarca(String marca);

	public String getModelo();

	public void setModelo(String modelo);

	public String getEtiqueta();

	public void setEtiqueta(String etiqueta);

	public String getDescricao();

	public void setDescricao(String descricao);

	public String getTipoRecurso();

	public void setTipoRecurso(String tipoRecurso);
	
	public String getNome();

	public void setNome(String nome);
	
	public void setMedida(IMedida medida);
	
	public IMedida getMedida();

}
