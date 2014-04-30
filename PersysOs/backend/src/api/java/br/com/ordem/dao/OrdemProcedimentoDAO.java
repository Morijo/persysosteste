package br.com.ordem.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.ordem.model.OrdemProcedimento;
import br.com.principal.helper.HibernateHelper;

public class OrdemProcedimentoDAO {

	@SuppressWarnings("unchecked")

	public List<OrdemProcedimento> listaOrdemProcedimento(Long ordemId) throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query c = HibernateHelper.currentSession().createQuery("select new "
					+ OrdemProcedimento.class.getName()
					+ " ("+OrdemProcedimento.CONSTRUTOR+") from "
					+ OrdemProcedimento.class.getName()
					+ " where ordem.id = :ordem ORDER BY id ASC");
			c.setParameter("ordem", ordemId);
			transaction.commit();
			transaction = null;
			return  (List<OrdemProcedimento>) c.list();

		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;

		}
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdemProcedimento> listaOrdemProcedimento(Long ordemId, Long servicoId) throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query c = HibernateHelper.currentSession().createQuery("select new "
					+ OrdemProcedimento.class.getName()
					+ " ("+OrdemProcedimento.CONSTRUTOR+") from "
					+ OrdemProcedimento.class.getName()
					+ " where ordem.id = :ordem and servico.id = :servico ORDER BY id ASC");
			c.setParameter("ordem", ordemId);
			c.setParameter("servico", servicoId);
			transaction.commit();
			transaction = null;
			return  (List<OrdemProcedimento>) c.list();
		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;

		}
	}
	
	public boolean alteraOrdemProcedimentoSituacao(br.com.ordem.model.OrdemProcedimento ordemProcedimento)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ ordemProcedimento.getClass().getName()
							+ " set obrigatorio = :obrigatorio,"
							+ " temAnexo = :anexo,"
							+ " statusModel= :statusModel,"
							+ " dataAlteracao = :dataAlteracao"
							+ " where id = :id");
			query.setParameter("obrigatorio", ordemProcedimento.isObrigatorio());
			query.setParameter("anexo", ordemProcedimento.isAnexo());
			query.setParameter("statusModel", ordemProcedimento.getStatusModel());
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", ordemProcedimento.getId());
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
	
	public boolean alteraOrdemProcedimentoSituacao(Long ordemId, Long servicoId, int status)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ OrdemProcedimento.class.getName()
							+ " set statusModel= :statusModel,"
							+ " dataAlteracao = :dataAlteracao"
							+ " where ordem.id = :ordem and servico.id = :servico");
			query.setParameter("ordem", ordemId);
			query.setParameter("servico", servicoId);
			query.setParameter("statusModel", status);
			query.setParameter("dataAlteracao", new Date());
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
