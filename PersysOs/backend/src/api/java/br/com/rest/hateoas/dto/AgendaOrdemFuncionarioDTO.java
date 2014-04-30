package br.com.rest.hateoas.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.funcionario.model.Funcionario;
import br.com.ordem.model.Ordem;
import br.com.rest.represention.JsonDateAdapter;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class AgendaOrdemFuncionarioDTO  extends ModelDTO {

	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAgendamento;
	private Ordem ordem = new Ordem();
	private Funcionario funcionario = new Funcionario();
	private Integer statusModel;
	
	public void setFuncionarioId(Long id){
		funcionario.setId(id);
	}
	
	public void setFuncionarioRazao(String razao){
		funcionario.setRazaoNome(razao);
	}
	
	public void setOrdemId(Long id){
		ordem.setId(id);
	}

	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public Integer getStatusModel() {
		return statusModel;
	}

	public void setStatusModel(Integer statusModel) {
		this.statusModel = statusModel;
	}
	
	
	
}
