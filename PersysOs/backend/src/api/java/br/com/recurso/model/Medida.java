package br.com.recurso.model;


import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sun.xml.bind.CycleRecoverable;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;

/**
 *  <p> O modelo Medida eh composto pelos seguintes campos </p>
 * 	<p>id         (long) Campo Obrigatorio
 *  <p>codigo     (String)  tamanho 100 Campo Obrigatorio</p>
 *  <p>status     (integer)</p>
 *  <p>abreviacao (String)  tamanho 50 Campo Obrigatorio</p>
 *  <p>nome       (String)  tamanho 100 Campo Obrigatorio</p>
 *	<p> obs       (String)  tamanho 255</p>
 *  <br>Tabela no banco: medida </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see Material
 */
@Entity
@Table(name="medida")
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class Medida extends Model<Medida> implements CycleRecoverable,br.com.model.interfaces.IMedida{
	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}

	@Column(name="nome", nullable=false, length=100)
	private String nome = null;

	@Column(name="abreviacao",nullable=false, length=50)
	private String abreviacao = null;

	@Column(name="obs",length=255)
	private String observacao = null;

	public Medida(){
		super(Medida.class);
	}

	public Medida(String nome, Long id){
		super(Medida.class);
		this.nome = nome;
		setId(id);
	}

	public static final String CONSTRUTOR = "id,codigo,statusModel,nome,abreviacao,observacao";

	public Medida(Long id, String codigo, Integer statusModel, 
			String nome, String abreviacao, String observacao){
		super(Medida.class);
		setId(id);
		setCodigo(codigo);
		setStatusModel(statusModel);
		this.nome = nome;
		this.abreviacao = abreviacao;
		this.observacao = observacao;
	}

	public String getObservacao() {
		return observacao;
	}
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getAbreviacao() {
		return abreviacao;
	}

	public void setAbreviacao(String abreviacao) {
		this.abreviacao = abreviacao;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Medida> pesquisalistaMedidaPorConstrutor(String consumerKey){
		Session session = HibernateHelper.openSession(Medida.class);
		Transaction tx = session.beginTransaction();
		try{
			return (ArrayList<Medida>) Medida.pesquisaListaPorConsumerSecret(session, 0, -1, CONSTRUTOR,consumerKey, Medida.class);
		}finally{
			tx.commit();
			session.close();
		}
	}

	/**
	 * 	A busca pode ser realizada por nome, codigo e ser filtrado por statusModel 
	 * @author ricardosabatine, jpmorijo	
	 * @version 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Medida> busca(String consumerKey, String nomeMedida, String codigo,
			Integer status) throws ModelException {

		Session session = HibernateHelper.openSession(Produto.class.getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		ArrayList<Medida> medidaLista;
		try{

			if(nomeMedida.length() >= 3){
				parameter.add(ParameterDAO.with("nome","%"+nomeMedida+"%",ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.ILIKE));
			}else{
				PreconditionsModel.checkNotNull(null,"not found");
			}
			parameter.add(ParameterDAO.with("statusModel",status,ParameterDAOHelper.EQ));
			
			medidaLista =(ArrayList<Medida>) Medida.pesquisaListaPorConsumerSecret(session,Medida.class,consumerKey,parameter);
			tx.commit();

			return medidaLista;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Override
	public void valida() throws ModelException {

		PreconditionsModel.checkEmptyString(nome,bundle.getString("invalidmeasure"));
		PreconditionsModel.checkEmptyString(abreviacao,bundle.getString("invalidabbreviation"));
		PreconditionsModel.checkNotNull(getConsumerSecret(), bundle.getString("consumersecretnotset"));

		if(getCodigo().isEmpty()){
			setCodigo("PMED"+countPorConsumerSecret(Medida.class,getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
