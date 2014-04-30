package br.com.model.interfaces;


public interface IAnexo extends IModel {

	public String getDescricao();
	public void setDescricao(String descricao);

	public String getCaminho();
	public void setCaminho(String caminho);

	public int getTamanho();
	public void setTamanho(int tamanho);

	public IOrdem getOrdem();
	public void setOrdem(IOrdem ordem);
}
