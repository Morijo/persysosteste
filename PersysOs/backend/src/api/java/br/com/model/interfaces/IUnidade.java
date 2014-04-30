package br.com.model.interfaces;

public interface IUnidade extends IModel{

	public String getNome();
	public void setNome(String nome);
	public String getEmail();
	public void setEmail(String email);
	public String getTelefone();
	public void setTelefone(String telefone);
	public String getRamal();
	public void setRamal(String ramal);
	public IEndereco getEndereco();
	public void setEndereco(IEndereco endereco);

}
