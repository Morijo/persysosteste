package br.com.ordem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IPrioridade;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;
import br.com.rest.resources.exception.PreconditionsREST;

import com.sun.xml.bind.CycleRecoverable;

/**
 *  <p> O modelo Prioridade eh composto pelos seguintes campos </p>
 * 	<p>Prioridade	(String) campo obrigatorio, tamanho 100 caracteres, Nome da Prioridade </p>
 *  <p>Cor 			(String) tamanho 100,campo usado para selecionar uma cor especifica para cada prioridade </p>
 *  <p>publico		(String) tamanha 20, se é aberto para a empresa ou para o publico 
 *  <p>Descricao 	(String) tamanho 255,campo usado para descrever a Prioridade</p>
 *  <p>Urgencia 	(String) tamanho 50,campo usado para descrever se a prioridade é urgente ou normal </p>
 *  <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/backend/model</p>
 *  <br>Tabela no banco: prioridade </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @since 27/03/2013 
 * @see Ordem
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="prioridade")
public class Prioridade extends Model<Prioridade> implements Serializable, CycleRecoverable, IPrioridade {

	private final static ResourceBundle bundle;
	static{
		 bundle = ResourceBundle.getBundle("com/persys/backend/notification",Locale.getDefault());
	}

	private static final long serialVersionUID = 1L;

	@Column(name = "prioridade", nullable=false, length=100)
	private String prioridade = null; 

	@Column(name = "cor", nullable=false, length=100)
	private String cor = null;

	@Column(name = "publico", length=20)
	private String  publico = null; 
	
	@Column(name = "urgencia", length=50)
	private Integer prioridadeUrgencia = null;

	@Column(name = "descricao", length=255)
	private String descricao = null; 

	public static final String CONSTRUTOR = "id,codigo,prioridade,cor";
	
	public Prioridade(Long id, String prioridade, String cor, String publico,
			Integer prioridadeUrgencia) {
		super(Prioridade.class);
		this.prioridade = prioridade;
		this.cor = cor;
		this.publico = publico;
		this.prioridadeUrgencia = prioridadeUrgencia;
	}
	
	public Prioridade(Long id, String codigo, String prioridade, String cor) {
		super(Prioridade.class);
		this.prioridade = prioridade;
		this.cor = cor;
		setCodigo(codigo);
		setId(id);
	}

	public Prioridade() {
		super(Prioridade.class);
	}

	/**
	 * @return the prioridade
	 */
	public String getPrioridade() {
		return prioridade;
	}

	/**
	 * @param prioridade the prioridade to set
	 */
	public void setPrioridade(String prioridade) {
		this.prioridade = prioridade;
	}

	/**
	 * @return the cor
	 */
	public String getCor() {
		return cor;
	}

	/**
	 * @param cor the cor to set
	 */
	public void setCor(String cor) {
		this.cor = cor;
	}

	/**
	 * @return the publico
	 */
	public String getPublico() {
		return publico;
	}

	/**
	 * @param publico the publico to set
	 */
	public void setPublico(String publico) {
		this.publico = publico;
	}

	/**
	 * @return the prioridadeUrgencia
	 */
	public Integer getPrioridadeUrgencia() {
		return prioridadeUrgencia;
	}

	/**
	 * @param prioridadeUrgencia the prioridadeUrgencia to set
	 */
	public void setPrioridadeUrgencia(Integer prioridadeUrgencia) {
		this.prioridadeUrgencia = prioridadeUrgencia;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * 	A busca pode ser realizada por nome, codigo e ser filtrado por statusModel 
	 * @author ricardosabatine, jpmorijo	
	 * @version 1.0.0
	*/
	@SuppressWarnings("unchecked")
	public static ArrayList<Prioridade> busca(String consumerKey, String nomePrioridade, String codigo,
			Integer statusModel) throws ModelException {

		ArrayList<Prioridade> prioridadeLista = null;
		Session session = HibernateHelper.openSession(Produto.class.getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		
		try{
			if(nomePrioridade.length() >= 3){
				parameter.add(ParameterDAO.with("nome","%"+nomePrioridade+"%",ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.ILIKE));
			}else{
				PreconditionsREST.error(bundle.getString("invalidcondition"));
			}

			parameter.add(ParameterDAO.with("statusModel",statusModel,ParameterDAOHelper.EQ));

			prioridadeLista =(ArrayList<Prioridade>) Prioridade.pesquisaListaPorConsumerSecret(session,Prioridade.class,consumerKey, parameter);
			tx.commit();
			return prioridadeLista;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(prioridade,bundle.getString("nameofpriorityinvalid"));
		if(getCodigo().isEmpty()){
			setCodigo("PPRI"+countPorConsumerSecret(Prioridade.class,getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		Prioridade prioridade = new Prioridade();
		return prioridade;
	}

	@Override
	public String toString() {
		return "Prioridade [prioridade=" + prioridade + ", cor=" + cor
				+ ", publico=" + publico + ", prioridadeUrgencia="
				+ prioridadeUrgencia + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cor == null) ? 0 : cor.hashCode());
		result = prime * result
				+ ((prioridade == null) ? 0 : prioridade.hashCode());
		result = prime
				* result
				+ ((prioridadeUrgencia == null) ? 0 : prioridadeUrgencia
						.hashCode());
		result = prime * result + ((publico == null) ? 0 : publico.hashCode());
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
		Prioridade other = (Prioridade) obj;
		if (cor == null) {
			if (other.cor != null)
				return false;
		} else if (!cor.equals(other.cor))
			return false;
		if (prioridade == null) {
			if (other.prioridade != null)
				return false;
		} else if (!prioridade.equals(other.prioridade))
			return false;
		if (prioridadeUrgencia == null) {
			if (other.prioridadeUrgencia != null)
				return false;
		} else if (!prioridadeUrgencia.equals(other.prioridadeUrgencia))
			return false;
		if (publico == null) {
			if (other.publico != null)
				return false;
		} else if (!publico.equals(other.publico))
			return false;
		return true;
	}
}
