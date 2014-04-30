package br.com.model.interfaces;

public interface IDepartamento extends IModel{

	public String getNomeDepartamento();

	public void setNomeDepartamento(String nomeDepartamento);

	public String getDescricao();

	public void setDescricao(String descricao);

	public String getTipo();

	public void setTipo(String tipo);

	public String getEmail();

	public void setEmail(String email);

	public String getResponsavel();

	public void setResponsavel(String responsavel);

	public IUnidade getUnidade();

	public void setUnidade(IUnidade unidade);
	
	public String getTelefone();

	public void setTelefone(String telefone);

	public String getRamal();

	public void setRamal(String ramal);
}
