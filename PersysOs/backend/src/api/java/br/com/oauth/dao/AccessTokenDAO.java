package br.com.oauth.dao;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.oauth.model.AccessToken;
import br.com.principal.helper.HibernateHelper;
import br.com.usuario.model.Usuario;

public class AccessTokenDAO extends DAO<AccessToken> {

	public AccessTokenDAO(){
		super(AccessTokenDAO.class);
	}

	public AccessToken pesquisaAccessToken(Session session, String token) {
		Criteria c = session.createCriteria(AccessToken.class,"at");
		c.setProjection(Projections.projectionList()
				.add(Projections.property("at.id"), "id")
				.add(Projections.property("at.secret"), "secret")
				.add(Projections.property("at.token"), "token"))
				.setResultTransformer(Transformers.aliasToBean(AccessToken.class));
		c.add(Restrictions.eq("at.token", token));

		return (AccessToken) c.uniqueResult();
	}

	public AccessToken pesquisaToken(String token) {
		Criteria c = HibernateHelper.currentSession().createCriteria(AccessToken.class);
		c.add(Restrictions.eq("token",  token ));
		AccessToken accessToken = (AccessToken)c.uniqueResult();
		Hibernate.initialize(accessToken);
		HibernateHelper.closeSession();
		c = null;
		return accessToken;
	}

	public Usuario pesquisaTokenUsuario(String token) {
		Criteria c = HibernateHelper.currentSession().createCriteria(AccessToken.class);
		c.add(Restrictions.eq("token",  token ));
		AccessToken accessToken = (AccessToken)c.uniqueResult();
		Hibernate.initialize(accessToken);
		HibernateHelper.closeSession();
		if(accessToken != null){
			c = HibernateHelper.currentSession().createCriteria(Usuario.class);
			c.add(Restrictions.eq("accessToken",  accessToken));
			Usuario usuario = (Usuario)c.uniqueResult();
			Hibernate.initialize(usuario);
			HibernateHelper.closeSession();
			c = null;
			return usuario;
		}else
			return null;

	}

	public Usuario pesquisaAccessToken(Long idUsuario) throws DAOException {
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();
		try{

			Query c = session.createQuery("select new Usuario(id,accessToken.token, accessToken.secret) from Usuario"
					+ " where id = :usuario");

			c.setParameter("usuario",idUsuario);
			Usuario usuario = (Usuario) c.uniqueResult();
			tx.commit();
			Hibernate.initialize(usuario);
			return usuario;
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}
}
