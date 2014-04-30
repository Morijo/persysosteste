package br.com.despesas.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.usuario.model.Usuario;

@Entity
@Table(name="despesas")
public class Despesas extends Model<Despesas>{

	@Column(name = "despesascentro", length=30)
	private String centroDeCusto;
	
	@Column(name = "despesasdestino", length=30)
	private String cidadeDestino;
	
	@Column(name = "despesasdatainicio", length=30)
	private Date dataInicio;
	
	@Column(name = "despesasdatafinal", length=30)
	private Date dataFinal;
	
	@Column(name = "despesasmotivo")
	private Boolean motivoDaViagemProspec;
	
	@Column(name = "despesasinstalacao")
	private Boolean motivoDaViagemInstalacao;
	
	@Column(name = "despesasimplantacao")
	private Boolean motivoDaViagemImplantacao;

	@Column(name = "despesastreinamento")
	private Boolean motivoDaViagemTreinamento;
	
	@Column(name = "despesasoutros")
	private Boolean motivoDaViagemOutros;
	
	@Column(name = "despesastextfieldoutros", length=30)
	private String outros;
	
	@Column(name = "despesasgasto")
	private Double totalGasto;
	
	@Column(name = "despesasrecebido")
	private Double adianRecebido;
	
	@Column(name = "despesassaldo")
	private Double saldo;
	
	@Column(name = "despesasdeducao")
	private Double deducaoGasto;
	
	@Column(name = "despesassaldofinal")
	private Double saldoFinal;
	
	@Column(name = "despesasbanco", length=20)
	private String banco;
	
	@Column(name = "despesasagencia", length=20)
	private String agencia;
	
	@Column(name = "despesasconta", length=20)
	private String nConta;
	
	@Column(name = "despesasdataliberacao", length=30)
	private Date dataLiberacao;
	
	@Column(name = "despesasnomeinterno", length=50)
	private String nomeInterno;
	
	@Column(name = "despesasstatus", length=15)
	private String status;
	
	@Column(name = "despesasobservacao", length=150)
	private String observacao;
	
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "usuarioidpk", referencedColumnName="id"),
		})
	private Usuario usuario;
	
	@OneToMany(mappedBy = "despesas" , targetEntity = ItensDespesas.class, fetch = FetchType.LAZY)
	private Collection<ItensDespesas> itensDespesa = new ArrayList<ItensDespesas>();
	
	public Despesas(){}

	//GETTERS AND SETTERS
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getCentroDeCusto() {
		return centroDeCusto;
	}
	public void setCentroDeCusto(String centroDeCusto) {
		this.centroDeCusto = centroDeCusto;
	}

	public String getCidadeDestino() {
		return cidadeDestino;
	}
	public void setCidadeDestino(String cidadeDestino) {
		this.cidadeDestino = cidadeDestino;
	}

	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Boolean getMotivoDaViagemProspec() {
		return motivoDaViagemProspec;
	}
	public void setMotivoDaViagemProspec(Boolean motivoDaViagemProspec) {
		this.motivoDaViagemProspec = motivoDaViagemProspec;
	}

	public Boolean getMotivoDaViagemInstalacao() {
		return motivoDaViagemInstalacao;
	}
	public void setMotivoDaViagemInstalacao(Boolean motivoDaViagemInstalacao) {
		this.motivoDaViagemInstalacao = motivoDaViagemInstalacao;
	}

	public Boolean getMotivoDaViagemImplantacao() {
		return motivoDaViagemImplantacao;
	}
	public void setMotivoDaViagemImplantacao(Boolean motivoDaViagemImplantacao) {
		this.motivoDaViagemImplantacao = motivoDaViagemImplantacao;
	}

	public Boolean getMotivoDaViagemTreinamento() {
		return motivoDaViagemTreinamento;
	}
	public void setMotivoDaViagemTreinamento(Boolean motivoDaViagemTreinamento) {
		this.motivoDaViagemTreinamento = motivoDaViagemTreinamento;
	}

	public Boolean getMotivoDaViagemOutros() {
		return motivoDaViagemOutros;
	}
	public void setMotivoDaViagemOutros(Boolean motivoDaViagemOutros) {
		this.motivoDaViagemOutros = motivoDaViagemOutros;
	}

	public Collection<ItensDespesas> getItensDespesa() {
		return itensDespesa;
	}
	public void setItensDespesa(Collection<ItensDespesas> itensDespesa) {
		this.itensDespesa = itensDespesa;
	}

	public String getOutros() {
		return outros;
	}
	public void setOutros(String outros) {
		this.outros = outros;
	}

	public Double getTotalGasto() {
		return totalGasto;
	}
	public void setTotalGasto(Double totalGasto) {
		this.totalGasto = totalGasto;
	}

	public Double getAdianRecebido() {
		return adianRecebido;
	}
	public void setAdianRecebido(Double adianRecebido) {
		this.adianRecebido = adianRecebido;
	}

	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Double getDeducaoGasto() {
		return deducaoGasto;
	}
	public void setDeducaoGasto(Double deducaoGasto) {
		this.deducaoGasto = deducaoGasto;
	}

	public Double getSaldoFinal() {
		return saldoFinal;
	}
	public void setSaldoFinal(Double saldoFinal) {
		this.saldoFinal = saldoFinal;
	}

	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getnConta() {
		return nConta;
	}
	public void setnConta(String nConta) {
		this.nConta = nConta;
	}

	public Date getDataLiberacao() {
		return dataLiberacao;
	}
	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	public String getNomeInterno() {
		return nomeInterno;
	}
	public void setNomeInterno(String nomeInterno) {
		this.nomeInterno = nomeInterno;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	//METODOS
	
	public void addDespesa(ItensDespesas itensDespesas){
			
		this.itensDespesa.add(itensDespesas);
	}
	
	@Override
	public String toString() {
		return "Despesas [ usuario=" + usuario
				+ ", centroDeCusto=" + centroDeCusto + ", cidadeDestino="
				+ cidadeDestino + ", dataInicio=" + dataInicio + ", dataFinal="
				+ dataFinal + ", motivoDaViagemProspec="
				+ motivoDaViagemProspec + ", motivoDaViagemInstalacao="
				+ motivoDaViagemInstalacao + ", motivoDaViagemImplantacao="
				+ motivoDaViagemImplantacao + ", motivoDaViagemTreinamento="
				+ motivoDaViagemTreinamento + ", motivoDaViagemOutros="
				+ motivoDaViagemOutros + ", outros=" + outros + ", totalGasto="
				+ totalGasto + ", adianRecebido=" + adianRecebido + ", saldo="
				+ saldo + ", deducaoGasto=" + deducaoGasto + ", saldoFinal="
				+ saldoFinal + ", banco=" + banco + ", agencia=" + agencia
				+ ", nConta=" + nConta + ", dataLiberacao=" + dataLiberacao
				+ ", nomeInterno=" + nomeInterno + ", status=" + status
				+ ", observacao=" + observacao + "]";
	}

	@Override
	public void valida() throws ModelException {
		// TODO Auto-generated method stub
		
	}
	
}