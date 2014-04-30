package br.com.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;

import br.com.exception.DAOException;
import br.com.model.Model;
import br.com.model.StatusModel;
import br.com.principal.helper.HibernateHelper;

/**
 * Classe generica de DAO para tratar os metodos CRUD
 * 
 * @author
 * 
 */
public class DAO<T> {

	private final static ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}
	
	private Class<?> persistentClass;
	
	public DAO(Class<?> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public DAO() {}

	/**
	 * Retorna se existe tanto ativo como inativo
	 * 
	 * @return Boolean
	 */
	public boolean existIdAtivoInativoPorConsumerSecret(Session session, String consumerSecret, Long id) {
		Criteria criteria = null;
		try {
			criteria = session.createCriteria(persistentClass).createAlias(
					"consumerSecret", "consumerSecret");
			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
			criteria.add(Restrictions.le("statusModel", StatusModel.ATIVO));
			criteria.add(Restrictions.eq("id", id));
			Number number = (Number) criteria.uniqueResult();
			if (number.intValue() > 0) {
				return true;
			} else {
				return false;
			}

		} finally {
			criteria = null;
		}
	}

	/**
	 * Retorna se existe tanto ativo como inativo de um determinado validador
	 * 
	 * @return Boolean
	 */
	public boolean existValidador(Session session, String validador) {
		Criteria criteria = null;
		try {
			criteria = session.createCriteria(persistentClass).createAlias(
					"consumerSecret", "consumerSecret");

			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.le("statusModel", StatusModel.ATIVO));
			criteria.add(Restrictions.eq("validador", validador));
			Number number = (Number) criteria.uniqueResult();
			if (number.intValue() > 0) {
				return true;
			} else {
				return false;
			}

		} finally {
			criteria = null;
		}
	}

	/**
	 * Retorna se existe algum restrito
	 * 
	 * @return Boolean
	 */
	public boolean existAll(Session session, String consumerSecret, Long id) {
		Criteria c = null;
		try {
			c = session.createCriteria(persistentClass).createAlias(
					"consumerSecret", "consumerSecret");
			c.setProjection(Projections.rowCount());
			c.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
			c.add(Restrictions.eq("id", id));
			Number number = (Number) c.uniqueResult();
			if (number.intValue() > 0) {
				return true;
			} else {
				return false;
			}

		} finally {
			c = null;
		}
	}

	/**
	 * Retorna o n�mero de registros
	 * 
	 * @return Number
	 */
	public Number count() {
		Session session = HibernateHelper.openSession(persistentClass);
		try {
			return (Number) session.createCriteria(persistentClass)
					.setProjection(Projections.rowCount()).uniqueResult();
		}catch(Exception e){
			return 0;
		}
		finally {
			session.close();
		}
	}

	/**
	 * Retorna o n�mero de registros
	 * 
	 * @return Number
	 */
	public Number count(Session session) {
		return (Number) session.createCriteria(persistentClass)
				.setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * Retorna o n�mero de registros para um consumerSecret
	 * 
	 * @return Number
	 */
	public Number countPorConsumerSecret(Session session, String consumerKey, Integer statusModel, Date dataAlteracao) {
		Query c = session.createQuery("select "
				+ " count(*) from "
				+ persistentClass.getName()
				+ " where consumerSecret.consumerKey = :consumerKey and statusModel < :statusModel and dataAlteracao >= :date");
		c.setParameter("consumerKey", Long.parseLong(consumerKey));
		c.setParameter("statusModel", statusModel);
		c.setParameter("date", dataAlteracao);
		return (Number) c.uniqueResult();
	}

	/**
	 * Retorna o n�mero de registros para um consumerSecret
	 * 
	 * @return Number
	 */
	public Number countPorConsumerSecret(Long consumerKey) {
		Session session = HibernateHelper.openSession(persistentClass);
		try {

			Query c = session.createQuery("select "
					+ " count(*) from "
					+ persistentClass.getName()
					+ " where consumerSecret.consumerKey = :consumerKey");
			c.setParameter("consumerKey", consumerKey);
			return (Number) c.uniqueResult();
		} finally {
			session.clear();
			session.close();
			session = null;
		}
	}

	public Number countAll(ParameterDAO... parameters) {
		Session session = HibernateHelper.openSession(persistentClass);
		try {
			Criteria criteria = session.createCriteria(persistentClass);
			criteria.setProjection(Projections.rowCount());
			ParameterDAO.createRestrictions(criteria,parameters);
			return (Number) criteria.uniqueResult();
		} finally {
			session.clear();
			session.close();
		}
	}

	public Query consulta(Session session, String query) {
		Query q = session.createQuery(query);
		return q;
	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisaLista(Session session, ArrayList<ParameterDAO> parameters) {
		Criteria criteria = session.createCriteria(persistentClass);
		ParameterDAO.createRestrictions(criteria,parameters);
		List<T> lista = criteria.list();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisaLista(Session session, ParameterDAO... parameters) {
		Criteria criteria = session.createCriteria(persistentClass);
		ParameterDAO.createRestrictions(criteria,parameters);
		List<T> lista = criteria.list();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisaLista(ParameterDAO... parameters) {
		Session session = HibernateHelper.currentSession();
		Transaction tx = session.beginTransaction();
		Criteria criteria = null;
		try {
			criteria = session.createCriteria(persistentClass);
			ParameterDAO.createRestrictions(criteria, parameters);
			List<T> t = criteria.list();
			tx.commit();
			return t;
		} finally {
			tx = null;
			criteria = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisaLista(ArrayList<ParameterDAO> parameters) {
		Session session = HibernateHelper.currentSession();
		Transaction tx = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(persistentClass);
			ParameterDAO.createRestrictions(criteria, parameters);
			List<T> lista = criteria.list();
			tx.commit();
			return lista;
		} finally {
			tx = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisaListaPorConsumerSecret(Session session, Long consumerKey,
			ParameterDAO... parameters) {
		Criteria criteria = session.createCriteria(persistentClass);
		criteria.createAlias("consumerSecret", "consumerSecret");
		criteria.add(Restrictions.eq("consumerSecret.consumerKey", consumerKey));
		ParameterDAO.createRestrictions(criteria, parameters);
		List<T> lista = criteria.list();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisaListaPorConsumerSecret(Long consumerKey, ParameterDAO... parameters) {
		Session session = HibernateHelper.currentSession();
		Transaction transaction = session.beginTransaction();
	
		try {
			Criteria criteria = session.createCriteria(persistentClass);
			criteria.createAlias("consumerSecret", "consumerSecret");
			criteria.add(Restrictions.eq("consumerSecret.consumerKey", consumerKey));
			ParameterDAO.createRestrictions(criteria, parameters);
			List<T> lista = criteria.list();
			transaction.commit();
			return lista;
		} finally {
			transaction = null;
			session.close();
			session = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisaListaPorConsumerSecret(Session session, Long consumerSecret,
			ArrayList<ParameterDAO> parameters) {
		Criteria criteria = session.createCriteria(persistentClass);
		criteria.createAlias("consumerSecret", "consumerSecret");
		ParameterDAO.createRestrictions(criteria, parameters);
		criteria.add(Restrictions.eq("consumerSecret.consumerKey", consumerSecret));
		List<T> t = criteria.list();
		return t;
	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisaListaPorConsumerSecretAtivo(Session session,
			ArrayList<ParameterDAO> parameters, String consumerSecret) {
		Criteria criteria = session.createCriteria(persistentClass);
		criteria.createAlias("consumerSecret", "consumerSecret");
		criteria.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
		ParameterDAO.createRestrictions(criteria, parameters);
		criteria.add(Restrictions.le("statusModel", StatusModel.ATIVO));
		List<T> lista = criteria.list();
		return lista;
	}

	/**
	 * Retorna todos os registros da tabela (classe) informada
	 * 
	 * @param objClass
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<T> listaAll() {
		Session session = HibernateHelper.currentSession();
		Transaction tx = session.beginTransaction();

		try {
			List<T> t = session.createCriteria(persistentClass).list();
			tx.commit();
			return t;
		} finally {
			tx = null;
		}
	}

	/**
	 * Retorna todos os registros da tabela (classe) informada
	 * 
	 * @param objClass
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<T> listaAll(Session session) {
		List<T> t = session.createCriteria(persistentClass).list();
		return t;
	}

	/**
	 * Retorna todos os registros da tabela (classe) informada
	 * 
	 * @param objClass
	 * @return ArrayList<Object>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<T> listaPorConstrutor(String atributos) {
		Query q;
		Session session = HibernateHelper.currentSession();
		Transaction tx = session.beginTransaction();

		q = session.createQuery("select new "
				+ persistentClass.getName()
				+ "("+ atributos + ") from "
				+ persistentClass.getName() 
				+ " where statusModel < 2 ORDER BY id ASC");

		ArrayList<T> t =(ArrayList<T>) q.list();
		tx.commit();
		session.close();
		return t;
	}

	/**
	 * Retorna todos os registros da tabela (classe) informada
	 * 
	 * @param objClass
	 * @return ArrayList<Object>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<T> listaPorConstrutorConsumerKey(String atributos, String consumerKey) {
		Query q;
		Session session = HibernateHelper.currentSession();
		Transaction tx = session.beginTransaction();

		q = session.createQuery("select new "
				+ persistentClass.getName()
				+ "("+ atributos + ") from "
				+ persistentClass.getName()
				+ " where consumerSecret.consumerKey = :cs and statusModel < 2 ORDER BY id ASC");
		q.setParameter("cs", Long.parseLong(consumerKey));

		ArrayList<T> t =(ArrayList<T>) q.list();
		tx.commit();
		session.close();
		return t;
	}


	/**
	 * Retorna todos os registros da tabela (classe) informada
	 * 
	 * @param objClass
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<T> pesquisalistaPorDataAlteracaEConsumerSecret(Session session, Integer inicial, Integer tamanho,
			String consumerSecret, String atributos, Date dataAlteracao, Integer statusModel) {
		Query q;
		if (atributos.length() > 0)
			q = session
			.createQuery("select new "
					+ persistentClass.getName()
					+ "("+ atributos+ ") from "
					+ persistentClass.getName()
					+ " where consumerSecret.consumerKey = :cs and dataAlteracao >= :date and statusModel < "+statusModel+" ORDER BY id ASC");
		else
			q = session
			.createQuery("from "
					+ persistentClass.getName()
					+ " where consumerSecret.consumerKey = :cs and dataAlteracao >= :date and statusModel < "+statusModel+" ORDER BY id ASC");

		q.setParameter("cs", Long.parseLong(consumerSecret));
		q.setParameter("date", dataAlteracao);
		return (ArrayList<T>) q.setFirstResult(inicial).setMaxResults(tamanho)
				.list();

	}

	/**
	 * Retorna todos os registros da tabela (classe) informada
	 * 
	 * @param objClass
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<T> pesquisalistaPorConsumerSecret(Session session, Integer inicial, Integer tamanho,
			String consumerSecret, String atributos) {
		Query q;
		if (atributos.length() > 0)
			q = session
			.createQuery("select new "
					+ persistentClass.getName()
					+ "("
					+ atributos
					+ ") from "
					+ persistentClass.getName()
					+ " where consumerSecret.consumerKey = :cs and statusModel < 2 ORDER BY id ASC");
		else
			q = session
			.createQuery("from "
					+ persistentClass.getName()
					+ " where consumerSecret.consumerKey = :cs and statusModel < 2 ORDER BY id ASC");

		q.setParameter("cs", Long.parseLong(consumerSecret));
		return (ArrayList<T>) q.setFirstResult(inicial).setMaxResults(tamanho)
				.list();

	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisalistaPorDataAlteracao(Integer inicial, Integer tamanho, String atributos,
			Date dataAlteracao) {

		Session session = HibernateHelper.currentSession();
		Transaction tx = session.beginTransaction();
		Query q = null;

		try {
			if (atributos.length() > 0)
				q = session.createQuery("select new "
						+ persistentClass.getName() + "(" + atributos
						+ ") from " + persistentClass.getName()
						+ " where dataAlteracao >= :date and statusModel < 2 ORDER BY id ASC");
			else
				q = session.createQuery("from " + persistentClass.getName()
						+ " where dataAlteracao >= :date and statusModel < 2 ORDER BY id ASC");

			q.setParameter("date", dataAlteracao);
			List<T> t = (ArrayList<T>) q.setFirstResult(inicial)
					.setMaxResults(tamanho).list();
			tx.commit();
			return t;

		} finally {
			tx = null;
			q = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> pesquisalistaPorDataAlteracao(Session session, Integer inicial, Integer tamanho,
			String atributo, Date dataAlteracao) {
		Query query;
		if (atributo.length() > 0)
			query = session.createQuery("select new " + persistentClass.getName()
					+ "(" + atributo + ") from " + persistentClass.getName()
					+ " where dataAlteracao >= :date and statusModel < 2 ORDER BY id ASC");
		else
			query = session.createQuery("from " + persistentClass.getName()
					+ " where dataAlteracao >= :date and statusModel < 2 ORDER BY id ASC");

		query.setParameter("date", dataAlteracao);
		return (ArrayList<T>) query.setFirstResult(inicial).setMaxResults(tamanho)
				.list();
	}


	@SuppressWarnings("unchecked")
	public T pesquisa(ArrayList<ParameterDAO> parameters) {
		Session session = HibernateHelper.currentSession();
		Transaction tx = session.beginTransaction();
		Criteria criteria = null;
		try {
			criteria = session.createCriteria(persistentClass);
			ParameterDAO.createRestrictions(criteria, parameters);
			T model = (T) criteria.uniqueResult();
			tx.commit();
			return model;
		} finally {
			tx = null;
			criteria = null;
		}
	}

	@SuppressWarnings("unchecked")
	public T pesquisa(Session session, ArrayList<ParameterDAO> parameters) {
		Criteria criteria = session.createCriteria(persistentClass);
		ParameterDAO.createRestrictions(criteria, parameters);
		return (T) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public T pesquisaPorConsumerSecret(Session session, ArrayList<ParameterDAO> parameters,
			String consumerSecret) {
		Criteria criteria = session.createCriteria(persistentClass);
		criteria.createAlias("consumerSecret", "consumerSecret");
		criteria.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
		ParameterDAO.createRestrictions(criteria, parameters);
		return (T) criteria.uniqueResult();
	}

	/**
	 * Retorna apenas um objeto referente a classe e id informados
	 * 
	 * @param id
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public T loadPorIdConsumerSecret(Long id, String consumerSecret) {

		Session session = HibernateHelper.currentSession();
		Transaction tx = session.beginTransaction();
		Criteria c = null;
		try {
			c = session.createCriteria(persistentClass);
			c.createAlias("consumerSecret", "consumerSecret");
			c.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
			c.add(Restrictions.eq("id", id));
			c.add(Restrictions.le("statusModel", StatusModel.ATIVO));
			T t = (T) c.uniqueResult();
			tx.commit();
			Hibernate.initialize(t);
			return t;
		} finally {
			tx = null;
			c = null;
		}
	}

	/**
	 * Retorna apenas um objeto referente a classe e id informados
	 * 
	 * @param id
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public T loadPorIdConsumerSecret(Session session, Long id, String consumerSecret) {

		Criteria c = session.createCriteria(persistentClass);
		c.createAlias("consumerSecret", "consumerSecret");
		c.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
		c.add(Restrictions.eq("id", id));
		c.add(Restrictions.le("statusModel", StatusModel.ATIVO));
		T t = (T) c.uniqueResult();
		Hibernate.initialize(t);
		return t;
	}

	/**
	 * Retorna apenas um objeto referente a classe e id informados
	 * 
	 * @param codigo
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public T loadPorCodigoConsumerSecret(Session session, String codigo, String consumerSecret) {

		Criteria c = session.createCriteria(persistentClass);
		c.createAlias("consumerSecret", "consumerSecret");
		c.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
		c.add(Restrictions.eq("codigo", codigo));
		c.add(Restrictions.le("statusModel", StatusModel.ATIVO));
		T t = (T) c.uniqueResult();
		Hibernate.initialize(t);
		return t;
	}


	/**
	 * Retorna apenas um objeto referente a classe e id informados
	 * 
	 * @param codigo
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public T loadPorCodigoConsumerSecret(String codigo, String consumerSecret) {
		Session session = HibernateHelper.openSession(getClass());
		try{
			Criteria c = session.createCriteria(persistentClass);
			c.createAlias("consumerSecret", "consumerSecret");
			c.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
			c.add(Restrictions.eq("codigo", codigo));
			c.add(Restrictions.le("statusModel", StatusModel.ATIVO));
			T t = (T) c.uniqueResult();
			Hibernate.initialize(t);
			return t;
		}finally{
			session.close();
		}
	}

	/**
	 * Retorna apenas um objeto referente a classe e id informados
	 * 
	 * @param id
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public T loadPorIdConsumerSecret(Session session, Long id, String consumerSecret, Integer status) {

		Criteria c = session.createCriteria(persistentClass);
		c.createAlias("consumerSecret", "consumerSecret");
		c.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
		c.add(Restrictions.eq("id", id));
		c.add(Restrictions.le("statusModel", status));
		T t = (T) c.uniqueResult();
		Hibernate.initialize(t);
		return t;
	}

	/**
	 * Retorna apenas um objeto referente a classe e id informados
	 * 
	 * @param id
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public T loadPorId(Session session, Long id) {
		Criteria c = session.createCriteria(persistentClass);
		c.add(Restrictions.eq("id", id));
		c.add(Restrictions.ne("statusModel", StatusModel.DELETADO));
		T t = (T) c.uniqueResult();
		Hibernate.initialize(t);
		return t;
	}

	/**
	 * Retorna apenas um objeto referente a classe e id informados
	 * 
	 * @param id
	 * @return Object
	 */
	public T loadPorId(Long id) {
		Session session = HibernateHelper.openSession(persistentClass);
		T t = loadPorId(session, id);
		session.close();
		return t;
	}


	/**
	 * Retorna apenas um objeto referente a classe e id informados
	 * 
	 * @param id
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public T pesquisaReturnID(Session session, Long id, String consumerSecret) {
		try {
			Criteria criteria = session
					.createCriteria(persistentClass)
					.createAlias("consumerSecret", "consumerSecret")
					.setProjection(
							Projections
							.projectionList()
							.add(Projections.property("id"), "id")
							.add(Projections
									.property("consumerSecret.id"),
									"consumerId"))
									.setResultTransformer(
											Transformers.aliasToBean(persistentClass));
			criteria.add(Restrictions.eq("consumerSecret.consumerKey",
					Long.parseLong(consumerSecret)));
			criteria.add(Restrictions.eq("id", id));
			T t = (T) criteria.uniqueResult();
			Hibernate.initialize(t);
			return t;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Persiste o objeto passado por par�metro
	 * 
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	public boolean save(T model) throws DAOException {
		Session session = HibernateHelper.openSession(persistentClass);
		Transaction transaction = session.beginTransaction();
		try {
			model = (T) session.save(model);
			transaction.commit();
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			throw new DAOException(bundle.getString("invalidregistry"), 1);
		}
		catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
			session = null;
			transaction = null;
		}
	}

	/**
	 * Persiste o objeto passado por par�metro
	 * 
	 * @param obj
	 */
	public boolean save(Session session, T obj) throws DAOException {
		Transaction transaction = session.beginTransaction();
		try {
			session.save(obj);
			transaction.commit();
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			throw new DAOException(bundle.getString("invalidregistry"), 1);
		}catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}catch (Exception e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			transaction = null;
		}
	}

	/**
	 * Persiste ou altera o objeto passado por par�metro
	 * 
	 * @param obj
	 */
	public boolean saveOrMerge(T obj) throws DAOException {
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction transaction = session.beginTransaction();

		try {
			session.saveOrUpdate(obj);
			transaction.commit();
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			throw new DAOException(bundle.getString("invalidregistry"), 1);
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
			session = null;
			transaction = null;
		}
	}

	/**
	 * Persiste ou altera o objeto passado por par�metro
	 * 
	 * @param obj
	 */
	public boolean saveOrMerge(Session session, T obj) throws DAOException {
		Transaction transaction = session.beginTransaction();
		try {
			session.saveOrUpdate(obj);
			transaction.commit();
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			throw new DAOException(bundle.getString("invalidregistry"), 1);
		}catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			transaction = null;
		}
	}

	/**
	 * Exclui o objeto passado por par�metro
	 * 
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public boolean delete(Long id) throws DAOException {
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction transaction = session.beginTransaction();
		try {
			T t = (T) session.load(persistentClass, id);
			session.delete(t);
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
			session = null;
			transaction = null;
		}
	}

	/**
	 * Exclui o objeto passado por parametro
	 * 
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public boolean delete(Session session, Long id) throws DAOException {
		Transaction transaction = session.beginTransaction();
		try {
			T model = (T) session.load(persistentClass, id);
			session.delete(model);
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			transaction = null;
		}
	}

	/**
	 * Exclui o objeto passado por par�metro
	 * 
	 * @param obj
	 */
	public boolean delete(T model) throws DAOException {
		Session session = HibernateHelper.openSession(getClass());
		Transaction transaction = session.beginTransaction();

		try {
			session.delete(model);
			transaction.commit();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
			session = null;
			transaction = null;
		}
	}

	public boolean delete(Session session, Model<?> model) throws DAOException {
		Query query = session.createQuery("delete " + model.getClass().getName()
				+ " where id = :id");
		query.setParameter("id", model.getId());
		if (query.executeUpdate() > 0)
			return true;
		else
			return false;
	}

	/**
	 * Exclui o objeto passado por par�metro
	 * 
	 * @param obj
	 */
	public boolean delete(Session session, T model) throws DAOException {
		Transaction transaction = session.beginTransaction();
		try {
			session.delete(model);
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			transaction = null;
		}
	}

	/**
	 * Altera o objeto passado por par�metro
	 * 
	 * @param obj
	 */
	public boolean merge(T model) throws DAOException {
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction transaction = session.beginTransaction();
		try {
			session.evict(model);
			session.merge(model);
			transaction.commit();
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(bundle.getString("invalidregistry"), 1);
		}catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
			session = null;
			transaction = null;
		}
	}

	/**
	 * Altera o objeto passado por par�metro
	 * 
	 * @param obj
	 */
	public boolean merge(Session session, T model) throws DAOException {
		Transaction transaction = session.beginTransaction();
		
		try {
			session.merge(model);
			transaction.commit();
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			throw new DAOException(bundle.getString("invalidregistry"), 1);
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
			session = null;
			transaction = null;
		}
	}

	public boolean update(T model) throws DAOException {
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction transaction = session.beginTransaction();
		
		try {
			transaction = session.beginTransaction();
			session.update(model);
			transaction.commit();
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			throw new DAOException(bundle.getString("invalidregistry"), 1);
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
			session = null;
			transaction = null;
		}
	}

	public boolean update(Session session, T model) throws DAOException {
		Transaction transaction = session.beginTransaction();
		try {
			transaction = session.beginTransaction();
			session.update(model);
			transaction.commit();
			return true;
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			transaction = null;
		}
	}

	/**
	 * Altera as data de altera��o de um modelo especifico
	 * 
	 * @param model //modelo
	 * @param session //sess�o corrente
	 */
	public boolean updateDataAlteracaoPorId(Session session, Model<T> model) throws DAOException {
		Transaction transaction = session.beginTransaction();
		
		try {
			
			Query query = session.createQuery("update "
					+ persistentClass.getName()
					+ " set dataAlteracao = :dataAlteracao where id = :id");
			query.setParameter("dataAlteracao", model.getDataAlteracao());
			query.setParameter("id", model.getId());
			query.getQueryString();
			query.executeUpdate();

			transaction.commit();
			return true;
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		}finally{
			transaction = null;
		}
	}
}