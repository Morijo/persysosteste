package br.com.empresa.dao;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import br.com.contato.model.Endereco;
import br.com.dao.DAO;
import br.com.empresa.model.Organizacao;
import br.com.exception.DAOException;
import br.com.principal.helper.HibernateHelper;

public class OrganizacaoDAO extends DAO<Organizacao> {

	private Session session = null;

	public OrganizacaoDAO( Class<?> classe) {
		super(Organizacao.class);
	}

	public OrganizacaoDAO(Session session, Class<?> classe) {
		super(Organizacao.class);
		this.session = session;
	}

	public Organizacao pesquisaConsumerSecret(String consumerSecret){
		if(session == null)
			session = HibernateHelper.openSession(Organizacao.class);
		Criteria c = session.createCriteria(Organizacao.class);
		c.createAlias("consumerSecret", "consumerSecret");
		c.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerSecret)));
		c.add(Restrictions.eq("statusModel", 1));
		Organizacao organizacao = (Organizacao) c.uniqueResult();
		Hibernate.initialize(organizacao);
		c = null;
		return organizacao;
	}


	public Organizacao pesquisaOrganizacaoId(Long id) throws DAOException {
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();
		try{
			Criteria criteria = session.createCriteria(Organizacao.class);
			criteria.add(Restrictions.eq("id", id));
			criteria.add(Restrictions.eq("statusModel", 1));
			Organizacao organizacao = (Organizacao) criteria.uniqueResult();
			Hibernate.initialize(organizacao);
			tx.commit();
			return organizacao;
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
		}
	}
	
	public boolean alteraEndereco(Long id, Endereco endereco)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("update "
							+  Organizacao.class.getName()
							+ " set endereco = :endereco "
							+ " where id = :id");
			query.setParameter("endereco", endereco);
			query.setParameter("id", id);
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