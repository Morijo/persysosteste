package br.com.clienteobjeto.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.clienteobjeto.model.ClienteObjetoProduto;
import br.com.clienteobjeto.model.Contrato;
import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.ordem.model.Ordem;
import br.com.principal.helper.HibernateHelper;

/**
 * Consultas especificas para Contrato
 * 
 *@author ricardosabatine
 *@version 1.0
 *@since 1.0
 *@see DAO, Contrato, ContratoDTO
 */
public class ContratoDAO extends DAO<Contrato> {

	public ContratoDAO() {
		super(Contrato.class);
	}

	/**
	 * Consulta tdos os contratos ativos de uma organiza��o
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
	public List<Contrato> listaContrato(Integer inicial, Integer tamanho, String consumerSecret, Integer statusModel) throws DAOException{

		Session session = HibernateHelper.openSession(ContratoDAO.class);
		Transaction tx = session.beginTransaction();

		try{

			Query q = session.createQuery("select new "
					+  Contrato.class.getName() 
					+  Contrato.CONSTRUTOR	+" from "
					+  Contrato.class.getName()
					+ " where consumerSecret.consumerKey = :consumer and statusModel < :statusModel");

			q.setParameter("consumer", Long.parseLong(consumerSecret));
			q.setParameter("statusModel", statusModel);
			List<Contrato> list = q.setFirstResult(inicial).setMaxResults(tamanho).list();
			tx.commit();
			return list;
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	/**
	 * Consulta tdos os contratos ativos de um cliente
	 * 
	 * 
	 * @param session  //sess�o com o banco de dados
	 * @param inicial  //posi��o inicial dos dados
	 * @param tamanho  //n�mero de registro
	 * @param consumerSecret //chave da empresa
	 *  @param idCliente //id do cliente
	 * @return List<ContratoDTO>
	 * @throws DAOException 
	 **/
	@SuppressWarnings("unchecked")
	public List<Contrato> listaContratoCliente(Integer inicial, Integer tamanho, String consumerSecret, Long idCliente) throws DAOException{

		Session session = HibernateHelper.openSession(ContratoDAO.class);
		Transaction tx = session.beginTransaction();

		try{

			Query q = session.createQuery("select new "
					+  Contrato.class.getName() 
					+  Contrato.CONSTRUTOR	+" from "
					+  Contrato.class.getName()
					+ " where consumerSecret.consumerKey = :consumer and statusModel < :statusModel and cliente.id = :idCliente");

			q.setParameter("consumer", Long.parseLong(consumerSecret));
			q.setParameter("idCliente", idCliente);
			q.setParameter("statusModel", 2);
			List<Contrato> list = q.setFirstResult(inicial).setMaxResults(tamanho).list();
			tx.commit();
			return list;
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}finally {
			tx = null;
			session.close();
			session = null;
		}
	}


	/**
	 * Consulta tdos os produtos de um contrato ativo
	 * 
	 * 
	 * @param session  //sess�o com o banco de dados
	 * @param clienteObjeto
	 * @param consumerSecret //chave da empresa
	 * @return List<ClienteObjetoProdutoDTO>
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public List<ClienteObjetoProduto> listaContratoProduto(Long clienteObjeto, String consumerSecret) throws DAOException{

		Session session = HibernateHelper.openSession(ContratoDAO.class);
		Transaction tx = session.beginTransaction();

		try{

			Query q = session.createQuery("select new "
					+  ClienteObjetoProduto.class.getName() 
					+  ClienteObjetoProduto.CONSTRUTOR	+" from "
					+  ClienteObjetoProduto.class.getName()
					+ " where consumerSecret.consumerKey = :consumer and clienteObjeto.id = :id and statusModel < 2");

			q.setParameter("consumer", Long.parseLong(consumerSecret));
			q.setParameter("id", clienteObjeto);
			tx.commit();
			return q.list();
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public ArrayList<Ordem> listaContratoOrdem(Long idContrato, String consumerKey) throws DAOException{
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Ordem.class.getName() 
					+  Ordem.CONSTRUTOR	
					+ Ordem.class.getName()
					+ " where consumerSecret.consumerKey = :consumer and clienteObjeto.id = :id");

			q.setParameter("id", idContrato);
			q.setParameter("consumer", Long.parseLong(consumerKey));

			@SuppressWarnings("unchecked")
			ArrayList<Ordem> ordens = (ArrayList<Ordem>) q.list();
			tx.commit();
			return ordens;
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public boolean alteraSituacao(ClienteObjetoProduto clienteObjetoProduto, String consumerKey)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ clienteObjetoProduto.getClass().getName()
							+ " set dataAlteracao =:dataAlteracao, statusModel = :situacao"
							+ " where consumerSecret.consumerKey = :consumer and id = :id");

			query.setParameter("situacao", clienteObjetoProduto.getStatusModel());
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("consumer", Long.parseLong(consumerKey));
			query.setParameter("id", clienteObjetoProduto.getId());
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