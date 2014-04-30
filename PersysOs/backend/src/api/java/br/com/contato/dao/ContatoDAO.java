package br.com.contato.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.contato.model.Contato;
import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.principal.helper.HibernateHelper;

public class ContatoDAO extends DAO<Contato> {

	public ContatoDAO() {
		super(Contato.class);
	}
	
	public Contato pesquisa(Long id, Long consumerKey) throws DAOException{
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Contato.class.getName()
					+ "(id,codigo,endereco.id) from "
					+ Contato.class.getName()
					+ " where consumerSecret.consumerKey = :cs and id = :id");

			q.setParameter("cs", consumerKey);
			q.setParameter("id", id);

			Contato contato = (Contato) q.uniqueResult();
			tx.commit();
			return contato;
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public void altera(Contato contato) throws DAOException{
		Session session =	HibernateHelper.openSession(ContatoDAO.class);
		
		try{
		session.beginTransaction();
		Query query = session.createQuery("update "
				+ contato.getClass().getName()
				+ " set tipoContato = :tipoContato, nome = :nome, "
				+ " email = :email, telefoneFixo = :telefoneFixo, telefoneMovel = :telefoneMovel"
				+ " where id = :id");
		query.setParameter("tipoContato", contato.getTipoContato());
		query.setParameter("nome", contato.getNome());
		query.setParameter("email", contato.getEmail());
		query.setParameter("telefoneFixo", contato.getTelefoneFixo());
		query.setParameter("telefoneMovel", contato.getTelefoneMovel());
		query.setParameter("id", contato.getId());
		query.executeUpdate();
		session.getTransaction().commit();
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
		}
	}
	
	public void alteraEndereco(Contato contato) throws DAOException{
		Session session =	HibernateHelper.openSession(ContatoDAO.class);
		
		try{
		session.beginTransaction();
		Query query = session.createQuery("update "
				+ contato.getClass().getName()
				+ " set endereco = :endereco "
				+ " where id = :id");
		query.setParameter("endereco", contato.getEndereco());
		query.setParameter("id", contato.getId());
		query.executeUpdate();
		session.getTransaction().commit();
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
		}
		
	}
}