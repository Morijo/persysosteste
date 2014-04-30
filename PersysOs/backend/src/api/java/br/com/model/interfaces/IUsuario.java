package br.com.model.interfaces;

public interface IUsuario extends IModel {

	public String getNomeUsuario();

	public void setNomeUsuario(String nomeUsuario);

	public String getRazaoNome();

	public void setRazaoNome(String razaoNome);

	public String getFantasiaSobrenome();

	public void setFantasiaSobrenome(String fantasiaSobrenome);

	public String getCnpjCpf();
	
	public void setCnpjCpf(String cnpjCpf);

	public String getIeRg();

	public void setIeRg(String ieRg);
	
	public String getEmailPrincipal();

	public void setEmailPrincipal(String emailPrincipal);

	public String getNota();

	public void setNota(String nota);

	public String getDashboardNome();

	public void setDashboardNome(String dashboardNome);

	public String getTimezone();

	public void setTimezone(String timezone);

	public String getLocale();

	public void setLocale(String locale);
	
	public br.com.model.interfaces.IGrupoUsuario getGrupoUsuario();

	public void setGrupoUsuario(br.com.model.interfaces.IGrupoUsuario grupoUsuario);
	
	public void setSenha(String senha);
	
	public IAccessToken getAccessToken();

	public void setAccessToken(IAccessToken accessToken);

}
