package com.restmb.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.restmb.RestMB;

public class Cliente extends Usuario<Cliente> implements br.com.model.interfaces.ICliente {

	@RestMB
	private String  situacao = "";
	@RestMB
	private String tipoCliente = "";
	@RestMB
	private String site ="";
	@RestMB
	private Date dataNascimento;	
	@RestMB
	private String imagem;
	
	@RestMB("contato")
	private List<Contato> contatos = new ArrayList<Contato>();	
	
	public Cliente() {
		super("/cliente", Cliente.class);
	}
	
	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	@Override
	public String toString() {
		return getRazaoNome() + "[Cod=" + getCodigo() + ", CNJP/CPF=" + getCnpjCpf() + "]";
	}

	
}
