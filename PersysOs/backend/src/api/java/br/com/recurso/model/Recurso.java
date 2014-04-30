package br.com.recurso.model;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.frota.model.Veiculo;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IMedida;
import br.com.model.interfaces.IRecurso;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.dao.RecursoDAO;
import br.com.rest.resources.exception.PreconditionsREST;
/**
 *  <p> O modelo Recurso eh composto pelos seguintes campos </p>
 * 	<p>Id	  		(long) Campo Obrigatorio
 *  <p>codigo  		(String) tamanho (100) Campo Obrigatorio</p>
 *  <p>Status	    (Integer)</p>
 *  <p>Descricao	(String)tamanho(255)</p>
 *  <p>etiqueta 	(String)tamanho(100)</p>
 * 	<p>marca 		(String)tamanho(100)</p>
 * 	<p>modelo 		(String)tamanho(100)</p>
 * 	<p>nome 		(String)tamanho(100)</p>
 * 	<p>tipo 		(String)tamanho(50)Campo Obrigatorio</p>
 * 	<p>medidaid 	(bigint)Serve Para todos os recursos que utilizem alguma medida</p> 
 *  <br>Tabela no banco: recurso </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see ChipDispositivo
 * @see Chip
 * @see Dispositivo
 * @see Equipamento
 * @see Material
 * @see Medida
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@XmlSeeAlso({Material.class, Equipamento.class, Dispositivo.class, Chip.class, Veiculo.class})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Recurso extends Model<Recurso> implements IRecurso {
	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}

	public final static String CONSTRUTOR = "id,codigo,nome,statusModel,modelo,tipoRecurso";

	@Column(name = "nome", length = 100)
	private String nome = null;

	@Column(name = "marca", length = 100)
	private String marca = null;

	@Column(name = "modelo", length = 100)
	private String modelo = null;

	@Column(name = "etiqueta", length = 100)
	private String etiqueta = null;

	@Column(name = "descricao", length=255)
	private String descricao = null;

	@Column(name = "tipo", nullable=false, length = 50)
	private String tipoRecurso = null;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumn(name = "medidaidpk")
	@Cascade(CascadeType.PERSIST)
	private Medida medida = null;


	public Recurso(Long id, String codigo, String nome, Integer statusModel, String modelo, String tipoRecurso){
		super(Recurso.class);
		setId(id);
		setCodigo(codigo);
		setNome(nome);
		setStatusModel(statusModel);
		setModelo(modelo);
		setTipoRecurso(tipoRecurso);
	}

	public Recurso(){
		super(Recurso.class);
	}

	public Recurso(Long id){
		super(Recurso.class);
		setId(id);
	}
	
	public Recurso(Class<?> modelClass){
		super(modelClass);
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}


	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public Medida getMedida() {
		return medida;
	}
	
	@Override
	public void setMedida(IMedida medida) {
		this.medida = (Medida) medida;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoRecurso() {
		return tipoRecurso;
	}

	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	@Override
	public String toString() {
		return "Recurso [marca=" + marca + ", modelo=" + modelo + ", etiqueta="
				+ etiqueta +"]";
	}

	public static ArrayList<Recurso> listaRecurso(String fields, Integer statusModel, String consumerKey){
		try{
			RecursoDAO recursoDAO = new RecursoDAO(Recurso.class.getClass());
			return recursoDAO.listaRecurso(fields, statusModel, Long.parseLong(consumerKey));
		}catch(Exception e){
			return new ArrayList<Recurso>();
		}
	}

	public static ArrayList<Recurso> busca(String cs, String nomeRecurso, String codigo,String tipo,
			Integer status) throws ModelException {

		Session session = HibernateHelper.openSession(Recurso.class.getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();

		try{
			ArrayList<Recurso> recursos = null;

			if(nomeRecurso.length() >= 3){
				parameter.add(ParameterDAO.with("nome","%"+nomeRecurso+"%",ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 2){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.ILIKE));
			}else if(tipo.length() >= 3){
				parameter.add(ParameterDAO.with("tipoRecurso","%"+tipo+"%",ParameterDAOHelper.ILIKE));
			}else{
				PreconditionsREST.error(bundle.getString("invalidcondition"));
			}

			parameter.add(ParameterDAO.with("statusModel",status,ParameterDAOHelper.EQ));

			RecursoDAO recursoDAO = new RecursoDAO(Recurso.class.getClass());
			recursos = (ArrayList<Recurso>) recursoDAO.listaRecurso(session,cs,parameter);
			tx.commit();

			return recursos;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}

	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(nome, bundle.getString("enterthename"));
		PreconditionsModel.checkNotNull(getConsumerSecret(), bundle.getString("consumersecretnotset"));
		
	}
}	

