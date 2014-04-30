package com.restmb.types;

import java.util.Date;

import com.restmb.RestMB;


public class AgendaOrdemFuncionario extends RestMbType<AgendaOrdemFuncionario>{
	
	@RestMB
	private Date dataConclusao;
	
	@RestMB
	private Date dataAtribuicao; 
	
	@RestMB
    private OrdemServico ordem;
	
	@RestMB
	private com.restmb.types.Funcionario funcionario;
	
	
	public AgendaOrdemFuncionario() {
		 super("/ordem/funcionario", AgendaOrdemFuncionario.class);
	}
	
   	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public Date getDataAtribuicao() {
		return dataAtribuicao;
	}

	public void setDataAtribuicao(Date dataAgendamento) {
		this.dataAtribuicao = dataAgendamento;
	}
	
	public OrdemServico getOrdem() {
		return ordem;
	}

	public void setOrdem(OrdemServico ordem) {
		this.ordem = ordem;
	}

	public Funcionario getFuncionario() {
		if(funcionario == null){
			funcionario = new Funcionario();
		}
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	
}
