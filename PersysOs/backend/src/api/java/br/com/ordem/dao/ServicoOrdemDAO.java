package br.com.ordem.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.ordem.model.ServicoOrdem;
import br.com.principal.helper.HibernateHelper;


public class ServicoOrdemDAO extends DAO<ServicoOrdem> {

	public ServicoOrdemDAO() {
		super(ServicoOrdem.class);
	}

	/**
	 * Consulta tdos os servi�o ativos de uma ordem de servi�o
	 * 
	 * 
	 * @param session  //sess�o com o banco de dados
	 * @param inicial  //posi��o inicial dos dados
	 * @param tamanho  //n�mero de registro
	 * @param consumerSecret //chave da empresa
	 * @return List<ContratoDTO>
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public List<ServicoOrdem> listaServico(Long idOrdem, int statusModel) throws DAOException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query c = HibernateHelper.currentSession().createQuery("select new "
					+ ServicoOrdem.class.getName()
					+ " ("+ServicoOrdem.CONSTRUTOR+") from "
					+ ServicoOrdem.class.getName()
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
	
	public boolean altera(ServicoOrdem servicoOrdem)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ servicoOrdem.getClass().getName()
							+ " set dataAlteracao = :dataAlteracao,"
							+ " statusModel = :statusModel where id = :id");
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("statusModel", servicoOrdem.getStatusModel());
			query.setParameter("id", servicoOrdem.getId());
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
	
	public boolean remover(ServicoOrdem servicoOrdem)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("delete "
							+ servicoOrdem.getClass().getName()
							+ " where id = :id");
			query.setParameter("id", servicoOrdem.getId());
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
