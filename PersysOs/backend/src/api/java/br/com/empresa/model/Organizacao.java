package br.com.empresa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.contato.model.Endereco;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.empresa.dao.OrganizacaoDAO;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.HashHelper;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.resources.exception.PreconditionsREST;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="organizacao")
public class Organizacao extends Model<Organizacao> implements Serializable, br.com.model.interfaces.IOrganizacao {
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "razaosocial")
	private String razaoSocial = ""; 
	
	@Column(name = "nomefantasia")
	private String nomeFantasia = ""; 
	
	@Column(name = "cnpj", unique = true, length=20)
	private String cnpj = "";
	
	@Column(name = "inscricaoestadual")
	private String inscricaoEstadual = "";
	
	@Column(name = "inscricaomunicipal")
	private String inscricaoMunicipal = "";
	
	@Column(name = "cnae")
	private String cnaeFiscal = "";
	
	@Column(name = "inscricaoestadualsubst")
	private String inscricaoEstadualSubstTributario = "";
	
	@Column(name = "regimetributario")
	private String regimeTributario = "";
	
	@Column(name = "email")
	private String email = "";
	
	@Column(name = "logo")
	private String logo = "";
	
	@XmlTransient
	@Column(name = "logoimage",length=100000)
	@Lob
	private byte[] imagem = null;
	
	@OneToOne(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinColumn(name = "enderecoidpk")
	private Endereco endereco;
		
	@Column(name = "telefonenumero", length=20)
	private String telefone = "";
	
	@OneToMany(mappedBy = "organizacao", targetEntity = Unidade.class, fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    private Collection<Unidade> unidade = new ArrayList<Unidade>();

    public Organizacao(){
    	super(Organizacao.class);
    }
  
    public Organizacao(String razaoSocial, String nomeFantasia, String cnpj) {
		super(Organizacao.class);
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.cnpj = cnpj;
	}
    
    public final static String CONSTRUTOR = "id,codigo,razaoSocial,nomeFantasia,cnpjid,codigo,razaoSocial,nomeFantasia,cnpj";
    
    public Organizacao(Long id, String razaoSocial, String nomeFantasia, String cnpj) {
		super(Organizacao.class);
		setId(id);
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.cnpj = cnpj;
	}
    
    public Organizacao(Long id,String codigo, String razaoSocial, String nomeFantasia, String cnpj) {
  		super(Organizacao.class);
  		setId(id);
  		setCodigo(codigo);
  		this.razaoSocial = razaoSocial;
  		this.nomeFantasia = nomeFantasia;
  		this.cnpj = cnpj;
  	}
    
    public Organizacao(Long id, String razaoSocial, String cnpj) {
		super(Organizacao.class);
		setId(id);
		this.razaoSocial = razaoSocial;
		this.cnpj = cnpj;
	}
	
  //GETTERS AND SETTERS

  	public String getRazaoSocial() {
  		return razaoSocial;
  	}
  	public void setRazaoSocial(String razaoSocial) {
  		this.razaoSocial = razaoSocial;
  	}

  	public String getNomeFantasia() {
  		return nomeFantasia;
  	}
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
	
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getCnaeFiscal() {
		return cnaeFiscal;
	}
	public void setCnaeFiscal(String cnaeFiscal) {
		this.cnaeFiscal = cnaeFiscal;
	}

	public String getInscricaoEstadualSubstTributario() {
		return inscricaoEstadualSubstTributario;
	}
	public void setInscricaoEstadualSubstTributario(String inscricaoEstadualSubstTributario) {
		this.inscricaoEstadualSubstTributario = inscricaoEstadualSubstTributario;
	}

	public String getRegimeTributario() {
		return regimeTributario;
	}
	public void setRegimeTributario(String regimeTributario) {
		this.regimeTributario = regimeTributario;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public Collection<Unidade> getUnidade() {
		return unidade;
	}
	public void setUnidade(Collection<Unidade> unidade) {
		this.unidade = unidade;
	}
	
	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

		@Override
	public void valida() throws ModelException {
		
		cnpj = cnpj.trim().replace(" ", "");
		
		PreconditionsModel.checkEmptyString(razaoSocial,"Razão Social inválida");
		if(nomeFantasia.isEmpty()){
			nomeFantasia = razaoSocial;
		}
		PreconditionsModel.checkValidCPFCNPJ(cnpj, "CNPJ inválido");
		
		setDataAlteracao(new Date());
		setPermitidoExcluir(false);
		
		if(getConsumerSecret() == null){
			ConsumerSecret consumer = new ConsumerSecret();
			consumer.setConsumerSecret(HashHelper.chave(cnpj));
			consumer.setConsumerKey(Long.parseLong(cnpj));
			consumer.salvar();
			this.setConsumerSecret(consumer);
		}
		
		PreconditionsModel.checkNotNull(getConsumerSecret(), "Consumer secret não definido");
		
		if(getCodigo().isEmpty()){
			setCodigo("PORG"+count(Organizacao.class));
		}
	}
	
	public static Organizacao pesquisaOrganizacaoID(Long id){
		
		Organizacao organizacao = null;
	
		try{
			OrganizacaoDAO organizacaoDAO = new OrganizacaoDAO(Organizacao.class);
			organizacao = organizacaoDAO.pesquisaOrganizacaoId(id);
			return organizacao;
		}
		catch (Exception e) {
			return null;
		}	
	}
	
	public static void alteraEndereco(Long id, Endereco endereco) throws ModelException{
		
		try{
			OrganizacaoDAO organizacaoDAO = new OrganizacaoDAO(Organizacao.class);
			organizacaoDAO.alteraEndereco(id, endereco);
		}
		catch (Exception e) {
			throw new ModelException(e.getMessage());
		}	
	}
	
	public static Organizacao pesquisaConsumerSecret(Session session, String consumerSecret){
		
		try{
			OrganizacaoDAO organizacaoDAO = new OrganizacaoDAO(session, Organizacao.class);
			Organizacao organizacao = organizacaoDAO.pesquisaConsumerSecret(consumerSecret);
			return organizacao;
		}
		catch (Exception e) {
			return null;
		}	
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Organizacao> busca(String nomeFantasia, String cnpj,
			String razaoSocial, String codigo, Integer statusModel) throws ModelException {
		
		Session session = HibernateHelper.openSession(Organizacao.class);
		Transaction tx = session.beginTransaction();
		ArrayList<Organizacao> organizacaoLista = null;
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();

		try{

			if(nomeFantasia.length() >= 3){
				parameter.add(ParameterDAO.with("nomeFantasia",nomeFantasia,ParameterDAOHelper.ILIKE));
			}else
				if(razaoSocial.length() >= 3){
					parameter.add(ParameterDAO.with("razaoSocial",razaoSocial,ParameterDAOHelper.ILIKE));
				}else
					if(cnpj.length() >= 3){
						parameter.add(ParameterDAO.with("cnpj",cnpj,ParameterDAOHelper.ILIKE));
					}else if(codigo.length() >= 1){
						parameter.add(ParameterDAO.with("codigo",codigo,ParameterDAOHelper.EQ));
					}else{
						PreconditionsModel.checkNotNull(null, "not found");
					}

			organizacaoLista = (ArrayList<Organizacao>) Organizacao.pesquisaLista(session,Organizacao.class, parameter);
			tx.commit();
			PreconditionsREST.checkNotNull(organizacaoLista, "not found");
			return organizacaoLista;
		}finally{
			organizacaoLista = null;
			parameter = null;
			session.close();
			session = null;
			tx = null;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getId() == null) ? 0 : getId().hashCode());
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
		Organizacao other = (Organizacao) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Organizacao [razaoSocial=" + razaoSocial + ", nomeFantasia="
				+ nomeFantasia + ", cnpj=" + cnpj + ", inscricaoEstadual="
				+ inscricaoEstadual + ", inscricaoMunicipal="
				+ inscricaoMunicipal + ", cnaeFiscal=" + cnaeFiscal
				+ ", inscricaoEstadualSubstTributario="
				+ inscricaoEstadualSubstTributario + ", regimeTributario="
				+ regimeTributario + ", email=" + email + ", logo=" + logo
				+ ", endereco=" + endereco + ", telefone=" + telefone
				+ ", unidade=" + unidade + "]";
	}
}