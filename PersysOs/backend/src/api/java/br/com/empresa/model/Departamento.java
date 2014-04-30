package br.com.empresa.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;
import com.sun.xml.bind.CycleRecoverable;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.empresa.dao.DepartamentoDAO;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IDepartamento;
import br.com.model.interfaces.IOrdem;
import br.com.model.interfaces.IUnidade;
import br.com.ordem.model.Ordem;
import br.com.principal.helper.HibernateHelper;

/**
 *Departamento da organiza��o, vinculado a uma unidade 
 *@author ricardosabatine
 *@version 1.0
 *@since 1.0
 */
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="departamento")
public class Departamento extends Model<Departamento> implements CycleRecoverable, IDepartamento{

	public static final String CONSTRUTOR = "id,codigo,nomeDepartamento,tipo,unidade.id,unidade.nomeUnidade,telefone";

	@Column(name = "nomeDepartamento", nullable=false, length=100)
	private String nomeDepartamento = null;

	@Column(name = "descricao")
	private String descricao = null; 

	@Column(name = "tipo", length=100)
	private String tipo= null; //{ publico, interno}

	@Column(name = "email", length=100)
	private String email = null; 

	@Column(name = "responsavel", length=100)
	private String responsavel = null; 

	@Column(name = "telefone", length=12)
	private String telefone = null; 

	@Column(name = "ramal", length=10)
	private String ramal=null; 

	@ManyToOne
	@JoinColumn( name = "unidadeidpk")
	private Unidade unidade;

	@XmlTransient
	@OneToMany(mappedBy = "departamento", targetEntity = Ordem.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<IOrdem> ordem = new ArrayList<IOrdem>();

	public Departamento(){
		super(Departamento.class);
	}

	public Departamento(long id, String codigo, String nomeDepartamento,String tipo, Long unidadeId, String unidadeNome, String telefone){
		super(Departamento.class);
		setId(id);
		setCodigo(codigo);
		this.unidade = new Unidade(unidadeId,null,unidadeNome);
		this.tipo = tipo;
		this.telefone = telefone;
		this.nomeDepartamento = nomeDepartamento;
	}

	public Departamento(long id, String codigo, String nomeDepartamento, String descricao){
		super(Departamento.class);
		setId(id);
		setCodigo(codigo);
		this.nomeDepartamento = nomeDepartamento;
		this.descricao = descricao;
	}

	/**
	 * @return the ordem
	 */
	public Collection<IOrdem> getOrdem() {
		return ordem;
	}

	public String getNomeDepartamento() {
		return nomeDepartamento;
	}

	public void setNomeDepartamento(String nomeDepartamento) {
		this.nomeDepartamento = nomeDepartamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(IUnidade unidade) {
		this.unidade = (Unidade) unidade;
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

	/**
	 * @param ordem the ordem to set
	 */
	public void setOrdem(Collection<IOrdem> ordem) {
		this.ordem = ordem;
	}

	public static ArrayList<Departamento> listaDepartamentosPorUnidade(Long unidadeId, String consumerKey, Integer statusModel) throws ModelException{
		DepartamentoDAO departamentoDAO = new DepartamentoDAO();
		try {
			return departamentoDAO.listaDepartamentosPorUnidade(unidadeId, statusModel, Long.parseLong(consumerKey));
		} catch (NumberFormatException e) {
			throw new ModelException(e.getMessage());
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Departamento> busca(String consumerKey, String nomeDepartamento, String codigo, Integer status) 
			throws ModelException{
		
		ArrayList<Departamento> departamentoLista = null;
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();

		Session session = HibernateHelper.openSession(Departamento.class);
		Transaction tx = session.beginTransaction();

		if(nomeDepartamento.length() >= 3){
			parameter.add(ParameterDAO.with("nomeDepartamento","%"+nomeDepartamento+"%",ParameterDAOHelper.ILIKE));
		}else if(codigo.length() >= 1){
			parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.ILIKE));
		}else{
			PreconditionsModel.checkNotNull(null, "not found");
		}

		parameter.add(ParameterDAO.with("consumerSecret", consumerKey,ParameterDAOHelper.EQ));

		departamentoLista =(ArrayList<Departamento>) Departamento.pesquisaLista(Departamento.class, parameter);
		tx.commit();
		session.close();
		
		return departamentoLista;
	}

	@Override
	public String toString() {
		return "Departamento [id=" + getId() + ", nomeDepartamento=" + nomeDepartamento
				+ ", descricao=" + descricao
				+ ", tipo=" + tipo + ", email=" + email + ", responsavel="
				+ responsavel + ", dataCriacao=" + getDataCriacao() + ", dataUpdate=" + getDataAlteracao()
				+  ", unidade=" + unidade +"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkNotNull(unidade, "unidade é obrigatorio");
		PreconditionsModel.checkNotNull(nomeDepartamento, "nome é obrigatorio");
		PreconditionsModel.checkNotNull(getConsumerSecret(), "Consumer secret não definido");
		if(getCodigo().isEmpty()){
			setCodigo("PDEP"+countPorConsumerSecret(Departamento.class, getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
