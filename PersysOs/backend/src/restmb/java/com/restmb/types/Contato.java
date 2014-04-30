package com.restmb.types;

import br.com.model.interfaces.IContato;
import br.com.model.interfaces.IEndereco;
import br.com.model.interfaces.IUsuario;

import com.restmb.DefaultJsonMapper;
import com.restmb.RestMB;
import com.restmb.RestMBClient;
import com.restmb.oauth.service.ParameterList;

public class Contato extends RestMbType<Contato> implements IContato{

	
	public Contato() {
		super("/contato",Contato.class);
		// TODO Auto-generated constructor stub
	}
	public Contato(String resourcePath, Class<Contato> paClass) {
		super(resourcePath, paClass);
	}

	@RestMB("tipoContato")
	private String tipoContato;
	@RestMB("nome")
	private String nome="";
	@RestMB("departamento")
	private String departamento="";
	@RestMB("email")
	private String email = "";
	
	@RestMB
	private Endereco endereco = null;
	
	@RestMB
	private String telefoneFixo = "";

	@RestMB
	private String telefoneMovel = "";

	public String getTipoContato() {
		return tipoContato;
	}
	public void setTipoContato(String tipoContato) {
		this.tipoContato = tipoContato;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String emailPrincipal) {
		this.email = emailPrincipal;
	}
	public IEndereco getEndereco() {
		return endereco;
	}
	public void setEndereco(IEndereco endereco) {
		this.endereco = (Endereco) endereco;
	}
	
	public String getTelefoneFixo() {
		return telefoneFixo;
	}
	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}
	public String getTelefoneMovel() {
		return telefoneMovel;
	}
	public void setTelefoneMovel(String telefoneMovel) {
		this.telefoneMovel = telefoneMovel;
	}
	
	@Override
	public IUsuario getUsuario() {
		return null;
	}
	
	@Override
	public void setUsuario(IUsuario usuario) {}

	@Override
	public boolean isEmailEnviar() {
		return true;
	}
	
	@Override
	public void setEmailEnviar(boolean emailEnviar) {}
	
	@Override
	public String toString() {
		
		StringBuilder contato = new StringBuilder();
		contato.append(nome);
		if(!email.isEmpty()){
			contato.append(" Email:" + email);
		}
		contato.append(" Endereco:" + endereco);
		
		return contato.toString();
	}
	

	public Endereco atualizaContatoEndereco(RestMBClient client,Endereco endereco) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		try{
			endereco = client.publishChanges("/contato/"+getId()+"/endereco",Endereco.class,json.toJson(endereco),headers);
		}catch(Exception e){
			endereco = null;
		}
		return endereco;
	}
	
	public boolean removerContatoTelefone(RestMBClient client) {
		return client.deleteObject("/contato/"+getId()+"/telefone");
	}
	
	public boolean removerContatoEndereco(RestMBClient client) {
		return client.deleteObject("/contato/"+getId()+"/endereco");
	}
}
