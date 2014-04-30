package br.com.empresa.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import br.com.dao.DAO;
import br.com.empresa.model.Unidade;
import br.com.exception.DAOException;
import br.com.principal.helper.HibernateHelper;

public class UnidadeDAO {

	public Unidade pesquisaUnidadePadrao(Long consumerKey) throws DAOException
	{
		Session session = null;
		try {
			session = HibernateHelper.openSession(DAO.class);

			Query c = session.createQuery("select new "
					+ Unidade.class.getName()
					+ " (id, codigo, nomeUnidade) from "
					+ Unidade.class.getName()
					+ " where codigo = 'PUNI0' and consumerSecret.consumerKey = :consumerKey");
			c.setParameter("consumerKey", consumerKey);
			return (Unidade) c.uniqueResult();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}	
	}
}
