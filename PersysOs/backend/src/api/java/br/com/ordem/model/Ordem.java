package br.com.ordem.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.com.clienteobjeto.model.ClienteObjeto;
import br.com.contato.model.Contato;
import br.com.empresa.model.Departamento;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.funcionario.model.Funcionario;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.StatusModel;
import br.com.model.interfaces.IOrdem;
import br.com.ordem.dao.OrdemDAO;
import br.com.principal.helper.FormatDateHelper;
import br.com.rest.hateoas.dto.OrdemDTO;
import br.com.rest.represention.JsonDateAdapter;
import br.com.servico.model.Servico;
import br.com.servico.model.ServicoProcedimento;
import br.com.usuario.model.Usuario;

import com.sun.xml.bind.CycleRecoverable;


@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="ordem")
public class Ordem extends Model<Ordem> implements Serializable, IOrdem, CycleRecoverable {

	public static final String CONSTRUTOR = " (id, codigo, assunto, dataCriacao, dataConclusao, situacaoOrdem.nome) from ";

	public static String CONSTRUTORFULL= "id,codigo,assunto,dataCriacao,dataConclusao,situacaoOrdem.id, situacaoOrdem.nome,"
			+ "dataAgendamento,dataAgendamentoInicio, dataAgendamentoFim, clienteObjeto.id, clienteObjeto.codigo,clienteObjeto.cliente.id, clienteObjeto.cliente.razaoNome, clienteObjeto.cliente.cnpjCpf";

	private static final long serialVersionUID = 1L;

	@Column(name = "dataconclusao")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataConclusao = null;

	@Column(name = "datatransferencia")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataTransferencia = null;

	@Column(name = "assunto", nullable=false, length=2000)
	private String assunto = null;

	@Column(name = "informacaoadicional", length=500)
	private String informacaoAdicional = null;

	@Column(name = "fonte", nullable=false, length=50)
	private String fonte = null;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	private SituacaoOrdem situacaoOrdem = null; 	

	@Column(name = "dataagendamento")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAgendamento; 

	//pra quando
	@Column(name = "dataagendamentoinicio")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAgendamentoInicio; 

	//pra quando
	@Column(name = "dataagendamentofim")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAgendamentoFim; 

	@Column(name = "reagendamento")
	private Boolean reagendamento = null;

	@Column(name = "atribuida")
	private Boolean atribuida = null;

	@Column(name = "agendada")
	private Boolean agendada = null;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	private Ordem ordemOrigem = null; 

	@ManyToOne
	@JoinColumn( name = "departamentoidpk")
	private Departamento departamento;

	@ManyToOne
	@JoinColumn( name = "contatoidpk")
	private Contato contato = null;

	@OneToOne
	private Prioridade prioridade;

	@OneToOne
	private ClienteObjeto clienteObjeto;

	@OneToMany(mappedBy = "ordem", targetEntity = Nota.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<Nota> nota = new ArrayList<Nota>();

	@OneToMany(mappedBy = "ordem", targetEntity = ServicoOrdem.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<ServicoOrdem> servicoOrdem = new ArrayList<ServicoOrdem>();

	@OneToMany(mappedBy = "ordem", targetEntity = RecursoOrdem.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<RecursoOrdem> recursoOrdem = new ArrayList<RecursoOrdem>();

	@OneToMany(mappedBy = "ordem", targetEntity = Anexo.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<Anexo> anexo = new ArrayList<Anexo>();

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "ordem", targetEntity = AgendaOrdemFuncionario.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<AgendaOrdemFuncionario> agendaOrdemFuncionario = new ArrayList<AgendaOrdemFuncionario>();


	public Ordem() {
		super(Ordem.class);
	}

	public Ordem(Long id) {
		super(Ordem.class);
		setId(id);
	}
	
	public Ordem(Long id, String codigo, Integer statusModel, Date dataAgendamentoInicio, Date dataAgendamentoFim) {
		super(Ordem.class);
		setId(id);
		setCodigo(codigo);
		setStatusModel(statusModel);
		setDataAgendamentoInicio(dataAgendamentoInicio);
		setDataAgendamentoFim(dataAgendamentoFim);
	}

	public Ordem(Long id, String codigo, String assunto, Date dataCriacao, Date dataConclusao,Long situacaoId, String situacaoOrdem,
			Date dataAgendamento,Date dataAgendamentoInicio, Date dataAgendamentoFim, Long clienteObjetoId, String clienteObjetoCodigo, Long clienteId, String clienteRazao,
			String clienteCpf) {
		super(Ordem.class);
		setId(id);
		setCodigo(codigo);
		this.assunto = assunto;
		this.setDataCriacao(dataCriacao);
		this.setDataConclusao(dataConclusao);
		this.setSituacaoOrdem(new SituacaoOrdem(situacaoId, "", situacaoOrdem));
		this.dataAgendamento = dataAgendamento;
		this.dataAgendamentoInicio = dataAgendamento;
		this.dataAgendamentoFim = dataAgendamentoFim;
		this.setClienteObjeto(new ClienteObjeto(clienteObjetoId, clienteObjetoCodigo,clienteId,clienteRazao,clienteCpf));
	}

	public Ordem(Long id, String codigo, String assunto, Date dataCriacao, Date dataConclusao, String situacaoOrdem) {
		super(Ordem.class);
		setId(id);
		setCodigo(codigo);
		this.assunto = assunto;
		this.setDataCriacao(dataCriacao);
		this.setDataConclusao(dataConclusao);
		this.setSituacaoOrdem(new SituacaoOrdem(null, "", situacaoOrdem));
	}

	public ClienteObjeto getClienteObjeto() {
		return clienteObjeto;
	}

	public void setClienteObjeto(ClienteObjeto clienteObjeto) {
		this.clienteObjeto = clienteObjeto;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	/**
	 * @return the dataConclusao
	 */
	public Date getDataConclusao() {
		return dataConclusao;
	}

	/**
	 * @param dataConclusao the dataConclusao to set
	 */
	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}


	/**
	 * @return the assunto
	 */
	public String getAssunto() {
		return assunto;
	}

	/**
	 * @param assunto the assunto to set
	 */
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	/**
	 * @return the fonte
	 */
	public String getFonte() {
		return fonte;
	}

	/**
	 * @param fonte the fonte to set
	 */
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public SituacaoOrdem getSituacaoOrdem() {
		return situacaoOrdem;
	}

	public void setSituacaoOrdem(SituacaoOrdem situacaoOrdem) {
		this.situacaoOrdem = situacaoOrdem;
	}

	/**
	 * @return the departamento
	 */
	public Departamento getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return the prioridade
	 */
	public Prioridade getPrioridade() {
		return prioridade;
	}

	/**
	 * @param prioridade the prioridade to set
	 */
	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}


	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public Boolean getReagendamento() {
		return reagendamento;
	}

	public void setReagendamento(Boolean reagendamento) {
		this.reagendamento = reagendamento;
	}

	public Ordem getOrdemOrigem() {
		return ordemOrigem;
	}

	public void setOrdemOrigem(Ordem ordemOrigem) {
		this.ordemOrigem = ordemOrigem;
	}


	public Boolean getAtribuida() {
		return atribuida;
	}

	public void setAtribuida(Boolean atribuida) {
		this.atribuida = atribuida;
	}

	public String getInformacaoAdicional() {
		return informacaoAdicional;
	}

	public void setInformacaoAdicional(String informacaoAdicional) {
		this.informacaoAdicional = informacaoAdicional;
	}

	public Date getDataAgendamentoFim() {
		return dataAgendamentoFim;
	}

	public void setDataAgendamentoFim(Date dataAgendamentoFim) {
		this.dataAgendamentoFim = dataAgendamentoFim;
	}

	public Boolean getAgendada() {
		return agendada;
	}

	public void setAgendada(Boolean agendada) {
		this.agendada = agendada;
	}

	public Collection<AgendaOrdemFuncionario> getAgendaOrdemFuncionario() {
		return agendaOrdemFuncionario;
	}

	public void setAgendaOrdemFuncionario(
			Collection<AgendaOrdemFuncionario> agendaOrdemFuncionario) {
		this.agendaOrdemFuncionario = agendaOrdemFuncionario;
	}

	/**
	 * @return the notas
	 */
	public Collection<Nota> getNota() {
		if(nota == null){
			nota = new ArrayList<Nota>();
		}
		return nota;
	}

	/**
	 * @param notas the notas to set
	 */
	public void setNota(Collection<Nota> nota) {
		this.nota = nota;
	}


	private void gerarNota(String titulo, Usuario usuario, String consumerKey) {
		try{
			Nota nota = new Nota();
			nota.setTitulo("Nota Criada: " + FormatDateHelper.formatTimeZoneUSToBR(new Date().getTime())+ " por: " + usuario.getNomeUsuario());
			nota.setNota(titulo);
			nota.setUsuario(usuario);
			nota.setOrdem(this);
			nota.setDataCriacao(new Date());
			nota.setConsumerId(Long.parseLong(consumerKey));
			nota.salvar();
		}catch(Exception e){
			e.getMessage();
		}
	}

	public Date getDataTransferencia() {
		return dataTransferencia;
	}

	public void setDataTransferencia(Date dataTransferencia) {
		this.dataTransferencia = dataTransferencia;
	}

	public Date getDataAgendamentoInicio() {
		return dataAgendamentoInicio;
	}

	public void setDataAgendamentoInicio(Date dataAgendamentoInicio) {
		this.dataAgendamentoInicio = dataAgendamentoInicio;
	}

	public Collection<ServicoOrdem> getServicoOrdem() {
		if(servicoOrdem == null){
			servicoOrdem = new ArrayList<ServicoOrdem>();
		}
		return servicoOrdem;
	}

	public void setServicoOrdem(Collection<ServicoOrdem> servicoOrdem) {
		this.servicoOrdem = servicoOrdem;
	}

	public Collection<Anexo> getAnexo() {
		if(anexo == null){
			anexo = new ArrayList<Anexo>();
		}
		return anexo;
	}


	public Collection<RecursoOrdem> getRecursoOrdem() {
		if(recursoOrdem == null){
			recursoOrdem = new ArrayList<RecursoOrdem>();
		}
		return recursoOrdem;
	}

	public void setRecursoOrdem(Collection<RecursoOrdem> recursoOrdem) {
		this.recursoOrdem = recursoOrdem;
	}

	public void adicionarRecurso(RecursoOrdem recurso, Usuario usuario,String consumerKey) throws ModelException{
		gerarNota("Novo Recurso: "+ recurso.getRecurso().getNome(), usuario, consumerKey);
		recurso.setOrdem(this);
		recurso.salvar();
		alteraDataAlteracao();
	}

	public void alteraRecurso(RecursoOrdem recurso, Long consumerKey, Usuario usuario) throws ModelException{
		recurso.altera(consumerKey);
		alteraDataAlteracao();
	}

	public void removeRecurso(RecursoOrdem recurso, Long consumerKey, Usuario usuario) throws ModelException{
		recurso.setStatusModel(2);
		recurso.altera(consumerKey);
		alteraDataAlteracao();
	}

	public void setAnexo(Collection<Anexo> anexo) {
		this.anexo = anexo;
	}

	public ServicoOrdem adicionarServico(Servico servico, Usuario usuario) throws ModelException{

		ServicoOrdem servicoOrdem = new ServicoOrdem();
		servicoOrdem.setOrdem(this);
		servicoOrdem.setServico(servico);
		servicoOrdem.setConsumerSecret(this.getConsumerSecret());
		getServicoOrdem().add(servicoOrdem);
		servicoOrdem.salvar();

		try{
			ArrayList<ServicoProcedimento> arrayList = ServicoProcedimento.listaServicoProcedimento(servico.getId(),String.valueOf(this.getConsumerSecret().getConsumerKey()));
			for(ServicoProcedimento servicoProcedimento : arrayList){
				if(servicoProcedimento.getStatusModel() == 1){
					OrdemProcedimento ordemProcedimento = new OrdemProcedimento();
					ordemProcedimento.setOrdem(this);
					ordemProcedimento.setServico(servicoProcedimento.getServico());
					ordemProcedimento.setProcedimento(servicoProcedimento.getProcedimento());
					ordemProcedimento.setObrigatorio(servicoProcedimento.isObrigatorio());
					ordemProcedimento.setAnexo(servicoProcedimento.isAnexo());
					ordemProcedimento.setStatusModel(1);
					ordemProcedimento.salvar();
				}
			}
		}catch(Exception e){}

		alteraDataAlteracao();
		gerarNota("Serviço "+servico.getTitulo() +  " incluído", usuario, this.getConsumerSecret().getConsumerKey().toString());

		return servicoOrdem;
	}

	public void alteraServico(ServicoOrdem servico, Usuario usuario) throws ModelException{
		servico.altera();
		OrdemProcedimento.alteraOrdemProcedimentoSituacao(getId(), servico.getServico().getId(), servico.getStatusModel());
		alteraDataAlteracao();
	}

	public void removeServico(ServicoOrdem servico, Usuario usuario) throws ModelException{
		servico.setStatusModel(2);
		servico.altera();
		OrdemProcedimento.alteraOrdemProcedimentoSituacao(getId(), servico.getServico().getId(), servico.getStatusModel());
		alteraDataAlteracao();
	}

	public void adicionarContratoCliente(ClienteObjeto clienteObjeto, Usuario usuario, String consumerKey) throws ModelException{
		setClienteObjeto(clienteObjeto);
		alteraClienteObjeto();
		gerarNota(clienteObjeto.getCodigo(), usuario, consumerKey);

	}
	public void adicionarContato(Contato contato) throws ModelException{
		this.setContato(contato);;
		alteraContato();
	}


	public void adicionarAnexo(Anexo anexo, Funcionario usuario, String consumerKey) throws ModelException{
		gerarNota("Um anexo incluído", usuario, consumerKey);
		getAnexo().add(anexo);
		alterar();
	}


	public Collection<AgendaOrdemFuncionario> getAgenda() {
		if(agendaOrdemFuncionario == null){
			agendaOrdemFuncionario = new ArrayList<AgendaOrdemFuncionario>();
		}
		return agendaOrdemFuncionario;
	}

	public void setAgenda(Collection<AgendaOrdemFuncionario> agenda) {
		this.agendaOrdemFuncionario = agenda;
	}


	public AgendaOrdemFuncionario adicionarFuncionarioExecucao(AgendaOrdemFuncionario agendaOrdem, Usuario funcionarioAgendamento, String consumerKey) throws ModelException{

		PreconditionsModel.checkNotNull(agendaOrdem, "Dados inválidos");
		PreconditionsModel.checkNotNull(agendaOrdem.getFuncionario(), "Funcionário inválido");


		agendaOrdem.setOrdem(this);
		agendaOrdem.setConsumerSecret(getConsumerSecret());
		agendaOrdem.setDataAlteracao(new Date());
		agendaOrdem.setDataCriacao(new Date());
		agendaOrdem.setStatusModel(StatusModel.ATIVO);
		agendaOrdem.salvar();

		setAtribuida(true);
		alteraFuncionarioExecucao();

		gerarNota("Ordem Atribuida " + agendaOrdem.getFuncionario().getNomeUsuario(), funcionarioAgendamento, consumerKey);

		return agendaOrdem;
	}

	public Ordem adicionarAgendamento(Ordem ordemServico, Usuario funcionarioAgendamento, String consumerKey) throws ModelException{

		if(ordemServico.getDataAgendamento() == null)
			ordemServico.setDataAgendamento(new Date());
		
		gerarNota("Ordem Agendada para " + FormatDateHelper.formatTimeZoneUSToBR(ordemServico.getDataAgendamento().getTime()),
				funcionarioAgendamento, consumerKey);

		if(this.getAgendada())
			setReagendamento(true);

		setDataAgendamento(ordemServico.getDataAgendamento());
		setAgendada(true);
		setReagendamento(false);
		setSituacaoOrdem(SituacaoOrdem.getSituacaoOrdem("PS3",consumerKey, 1));
		alteraDataAgendamento();
		return ordemServico;
	}

	public Ordem cancelarAgendamento(Ordem ordemServico, Usuario usuario, String consumerKey) throws ModelException{

		gerarNota("Agendamento cancelado em " + FormatDateHelper.formatTimeZoneUSToBR(ordemServico.getDataAgendamento().getTime()),
				usuario, consumerKey);

		setAgendada(false);
		setReagendamento(true);
		setDataAgendamento(null);
		setDataAgendamentoFim(null);
		setDataAgendamentoInicio(null);
		setSituacaoOrdem(SituacaoOrdem.getSituacaoOrdem("PS2",consumerKey, 1));
		alteraDataAgendamento();

		return ordemServico;
	}

	public void criarOrdem(Usuario usuario, String consumerKey) throws ModelException{
		atribuida = false;
		agendada = false;
		reagendamento = false;

		//limpa contato
		Contato contato = this.getContato();
		contato.setId(null);
		contato.setCodigo("");

		if(contato.getEndereco() != null){
			contato.getEndereco().setId(null);
			contato.getEndereco().setCodigo("");
			contato.getEndereco().setConsumerSecret(getConsumerSecret());
			contato.setConsumerSecret(getConsumerSecret());
			contato.getEndereco().salvar();
			contato.salvar();
		}
		this.setContato(contato);

		salvar();
		gerarNota("Ordem criada: "+ fonte, usuario, consumerKey);
	}

	public void finalizar(Funcionario funcionario, String consumerKey){
		gerarNota("Ordem Finalizada", funcionario, consumerKey);
	}

	public void reabrir(Funcionario funcionario, String consumerKey){
		gerarNota("Ordem Ativa", funcionario, consumerKey);
	}

	public boolean alteraOrdemCustomizado(Usuario usuario, String consumerKey) throws ModelException{
		OrdemDAO ordemDao = new OrdemDAO(OrdemDAO.class);
		try {
			ordemDao.alteraOrdemCustomizado(this);
			gerarNota("Ordem alterada", usuario, consumerKey);

			return true;
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	public boolean alteraOrdemSituacao(Usuario usuario, String consumerKey) throws ModelException{
		OrdemDAO ordemDao = new OrdemDAO(OrdemDAO.class);
		try {
			ordemDao.alteraOrdemSituacao(this);
			gerarNota("Situação da ordem alterada ", usuario, consumerKey);
			return true;
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	private void alteraDataAgendamento() throws ModelException {
		OrdemDAO ordemDAO = new OrdemDAO(Ordem.class);
		try {
			ordemDAO.alteraDataAgendamento(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	private void alteraFuncionarioExecucao() throws ModelException {
		try {
			OrdemDAO.alteraFuncionarioExecucao(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	public boolean alteraDataAlteracao() throws ModelException{
		try {
			return OrdemDAO.alteraDataAlteracao(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	public boolean alteraClienteObjeto() throws ModelException{
		try {
			return OrdemDAO.alteraContrato(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	public boolean alteraContato() throws ModelException{
		try {
			return OrdemDAO.alteraContato(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	public static ArrayList<Ordem> listaOrdem(Integer statusModel, String consumerKey) {
		OrdemDAO ordemDAO = new OrdemDAO(Ordem.class);
		try {
			return ordemDAO.listaOrdem(statusModel,consumerKey);
		} catch (DAOException e) {
			return new ArrayList<Ordem>();
		}
	}

	public static ArrayList<OrdemDTO> listaOrdem(Session session,Integer inicial, Integer tamanho, String cs, Boolean agendada) {
		OrdemDAO ordemDAO = new OrdemDAO(Ordem.class);
		return ordemDAO.listaOrdem(session,inicial,tamanho, cs, agendada);
	}

	public static ArrayList<OrdemDTO> listaOrdem(Session session,Integer inicial, Integer tamanho, String cs, 
			Long idFuncionario, Date dataInicio, Date dataFim) {
		OrdemDAO ordemDAO = new OrdemDAO(Ordem.class);
		return ordemDAO.listaOrdemPorFuncionario(session, inicial, tamanho, cs, idFuncionario, dataInicio, dataFim);
	}

	public static ArrayList<AgendaOrdemFuncionario> listaOrdemAgendamentoPorFuncionario(Integer inicial, Integer tamanho, String cs, 
			Long idFuncionario, Date dataInicio, Date dataFim) {
		OrdemDAO ordemDAO = new OrdemDAO(Ordem.class);
		try {
			return ordemDAO.listaOrdemAgendamentoPorFuncionario(inicial, tamanho, cs, idFuncionario, dataInicio, dataFim);
		} catch (DAOException e) {
			return new ArrayList<AgendaOrdemFuncionario>();
		}
	}

	public static ArrayList<AgendaOrdemFuncionario> listaOrdemAgendamento(Integer inicial, Integer tamanho, String cs, 
			Date dataInicio, Date dataFim) {
		OrdemDAO ordemDAO = new OrdemDAO(Ordem.class);
		try {
			return ordemDAO.listaOrdemAgendamentoGeral(inicial, tamanho, cs, dataInicio, dataFim);
		} catch (DAOException e) {
			return new ArrayList<AgendaOrdemFuncionario>();
		}
	}

	public static ArrayList<OrdemDTO> listaOrdemAgendamento(Session session,Integer inicial, Integer tamanho, String cs, 
			Date dataInicio, Date dataFim) {
		OrdemDAO ordemDAO = new OrdemDAO(Ordem.class);
		return ordemDAO.listaOrdemAgendamento(session, inicial, tamanho, cs, dataInicio, dataFim);
	}

	public static OrdemDTO getOrdem(Session session,Long id, String cs) {
		OrdemDAO ordemDAO = new OrdemDAO(Ordem.class);
		return ordemDAO.pesquisaOrdem(session,cs,id);
	}

	public static Ordem pesquisaOrdem(Long id){
		OrdemDAO ordemDao = new OrdemDAO(Ordem.class);
		return ordemDao.pesquisaOrdemId(id);
	}

	public static Ordem pesquisaOrdem(Long id, int statusModel, String consumerKey){
		OrdemDAO ordemDao = new OrdemDAO(Ordem.class);
		try {
			return ordemDao.pesquisaOrdem(id, statusModel, consumerKey);
		} catch (DAOException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "Ordem [dataConclusao=" + dataConclusao
				+ ", dataUltimaMensagem="
				+ dataTransferencia + ", assunto=" + assunto + ", fonte="
				+ fonte + ", status=" + situacaoOrdem + ", departamento="
				+ departamento + ", cliente=" + clienteObjeto + ", prioridade="
				+ prioridade + ", nota=" + nota
				+ "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataConclusao == null) ? 0 : dataConclusao.hashCode());
		result = prime
				* result
				+ ((dataTransferencia == null) ? 0 : dataTransferencia
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ordem other = (Ordem) obj;
		if (dataConclusao == null) {
			if (other.dataConclusao != null)
				return false;
		} else if (!dataConclusao.equals(other.dataConclusao))
			return false;
		if (dataTransferencia == null) {
			if (other.dataTransferencia != null)
				return false;
		} else if (!dataTransferencia.equals(other.dataTransferencia))
			return false;
		return true;
	}

	@Override
	public void valida() throws ModelException {

		PreconditionsModel.checkNotNull(this.getContato(),"invalid contato");

		if(dataAgendamento != null){
			setAgendada(true);
		}

		if(getCodigo().isEmpty()){
			setCodigo("POS"+new SimpleDateFormat("MMyyyy").format(new Date())+count(Ordem.class));
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		Ordem ordem = new Ordem();
		return ordem;
	}
}
