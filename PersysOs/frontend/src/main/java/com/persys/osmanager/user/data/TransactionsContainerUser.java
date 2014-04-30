package com.persys.osmanager.user.data;

import com.restmb.RestMBClient;
import com.restmb.oauth.OAuth;
import com.restmb.types.Login;
import br.com.exception.ModelException;
import br.com.exception.ServiceException;
import br.com.model.interfaces.IUsuario;
import br.com.oauth.model.AccessToken;
import br.com.principal.helper.HashHelper;
import br.com.usuario.model.GrupoUsuario;

public class TransactionsContainerUser {

	public static final int MODOREST = 0;
	public static final int MODOBD = 1;


	public static IUsuario login(RestMBClient client, String userName, String password,int modo) throws ServiceException{
		if(modo == MODOREST){
			return loginRest(client,userName, password);
		}else{
			return loginBD(client, userName, password);
		}
	}

	private static IUsuario loginRest(RestMBClient client, String userName, String password) throws ServiceException{
		try{
			Login login = new Login(client);
			String codigo = login.sign(userName, password);
			login.accessToken(codigo);
			if (!codigo.isEmpty()){
				return com.restmb.types.Usuario.getHomeUsuario(client);
			}
			else{
				throw new ServiceException("\n Falha na geração do token de acesso");
			}	
		}
		catch (Exception e) {
			throw new ServiceException("\n Falha ao logar. Senha ou usuário inválidos");
		}
	}

	private static IUsuario loginBD(RestMBClient client, String userName, String password) throws ServiceException{
		try{
			br.com.usuario.model.Usuario usuario = br.com.usuario.model.Usuario.autenticaUsuario(userName, password);
			if (usuario == null){
				throw new ServiceException("\n Usuário não encontrado");
			}
			AccessToken accessToken;
			try{
				accessToken = (AccessToken) AccessToken.retrieveAccessToken(usuario.getId()).getAccessToken();
			}catch (ModelException e) {
				accessToken = br.com.usuario.model.Usuario.createTokenUser(usuario);
			}

			client.setOauth(new OAuth(usuario.getConsumerSecret().getConsumerKey().toString(), HashHelper.chave(usuario.getConsumerSecret().getConsumerKey().toString())));
			client.getOauth().setTokenKey(accessToken.getToken());
			client.getOauth().setTokenSecret(accessToken.getSecret());
			return com.restmb.types.Usuario.getHomeUsuario(client);
		}
		catch (Exception e) {
			throw new ServiceException("\n Falha ao logar. Senha ou usuário inválidos" + e.getMessage());
		}
	}

	public static GrupoUsuario pesquisaGrupoUsuario(Long idUsuario){
		return br.com.usuario.model.Usuario.pesquisaGrupoUsuario(idUsuario);
	}
}
