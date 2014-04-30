package com.restmb.types;

import java.math.BigDecimal;

import com.restmb.RestMB;

public class Produto extends RestMbType<Produto> implements br.com.model.interfaces.IProduto{

	@RestMB
	private String nome ="";
	@RestMB
	private String descricao = "";
	@RestMB
	private String marca = "";
	@RestMB
	private String codigoBarra = "";
	@RestMB
	private BigDecimal valor= new BigDecimal(0.0);
	
	public Produto(){
		super("/produto", Produto.class);
	}
	
	public Produto(String resourcePath, Class<Produto> paClass) {
		super(resourcePath, paClass);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}
	
	@Override
	public String toString() {
		return nome + " (Cod = " + getCodigo() + ")";
	}

	@Override
	public BigDecimal getValor() {
		// TODO Auto-generated method stub
		return valor;
	}

	@Override
	public void setValor(BigDecimal valor) {
		this.valor = valor;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((marca == null) ? 0 : marca.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		Produto other = (Produto) obj;
		if (marca == null) {
			if (other.marca != null)
				return false;
		} else if (!marca.equals(other.marca))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}	
	
	
}
