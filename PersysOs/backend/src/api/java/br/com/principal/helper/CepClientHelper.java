package br.com.principal.helper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class CepClientHelper {

	public static StringBuilder consultaCEP(String cep){
		StringBuilder output = new StringBuilder();

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(
					"http://cep.republicavirtual.com.br/web_cep.php?cep="+cep+"&formato=json");
			getRequest.addHeader("accept", "application/json");
			getRequest.setHeader("Accept-Charset", "iso-8859-1");

			HttpResponse response = httpClient.execute(getRequest);


			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(
					new InputStreamReader((response.getEntity().getContent()),"iso-8859-1"));

			String lineResponse;
			while ((lineResponse = br.readLine()) != null) {
				output.append(lineResponse) ;
			}

			httpClient.getConnectionManager().shutdown();

		} catch (IOException e) {} 
		catch (HttpException e) {} 
		catch (URISyntaxException e) {}

		return output;

	}

}