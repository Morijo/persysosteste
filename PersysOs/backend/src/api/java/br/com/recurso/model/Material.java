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
import br.com.recurso.dao.MaterialDAO;
import com.sun.xml.bind.CycleRecoverable;

/**
 *  <p> O modelo Material eh composto pelos seguintes campos </p>
 * 	<p>id				 (bigint) Campo Obrigatorio
 *  <p>material          (String) tamanho 150 Campo Obrigatorio</p> 
 *  <br>Tabela no banco: recursomaterial </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see Medida
 * @see Recurso
 */
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="recursomaterial")
@PrimaryKeyJoinColumn(name="id")
public class Material extends Recurso implements Serializable, CycleRecoverable,br.com.model.interfaces.IMaterial {

	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}

	private static final long serialVersionUID = 1L;

	@Column(name = "material", nullable=false, length=150)
	private String material = null;

	public Material(Long id) {
		super(Material.class);
		this.setId(id);
	}

	public static final String CONSTRUTOR = "id,codigo,material,medida.nome,statusModel";

	public Material(Long id, String codigo, String material,String medida, Integer statusModel) {
		super(Material.class);
		this.material = material;
		this.setId(id);
		this.setCodigo(codigo);
		this.setStatusModel(statusModel);   
		this.setMedida(new Medida(medida, null));
	}

	public Material(Long id, String codigo, Integer statusModel, String material, String medida, Integer medidaId) {
		super(Material.class);
		this.material = material;
		this.setId(id);
		this.setCodigo(codigo);
		this.setStatusModel(statusModel);   
		this.setMedida(new Medida(medida, null));
	}

	public Material(){
		super(Material.class);
	}

	public Material(String material){
		super(Material.class);
		this.material = material;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public static ArrayList<Material> listaMaterial(String fields, Integer statusModel){
		try{
			MaterialDAO materialDAO = new MaterialDAO(Material.class.getClass());
			return materialDAO.listaMaterial(fields, statusModel);
		}catch(Exception e){
			return new ArrayList<Material>();
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Material> pesquisalistaMaterialPorConstrutor(String consumerKey){
		Session session = HibernateHelper.openSession(Material.class);
		Transaction tx = session.beginTransaction();
		try{
			return (ArrayList<Material>) Material.pesquisaListaPorConsumerSecret(session, 0, -1, CONSTRUTOR,consumerKey, Material.class);
		}finally{
			tx.commit();
			session.close();
		}
	}	

	@SuppressWarnings("unchecked")
	public static ArrayList<Material> busca(String material, String codigo, Integer statusModel,
			String consumerKey) throws ModelException {

		ArrayList<Material> materialLista;
		Session session = HibernateHelper.openSession(Dispositivo.class);
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();

		try{
			if(material.length() >= 3){
				parameter.add(ParameterDAO.with("material","%"+material+"%",ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.EQ));
			}else{
				PreconditionsModel.checkNotNull(null,bundle.getString("invalidcondition"));
			}
			
			parameter.add(ParameterDAO.with("statusModel", statusModel,ParameterDAOHelper.EQ));
			
			materialLista =(ArrayList<Material>) Material.pesquisaListaPorConsumerSecret(session,Material.class,consumerKey, parameter);
			tx.commit();
			return materialLista;
		}finally{
			session.close();
			session = null;
			tx = null;
		}
	}

	@Override
	public void valida() throws ModelException {

		setNome(getMaterial());
		setTipoRecurso("material");
		
		super.valida();
		
		if(getCodigo().isEmpty()){
			setCodigo("PMAT"+countPorConsumerSecret(Material.class,getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
