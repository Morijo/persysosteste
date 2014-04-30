package br.com.despesas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.exception.ModelException;
import br.com.model.Model;

@Entity
@Table(name="itensDespesas")
public class ItensDespesas extends Model<ItensDespesas> {
	
	@Column (name = "itensdespesasnome", nullable=false, length=30)
	private String nome;
	
	@Column (name = "itensdespesasvalor")
	private Double valor;
	
	@Column (name = "itensdespesasobervacao", length=150)
	private String obervacao;
	
	@Column (name = "itensdespesasdeducao")
	private Double deducao;
	 
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "despesasidpk", referencedColumnName="id"),
		})
	private Despesas despesas;
	
	public ItensDespesas(){}
	
	//GETTERS AND SETTERS
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getObervacao() {
		return obervacao;
	}
	public void setObervacao(String obervacao) {
		this.obervacao = obervacao;
	}

	public Double getDeducao() {
		return deducao;
	}
	public void setDeducao(Double deducao) {
		this.deducao = deducao;
	}

	public Despesas getDespesas() {
		return despesas;
	}
	public void setDespesas(Despesas despesas) {
		this.despesas = despesas;
	}

	//METODOS
	@Override
	public void valida() throws ModelException {
		// TODO Auto-generated method stub
		
	}
}