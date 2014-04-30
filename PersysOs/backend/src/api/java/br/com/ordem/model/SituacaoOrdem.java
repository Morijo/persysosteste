package br.com.ordem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.sun.xml.bind.CycleRecoverable;

import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.ISituacaoOrdem;
import br.com.ordem.dao.SituacaoOrdemDAO;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;

/**
 *  <p> O modelo SituacaoOrdem eh composto pelos seguintes campos </p>
 * 	<p>Nome 		(String) campo obrigatorio, tamanho 100 caracteres, Nome da situacao </p>
 *  <p>Descricao 	(String) tamanho 255,campo usado para descrever a finalidade da situacao </p>
 *  <p>publico		(String) tamanha 20,se é aberto para a empresa ou para o publico 
 *  <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/backend/model</p>
 *  <br>Tabela no banco: situacaoordem </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @since 27/03/2013
 * @see Ordem
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="situacaoordem")
public class SituacaoOrdem extends Model<SituacaoOrdem> implements Serializable, CycleRecoverable, ISituacaoOrdem {

	private final static ResourceBundle bundle;
	static{
		 bundle = ResourceBundle.getBundle("com/persys/backend/notification",Locale.getDefault());
	}
	private static final long serialVersionUID = 1L;

	@Column(name = "nome", nullable=false, length=100)
	private String  nome= null; 

	@Column(name = "descricao", length=255)
	private String  descricao= null; 

	@Column(name = "publico",  length=20)
	private String  publico = null;
	
	@OneToMany(mappedBy = "situacaoOrdem", targetEntity = SituacaoOrdemPermissao.class, fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<SituacaoOrdemPermissao> situacaoOrdemPermissao = new ArrayList<SituacaoOrdemPermissao>();
	
	public SituacaoOrdem(Long id) {
		super(SituacaoOrdem.class);
		setId(id);
	}

	public static final String CONSTRUTOR = "id,codigo,nome";
	
	public SituacaoOrdem(Long id, String codigo, String nome) {
		super();
		setId(id);
		setCodigo(codigo);
		this.nome = nome;
	}

	public SituacaoOrdem() {
		super(SituacaoOrdem.class);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getPublico() {
		return publico;
	}

	public void setPublico(String publico) {
		this.publico = publico;
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(nome,bundle.getString("situationnameinvalid"));
		if(getCodigo().isEmpty()){
			setCodigo("PSOS"+countPorConsumerSecret(SituacaoOrdem.class,getConsumerSecret().getConsumerKey()));
		}
	}
	
	public static SituacaoOrdem getSituacaoOrdem(String codigo, String consumerKey, int statusModel) throws ModelException{
		SituacaoOrdemDAO situacaoOrdemDAO = new SituacaoOrdemDAO();
		try {
			return situacaoOrdemDAO.getSituacaoServico(codigo, Long.parseLong(consumerKey), statusModel);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	/**
	 * 	A busca pode ser realizada por nome, codigo e ser filtrado por statusModel 
	 * @author ricardosabatine, jpmorijo	
	 * @version 1.0.0
	*/
	@SuppressWarnings("unchecked")
	public static ArrayList<SituacaoOrdem> busca(String consumerKey, String nomeSituacaoOrdem, String codigo,
			Integer statusModel) throws ModelException {

		Session session = HibernateHelper.openSession(Produto.class.getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		
		try{
			if(nomeSituacaoOrdem.length() >= 3){
				parameter.add(ParameterDAO.with("nome","%"+nomeSituacaoOrdem+"%",ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.EQ));
			}else{
				PreconditionsModel.checkNotNull(null,bundle.getString("invalidcondition"));
			}

			parameter.add(ParameterDAO.with("statusModel",statusModel,ParameterDAOHelper.EQ));

			ArrayList<SituacaoOrdem> situacaoLista =(ArrayList<SituacaoOrdem>) SituacaoOrdem.pesquisaListaPorConsumerSecret(session,SituacaoOrdem.class,consumerKey, parameter);
			tx.commit();
			return situacaoLista;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}
	
	@Override
	public Object onCycleDetected(Context arg0) {
		SituacaoOrdem situacaoOrdem = new SituacaoOrdem();
		return situacaoOrdem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		SituacaoOrdem other = (SituacaoOrdem) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (publico == null) {
			if (other.publico != null)
				return false;
		} else if (!publico.equals(other.publico))
			return false;
		return true;
	}
}