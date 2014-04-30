package br.com.model.interfaces;

public interface IContato extends IModel {

	public String getTipoContato();
	
	public void setTipoContato(String tipoContato);
	
	public String getNome();
	
	public void setNome(String nome);

	public IUsuario getUsuario();
	
	public void setUsuario(IUsuario usuario);

	public IEndereco getEndereco();

	public void setEndereco(IEndereco endereco);

	public String getEmail();

	public void setEmail(String email);

	public boolean isEmailEnviar();

	public void setEmailEnviar(boolean emailEnviar);
	
	public String getTelefoneFixo();

	public void setTelefoneFixo(String telefoneFixo);

	public String getTelefoneMovel();

	public void setTelefoneMovel(String telefoneMovel);
}
