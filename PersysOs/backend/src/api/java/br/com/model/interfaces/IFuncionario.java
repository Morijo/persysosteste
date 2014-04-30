package br.com.model.interfaces;

import java.util.Date;

public interface IFuncionario extends IUsuario {

	public String getTipoFuncionario();
	public void setTipoFuncionario(String tipoFuncionario);
	public String getDesligamento();
	public void setDesligamento(String desligamento);
	public String getSituacao();
	public void setSituacao(String situacao);
	public String getCtps();
	public void setCtps(String ctps);
	public String getEstadoCivil();
	public void setEstadoCivil(String estadoCivil);
	public Date getDataAdmissao();
	public void setDataAdmissao(Date dataadmissao);
	public Date getDataDemissao();
	public void setDataDemissao(Date datademissao);
	public Date getDataNascimento();
	public void setDataNascimento(Date datanascimento);
	public String getNomePai();
	public void setNomePai(String nomePai);
	public String getNomeMae();
	public void setNomeMae(String nomeMae);
	public String getNumeroPis();
	public void setNumeroPis(String numeropis);
	public String getMotivoDesligamento();
	public void setMotivoDesligamento(String motivoDesligamento);
	public IDepartamento getDepartamento();
	public void setDepartamento(IDepartamento departamento);
}
