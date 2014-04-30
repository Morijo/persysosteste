package com.restmb.types;

import java.io.Serializable;

import br.com.model.interfaces.IEndereco;
import br.com.model.interfaces.IUnidade;

import com.restmb.Connection;
import com.restmb.RestMB;
import com.restmb.RestMBClient;

public class Unidade extends RestMbType<Unidade> implements Serializable, IUnidade{

	private static final long serialVersionUID = 1L;

	@RestMB("nomeUnidade")
	private String nome = ""; 

	@RestMB("email")
	private String email = "";

	@RestMB
	private String telefone = "";
	
	@RestMB
	private String ramal = ""; 
	   
	@RestMB("endereco")
	private Endereco endereco;
	
	public Unidade(String resourcePath, Class<Unidade> paClass) {
		super(resourcePath, paClass);
	}
	
	public Unidade() {
		super("/unidade", Unidade.class);
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(IEndereco endereco) {
		this.endereco = (Endereco) endereco;
	}


	public static Connection<Unidade> listaUnidadeHome(RestMBClient client) {
		return client.fetchConnection("/unidade/home", Unidade.class, "data");
	}
	
	public Endereco getEnderecoUnidade(RestMBClient client) {
		return client.fetchObject("/unidade/"+this.getId()+"/endereco", Endereco.class);
	}
	
	public Endereco alterarEnderecoUnidade(RestMBClient client) {
		endereco.resourcePath = "/unidade/"+this.getId()+"/endereco";
		return endereco.alterarHome(client);
	}
	
	public Endereco salvarEnderecoUnidade(RestMBClient client) {
		endereco.resourcePath = "/unidade/"+this.getId()+"/endereco";
		return endereco.salvar(client);
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unidade other = (Unidade) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}	
}
