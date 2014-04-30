package br.com.empresa.dao;

import java.util.ArrayList;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import br.com.dao.DAO;
import br.com.empresa.model.Departamento;
import br.com.exception.DAOException;
import br.com.principal.helper.HibernateHelper;

/**
 *Consutas especificas para Departamento 
 *@author ricardosabatine
 *@version 1.0
 *@since 1.0
 */
public class DepartamentoDAO extends DAO<Departamento> {
	
	public DepartamentoDAO() {
		super(Departamento.class);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Departamento> listaDepartamentosPorUnidade(Long unidadeId, Integer statusModel, Long consumerKey) throws DAOException
	{
		Session session = null;
		try {
			session = HibernateHelper.openSession(DAO.class);

			Query c = session.createQuery("select new "
					+ Departamento.class.getName()
					+ " ("+Departamento.CONSTRUTOR+") from "
					+ Departamento.class.getName()
					+ " where statusModel <= :statusModel and consumerSecret.consumerKey = :consumerKey and unidade.id = :unidadeid"
					+ " ORDER BY id DESC");
			c.setParameter("statusModel", statusModel);
			c.setParameter("consumerKey", consumerKey);
			c.setParameter("unidadeid", unidadeId);

			return (ArrayList<Departamento>) c.list();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}	
	}
}