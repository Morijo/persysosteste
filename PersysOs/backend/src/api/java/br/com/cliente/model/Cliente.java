package br.com.cliente.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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

import com.sun.xml.bind.CycleRecoverable;

import br.com.cliente.dao.ClienteDAO;
import br.com.clienteobjeto.model.ClienteObjeto;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.funcionario.model.Funcionario;
import br.com.model.PreconditionsModel;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.represention.JsonDateAdapter;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.usuario.model.Usuario;

/**
 * Modelo Cliente extende a {@link Usuario}
 * 
 *@author ricardosabatine
 *@version 1.0
 *@since 1.0
 *@see Usuario
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="cliente")
@PrimaryKeyJoinColumn(name="id")
public class Cliente extends Usuario  implements br.com.model.interfaces.ICliente, Serializable, CycleRecoverable {

	public static final String CONSTRUTOR = "id,codigo,razaoNome,cnpjCpf,nomeUsuario";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "situacao", length=100) //regular, pendente...
	private String  situacao = null;

	@Column(name = "tipocliente") //fisico ou juridico
	private String tipoCliente = null;
	
	@Column(name = "site")
	private String site = null;
	
	@Column(name = "datanascimento")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataNascimento;	
	
	@XmlTransient
	@OneToMany(mappedBy = "cliente", targetEntity = ClienteObjeto.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<ClienteObjeto> clienteObjeto = new ArrayList<ClienteObjeto>();
	
	public Cliente(){
		super(Cliente.class);
	}
	
	public Cliente(Long id, String codigo, String razao, String cnpjcpf, String nomeUsuario){
		super(Cliente.class);
		setId(id);
		setCodigo(codigo);
		setNomeUsuario(nomeUsuario);
		setRazaoNome(razao);
		setCnpjCpf(cnpjcpf);
	}
	
	public Cliente(Long id, String codigo, String razao, String cnpjcpf){
		super(Cliente.class);
		setId(id);
		setCodigo(codigo);
		setRazaoNome(razao);
		setCnpjCpf(cnpjcpf);
	}
	
	public Collection<ClienteObjeto> getClienteOrdem() {
		return clienteObjeto;
	}

	public void setClienteOrdem(Collection<ClienteObjeto> clienteOrdem) {
		this.clienteObjeto = clienteOrdem;
	}

	public void addClienteOrdem(ClienteObjeto clienteOrdem) {
		if(this.clienteObjeto == null)
			this.clienteObjeto = new ArrayList<ClienteObjeto>();
		this.clienteObjeto.add(clienteOrdem);
	}

	public String getSituacao() {
		if(situacao == null){
		  situacao = new String();
		}
		return situacao;
	}
	
	public void setSituacao(String situacao) {
			this.situacao = situacao;
	}
	
	public String getTipoCliente() {
		if(tipoCliente == null){
			   tipoCliente = new String();
		}
	
		return tipoCliente;
	}
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	
	public String getSite() {
		return site;
	}
	
	public void setSite(String site) {
		this.site = site;
	}
	
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;;
	}

	@Override
	public void valida() throws ModelException {
		
		super.valida();
		
		PreconditionsModel.checkValidCPFCNPJ(getCnpjCpf(), "CPF ou CNPJ inv��lido");
		PreconditionsModel.checkEmptyString(getRazaoNome(), "Razao/Nome inv��lido");
		PreconditionsModel.checkNotNull(getConsumerSecret(), "ConsumerKey not found");
		
		if(getNomeUsuario() != null || getNomeUsuario().isEmpty())
			setNomeUsuario("C"+getCnpjCpf());
		
		setGrupoUsuario(null);
		
		if(getCodigo().isEmpty())
			setCodigo("PCL"+countPorConsumerSecret(Cliente.class,getConsumerSecret().getConsumerKey()));
		
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Cliente> busca(String razaoSocial, String nomeFantasia, String cnpj, String codigo, Integer statusModel,
			String consumerKey) throws ModelException {

		ArrayList<Cliente> clienteLista = new ArrayList<Cliente>();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		Session session = HibernateHelper.openSession(Funcionario.class.getClass());
		Transaction tx = session.beginTransaction();

		try{
			if(razaoSocial.length() >= 3){
				parameter.add(ParameterDAO.with("razaoNome","%"+razaoSocial+"%",ParameterDAOHelper.ILIKE));
			}else if(nomeFantasia.length() >= 3){
				parameter.add(ParameterDAO.with("fantasiaSobrenome","%"+nomeFantasia+"%",ParameterDAOHelper.ILIKE));
			}else if(cnpj.length() >= 3){
				parameter.add(ParameterDAO.with("cnpjCpf","%"+cnpj+"%",ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 3){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.ILIKE));
			}else{
				PreconditionsREST.error("invalid parameter");
			}

			parameter.add(ParameterDAO.with("statusModel", statusModel,ParameterDAOHelper.EQ));
			clienteLista = (ArrayList<Cliente>) Cliente.pesquisaListaPorConsumerSecret(session, Cliente.class,consumerKey, parameter);
			tx.commit();
			return clienteLista;
		}finally{
			session.close();
			session = null;
			tx = null;
		}
	}
	
	public void altera() throws ModelException{
		ClienteDAO clienteDAO = new ClienteDAO();
		try {
			clienteDAO.alteraCliente(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	@Override
	public String toString() {
		return "Cliente [situacao=" + situacao + ", tipoCliente=" + tipoCliente
				+ ", site=" + site + ", dataNascimento=" + dataNascimento
				+ ", validador=" +", clienteOrdem="
				+ clienteObjeto + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((clienteObjeto == null) ? 0 : clienteObjeto.hashCode());
		result = prime * result
				+ ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result
				+ ((situacao == null) ? 0 : situacao.hashCode());
		result = prime * result
				+ ((tipoCliente == null) ? 0 : tipoCliente.hashCode());
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
		Cliente other = (Cliente) obj;
		if (clienteObjeto == null) {
			if (other.clienteObjeto != null)
				return false;
		} else if (!clienteObjeto.equals(other.clienteObjeto))
			return false;
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (situacao == null) {
			if (other.situacao != null)
				return false;
		} else if (!situacao.equals(other.situacao))
			return false;
		if (tipoCliente == null) {
			if (other.tipoCliente != null)
				return false;
		} else if (!tipoCliente.equals(other.tipoCliente))
			return false;
		return true;
	}
	
	@Override
	public Object onCycleDetected(com.sun.xml.bind.CycleRecoverable.Context arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
