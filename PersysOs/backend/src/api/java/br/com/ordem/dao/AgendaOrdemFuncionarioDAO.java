package br.com.ordem.dao;

import java.util.ArrayList;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.ordem.model.AgendaOrdemFuncionario;
import br.com.ordem.model.Anexo;
import br.com.ordem.model.Ordem;
import br.com.principal.helper.HibernateHelper;

public class AgendaOrdemFuncionarioDAO extends DAO<Anexo> {

	public AgendaOrdemFuncionarioDAO(Class<?> classe) {
		super(classe);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<AgendaOrdemFuncionario> listaOrdemFuncinario(Long idOrdem, String consumerKey) throws DAOException {

		Session session = null;
		try {
			session = HibernateHelper.openSession(DAO.class);

			Query query = session.createQuery("select new "
					+ AgendaOrdemFuncionario.class.getName()
					+ " ("+AgendaOrdemFuncionario.CONSTRUTORL+") from "
					+ AgendaOrdemFuncionario.class.getName()
					+ " where ordem.id = :id and "
					+ "consumerSecret.consumerKey = :consumerKey");

			query.setParameter("id", idOrdem);
			query.setParameter("consumerKey", Long.parseLong(consumerKey));

			return (ArrayList<AgendaOrdemFuncionario>) query.list();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}	
	}

	@SuppressWarnings("unchecked")
	public ArrayList<AgendaOrdemFuncionario> listaFuncinarioOrdem(Long idFuncionario,
			Date dataInicio, Date dataFim, Long consumerKey) throws DAOException {

		Session session = null;
		try {
			session = HibernateHelper.openSession(DAO.class);

			Query query = session.createQuery("select new "
					+ AgendaOrdemFuncionario.class.getName()
					+ " ("+AgendaOrdemFuncionario.CONSTRUTORL+") from "
					+ AgendaOrdemFuncionario.class.getName()
					+ " where ordem.id = :id and ordem.dataAlteracaoInicio = :dataInicio and"
					+ " ordem.dataAlteracaoInicio = :dataFim and consumerSecret.consumerKey = :consumerKey");

			query.setParameter("funcionarioid", idFuncionario);
			query.setParameter("dataInicio", dataInicio);
			query.setParameter("dataFim", dataFim);
			query.setParameter("consumerKey", consumerKey);

			return (ArrayList<AgendaOrdemFuncionario>) query.list();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}	
	}

	public boolean altera(AgendaOrdemFuncionario agendaOrdemFuncionario)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ agendaOrdemFuncionario.getClass().getName()
							+ " set dataAlteracao = :dataAlteracao,"
							+ " statusModel = :statusModel where id = :id");
			query.setParameter("dataAlteracao", agendaOrdemFuncionario.getDataAlteracao());
			query.setParameter("statusModel", agendaOrdemFuncionario.getStatusModel());
			query.setParameter("id", agendaOrdemFuncionario.getId());
			query.getQueryString();
			query.executeUpdate();

			query = session
					.createQuery("update "
							+ Ordem.class.getName()
							+ " set dataAlteracao = :dataAlteracao"
							+ " where id = :id");
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", agendaOrdemFuncionario.getOrdem().getId());
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
