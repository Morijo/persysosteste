package com.restmb.types;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import br.com.model.interfaces.IOrdem;
import br.com.model.interfaces.IPrioridade;
import br.com.model.interfaces.ISituacaoOrdem;

import com.restmb.BinaryAttachment;
import com.restmb.DefaultJsonMapper;
import com.restmb.RestMB;
import com.restmb.RestMBClient;
import com.restmb.exception.RestMBGraphException;
import com.restmb.oauth.service.ParameterList;

public class OrdemServico extends RestMbType<OrdemServico> implements IOrdem{

	@RestMB
	private Long   idOrdem = null;
		
	@RestMB
	private Date dataConclusao = null;
	
	@RestMB
	private Date dataReaberto = null; 
	
	@RestMB
	private Date dataTransferencia;
	
	@RestMB
	private String assunto = "";

	@RestMB
	private String fonte = null;

	@RestMB
	private SituacaoOrdem situacaoOrdem = null; 
	
	@RestMB 
	private ClienteObjeto<?> clienteObjeto;
	
	@RestMB 
	private Contato contato;
	
	@RestMB
	private ServicoOrdem servicoOrdem;
	
	@RestMB
	private Prioridade prioridade;
	
	@RestMB
	private Departamento departamento;
	
	@RestMB
	private Boolean atribuida = false;

	@RestMB
	private Boolean agendada = false;
	
	@RestMB 
	private Date dataAgendamento; 
	
	@RestMB 
	private Date dataAgendamentoInicio;
	
	@RestMB 
	private Date dataAgendamentoFim;
	
	@RestMB
	private String informacaoAdicional = "";
	
	public Long getIdOrdem() {
		return idOrdem;
	}

	public void setIdOrdem(Long idOrdem) {
		this.idOrdem = idOrdem;
	}

	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public Date getDataReaberto() {
		return dataReaberto;
	}

	public void setDataReaberto(Date dataReaberto) {
		this.dataReaberto = dataReaberto;
	}

	public Date getDataTransferencia() {
		return dataTransferencia;
	}

	public void setDataTransferencia(Date dataTransferencia) {
		this.dataTransferencia = dataTransferencia;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public ISituacaoOrdem getSituacaoOrdem() {
		return situacaoOrdem;
	}

	public void setSituacaoOrdem(SituacaoOrdem status) {
		this.situacaoOrdem = status;
	}

	public ClienteObjeto<?> getClienteObjeto() {
		return clienteObjeto;
	}

	public void setClienteObjeto(ClienteObjeto<?> contrato) {
		this.clienteObjeto = contrato;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public ServicoOrdem getServicoOrdem() {
		return servicoOrdem;
	}

	public void setServicoOrdem(ServicoOrdem servicoOrdem) {
		this.servicoOrdem = servicoOrdem;
	}
	
	public IPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Boolean getAtribuida() {
		return atribuida;
	}

	public void setAtribuida(Boolean atribuida) {
		this.atribuida = atribuida;
	}

	public Boolean getAgendada() {
		return agendada;
	}

	public void setAgendada(Boolean agendada) {
		this.agendada = agendada;
	}

	public Date getDataAgendamento() {
		return dataAgendamento;
	}
	
	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}
	
	@Override
	public Date getDataAgendamentoInicio() {
		return dataAgendamentoInicio;
	}

	@Override
	public void setDataAgendamentoInicio(Date dataAgendamentoInicio) {
		this.dataAgendamentoInicio = dataAgendamentoInicio;
		
	}
	
	@Override
	public Date getDataAgendamentoFim() {
		return dataAgendamentoFim;
	}

	@Override
	public void setDataAgendamentoFim(Date dataAgendamentoFim) {
		this.dataAgendamentoFim = dataAgendamentoFim;
		
	}
	
	@Override
	public String getInformacaoAdicional() {
		
		return informacaoAdicional;
	}

	@Override
	public void setInformacaoAdicional(String informacaoAdicional) {
		this.informacaoAdicional = informacaoAdicional;
		
	}

	public OrdemServico(){
		super("/ordem", OrdemServico.class);
	}
	
	public OrdemServico(String resourcePath, Class<OrdemServico> paClass) {
		super(resourcePath, paClass);
	}
	
	public AgendaOrdemFuncionario adicionarFuncionarioExecucao(RestMBClient client, AgendaOrdemFuncionario agendaOrdemFuncionario) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/ordem/"+this.getId()+"/funcionario",AgendaOrdemFuncionario.class,json.toJson(agendaOrdemFuncionario),headers);
	}
	
	public Nota adicionarNotaOrdem(RestMBClient client,Nota nota) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/ordem/"+this.getId()+"/nota",Nota.class,json.toJson(nota),headers);
	}
	
	public Mensagem adicionarMensagemOrdem(RestMBClient client, Mensagem mensagem) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/ordem/"+this.getId()+"/mensagem",Mensagem.class,json.toJson(mensagem),headers);
	}
	
	public ServicoOrdem adicionarServicoOrdem(RestMBClient client, Servico servico) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/ordem/"+this.getId()+"/servico",ServicoOrdem.class,json.toJson(servico),headers);
	}
	
	public RecursoOrdem adicionarRecursoOrdem(RestMBClient client, RecursoOrdem recursoOrdem) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/ordem/"+this.getId()+"/recurso",RecursoOrdem.class,json.toJson(recursoOrdem),headers);
	}
	
	public OrdemServico adicionarContato(RestMBClient client, Contato contato) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/ordem/"+this.getId()+"/contato",OrdemServico.class,json.toJson(contato),headers);
	}
	
	public OrdemServico adicionarContrato(RestMBClient client, Contrato contrato) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/ordem/"+this.getId()+"/contrato",OrdemServico.class,json.toJson(contrato),headers);
	}
	
	public OrdemServico adicionarAgendamento(RestMBClient client) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publishChanges("/ordem/"+this.getId()+"/agendamento",OrdemServico.class,json.toJson(this),headers);
	}
	
	public OrdemServico cancelarAgendamento(RestMBClient client) {
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publishChanges("/ordem/agendamento/"+this.getId()+"/cancelar",OrdemServico.class,json.toJson(this),headers);
	}
	
	public OrdemServico adicionarAnexo(RestMBClient client, Anexo anexo) {
		if(!anexo.getCaminho().isEmpty()){
			InputStream fos = null; 
			try {
				fos = new FileInputStream(anexo.getCaminho());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
	        
			return client.publish("ordem/"+getId()+"/anexo",paClass,BinaryAttachment.with(anexo.getDescricao(), fos));
		}else{
			throw new RestMBGraphException("REST", "Sem imagem", 1);
		}
	}
}
