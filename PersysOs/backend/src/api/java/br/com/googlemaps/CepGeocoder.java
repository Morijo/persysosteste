package br.com.googlemaps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;

@SuppressWarnings("deprecation")
public class CepGeocoder {

	 private CepServiceVO cepService; 
	
  
	@SuppressWarnings("rawtypes")
	public CepGeocoder(String cep) {
        	
		// a string da url		
		String urlString = "http://cep.republicavirtual.com.br/web_cep.php";

		// os parametros a serem enviados
		Properties parameters = new Properties();
		parameters.setProperty("cep", cep);
		parameters.setProperty("formato", "xml");

		// o iterador, para criar a URL
		Iterator i = parameters.keySet().iterator();
		// o contador
		int counter = 0;

		// enquanto ainda existir parametros
		while (i.hasNext()) {

			// pega o nome
			String name = (String) i.next();
			// pega o valor
			String value = parameters.getProperty(name);

			// adiciona com um conector (? ou &)
			// o primeiro é ?, depois são &
			urlString += (++counter == 1 ? "?" : "&") + name + "=" + value;
			
		}

		try {
			// cria o objeto url
			URL url = new URL(urlString);

			// cria o objeto httpurlconnection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// seta o metodo
			connection.setRequestProperty("Request-Method", "GET");

			// seta a variavel para ler o resultado
			connection.setDoInput(true);
			connection.setDoOutput(false);

			// conecta com a url destino
			connection.connect();

			// abre a conexão pra input
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			// le ate o final
			StringBuffer newData = new StringBuffer();
			String s = "";
			while (null != ((s = br.readLine()))) {
				newData.append(s);
			}
			br.close();

			// Controi classe a partir do XML 
			XStream xstream = new XStream(new DomDriver());
			Annotations.configureAliases(xstream, CepServiceVO.class);
			xstream.alias("webservicecep", CepServiceVO.class);
			this.cepService = (CepServiceVO) xstream.fromXML(newData.toString());
			System.out.println(this.cepService.getCidade());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	   public CepServiceVO getCepService() {
			return cepService;
		}

		public void setCepService(CepServiceVO cepService) {
			this.cepService = cepService;
		}

}
