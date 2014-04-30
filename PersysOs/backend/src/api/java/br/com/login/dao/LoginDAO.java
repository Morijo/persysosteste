package br.com.login.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.login.model.Login;

import br.com.dao.DAO;
import br.com.principal.helper.HibernateHelper;
import br.com.usuario.model.Usuario;

/**
 * Classe LoginDAO: Consultas especificas ao banco de dados
 * @author 
 * 
 */
public class LoginDAO extends DAO<Login> {
	
	private Logger logger = Logger.getLogger(LoginDAO.class);  

	public LoginDAO( Class<?> classe) {
		super(Login.class);
	}

	@SuppressWarnings("unchecked")
	public List<Login> buscaLoginUsuario(Usuario usario) {
		Session session = HibernateHelper.currentSession();
		Criteria c = session.createCriteria(Login.class);
		c.add(Restrictions.eq("usuario", usario));
		logger.info("buscaLoginUsuario");
    	return c.list();
	}

	@SuppressWarnings("unchecked")
	public List<Login> buscaLoginUsuarioAtivo(Usuario usuario){
		Session session = HibernateHelper.currentSession();
		Query q =  session.createQuery("select p from " + Login.class.getName() + " as p where p.ativo = 1 and p.usuario like :usuario");
		q.setParameter("usuario", usuario);
		logger.info("buscaLoginUsuarioAtivo");
	    return q.list();
	}
	

	@SuppressWarnings("unchecked")
	public List<Login> buscaLoginAtivo(){
		Session session = HibernateHelper.currentSession();
		Query q =  session.createQuery("select p from " + Login.class.getName() + " as p where p.ativo = 1");
		logger.info("buscaLoginAtivos");
		return q.list();
	}
	
	public Login utimoLogin(Usuario usario){
		Session session = HibernateHelper.currentSession();
		Criteria c = session.createCriteria(Login.class);
		c.add(Restrictions.eq("usuario", usario));
		c.addOrder(Order.desc("dataCreate"));
		c.setMaxResults(1);
		logger.info("utimoLogin");
		return (Login) c.uniqueResult();
	}
	
}
