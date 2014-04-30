package br.com.evento.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.dao.DAO;
import br.com.eventos.model.Alerta;
import br.com.exception.DAOException;

import br.com.principal.helper.HibernateHelper;

public class AlertaDAO extends DAO<Alerta> {

	public AlertaDAO() {
		super(Alerta.class);
	}
	
	public List<Alerta> listaAlerta(Long consumerKey, int limit) throws DAOException{
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Alerta.class.getName()
					+ " ("+Alerta.CONSTRUTOR +") from "
					+ Alerta.class.getName()
					+ " where consumerSecret.consumerKey = :consumerKey");

			q.setParameter("consumerKey", consumerKey);
			
			@SuppressWarnings("unchecked")
			List<Alerta> alertas = (List<Alerta>) q.list();
			tx.commit();
			return alertas;
		} catch(Exception e){
			throw new DAOException(e.getMessage());
		}	
		 finally {
			tx = null;
			session.close();
			session = null;
		}
	}
	
	public Number countAlerta(Long consumerKey, int statusModel) throws DAOException{
		Session session = HibernateHelper.openSession(DAO.class);
		try{
			Query q = session.createQuery("select "
					+ " count(*) from "
					+ Alerta.class.getName()
					+ " where consumerSecret.consumerKey = :consumerKey and statusModel = :statusModel");
			q.setParameter("consumerKey", consumerKey);
			q.setParameter("statusModel", statusModel);
			return (Number) q.uniqueResult();
		} catch(Exception e){
			throw new DAOException(e.getMessage());
		}	
		 finally {
			session.close();
			session = null;
		}
	}
	
	public static void lida(Long consumerKey, int statusModel) throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ Alerta.class.getName()
							+ " set dataAlteracao = :dataAlteracao, statusModel = 0"
							+ " where consumerSecret.consumerKey = :consumerKey and statusModel = :statusModel");
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("consumerKey", consumerKey);
			query.setParameter("statusModel", statusModel);
			query.executeUpdate();
			transaction.commit();
			transaction = null;
		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}
	}
}