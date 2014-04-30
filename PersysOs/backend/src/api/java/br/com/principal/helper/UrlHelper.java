package br.com.principal.helper;

import br.com.principal.model.Aplicacao;
//import com.restmb.DefaultClient;
//import com.restmb.RestMBClient;

public class UrlHelper {

	public static String END_POINT_SERVICE = Aplicacao.getAplicacao().getEndPoint();
	
//	public static RestMBClient getClientREST(){
//		RestMBClient clientREST = new DefaultClient(END_POINT_SERVICE);
//		return clientREST;
//	}
}
