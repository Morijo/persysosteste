package br.com.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.dao.DAO;
import br.com.dao.ParameterDAO;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.oauth.model.ConsumerSecret;
import br.com.rest.represention.JsonDateAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED )
public abstract class Model<T> implements br.com.model.interfaces.IModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
	@Column(name = "id")
	private Long id = null;

	@XmlTransient
	@OneToOne(cascade={ CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "consumerkeyid", referencedColumnName="consumerKey")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ConsumerSecret consumerSecret = null;

	@Column(name = "codigo")
	private String codigo = null;

	@Column(name = "datacriacao")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataCriacao = null;

	@Column(name = "dataalteracao")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAlteracao=  null;

	@Column(name = "status")
	private Integer statusModel = 1;

	@Column(name = "permitidoexcluir")
	private Boolean permitidoExcluir = true;

	@Column(name = "permitidoalterar")
	private Boolean permitidoAlterar = true;

	@Column(name = "validador", unique=true, length=100)
	@XmlTransient
	protected String validador =  null;

	@Transient
	@XmlTransient
	private Class<?> persistentClass = null;
	
	public Model() {}

	public Model(Class<?> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		if(codigo == null){
			codigo ="";
		}
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Integer getStatusModel() {
		return statusModel;
	}

	public void setStatusModel(Integer statusModel) {
		this.statusModel = statusModel;
	}

	public Boolean getPermitidoExcluir() {
		return permitidoExcluir;
	}

	public void setPermitidoExcluir(Boolean permitidoExcluir) {
		this.permitidoExcluir = permitidoExcluir;
	}

	public Boolean getPermitidoAlterar() {
		if(permitidoAlterar == null)
			return true;
		return permitidoAlterar;
	}

	public void setPermitidoAlterar(Boolean permitidoAlterar) {
		this.permitidoAlterar = permitidoAlterar;
	}

	public ConsumerSecret getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(ConsumerSecret consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public void setConsumerId(Long consumerKey) {
		if(consumerSecret == null){
			consumerSecret = new ConsumerSecret();
		}
		this.consumerSecret.setConsumerKey(consumerKey);
	}

	@SuppressWarnings("rawtypes")
	public static Query consulta(Session s, String query){

		DAO dao;

		try{	
			dao = new DAO();
			return dao.consulta(s,query);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}
	
	public ArrayList<T> listaPorConstrutor(String atributos, String consumerKey) {
		DAO<T> dao;

		try{	
			dao = new DAO<T>(persistentClass);
			return dao.listaPorConstrutor(atributos);
		}catch (Exception e) {
			return new ArrayList<T>();
		}
		finally{
			dao = null;
		}
	}
	
	public ArrayList<T> listaPorConstrutorConsumerKey(String atributos, String consumerKey) {
		DAO<T> dao;

		try{	
			dao = new DAO<T>(persistentClass);
			return dao.listaPorConstrutorConsumerKey(atributos, consumerKey);
		}catch (Exception e) {
			return new ArrayList<T>();
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> pesquisaListaPorDataAlteracao(Integer inicio, Integer tamanhoPagina, String atributos,Date data, Class<?> persistentClass){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisalistaPorDataAlteracao(inicio,tamanhoPagina, atributos, data);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}	


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> pesquisaListaPorDataAlteracao(Session session, Integer inicio, Integer tamanhoPagina, 
			String atributos, Date data, Class<?> persistentClass){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisalistaPorDataAlteracao(session,inicio,tamanhoPagina, atributos, data);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> pesquisaListaPorDataAlteracaEConsumerSecret(Session session, Integer inicio, Integer tamanhoPagina, String atributos, 
			Date data, String consumerSecret, Class<?> persistentClass, Integer statusModel){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisalistaPorDataAlteracaEConsumerSecret(session, inicio, tamanhoPagina, 
					consumerSecret, atributos, data, statusModel);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> pesquisaListaPorConsumerSecret(Session session, Integer inicio, Integer tamanhoPagina, 
			String atributos, String consumerSecret, Class<?> persistentClass) {
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisalistaPorConsumerSecret(session, inicio, tamanhoPagina, consumerSecret, atributos);
		}finally{
			dao = null;
		}
	}	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> listaAll(Class<?> persistentClass){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.listaAll();
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> listaAll(Session session, Class<?> persistentClass){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.listaAll(session);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> pesquisaListaPorConsumerSecret(Class<?> persistentClass, String consumerKey, ParameterDAO... parameter){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisaListaPorConsumerSecret(Long.parseLong(consumerKey), parameter);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> pesquisaListaPorConsumerSecret(Session session, Class<?> persistentClass, String consumerKey, ParameterDAO... parameter){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisaListaPorConsumerSecret(session, Long.parseLong(consumerKey), parameter);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> pesquisaListaPorConsumerSecret(Session session, Class<?> persistentClass, String consumerKey, ArrayList<ParameterDAO> parameter){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisaListaPorConsumerSecret(session,Long.parseLong(consumerKey), parameter);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> pesquisaLista(Class<?> persistentClass,ArrayList<ParameterDAO> parameter){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisaLista(parameter);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> pesquisaLista(Session session, Class<?> persistentClass,ArrayList<ParameterDAO> parameter){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisaLista(session, parameter);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object pesquisaPorIdConsumerSecret(Class<?> persistentClass,Long id, String consumerSecret){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.loadPorIdConsumerSecret(id, consumerSecret);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object pesquisaPorIdConsumerSecret(Session session, Class<?> persistentClass, Long id, String consumerSecret){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.loadPorIdConsumerSecret(session, id, consumerSecret);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object pesquisaPorIdConsumerSecret(Session session, Class<?> persistentClass, Long id, String consumerSecret, Integer status){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.loadPorIdConsumerSecret(session, id, consumerSecret, status);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object pesquisaPorCodigoConsumerSecret(Session session, Class<?> persistentClass, String codigo, String consumerSecret){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.loadPorCodigoConsumerSecret(session, codigo, consumerSecret);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object pesquisaPorCodigoConsumerSecret(Class<?> persistentClass, String codigo, String consumerSecret){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.loadPorCodigoConsumerSecret(codigo, consumerSecret);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object pesquisaReturnID(Session session, Class<?> persistentClass,Long id, String consumerSecret){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.pesquisaReturnID(session, id, consumerSecret);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object pesquisaPorId(Session session, Class<?> persistentClass,Long id){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.loadPorId(session, id);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object pesquisaPorId(Class<?> persistentClass,Long id){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return dao.loadPorId(id);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T pesquisa(Class<T> persistentClass, ArrayList<ParameterDAO> parameter){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return (T) dao.pesquisa(parameter);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T pesquisa(Session session, Class<T> persistentClass, ArrayList<ParameterDAO> parameter){
		DAO<?> dao;

		try{	
			dao = new DAO(persistentClass);
			return (T) dao.pesquisa(session, parameter);
		}catch (Exception e) {
			return null;
		}
		finally{
			dao = null;
		}

	}

	@SuppressWarnings("unchecked")
	public Boolean salvar() throws ModelException{
		this.setDataAlteracao(new Date());

		if(dataCriacao == null)
			this.setDataCriacao(new Date());

		if(permitidoAlterar == null){ permitidoAlterar = true; }

		if(permitidoExcluir == null){ permitidoExcluir = true; }

		valida();

		DAO<T> dao;
		try{
			dao = new DAO<T>(persistentClass);
			
			if(codigo != null){ 
				if(codigo.isEmpty())
					codigo = "POS"+dataCriacao.getTime()+""+dao.countPorConsumerSecret(getConsumerSecret().getConsumerKey());
			}else{
				codigo = "POS"+dataCriacao.getTime()+""+dao.countPorConsumerSecret(getConsumerSecret().getConsumerKey());
			}
			
			Long consumer = getConsumerSecret().getConsumerKey();
			if(validador == null && consumer != null)
				validador = consumer+"_"+codigo;
			
			return dao.save((T) this);
		}
		catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
		finally{
			dao = null;
		}	
	}

	@SuppressWarnings("unchecked")
	public Boolean salvar(Session session) throws ModelException{
		this.setDataAlteracao(new Date());

		if(dataCriacao == null)
			this.setDataCriacao(new Date());
		
		if(permitidoAlterar == null){ permitidoAlterar = true; }

		if(permitidoExcluir == null){ permitidoExcluir = true; }

		valida();

		DAO<T> dao;
		try{
			dao = new DAO<T>(persistentClass);

			if(codigo != null){ 
				if(codigo.isEmpty())
					codigo = "POS"+dataCriacao.getTime()+""+dao.countPorConsumerSecret(getConsumerSecret().getConsumerKey());
			}else{
				codigo = "POS"+dataCriacao.getTime()+""+dao.countPorConsumerSecret(getConsumerSecret().getConsumerKey());
			}
			
			Long consumer = getConsumerSecret().getConsumerKey();
			if(validador == null && consumer != null)
				validador = consumer+"_"+codigo;
				
			return dao.save(session,(T) this);
		}
		catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
		finally{
			dao = null;
		}	
	}

	@SuppressWarnings("unchecked")
	public Boolean remover() throws ModelException{
		DAO<T> dao;

		if(getPermitidoExcluir()){

			this.setDataAlteracao(new Date());
			this.statusModel = StatusModel.DELETADO;

			try{
				dao = new DAO<T>(persistentClass);
				return dao.merge((T) this);
			}
			catch (DAOException e) {
				throw new ModelException(e.getMessage());
			}finally{
				dao = null;
			}
		}	else throw new ModelException("Remover não realizado: Não é permitido remover");
	}

	public Boolean removerFisico() throws ModelException{
		DAO<T> dao;
		if(getPermitidoExcluir())
			try{
				dao = new DAO<T>(persistentClass);
				return dao.delete(this.id);
			}
		catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}finally{
			dao = null;
		}
		else throw new ModelException("Remover não realizado: Não é permitido remover");
	}

	public Boolean removerFisico(Session session) throws ModelException{
		DAO<T> dao;
		if(getPermitidoExcluir())
			try{
				dao = new DAO<T>(persistentClass);
				return dao.delete(session, this);
			}
		catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}finally{
			dao = null;
		}
		else throw new ModelException("Remover não realizado: Não é permitido remover");
	}


	@SuppressWarnings("unchecked")
	public Boolean removerLogico() throws ModelException{
		DAO<T> dao;

		if(getPermitidoExcluir()){

			this.setDataAlteracao(new Date());
			this.statusModel = StatusModel.DELETADO;

			try{
				dao = new DAO<T>(persistentClass);
				return dao.merge((T) this);
			}
			catch (DAOException e) {
				throw new ModelException(e.getMessage());
			}finally{
				dao = null;
			}
		}	else throw new ModelException("Remover não realizado: Não é permitido remover");
	}

	@SuppressWarnings("unchecked")
	public Boolean alterar(Session session) throws ModelException{

		if(getPermitidoAlterar()){
			this.setDataAlteracao(new Date());

			DAO<T> dao;
			try{	
				dao = new DAO<T>(persistentClass);
				if(session !=null)
					return dao.merge(session,(T) this);
				else return dao.merge((T) this);
			}catch (DAOException e) {
				throw new ModelException(e.getMessage());
			}finally{
				dao = null;
			}
		}	else throw new ModelException("Alterar não realizado: Não é permitido remover");
	}

	@SuppressWarnings("unchecked")
	public Boolean alterar() throws ModelException{

		if(getPermitidoAlterar()){
			this.setDataAlteracao(new Date());

			DAO<T> dao;
			try{	
				dao = new DAO<T>(persistentClass);
				return dao.merge((T) this);
			}catch (DAOException e) {
				throw new ModelException(e.getMessage());
			}finally{
				dao = null;
			}
		}	else throw new ModelException("Alterar não realizado: Não é permitido remover");
	}

	@SuppressWarnings("unchecked")
	public Boolean salvarAlterar() throws ModelException{

		this.setDataAlteracao(new Date());

		if(dataCriacao == null)
			this.setDataCriacao(new Date());

		if(permitidoAlterar == null){ permitidoAlterar = true; }

		if(permitidoExcluir == null){ permitidoExcluir = true; }

		valida();

		DAO<T> dao;
		try{	
			dao = new DAO<T>(persistentClass);

			if(codigo != null){ 
				if(codigo.isEmpty())
					codigo = "POS"+dataCriacao.getTime()+""+dao.countPorConsumerSecret(getConsumerSecret().getConsumerKey());
			}else{
				codigo = "POS"+dataCriacao.getTime()+""+dao.countPorConsumerSecret(getConsumerSecret().getConsumerKey());
			}
			
			if(validador == null)
				validador = getConsumerSecret().getConsumerKey()+"_"+codigo;
			
			return dao.saveOrMerge((T) this);
		}catch (DAOException e) {
			throw new ModelException("Falha no banco de dados: "+ e.getMessage());
		}finally{
			dao = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Boolean salvarAlterar(Session session) throws ModelException{

		this.setDataAlteracao(new Date());

		if(dataCriacao == null)
			this.setDataCriacao(new Date());

		if(permitidoAlterar == null){ permitidoAlterar = true; }

		if(permitidoExcluir == null){ permitidoExcluir = true; }

		valida();

		DAO<T> dao;
		try{	
			dao = new DAO<T>(persistentClass);

			if(codigo != null){ 
				if(codigo.isEmpty())
					codigo = "POS"+dataCriacao.getTime()+""+dao.countPorConsumerSecret(getConsumerSecret().getConsumerKey());
			}else{
				codigo = "POS"+dataCriacao.getTime()+""+dao.countPorConsumerSecret(getConsumerSecret().getConsumerKey());
			}
			
			Long consumer = getConsumerSecret().getConsumerKey();
			if(validador == null && consumer != null)
				validador = consumer+"_"+codigo;
				
			return dao.saveOrMerge(session, (T) this);
		}catch (DAOException e) {
			throw new ModelException("Falha no banco de dados: "+ e.getMessage(), 1);
		}finally{
			dao = null;
		}
	}

	public Boolean atualizarDatas(Session session) throws ModelException{

		this.setDataAlteracao(new Date());

		DAO<T> dao;
		try{	
			dao = new DAO<T>(persistentClass);
			return dao.updateDataAlteracaoPorId(session, this);
		}catch (DAOException e) {
			throw new ModelException("Falha no banco de dados: "+ e.getMessage());
		}finally{
			dao = null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Number count(Class<?> persistentClass){
		DAO dao;
		dao = new DAO(persistentClass);
		return dao.count();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Number count(Session session,Class<?> persistentClass){
		DAO dao;
		dao = new DAO(persistentClass);
		return dao.count(session);
	}

	//Conta por consumerKey
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Number countPorConsumerSecret(Class<?> persistentClass, Long consumerKey){
		DAO dao;
		dao = new DAO(persistentClass);
		return dao.countPorConsumerSecret(consumerKey);
	}

	//Conta por consumerKey
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Number countPorConsumerSecret(Session session, Class<?> persistentClass, String consumerSecret, Integer statusModel, Date dataAlteracao){
		DAO dao;
		dao = new DAO(persistentClass);
		return dao.countPorConsumerSecret(session,consumerSecret, statusModel, dataAlteracao);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Number count(ParameterDAO... paramentros){
		DAO dao;
		dao = new DAO(persistentClass);
		return dao.countAll(paramentros);
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean existIdAtivoInativoPorConsumerSecret(Session session, Class<?> persistentClass, Long id, String consumerSecret){
		DAO dao = null;
		try{
			dao= new DAO(persistentClass);
			return dao.existIdAtivoInativoPorConsumerSecret(session, consumerSecret, id);
		}finally{
			dao = null;
		}	
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean existValidador(Session session, Class<?> persistentClass, String validador){
		DAO dao = null;
		try{
			dao= new DAO(persistentClass);
			return dao.existValidador(session, validador);
		}finally{
			dao = null;
		}	
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean existAll(Session session, Class<?> persistentClass, Long id, String consumerSecret){
		DAO dao = null;
		try{
			dao= new DAO(persistentClass);
			return dao.existAll(session, consumerSecret, id);
		}finally{
			dao = null;
		}	
	}

	public abstract void valida() throws ModelException;

}