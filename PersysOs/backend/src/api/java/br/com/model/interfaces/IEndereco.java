package br.com.model.interfaces;

public interface IEndereco extends IModel {

	public String getLogradouro();
	public void setLogradouro(String logradouro);

	public String getNumero();
	public void setNumero(String numero);

	public String getComplemento();
	public void setComplemento(String complemento);

	public String getBairro();
	public void setBairro(String bairro);

	public String getCidade();
	public void setCidade(String cidade);

	public String getEstado();
	public void setEstado(String estado);

	public String getCep();
	public void setCep(String cep);
	
	public Double getLatitude();

	public void setLatitude(Double latitude);

	public Double getLongitude();

	public void setLongitude(Double longitude);

	public String getAltura();
	public void setAltura(String altura);
	
}
