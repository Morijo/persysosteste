package br.com.cliente.dao;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.cliente.model.Cliente;
import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.principal.helper.HibernateHelper;

/**
 * Consultas especificas para cliente	
 *@author ricardosabatine
 *@version 1.0
 *@since 1.0
 *@see DAO
 */
public class ClienteDAO extends DAO<Cliente> {

	public ClienteDAO() {
		super(Cliente.class);
	}
	
	public boolean alteraCliente(Cliente cliente)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ cliente.getClass().getName()
							+ " set razaoNome = :razaoNome, fantasiaSobrenome = :fantasiaSobrenome, "
							+ " statusModel = :statusModel, dataNascimento = :dataNascimento, "
							+ " cnpjCpf = :cnpjCpf, ieRg = :ieRg, "
							+ " situacao = :situacao, emailPrincipal = :emailPrincipal, "
							+ " dataAlteracao = :dataAlteracao, site = :site, tipoCliente = :tipoCliente"
							+ " where id = :id and consumerSecret.consumerKey = :consumerKey");
			query.setParameter("razaoNome", cliente.getRazaoNome());
			query.setParameter("fantasiaSobrenome", cliente.getFantasiaSobrenome());
			query.setParameter("statusModel", cliente.getStatusModel());
			query.setParameter("dataNascimento", cliente.getDataNascimento());
			query.setParameter("cnpjCpf", cliente.getCnpjCpf());
			query.setParameter("ieRg", cliente.getIeRg());
			query.setParameter("site", cliente.getSite());
			query.setParameter("situacao", cliente.getSituacao());
			query.setParameter("tipoCliente", cliente.getTipoCliente());
			query.setParameter("emailPrincipal", cliente.getEmailPrincipal());
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", cliente.getId());
			query.setParameter("consumerKey", cliente.getConsumerSecret().getConsumerKey());
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