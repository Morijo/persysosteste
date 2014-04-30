package br.com.model.interfaces;

public interface INota extends IModel {

	public String getTitulo();
	public void setTitulo(String titulo);
	public String getNota();
	public void setNota(String nota);
	public IOrdem getOrdem();
	public void setOrdem(IOrdem ordem);

}
