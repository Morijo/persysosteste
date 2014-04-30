package br.com.funcionario.dao;

import java.util.ArrayList;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.funcionario.model.Funcionario;
import br.com.principal.helper.HibernateHelper;

public class FuncionarioDAO extends DAO<Funcionario> {

	public FuncionarioDAO() {
		super(Funcionario.class);
	}

	public Funcionario pequisaFuncionario(Long idFuncionario, String consumerKey) throws DAOException{
		Session session = HibernateHelper.openSession(FuncionarioDAO.class);
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery("select new "
					+ Funcionario.class.getName()
					+ " ("+Funcionario.CONSTRUTORF+") from "
					+ Funcionario.class.getName()
					+ " where id = :idFuncionario and consumerSecret.consumerKey = :consumerKey");
			query.setParameter("idFuncionario",idFuncionario);
			query.setParameter("consumerKey",Long.parseLong(consumerKey));
			return (Funcionario) query.uniqueResult();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			tx.commit();
			session.close();
			session = null;
			tx = null;
		}
	}
	
	public Funcionario pequisaFuncionarioPorCodigo(String codigo, String consumerKey) throws DAOException{
		Session session = HibernateHelper.openSession(DAO.class);
		session.clear();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery("select new "
					+ Funcionario.class.getName()
					+ " ("+Funcionario.CONSTRUTORF+") from "
					+ Funcionario.class.getName()
					+ " where codigo = :codigo and consumerSecret.consumerKey = :consumerKey ");
			query.setParameter("codigo",codigo);
			query.setParameter("consumerKey",Long.parseLong(consumerKey));
			return (Funcionario) query.uniqueResult();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			tx.commit();
			tx = null;
			session.close();
			session = null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Funcionario> listaFuncionario(Long consumerKey) throws DAOException{
		Session session = HibernateHelper.openSession(DAO.class);
		session.clear();
		Transaction tx = session.beginTransaction();

		try {
			Query query = session.createQuery("select new "
					+ Funcionario.class.getName()
					+ " ("+Funcionario.CONSTRUTORF+") from "
					+ Funcionario.class.getName()
					+ " where consumerSecret.consumerKey = :consumerKey ");
			query.setParameter("consumerKey",consumerKey);
			return (ArrayList<Funcionario>) query.list();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			tx.commit();
			tx = null;
			session.close();
			session = null;
		}
	}
	
	public boolean alteraFuncionario(Funcionario funcionario)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ funcionario.getClass().getName()
							+ " set razaoNome = :razaoNome, fantasiaSobrenome = :fantasiaSobrenome, "
							+ " statusModel = :statusModel, dataNascimento = :dataNascimento, "
							+ " cnpjCpf = :cnpjCpf, departamento = :departamento, "
							+ " situacao = :situacao, emailPrincipal = :emailPrincipal, "
							+ " grupoUsuario = :grupoUsuario, "
							+ " dataAlteracao = :dataAlteracao"
							+ " where id = :id and and consumerSecret.consumerKey = :consumerKey");
			query.setParameter("razaoNome", funcionario.getRazaoNome());
			query.setParameter("fantasiaSobrenome", funcionario.getFantasiaSobrenome());
			query.setParameter("statusModel", funcionario.getStatusModel());
			query.setParameter("dataNascimento", funcionario.getDataNascimento());
			query.setParameter("cnpjCpf", funcionario.getCnpjCpf());
			query.setParameter("departamento", funcionario.getDepartamento());
			query.setParameter("situacao", funcionario.getSituacao());
			query.setParameter("emailPrincipal", funcionario.getEmailPrincipal());
			query.setParameter("grupoUsuario", funcionario.getGrupoUsuario());
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", funcionario.getId());
			query.setParameter("consumerKey", funcionario.getConsumerSecret().getConsumerKey());
			
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