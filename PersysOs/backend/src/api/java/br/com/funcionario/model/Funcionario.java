package br.com.funcionario.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import br.com.funcionario.dao.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.sun.xml.bind.CycleRecoverable;

import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.empresa.model.Departamento;
import br.com.empresa.model.Unidade;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IDepartamento;
import br.com.model.interfaces.IFuncionario;
import br.com.ordem.model.AgendaOrdemFuncionario;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.represention.JsonDateAdapter;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.usuario.model.GrupoUsuario;
import br.com.usuario.model.Usuario;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="funcionario")
@PrimaryKeyJoinColumn(name="id")
public class Funcionario extends Usuario implements Serializable, CycleRecoverable, IFuncionario{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "situacao")
	private String situacao = null;

	@Column(name = "tipofuncionario") //(horista, mensalista)
	private String tipoFuncionario = null;

	@Column(name = "desligamento")
	private String  motivoDesligamento = null;

	@Column(name = "datanascimento")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataNascimento = null;

	@Column(name = "nomepai")
	private String nomePai = null;

	@Column(name = "nomemae")
	private String nomeMae = null;

	@Column(name = "numeropis")
	private String numeroPis = null;

	@Column(name = "ctps")
	private String ctps = null;

	@Column(name = "estadocivil")
	private String estadoCivil = null;

	@Column(name = "dataadmissao")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAdmissao = null;	

	@Column(name = "datademissao")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataDemissao = null;	

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumn(name = "departamentoidpk")
	private Departamento departamento = null;

	@XmlTransient
	@OneToMany(mappedBy = "funcionario", targetEntity = AgendaOrdemFuncionario.class, fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	private Collection<AgendaOrdemFuncionario> agendaOrdemFuncionario = new ArrayList<AgendaOrdemFuncionario>();

	@OneToOne
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "rastreadoridpk")
	private Rastreador rastreador = null;

	public Funcionario(){
		super(Funcionario.class);
	}

	public static final String CONSTRUTORF = "id,codigo,statusModel,razaoNome,nomeUsuario,fantasiaSobrenome,"
			+ "cnpjCpf,ieRg,dataNascimento,estadoCivil,timezone,emailPrincipal,departamento.id,"
			+ "departamento.unidade.id, grupoUsuario.id";

	public Funcionario(Long id,String codigo, Integer statusModel,String nome,String nomeUsuario,String nomeFantasia, 
			String cpf,String ieRg, Date dataNascimento, String estadoCivil, String timezone, String emailPrincipal,
			Long departamentoId,Long unidadeId, Long grupoUsuarioId) {
		super(Funcionario.class);
		this.setId(id);
		setCodigo(codigo);
		setStatusModel(statusModel);
		setRazaoNome(nome);
		setNomeUsuario(nomeUsuario);
		setFantasiaSobrenome(nomeFantasia);
		setCnpjCpf(cpf);
		setIeRg(ieRg);
		setDataNascimento(dataNascimento);
		setEstadoCivil(estadoCivil);
		setTimezone(timezone);
		setEmailPrincipal(emailPrincipal);

		Departamento departamento = new Departamento();
		departamento.setId(departamentoId);

		Unidade unidade = new Unidade(unidadeId);
		departamento.setUnidade(unidade);

		setDepartamento(departamento);

		setGrupoUsuario(new GrupoUsuario(grupoUsuarioId));
	}

	public static final String CONSTRUTOR = "id,codigo,statusModel,razaoNome,cnpjCpf,nomeUsuario";

	public Funcionario(Long id,String codigo, Integer statusModel,String nome, String cpf, String nomeUsuario) {
		super(Funcionario.class);
		this.setId(id);
		setCodigo(codigo);
		setNomeUsuario(nomeUsuario);
		setRazaoNome(nome);
		setCnpjCpf(cpf);
		setStatusModel(statusModel);
	}

	public Funcionario(Long id,String codigo,String nome, String cpf, String nomeUsuario) {
		super(Funcionario.class);
		this.setId(id);
		setCodigo(codigo);
		setNomeUsuario(nomeUsuario);
		setRazaoNome(nome);
		setCnpjCpf(cpf);
		this.setNomeUsuario(nomeUsuario);
	}

	public Funcionario(Long id,String nome, String cpf) {
		super(Funcionario.class);
		this.setId(id);
		setRazaoNome(nome);
		setCnpjCpf(cpf);
	}

	/**
	 * @return the tipofuncionario
	 */
	public String getTipoFuncionario() {
		return tipoFuncionario;
	}

	/**
	 * @param tipofuncionario the tipofuncionario to set
	 */
	public void setTipoFuncionario(String tipoFuncionario) {
		this.tipoFuncionario = tipoFuncionario;
	}

	/**
	 * @return the desligamento
	 */
	public String getDesligamento() {
		return motivoDesligamento;
	}

	/**
	 * @param desligamento the desligamento to set
	 */
	public void setDesligamento(String desligamento) {
		this.motivoDesligamento = desligamento;
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

	public void setDataAdmissao(Date dataadmissao) {
		this.dataAdmissao = dataadmissao;
	}

	public Date getDataDemissao() {
		return dataDemissao;
	}

	public void setDataDemissao(Date datademissao) {
		this.dataDemissao = datademissao;
	}

	/**
	 * @return the datanascimento
	 */
	public Date getDataNascimento() {
		return dataNascimento;
	}

	/**
	 * @param datanascimento the datanascimento to set
	 */
	public void setDataNascimento(Date datanascimento) {
		this.dataNascimento = datanascimento;
	}

	/**
	 * @return the nomePai
	 */
	public String getNomePai() {
		return nomePai;
	}

	/**
	 * @param nomePai the nomePai to set
	 */
	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	/**
	 * @return the nomeMae
	 */
	public String getNomeMae() {
		return nomeMae;
	}

	/**
	 * @param nomeMae the nomeMae to set
	 */
	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	/**
	 * @return the numeropis
	 */
	public String getNumeroPis() {
		return numeroPis;
	}

	/**
	 * @param numeropis the numeropis to set
	 */
	public void setNumeroPis(String numeropis) {
		this.numeroPis = numeropis;
	}

	/**
	 * @return the motivoDesligamento
	 */
	public String getMotivoDesligamento() {
		return motivoDesligamento;
	}

	/**
	 * @param motivoDesligamento the motivoDesligamento to set
	 */
	public void setMotivoDesligamento(String motivoDesligamento) {
		this.motivoDesligamento = motivoDesligamento;
	}

	/**
	 * @return the departamento
	 */
	public IDepartamento getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(IDepartamento departamento) {
		this.departamento = (Departamento) departamento;
	}

	public Rastreador getRastreador() {
		return rastreador;
	}

	public void setRastreador(Rastreador rastreador) {
		this.rastreador = rastreador;
	}

	public Collection<AgendaOrdemFuncionario> getAgendaOrdemFuncionario() {
		return agendaOrdemFuncionario;
	}

	public void setAgendaOrdemfuncionario(
			Collection<AgendaOrdemFuncionario> agendaOrdemFuncionario) {
		this.agendaOrdemFuncionario = agendaOrdemFuncionario;
	}

	public static ArrayList<Funcionario> listaFuncionario(String consumerKey){
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		try {
			return funcionarioDAO.listaFuncionario(Long.parseLong(consumerKey));
		} catch (DAOException e) {
			return new ArrayList<Funcionario>();
		}
	}

	public static Funcionario pesquisaFuncionario(Long idFuncionario, String consumerKey){
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		try {
			return funcionarioDAO.pequisaFuncionario(idFuncionario, consumerKey);
		} catch (DAOException e) {
			return new Funcionario();
		}
	}

	public static Funcionario pesquisaFuncionarioPorCodigo(String codigo, String consumerKey){
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		try {
			return funcionarioDAO.pequisaFuncionarioPorCodigo(codigo, consumerKey);
		} catch (DAOException e) {
			return new Funcionario();
		}
	}
	
	public void alteraFuncionario() throws ModelException{
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		try {
			funcionarioDAO.alteraFuncionario(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Funcionario> busca(String nome, String cpf, String rg, String nomeUsuario, Integer statusModel,
			String consumerKey) throws ModelException {

		ArrayList<Funcionario> funcionarioLista = null;
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		Session session = HibernateHelper.openSession(Funcionario.class.getClass());
		Transaction tx = session.beginTransaction();

		try{
			if(nome.length() >= 3){
				parameter.add(ParameterDAO.with("razaoNome","%"+nome+"%",ParameterDAOHelper.ILIKE));
			}else
				if(cpf.length() >= 3){
					parameter.add(ParameterDAO.with("cnpjCpf","%"+cpf+"%",ParameterDAOHelper.ILIKE));
				}else
					if(rg.length() >= 3){
						parameter.add(ParameterDAO.with("ieRg","%"+rg+"%",ParameterDAOHelper.ILIKE));
					}else if(nomeUsuario.length() >= 3){
						parameter.add(ParameterDAO.with("nomeUsuario","%"+nomeUsuario+"%",ParameterDAOHelper.ILIKE));
					}else{
						PreconditionsREST.error("invalid parameter");
					}

			parameter.add(ParameterDAO.with("statusModel", statusModel,ParameterDAOHelper.EQ));
			funcionarioLista = (ArrayList<Funcionario>) Funcionario.pesquisaListaPorConsumerSecret(session, Funcionario.class,consumerKey, parameter);
			tx.commit();
			return funcionarioLista;
		}finally{
			session.close();
			session = null;
			tx = null;
		}
	}

	@Override
	public void valida() throws ModelException {

		super.valida();
		PreconditionsModel.checkEmptyString(getRazaoNome(), "Nome inv��lido");
		PreconditionsModel.checkNotNull(getConsumerSecret(), "Consumer secret n��o definido");

		if(getCodigo().isEmpty())
			setCodigo("PEMP"+countPorConsumerSecret(Funcionario.class,getConsumerSecret().getConsumerKey()));

	}

	@Override
	public Object onCycleDetected(Context arg0) {
		Funcionario obj = new Funcionario();  
		return obj;  
	}
	
	@Override
	public String toString() {
		return "Funcionario [situacao=" + situacao + ", tipoFuncionario="
				+ tipoFuncionario + ", motivoDesligamento="
				+ motivoDesligamento + ", dataNascimento=" + dataNascimento
				+ ", nomePai=" + nomePai + ", nomeMae=" + nomeMae
				+ ", numeroPis=" + numeroPis + ", ctps=" + ctps
				+ ", estadoCivil=" + estadoCivil + ", dataAdmissao="
				+ dataAdmissao + ", dataDemissao=" + dataDemissao +", departamento=" + departamento
				+ ", agendaOrdemFuncionario=" + agendaOrdemFuncionario
				+ ", rastreador=" + rastreador + "]";
	}
}