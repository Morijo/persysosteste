package br.com.servico.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.principal.helper.HibernateHelper;
import br.com.servico.model.ServicoProcedimento;

public class ServicoProcedimentoDAO {

	@SuppressWarnings("unchecked")

	public List<ServicoProcedimento> listaServicoProcedimento(Long servicoId, Long consumerKey) throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query c = HibernateHelper.currentSession().createQuery("select new "
					+ ServicoProcedimento.class.getName()
					+ " ("+ServicoProcedimento.CONSTRUTOR+") from "
					+ ServicoProcedimento.class.getName()
					+ " where servico.id = :servico and consumerSecret.consumerKey = :cs ORDER BY id ASC");
			c.setParameter("servico", servicoId);
			c.setParameter("cs", consumerKey);
			transaction.commit();
			transaction = null;
			return  (List<ServicoProcedimento>) c.list();

		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;

		}
	}
	
	public boolean alteraServicoProcedimentoSituacao(ServicoProcedimento servicoProcedimento)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ servicoProcedimento.getClass().getName()
							+ " set obrigatorio = :obrigatorio, anexo = :anexo , statusModel= :statusModel,"
							+ " dataAlteracao = :dataAlteracao"
							+ " where id = :id");
			query.setParameter("obrigatorio", servicoProcedimento.isObrigatorio());
			query.setParameter("anexo", servicoProcedimento.isAnexo());
			query.setParameter("statusModel", servicoProcedimento.getStatusModel());
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", servicoProcedimento.getId());
			query.getQueryString();
			query.executeUpdate();

			transaction.commit();
			transaction = null;
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
