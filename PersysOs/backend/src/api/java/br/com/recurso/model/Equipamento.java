package br.com.recurso.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.model.PreconditionsModel;
import br.com.principal.helper.HibernateHelper;
import com.sun.xml.bind.CycleRecoverable;

/**
 *  <p> O modelo Equipamento eh composto pelos seguintes campos </p>
 * 	<p>id				 (bigint) Campo Obrigatorio
 *  <p>equipamento       (String)  tamanho 150 Campo Obrigatorio</p>
 *  <p>numeroserie       (String)  tamanho 200 Campo Obrigatorio</p> 
 *  <br>Tabela no banco: recursoequipamento </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see Recurso
 */
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="recursoequipamento")
@PrimaryKeyJoinColumn(name="id")
public class Equipamento extends Recurso implements Serializable , CycleRecoverable,br.com.model.interfaces.IEquipamento {


	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",Locale.getDefault());
	}
	private static final long serialVersionUID = 1L;
	public static final String CONSTRUTOR = "id, codigo, statusModel, equipamento, numeroSerie";

	@Column(name = "equipamento", nullable=false, length=150)
	private String equipamento = null;

	@Column(name = "numeroserie", nullable=false, length=150)
	private String numeroSerie = null;

	public Equipamento() {
		super(Equipamento.class);
	}

	public Equipamento(Long id, String codigo, Integer statusModel, String equipamento, String numeroSerie) {
		super(Equipamento.class);
		setCodigo(codigo);
		setId(id);
		setStatusModel(statusModel);
		this.equipamento = equipamento;
		this.numeroSerie = numeroSerie;

	}

	public String getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(String equipamento) {
		this.equipamento = equipamento;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Equipamento> pesquisalistaEquipamentoPorConstrutor(String consumerKey){
		Session session = HibernateHelper.openSession(Equipamento.class);
		Transaction tx = session.beginTransaction();
		try{
			return (ArrayList<Equipamento>) Equipamento.pesquisaListaPorConsumerSecret(session, 0, -1, CONSTRUTOR,consumerKey, Equipamento.class);
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
	public static ArrayList<Equipamento> buscaEquipamento(String consumerKey, String numeroSerie, String equipamento, String codigo,
			Integer statusModel) throws ModelException {


		Session session = HibernateHelper.openSession(Equipamento.class);
		Transaction tx = session.beginTransaction();

		ArrayList<Equipamento> equipamentoLista = null;
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		try{

			if(numeroSerie.length() >= 3){
				parameter.add(ParameterDAO.with("numeroSerie","%"+numeroSerie+"%",ParameterDAOHelper.ILIKE));
			}else if(equipamento.length() >= 3){
				parameter.add(ParameterDAO.with("equipamento","%"+equipamento+"%",ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.EQ));
			}else{
				PreconditionsModel.checkNotNull(null,"Not acceptable parameter");
			}

			parameter.add(ParameterDAO.with("statusModel",statusModel,ParameterDAOHelper.EQ));

			equipamentoLista =(ArrayList<Equipamento>) Equipamento.pesquisaListaPorConsumerSecret(session,Equipamento.class,consumerKey, parameter);
			tx.commit();
			return equipamentoLista;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	@Override
	public void valida() throws ModelException {

		PreconditionsModel.checkEmptyString(this.numeroSerie, bundle.getString("enterserialnumberofequipment"));
		PreconditionsModel.checkNotNull(getConsumerSecret(), bundle.getString("consumersecretnotset"));
		setTipoRecurso(bundle.getString("equipment"));
		setNome(getEquipamento());

		if(getCodigo().isEmpty()){
			setCodigo("PEQP"+countPorConsumerSecret(Equipamento.class,getConsumerSecret().getConsumerKey()));
		}
		super.valida();
	}
	
	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
