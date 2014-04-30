package br.com.ordem.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.ordem.model.Nota;
import br.com.ordem.model.SituacaoOrdem;
import br.com.principal.helper.HibernateHelper;

public class SituacaoOrdemDAO extends DAO<Nota> {

	public SituacaoOrdemDAO() {
		super(SituacaoOrdem.class);
	}


	public SituacaoOrdem getSituacaoServico(String codigo, Long consumerKey, int statusModel) throws DAOException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query c = HibernateHelper.currentSession().createQuery("select new "
					+ SituacaoOrdem.class.getName()
					+ " ("+SituacaoOrdem.CONSTRUTOR+") from "
					+ SituacaoOrdem.class.getName()
					+ " where codigo = :codigo and statusModel <= :statusModel and consumerSecret.consumerKey = :consumerKey");
			c.setString("codigo", codigo);
			c.setParameter("statusModel", statusModel);
			c.setParameter("consumerKey",consumerKey);
			return (SituacaoOrdem) c.uniqueResult();
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
