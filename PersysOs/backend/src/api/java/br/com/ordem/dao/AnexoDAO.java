package br.com.ordem.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.ordem.model.Anexo;
import br.com.principal.helper.HibernateHelper;

public class AnexoDAO extends DAO<Anexo> {

	public AnexoDAO(Class<?> classe) {
		super(classe);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Anexo> listaOrdemAnexo(Long consumerKey, Long idOrdem) throws DAOException {

		Session session = null;
		try {
			session = HibernateHelper.openSession(DAO.class);

			Query c = session.createQuery("select new "
					+ Anexo.class.getName()
					+ Anexo.CONSTRUTOR+" from "
					+ Anexo.class.getName()
					+ " where ordem.id = :id and "
					+ "consumerSecret.consumerKey = :consumerKey");

			c.setParameter("id", idOrdem);
			c.setParameter("consumerKey", consumerKey);

			return (ArrayList<Anexo>) c.list();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}	
	}
	
	public byte[] getImagemAnexo(Session session, String cs, Long idAnexo) {

		Criteria criteria = session.createCriteria(Anexo.class,"anexo")
				.setProjection(Projections.projectionList()
						.add(Projections.property("anexo.imagem"), "imagem"));
		criteria.add(Restrictions.eq("anexo.id", idAnexo));
		Object result = criteria.uniqueResult();
		return (byte[]) result;
	}
}
