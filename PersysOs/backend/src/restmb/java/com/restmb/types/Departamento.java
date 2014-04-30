package com.restmb.types;

import br.com.model.interfaces.IDepartamento;
import br.com.model.interfaces.IUnidade;

import com.restmb.RestMB;


public class Departamento extends RestMbType<Departamento> implements IDepartamento{

	@RestMB("nomeDepartamento")
	private String nomeDepartamento ="";
		
	@RestMB("descricao")
	private String descricao = ""; //{area de texto}

	@RestMB("tipo")
	private String tipo= ""; //{ publico, interno}
	
	@RestMB("email")
	private String email = ""; 
	
	@RestMB("responsavel")
	private String responsavel = ""; 
		
	@RestMB("emailTemplate")
	private String emailTemplate; //{ template de resposta}
	
	@RestMB("telefone")
	private String telefone="";
	
	@RestMB("ramal")
	private String ramal="";
	
	@RestMB("unidade")
	private Unidade unidade;
	
	public Departamento(String resourcePath, Class<Departamento> paClass) {
		super(resourcePath, paClass);
		// TODO Auto-generated constructor stub
	}
	
	public Departamento() {
		super("/departamento", Departamento.class);
		// TODO Auto-generated constructor stub
	}
	
	public String getNomeDepartamento() {
		return nomeDepartamento;
	}

	public void setNomeDepartamento(String nomeDepartamento) {
		this.nomeDepartamento = nomeDepartamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(IUnidade unidade) {
		this.unidade = (Unidade) unidade;
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

	@Override
	public String toString() {
		return  nomeDepartamento;
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
			Departamento other = (Departamento) obj;
			if (getId() == null) {
				if (other.getId() != null)
					return false;
			} else if (!getId().equals(other.getId()))
				return false;
			return true;
		}
	}
