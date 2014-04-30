package br.com.rest.hateoas.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.contato.model.Contato;
import br.com.empresa.model.Departamento;
import br.com.empresa.model.Unidade;
import br.com.ordem.model.Prioridade;
import br.com.ordem.model.SituacaoOrdem;
import br.com.rest.represention.JsonDateAdapter;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class OrdemDTO extends ModelDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long   idOrdem = null;
	
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataConclusao = null;
	
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAgendamento = null;
	
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAgendamentoFim = null;

	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAgendamentoInicio = null;
	
	private Boolean agendada = null;
	
	private String assunto = null;

	private String fonte = null;

	private SituacaoOrdem situacaoOrdem = new SituacaoOrdem();
	
	private Prioridade    prioridade = new Prioridade();
	
	private Contato contato = new Contato();
	
	private String informacaoAdicional="";

	private Departamento departamento = new Departamento();
	
	private ContratoDTO clienteObjeto = new ContratoDTO();
	
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
	
	public void setAgendada(Boolean agendada) {
		this.agendada = agendada;
	}
	
	public SituacaoOrdem getSituacaoOrdem() {
		return situacaoOrdem;
	}

	public void setSituacaoOrdem(SituacaoOrdem situacaoOrdem) {
		this.situacaoOrdem = situacaoOrdem;
	}

	public void setSituacaoOrdemId(Long situacaoOrdemId) {
		this.situacaoOrdem.setId(situacaoOrdemId);
	}
	
	public void setSituacaoOrdemNome(String situacaoOrdemNome) {
		this.situacaoOrdem.setNome(situacaoOrdemNome);
	}

	public Prioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}
	
	public void setPrioridadeId(Long prioridadeId) {
		this.prioridade.setId(prioridadeId);
	}
	
	public void setPrioridadeTitulo(String prioridade) {
		this.prioridade.setPrioridade(prioridade);
	}
	
	public void setPrioridadeCor(String cor) {
		this.prioridade.setCor(cor);
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public ContratoDTO getClienteObjeto() {
		return clienteObjeto;
	}

	public void setClienteObjeto(ContratoDTO clienteObjeto) {
		this.clienteObjeto = clienteObjeto;
	}
	
	public Long getClienteObjetoId() {
		return clienteObjeto.getId();
	}

	public void setClienteObjetoId(Long id) {
		this.clienteObjeto.setId(id);
	}
	
	public void setClienteObjetoCodigo(String codigo) {
		this.clienteObjeto.setCodigo(codigo);
	}
	
	public void setClienteObjetoDataAssinatura(Date dataAssinatura) {
		this.clienteObjeto.setDataAssinatura(dataAssinatura);
	}
	
	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}
	
	public void setDataAgendamentoInicio(Date dataAgendamento) {
		this.dataAgendamentoInicio = dataAgendamento;
	}
	
	public void setDataAgendamentoFim(Date dataAgendamento) {
		this.dataAgendamentoFim = dataAgendamento;
	}

	public String getContatoNome() {
		return contato.getNome();
	}

	public void setContatoNome(String contatoNome) {
		this.contato.setNome(contatoNome);
	}

	public Long getContatoId() {
		return contato.getId();
	}

	public void setContatoId(Long contatoId) {
		this.contato.setId(contatoId);
	}

	public Long getEnderecoId() {
		return contato.getEndereco().getId();
	}

	public void setEnderecoId(Long enderecoId) {
		this.contato.getEndereco().setId(enderecoId);
	}

	public String getEnderecoBairro() {
		return contato.getEndereco().getBairro();
	}

	public void setEnderecoBairro(String enderecoBairro) {
		this.contato.getEndereco().setBairro(enderecoBairro);
	}

	public String getEnderecoCep() {
		return contato.getEndereco().getCep();
	}

	public void setEnderecoCep(String enderecoCep) {
		contato.getEndereco().setCep(enderecoCep);
	}

	public String getEnderecoCidade() {
		return contato.getEndereco().getCidade();
	}

	public void setEnderecoCidade(String enderecoCidade) {
		contato.getEndereco().setCidade(enderecoCidade);
	}

	public String getEnderecoComplemento() {
		return contato.getEndereco().getComplemento();
	}

	public void setEnderecoComplemento(String enderecoComplemento) {
		contato.getEndereco().setComplemento(enderecoComplemento);
	}

	public String getEnderecoEstado() {
		return contato.getEndereco().getEstado();
	}

	public void setEnderecoEstado(String enderecoEstado) {
		contato.getEndereco().setEstado(enderecoEstado);
	}

	public Double getEnderecoLatitude() {
		return contato.getEndereco().getLatitude();
	}

	public void setEnderecoLatitude(Double enderecoLatitude) {
		contato.getEndereco().setLatitude(enderecoLatitude);
	}

	public Double getEnderecoLongitude() {
		return contato.getEndereco().getLongitude();
	}

	public void setEnderecoLongitude(Double enderecoLongitude) {
		contato.getEndereco().setLongitude(enderecoLongitude);
	}

	public String getEnderecoLogradouro() {
		return contato.getEndereco().getLogradouro();
	}

	public void setEnderecoLogradouro(String enderecoLogradouro) {
		contato.getEndereco().setLogradouro(enderecoLogradouro);
	}

	public String getEnderecoNumero() {
		return contato.getEndereco().getNumero();
	}

	public void setEnderecoNumero(String enderecoNumero) {
		contato.getEndereco().setNumero(enderecoNumero);
	}
	
	public String getRazaoNome() {
		return clienteObjeto.getCliente().getRazaoNome();
	}
	
	public void setRazaoNome(String razaoNome) {
		clienteObjeto.getCliente().setRazaoNome(razaoNome);
	}
	
	public Long getClienteId() {
		return clienteObjeto.getCliente().getId();
	}
	
	public void setClienteId(Long id) {
		clienteObjeto.getCliente().setId(id);
	}
	
	public String getClienteCodigo() {
		return clienteObjeto.getCliente().getCodigo();
	}
	
	public void setClienteCodigo(String codigo) {
		clienteObjeto.getCliente().setCodigo(codigo);
	}
	
	public String getClienteCnpjCpf() {
		return clienteObjeto.getCliente().getCnpjCpf();
	}
	public void setClienteCnpjCpf(String cnpjCpf) {
		clienteObjeto.getCliente().setCnpjCpf(cnpjCpf);
	}

	public Long getUnidadeId() {
		return clienteObjeto.getUnidade().getId();
	}

	public void setUnidadeId(Long unidadeId) {
		this.clienteObjeto.getUnidade().setId(unidadeId);
	}
	
	public void setUnidadeNome(String unidadeNome) {
		this.clienteObjeto.getUnidade().setNomeUnidade(unidadeNome);
	}

	public String getUnidadeCodigo() {
		return clienteObjeto.getUnidade().getCodigo();
	}

	public void setUnidadeCodigo(String unidadeCodigo) {
		this.clienteObjeto.getUnidade().setCodigo(unidadeCodigo);
	}
	 
	public void setDepartamentoId(Long departamentoId) {
		this.departamento.setId(departamentoId);
	}
	
	public void setDepartamentoNome(String departamentoNome) {
		this.departamento.setNomeDepartamento(departamentoNome);
	}
	
	public void setDepartamentoCodigo(String departamentoCodigo) {
		this.departamento.setCodigo(departamentoCodigo);
	}
	
	public void setUnidadeDepId(Long unidadeDepId) {
		this.departamento.setUnidade(new Unidade(unidadeDepId));
	}
	
	public void setUnidadeDepNome(String unidadeDepNome) {
		this.departamento.getUnidade().setNome(unidadeDepNome);
	}
	
	public void setUnidadeDepCodigo(String unidadeCodigo) {
		this.departamento.getUnidade().setCodigo(unidadeCodigo);
	}
	
	public void setInformacaoAdicional(String infoAdicional) {
		this.informacaoAdicional = infoAdicional;
	}
	
}
