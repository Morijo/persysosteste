package com.restmb.types;

import java.util.Date;

import br.com.model.interfaces.IDepartamento;
import br.com.model.interfaces.IFuncionario;
import br.com.principal.helper.UrlHelper;

import com.restmb.RestMB;

public class Funcionario extends Usuario<Funcionario> implements IFuncionario{

	@RestMB("situacao")
	private String situacao;
	
	@RestMB("tipoFuncionario")
	private String tipoFuncionario;

	@RestMB("motivoDesligamento")
	private String  motivoDesligamento;
	
	@RestMB("dataNascimento")
	private Date dataNascimento;
	
	@RestMB("nomePai")
	private String nomePai;
	
	@RestMB("nomeMae")
	private String nomeMae;
	
	@RestMB("numeroPis")
	private String numeroPis;
	
	@RestMB("ctps")
	private String ctps = null;
	
	@RestMB( "estadoCivil")
	private String estadoCivil = null;
	
	@RestMB( "dataAdmissao")
	private Date dataAdmissao = null;	
	
	@RestMB( "dataDemissao")
	private Date dataDemissao = null;	
	
	@RestMB
	private Departamento departamento= null;
	
	
	public Funcionario() {
		super("funcionario", Funcionario.class);
	}
	
	public Funcionario(String resourcePath, Class<Funcionario> paClass) {
		super(resourcePath, paClass);
	}

	public String getTipoFuncionario() {
		return tipoFuncionario;
	}

	public void setTipoFuncionario(String tipoFuncionario) {
		this.tipoFuncionario = tipoFuncionario;
	}
	
	public String getMotivoDesligamento() {
		return motivoDesligamento;
	}

	public void setMotivoDesligamento(String motivoDesligamento) {
		this.motivoDesligamento = motivoDesligamento;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public String getNumeroPis() {
		return numeroPis;
	}

	public void setNumeroPis(String numeroPis) {
		this.numeroPis = numeroPis;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		this.ctps = ctps;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Date getDataAdmissao() {
		return dataAdmissao;
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	public Date getDataDemissao() {
		return dataDemissao;
	}

	public void setDataDemissao(Date dataDemissao) {
		this.dataDemissao = dataDemissao;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(IDepartamento departamento) {
		this.departamento = (Departamento) departamento;
	}
	
	public String getImagem() {
		return UrlHelper.END_POINT_SERVICE+"/usuario/"+this.getId()+"/imagem";
	}
	
	@Override
	public String toString() {
		return getRazaoNome() + " [Cod: "+ getCodigo() +"]";
	}

	@Override
	public String getDesligamento() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDesligamento(String desligamento) {
		// TODO Auto-generated method stub
		
	}

	
}
