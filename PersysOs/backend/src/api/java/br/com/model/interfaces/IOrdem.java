package br.com.model.interfaces;

import java.util.Date;

public interface IOrdem extends IModel {

	public Date getDataConclusao();

	public void setDataConclusao(Date dataConclusao);

	public Date getDataTransferencia();

	public void setDataTransferencia(Date dataTransferencia);

	public String getAssunto();

	public void setAssunto(String assunto);

	public String getFonte();

	public void setFonte(String fonte);

	public ISituacaoOrdem getSituacaoOrdem();
	
	public IPrioridade getPrioridade();
	
	public IContato getContato();
	
	public IClienteObjeto getClienteObjeto();
	
	public Boolean getAtribuida();

	public void setAtribuida(Boolean atribuida);

	public Boolean getAgendada();

	public void setAgendada(Boolean agendada);

	public Date getDataAgendamento();
	
	public void setDataAgendamento(Date dataAgendamento);
	
	public Date getDataAgendamentoFim();
	public void setDataAgendamentoFim(Date dataAgendamentoFim);
	
	public Date getDataAgendamentoInicio();
	public void setDataAgendamentoInicio(Date dataAgendamentoInicio);
	
	public String getInformacaoAdicional();
	public void setInformacaoAdicional(String informacaoAdicional);

}
