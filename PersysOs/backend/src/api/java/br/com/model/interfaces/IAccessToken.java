package br.com.model.interfaces;

public interface IAccessToken {

	public Long getId();

	public void setId(Long id);

	public String getToken();

	public String getSecret();

	public void setSecret(String secret);

	public void setToken(String token);
}
