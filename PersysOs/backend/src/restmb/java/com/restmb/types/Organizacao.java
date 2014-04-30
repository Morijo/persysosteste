package com.restmb.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import br.com.principal.helper.UrlHelper;
import com.restmb.BinaryAttachment;
import com.restmb.DefaultJsonMapper;
import com.restmb.RestMB;
import com.restmb.RestMBClient;
import com.restmb.exception.RestMBGraphException;
import com.restmb.oauth.service.ParameterList;


public class Organizacao extends RestMbType<Organizacao> implements br.com.model.interfaces.IOrganizacao {

	@RestMB("razaoSocial")
	private String  razaoSocial = ""; 
	
	@RestMB("nomeFantasia")
	private String  nomeFantasia = ""; 
	
	@RestMB("cnpj")
	private String   cnpj = "";
	
	@RestMB("inscricaoEstadual")
	private String  inscricaoEstadual = "";
	
	@RestMB("inscricaoMunicipal")
	private String   inscricaoMunicipal = "";
	
	@RestMB("cnaeFiscal")
	private String     cnaeFiscal = "";
	
	@RestMB("inscricaoEstadualSubstTributario")
	private String   inscricaoEstadualSubstTributario = "";
	
	@RestMB("regimeTributario")
	private String   regimeTributario;
	
	@RestMB("email")
	private String   email = "";
	
	@RestMB("logo")
	private String   logo = "";
	
	@RestMB("endereco")
	private Endereco endereco;
	
	@RestMB("telefone")
	private String telefone = "";
	
	public Organizacao(){
		super("/organizacao", Organizacao.class);
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getCnaeFiscal() {
		return cnaeFiscal;
	}

	public void setCnaeFiscal(String cnaeFiscal) {
		this.cnaeFiscal = cnaeFiscal;
	}

	public String getInscricaoEstadualSubstTributario() {
		return inscricaoEstadualSubstTributario;
	}

	public void setInscricaoEstadualSubstTributario(
			String inscricaoEstadualSubstTributario) {
		this.inscricaoEstadualSubstTributario = inscricaoEstadualSubstTributario;
	}

	public String getRegimeTributario() {
		return regimeTributario;
	}

	public void setRegimeTributario(String regimeTributario) {
		this.regimeTributario = regimeTributario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogo() {
		URL url= null;
		try {
			url = new URL(UrlHelper.END_POINT_SERVICE+"/organizacao/"+this.getId()+"/logo");
			url.openStream();
			return UrlHelper.END_POINT_SERVICE+"/organizacao/"+this.getId()+"/logo";
		} catch (Exception ex) {
			return "img/profile-pic.png";
		}
	}

	public void setLogo(RestMBClient client, File file) throws RestMBGraphException {
		if(file != null){
			InputStream fos = null; 
			try {
				fos = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
			client.publish("organizacao/"+getId()+"/logo",paClass,BinaryAttachment.with("logo", fos));
		}
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Override
	public String toString() {
		return "Organizacao [razaoSocial=" + razaoSocial + ", nomeFantasia="
				+ nomeFantasia + ", cnpj=" + cnpj + ", inscricaoEstadual="
				+ inscricaoEstadual + ", inscricaoMunicipal="
				+ inscricaoMunicipal + ", cnaeFiscal=" + cnaeFiscal
				+ ", inscricaoEstadualSubstTributario="
				+ inscricaoEstadualSubstTributario + ", regimeTributario="
				+ regimeTributario + ", email=" + email + ", logo=" + logo
				+ ", endereco=" + endereco + ", telefone=" + telefone + "]";
	}
	
	public static Organizacao pesquisaHome(RestMBClient client) {
		return client.fetchObject("/organizacao/home", Organizacao.class);
	}
	
	public static Endereco getEnderecoOrganizacaoHome(RestMBClient client) {
		return client.fetchObject("/organizacao/home/endereco", Endereco.class);
	}
		
	public Organizacao alteraHome(RestMBClient client) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publishChanges("/organizacao/home",Organizacao.class,json.toJson(this),headers);
	}
	
	public boolean removerHome(RestMBClient client){
		try{
		  return client.deleteObject("/organizacao/home");
		}catch (RestMBGraphException e) {
			return false;
		}
	}
}
