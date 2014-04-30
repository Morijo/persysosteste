package com.restmb.types;

import com.restmb.RestMB;

public class Mensagem extends RestMbType<Mensagem>{

	@RestMB
	private String titulo = ""; 
	
	@RestMB
	private String fonte =""; //web telefone zabbix;
	
	@RestMB
	private String mensagem ="";;
	
	@RestMB
	private String tipo =""; 
	
	@RestMB
	private String ipEndereco = ""; 
	
	public Mensagem(){
		super("/mensagem", Mensagem.class);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getIpEndereco() {
		return ipEndereco;
	}

	public void setIpEndereco(String ipEndereco) {
		this.ipEndereco = ipEndereco;
		
	}
}
