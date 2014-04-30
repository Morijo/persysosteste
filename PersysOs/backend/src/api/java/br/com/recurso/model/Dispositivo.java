package br.com.recurso.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.model.PreconditionsModel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.principal.helper.HibernateHelper;
import com.sun.xml.bind.CycleRecoverable;

/**
 *  <p> O modelo Dispositivo eh composto pelos seguintes campos </p>
 * 	<p>id	  		(bigint) Campo Obrigatorio
 *  <p>imei   		(String) tamanho 100 Campo Obrigatorio,IMEI é o identificador unico de cada dispositivo</p>
 *  <br>Tabela no banco: recursodispositivo </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see ChipDispositivo
 * @see Recurso
 * @see Chip
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="recursodispositivo")
@PrimaryKeyJoinColumn(name="id")
public class Dispositivo extends Recurso implements Serializable, CycleRecoverable, br.com.model.interfaces.IDispositivo {

	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}
	private static final long serialVersionUID = 1L;

	public static final String CONSTRUTOR = "id,IMEI,codigo,statusModel";

	@Column(name = "imei", nullable=false, length=100)
	private String IMEI = null;

	@Column(name = "versaoapp", nullable=false, length=100)
	private String versaoApp = null;

	@OneToMany(mappedBy = "dispositivo", targetEntity = ChipDispositivo.class, fetch = FetchType.EAGER, cascade={CascadeType.MERGE})
	private Collection<ChipDispositivo> chipDispositivos = new ArrayList<ChipDispositivo>();

	public Dispositivo() {
		super(Dispositivo.class);
	}

	public Dispositivo(Long id, String imei, String codigo, Integer statusModel) {
		super(Dispositivo.class);
		this.IMEI = imei;
		this.setId(id);
		this.setCodigo(codigo);
		this.setStatusModel(statusModel);
	}

	public Dispositivo(String imei, Boolean principal, 
			Collection<ChipDispositivo> chipDispositivos) {
		super(Dispositivo.class);
		this.IMEI = imei;
		this.chipDispositivos = chipDispositivos;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public Collection<ChipDispositivo> getTelefoneDispositivos() {
		return chipDispositivos;
	}

	public void setTelefoneDispositivos(
			Collection<ChipDispositivo> chipDispositivos) {
		this.chipDispositivos = chipDispositivos;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Dispositivo> listaDispositivoPorConstrutor(String consumerKey){
		Session session = HibernateHelper.openSession(Dispositivo.class);
		Transaction tx = session.beginTransaction();
		try{
			return (ArrayList<Dispositivo>) Dispositivo.pesquisaListaPorConsumerSecret(session, 0, -1,CONSTRUTOR,consumerKey, Dispositivo.class);
		}finally{
			tx.commit();
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Dispositivo> busca(String numeroSerie, String codigo, Integer statusModel,
			String consumerKey) throws ModelException {
		ArrayList<Dispositivo> dispositivoLista;
		Session session = HibernateHelper.openSession(Dispositivo.class);
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();

		try{	

			if(numeroSerie.length() >= 3){
				parameter.add(ParameterDAO.with("IMEI","%"+numeroSerie+"%",ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.ILIKE));
			}else{
				PreconditionsModel.checkNotNull(null,bundle.getString("invalidcondition"));
			}

			parameter.add(ParameterDAO.with("statusModel",statusModel,ParameterDAOHelper.EQ));

			dispositivoLista =(ArrayList<Dispositivo>) Dispositivo.pesquisaListaPorConsumerSecret(session,Dispositivo.class,consumerKey, parameter);
			tx.commit();
			return dispositivoLista;
		}finally{
			session.close();
			session = null;
			tx = null;
		}
	}

	@Override
	public void valida() throws ModelException {

		PreconditionsModel.checkEmptyString(IMEI, bundle.getString("invalidimei"));
		setTipoRecurso("dispositivo");
		setNome(this.IMEI);
		
		super.valida();
		
		if(getCodigo().isEmpty()){
			setCodigo("PDIS"+countPorConsumerSecret(Dispositivo.class,getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
