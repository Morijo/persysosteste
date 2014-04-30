package com.restmb.types;

import com.restmb.RestMBClient;
import com.restmb.exception.RestMBSignedRequestVerificationException;
import com.restmb.oauth.service.Token;
import com.restmb.util.Preconditions;

public class Login {

	protected Token token;
	protected RestMBClient client;
	
	public Login(RestMBClient client){
		this.client = client;
	}
	
	protected void requestToken(){
		
		Token token = client.obtainAppRequestToken(client.getOauth(), "requestToken");
		client.getOauth().setTokenKey(token.getToken());
		client.getOauth().setTokenSecret(token.getSecret());
	}
	
	public String sign(String usuario, String senha){
		requestToken();
		if(client.getOauth() != null)
		return client.sign(client.getOauth(), "login/movel", "senha="+senha+"&nome="+usuario);
		else
			throw new RestMBSignedRequestVerificationException("oAuth inválido");
	}
	
	public void accessToken(String verificador){
		Preconditions.checkEmptyString(verificador, "Verificador inválido");
		try{
			client.getOauth().setVerifier(verificador);
			token = client.obtainAppAccessToken(client.getOauth(), "accessToken/movel");
			client.getOauth().setTokenKey(token.getToken());
			client.getOauth().setTokenSecret(token.getSecret());
		}catch(Exception e){
			throw new RestMBSignedRequestVerificationException("oAuth inválido");
		}	
	}
}
