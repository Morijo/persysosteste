package br.com.empresa.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;

import com.sun.xml.bind.CycleRecoverable;

import br.com.contato.model.Endereco;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.empresa.dao.UnidadeDAO;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IEndereco;
import br.com.model.interfaces.IUnidade;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.resources.exception.PreconditionsREST;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD)
@Entity
@Table(name="unidade")
public class Unidade extends Model<Unidade> implements CycleRecoverable, IUnidade{

	public static final String CONSTRUTOR = "id, codigo, nomeUnidade, telefone, ramal, endereco.id, endereco.cidade";

	@Column(name="nome")
	private String nomeUnidade = null; 

	@Column(name="email")
	private String email = null;

	@Column(name = "telefone", length=12)
	private String telefone = null; 

	@Column(name = "ramal", length=10)
	private String ramal = null; 

	@XmlTransient
	@OneToMany(mappedBy = "unidade", targetEntity = Departamento.class, fetch = FetchType.LAZY, cascade={ CascadeType.ALL})
	private Collection<Departamento> departamento = new ArrayList<Departamento>();

	@XmlTransient
	@OneToMany(mappedBy = "unidade", targetEntity = UnidadeRecurso.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<UnidadeRecurso> unidadeRecurso = new ArrayList<UnidadeRecurso>();

	@OneToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
	@JoinColumn(name = "enderecoidpk")
	private Endereco endereco;

	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "organizacaoidpk", referencedColumnName="id"),
	})
	private Organizacao organizacao;

	public Unidade (){
		super(Unidade.class);
	}

	public Unidade (Long id, String codigo, String nomeUnidade){
		super(Unidade.class);
		setId(id);
		setCodigo(codigo);
		this.nomeUnidade = nomeUnidade;
	}

	public Unidade (Long id, String codigo, String nomeUnidade, String telefone, String ramal,Long idEndereco, String cidade){
		super(Unidade.class);
		setId(id);
		setCodigo(codigo);
		this.nomeUnidade = nomeUnidade;
		this.telefone = telefone;
		this.ramal = ramal;
		this.endereco = new Endereco(idEndereco,cidade, null);
	}

	public Unidade (Long id){
		super(Unidade.class);
		setId(id);
	}

	public String getNome() {
		return nomeUnidade;
	}
	public void setNome(String nome) {
		this.nomeUnidade = nome;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Departamento> getDepartamento() {
		return departamento;
	}
	public void setDepartamento(Collection<Departamento> Departamento) {
		this.departamento = Departamento;
	}

	public Collection<UnidadeRecurso> getUnidadeRecurso() {
		return unidadeRecurso;
	}

	public void setUnidadeRecurso(Collection<UnidadeRecurso> unidadeRecurso) {
		this.unidadeRecurso = unidadeRecurso;
	}

	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(IEndereco endereco) {
		this.endereco = (Endereco) endereco;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public String getNomeUnidade() {
		return nomeUnidade;
	}

	public void setNomeUnidade(String nomeUnidade) {
		this.nomeUnidade = nomeUnidade;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkNotNull(organizacao, "Organização inválida");
		PreconditionsModel.checkEmptyString(nomeUnidade, "Nome da unidade inválido");
		PreconditionsModel.checkNotNull(getConsumerSecret(), "Consumer secret não definido");
		if(getCodigo().isEmpty()){
			setCodigo("PUNI"+countPorConsumerSecret(Unidade.class, getConsumerSecret().getConsumerKey()));
		}
	}

	public static Unidade getUnidadePadrao(Session session, String consumerKey) throws ModelException{
		UnidadeDAO unidadeDAO = new UnidadeDAO();
		try {
			return unidadeDAO.pesquisaUnidadePadrao(Long.parseLong(consumerKey));
		} catch (NumberFormatException e) {
			throw new ModelException(e.getMessage());
		} catch (DAOException e) {
			throw new ModelException("Not found");
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Unidade> busca(String consumerKey, String nomeUnidade, String codigo, Integer statusModel){

		Session session = HibernateHelper.openSession(Unidade.class);
		Transaction tx = session.beginTransaction();

		ArrayList<Unidade> unidadeLista = null;
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();

		try{
			if(nomeUnidade.length() >= 3){
				parameter.add(ParameterDAO.with("nomeUnidade",nomeUnidade,ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo",codigo,ParameterDAOHelper.EQ));
			}else{
				PreconditionsREST.error("not found");
			}
			parameter.add(ParameterDAO.with("consumerSecret", consumerKey,ParameterDAOHelper.EQ));
			unidadeLista =(ArrayList<Unidade>) Unidade.pesquisaLista(session, Unidade.class, parameter);
			tx.commit();
			return unidadeLista;
		}finally{
			tx = null;
			session.close();
			session = null;
			unidadeLista = null;
		}	
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}


}