package com.restmb.types;

import java.util.Date;

import br.com.principal.helper.FormatDateHelper;

import com.restmb.RestMB;

public class Contrato extends ClienteObjeto<Contrato> {

	@RestMB("tipoContrato")
	private String tipoContrato = "";
	
	@RestMB("fidelidade")
	private boolean fidelidade = false;
	
	@RestMB("prorrogavel")
	private boolean prorrogavel = false;
	
	@RestMB("gerenciamento")
	private boolean gerenciamento =true;
	
	@RestMB("dataAssinatura")
	private Date    dataAssinatura =   null;
	
	@RestMB("dataVigenciaInicio")
	private Date    dataVigenciaInicio =null;
	
	@RestMB("dataVigenciaFim")
	private Date    dataVigenciaFim =null;
	
	@RestMB("contato")
	private Contato contato = null;
	
	public Contrato() {
		super("contrato", Contrato.class);
	}
	
	public Contrato(String resourcePath, Class<Contrato> paClass) {
		super(resourcePath, paClass);
	}

	public String getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public boolean isFidelidade() {
		return fidelidade;
	}

	public void setFidelidade(boolean fidelidade) {
		this.fidelidade = fidelidade;
	}

	public boolean isGerenciamento() {
		return gerenciamento;
	}

	public void setGerenciamento(boolean gerenciamento) {
		this.gerenciamento = gerenciamento;
	}

	public Date getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public Date getDataVigenciaInicio() {
		return dataVigenciaInicio;
	}

	public void setDataVigenciaInicio(Date dataVigenciaInicio) {
		this.dataVigenciaInicio = dataVigenciaInicio;
	}

	public Date getDataVigenciaFim() {
		return dataVigenciaFim;
	}

	public void setDataVigenciaFim(Date dataVigenciaFim) {
		this.dataVigenciaFim = dataVigenciaFim;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public boolean isProrrogavel() {
		return prorrogavel;
	}

	public void setProrrogavel(boolean prorrogavel) {
		this.prorrogavel = prorrogavel;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contrato other = (Contrato) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String situacao; 
		if(getStatusModel() == 2){
			situacao = " (Este contrato foi removido)";
		}else {
			situacao = getSituacao().isEmpty() ? " ("+getSituacao()+") " : "";
		}
		String dataAssinatura = getDataAssinatura() != null ?" Data Assinatura= " 
					+ FormatDateHelper.formatTimeZoneBRDATE(getDataAssinatura().getTime()) : "";
		return getCodigo() + situacao + dataAssinatura;
	}
	
	
	
}
