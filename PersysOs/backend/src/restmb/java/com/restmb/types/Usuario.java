package com.restmb.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.model.interfaces.IAccessToken;
import br.com.principal.helper.UrlHelper;

import com.restmb.BinaryAttachment;
import com.restmb.Connection;
import com.restmb.DefaultJsonMapper;
import com.restmb.Parameter;
import com.restmb.RestMB;
import com.restmb.RestMBClient;
import com.restmb.exception.RestMBGraphException;
import com.restmb.oauth.service.ParameterList;
import com.restmb.types.Contato;
import com.restmb.types.Evento;
import com.restmb.types.Funcionario;
import com.restmb.types.RestMbType;

public class Usuario<T> extends RestMbType<T> implements br.com.model.interfaces.IUsuario {

	@RestMB("nomeUsuario")
	private String nomeUsuario = "";
	
	@RestMB("emailPrincipal")
	private String emailprincipal = "";

	@RestMB("razaoNome")
	private String razaoNome = "";
	
	@RestMB("fantasiaSobrenome")
	private String fantasiaSobrenome = "";

	@RestMB("cnpjCpf")
	private String cnpjCpf = "";
	
	@RestMB("ieRg")
	private String ieRg = "";
	
	@RestMB
	private String nota = null;
	
	@RestMB
	private String dashboardNome = null;
	
	@RestMB
	private String timezone = null;
	
	@RestMB
	private String locale = null;
	
	@RestMB("contato")
	private List<Contato> contatos = new ArrayList<Contato>();	
	
	@RestMB
	private com.restmb.types.GrupoUsuario grupoUsuario= null;
	
	
	public Usuario() {}
	
	public Usuario(String resourcePath, Class<T> paClass) {
		super(resourcePath, paClass);
	}
	
	public String getEmailPrincipal() {
		return emailprincipal;
	}

	public void setEmailPrincipal(String emailprincipal) {
		this.emailprincipal = emailprincipal;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}
	
	public String getImagem() {
		return UrlHelper.END_POINT_SERVICE+"/home/imagem";
	}
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}


	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}


	public String getRazaoNome() {
		return razaoNome;
	}


	public void setRazaoNome(String razaoNome) {
		this.razaoNome = razaoNome;
	}


	public String getFantasiaSobrenome() {
		return fantasiaSobrenome;
	}


	public void setFantasiaSobrenome(String fantasiaSobrenome) {
		this.fantasiaSobrenome = fantasiaSobrenome;
	}


	public String getCnpjCpf() {
		return cnpjCpf;
	}


	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}


	public String getIeRg() {
		return ieRg;
	}


	public void setIeRg(String ieRg) {
		this.ieRg = ieRg;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getDashboardNome() {
		return dashboardNome;
	}

	public void setDashboardNome(String dashboardNome) {
		this.dashboardNome = dashboardNome;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	@Override
	public void setSenha(String senha) {}
	
	@Override
	public br.com.model.interfaces.IGrupoUsuario getGrupoUsuario() {
		return (br.com.model.interfaces.IGrupoUsuario) this.grupoUsuario;
	}
	
	@Override
	public void setGrupoUsuario(
			br.com.model.interfaces.IGrupoUsuario grupoUsuario) {
			this.grupoUsuario = (com.restmb.types.GrupoUsuario) grupoUsuario;
	}	

	@Override
	public IAccessToken getAccessToken() {
		return null;
	}

	@Override
	public void setAccessToken(IAccessToken accessToken) {}
	
	public void setImagem(RestMBClient client, String imagem) {
		if(!imagem.isEmpty()){
			InputStream fos = null; 
			try {
				fos = new FileInputStream(imagem);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
			client.publish("funcionario/"+getId()+"/imagem",paClass,BinaryAttachment.with(imagem, fos));
		}else{
			throw new RestMBGraphException("REST", "Imagem vazia", 1);
		}
	}
	
	public void setImagemHome(RestMBClient client, String imagem) {
		if(!imagem.isEmpty()){
			InputStream fos = null; 
			try {
				fos = new FileInputStream(imagem);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
			client.publish("home/imagem",paClass,BinaryAttachment.with(imagem, fos));
		}else{
			throw new RestMBGraphException("REST", "Imagem vazia", 1);
		}
	}
	
	public void setImagem(RestMBClient client, File imagem) {
		if(imagem.exists()){
			InputStream fos = null; 
			try {
				fos = new FileInputStream(imagem);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
	        
			client.publish("usuario/"+getId()+"/imagem",paClass,BinaryAttachment.with("logo", fos));
		}else{
			throw new RestMBGraphException("REST", "Imagem vazia", 1);
		}
	}

	
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T getHome(RestMBClient client) {
		return (T) Usuario.pesquisa(client, "/home",getClass());
	}
	
	public static Usuario<?> getHomeUsuario(RestMBClient cliente) {
		return  Usuario.pesquisa(cliente,"/home",Usuario.class);
	}
	
	public static Usuario<?> getHomeUsuarioDashboard(RestMBClient cliente) {
		return  Usuario.pesquisa(cliente,"/home/dashboard",Usuario.class);
	}

	@SuppressWarnings("rawtypes")
	public static Usuario recuperaSenha(RestMBClient cliente, Parameter... p) {
		return  Usuario.pesquisa(cliente,"usuario/senha",Usuario.class,p);
	}

	public Connection<Contato> getContatosHome(RestMBClient client) {
		Connection<Contato> lista = Funcionario.listaAll(client,"/home/contato", Contato.class);
		return lista;
	}

	public Contato getContatoHome(RestMBClient client, long idContato) {
		Contato contato = Contato.pesquisa(client, "/contato", Contato.class,idContato);
		return contato;
	}
	
	public static Usuario<?> alteraDashboard(RestMBClient client, Usuario<?> usuario) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publishChanges("/home/dashboard",Usuario.class,json.toJson(usuario),headers);
	}

	public Contato adionarContatoHome(RestMBClient client, Contato contato) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/home/contato",Contato.class,json.toJson(contato),headers);
	}
	
	public Evento adionarEventoHome(RestMBClient client, Evento evento) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/home/evento",Evento.class,json.toJson(evento),headers);

	}

	public Contato adicionarContato(RestMBClient client, Contato contato) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/usuario/"+getId()+"/contato",Contato.class,json.toJson(contato),headers);
	}
}
