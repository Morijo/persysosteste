package br.com.recurso.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;

import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.model.PreconditionsModel;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.dao.ChipDAO;

import com.sun.xml.bind.CycleRecoverable;

/**
 *  <p> O modelo Chip eh composto pelos seguintes campos </p>
 * 	<p>id				 (Long) Campo Obrigatorio </p>
 *  <p>dddNumero         (Long) Campo Obrigatorio, Numero do Dispositivo em execucao</p>
 *  <p>operadora         (String)  Tamanho 100 Campo Obrigatorio</p> 
 *  <p>Principal         (boolean) Funcao retorna se o numero é o principal do dispositivo</p>
 *  <p>ligacao           (boolean) </p>
 *  <br>Tabela no banco: recursochip </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see Dispositivo
 * @see ChipDispositivo
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="recursochip")
@PrimaryKeyJoinColumn(name="id")
public class Chip extends Recurso implements Serializable, CycleRecoverable,br.com.model.interfaces.IChip {
	
	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}	

	private static final long serialVersionUID = 1L;

	@Column(name = "numero")
	private Long dddNumero = null;

	@Column(name = "operadora", nullable=false, length=100)
	private String operadora = null;
	
	@Column(name = "principal")
	private Boolean principal = null;

	@Column(name = "ligacao")
	private Boolean ligacao = null;

	@OneToMany(mappedBy = "dispositivo", targetEntity = ChipDispositivo.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<ChipDispositivo> chipDispositivos = new ArrayList<ChipDispositivo>();

	public Chip() {
		super(Chip.class);
	}

	public Chip(Long id, String codigo, Long dddNumero) {
		super(Chip.class);
		this.dddNumero = dddNumero;
		setId(id);
		setCodigo(codigo);
	}

	public Long getDddNumero() {
		return dddNumero;
	}

	public void setDddNumero(Long dddNumero) {
		this.dddNumero = dddNumero;
	}

	public String getOperadora() {
		return operadora;
	}

	public void setOperadora(String operadora) {
		this.operadora = operadora;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public Boolean getLigacao() {
		return ligacao;
	}

	public void setLigacao(Boolean ligacao) {
		this.ligacao = ligacao;
	}

	public Collection<ChipDispositivo> getTelefoneDispositivos() {
		return chipDispositivos;
	}

	public void setTelefoneDispositivos(
			Collection<ChipDispositivo> chipDispositivos) {
		this.chipDispositivos = chipDispositivos;
	}
	
	public static ArrayList<Chip> listaChip(String fields, Integer statusModel){
		try{
			ChipDAO chipDAO = new ChipDAO(Chip.class.getClass());
			return chipDAO.listaChip(fields, statusModel);
		}catch(Exception e){
			return new ArrayList<Chip>();
		}
	}

	/**
	 * 	A busca pode ser realizada por nome, codigo e ser filtrado por statusModel 
	 * @author ricardosabatine, jpmorijo	
	 * @version 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Chip> buscaChip(String consumerKey, String dddnumero, String codigo,
			Integer statusModel) throws ModelException {

		Session session = HibernateHelper.openSession(Chip.class);
		Transaction tx = session.beginTransaction();

		ArrayList<Chip> chipLista = null;
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		try{

			parameter.add(ParameterDAO.with("dddNumero","%"+dddnumero+"%",ParameterDAOHelper.ILIKE));
			if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.EQ));
			}
			parameter.add(ParameterDAO.with("statusModel",statusModel,ParameterDAOHelper.EQ));

			chipLista =(ArrayList<Chip>) Chip.pesquisaListaPorConsumerSecret(session,Chip.class,consumerKey, parameter);
			tx.commit();

			return chipLista;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkValidTelefone(dddNumero,bundle.getString("invalidnumber"));
		PreconditionsModel.checkNotNull(getConsumerSecret(), bundle.getString("consumersecretnotset"));
		
		setNome(String.valueOf(dddNumero));
		setTipoRecurso("chip");
	
		super.valida();
		
		if(getCodigo().isEmpty()){
			setCodigo("PCHIP"+count(Chip.class));
		}
	}
	
	@Override
	public Object onCycleDetected(Context arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}