package br.com.model.interfaces;

public interface IOrganizacao extends IModel {

	public String getRazaoSocial();
  	public void setRazaoSocial(String razaoSocial);

  	public String getNomeFantasia();
	public void setNomeFantasia(String nomeFantasia);

	public String getCnpj();
	public void setCnpj(String cnpj);

	public String getInscricaoEstadual();
	public void setInscricaoEstadual(String inscricaoEstadual);
	
	public String getInscricaoMunicipal();
	public void setInscricaoMunicipal(String inscricaoMunicipal);

	public String getCnaeFiscal();
	public void setCnaeFiscal(String cnaeFiscal);

	public String getInscricaoEstadualSubstTributario();
	public void setInscricaoEstadualSubstTributario(String inscricaoEstadualSubstTributario);

	public String getRegimeTributario();
	public void setRegimeTributario(String regimeTributario);

	public String getEmail();
	public void setEmail(String email);
	
	public String getTelefone();

	public void setTelefone(String telefone);
	
}
