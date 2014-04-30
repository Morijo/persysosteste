package br.com.ordem.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.ordem.model.RecursoOrdem;
import br.com.principal.helper.HibernateHelper;

public class RecursoOrdemDAO {

	@SuppressWarnings("unchecked")
	public List<RecursoOrdem> listaRecurso(Long idOrdem, int statusModel) throws DAOException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query c = HibernateHelper.currentSession().createQuery("select new "
					+ RecursoOrdem.class.getName()
					+ " ("+RecursoOrdem.CONSTRUTOR+") from "
					+ RecursoOrdem.class.getName()
					+ " where ordem.id = :ordemid and statusModel <= :statusModel ORDER BY id ASC");
			c.setParameter("ordemid", idOrdem);
			c.setParameter("statusModel", statusModel);
			return c.list();
		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}
	}
	
	public boolean altera(RecursoOrdem recursoOrdem, Long consumerKey)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ recursoOrdem.getClass().getName()
							+ " set dataAlteracao = :dataAlteracao,"
							+ " quantidadeConsumida = :quantidade,"
							+ " statusModel = :statusModel where id = :id and consumerSecret.consumerKey = :consumerKey");
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("statusModel", recursoOrdem.getStatusModel());
			query.setParameter("quantidade", recursoOrdem.getQuantidadeConsumida());
			query.setParameter("consumerKey", consumerKey);
			query.setParameter("id", recursoOrdem.getId());
			query.getQueryString();
			query.executeUpdate();

			transaction.commit();
			return true;
		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;

		}
	}
	
	public boolean remover(RecursoOrdem recursoOrdem)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("delete "
							+ recursoOrdem.getClass().getName()
							+ " where id = :id");
			query.setParameter("id", recursoOrdem.getId());
			query.getQueryString();
			query.executeUpdate();

			transaction.commit();
			return true;
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
