package br.com.model.interfaces;

public interface IPrioridade extends IModel {

	public String getPrioridade();

	public void setPrioridade(String prioridade);

	public String getCor();

	public void setCor(String cor);

	public String getPublico();

	public void setPublico(String publico);

	public Integer getPrioridadeUrgencia();

	public void setPrioridadeUrgencia(Integer prioridadeUrgencia);

	public String getDescricao() ;

	public void setDescricao(String descricao);
}
