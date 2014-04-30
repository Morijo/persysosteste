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
import br.com.oauth.model.RequestToken;
import br.com.principal.helper.HibernateHelper;

public class RequestTokenDAO extends DAO<RequestToken> {

	public RequestTokenDAO(){
		super(RequestToken.class);
	}
	
	public RequestToken pesquisaToken(String token) {
		Criteria c = HibernateHelper.currentSession().createCriteria(RequestToken.class);
		c.add(Restrictions.eq("token", token));
		RequestToken requestToken = (RequestToken) c.uniqueResult();
		Hibernate.initialize(requestToken);
		HibernateHelper.closeSession();
		c = null;
		return requestToken;
	}
	
	public RequestToken pesquisaRequestToken(Session session, String token) {
		Criteria c = session.createCriteria(RequestToken.class,"rt");
		c.setProjection(Projections.projectionList()
		        .add(Projections.property("rt.id"), "id")
		        .add(Projections.property("rt.secret"), "secret")
		        .add(Projections.property("rt.token"), "token")
		        .add(Projections.property("rt.verificationCode"), "verificationCode"))
		     	.setResultTransformer(Transformers.aliasToBean(RequestToken.class));
		c.add(Restrictions.eq("rt.token", token));
		
		return (RequestToken) c.uniqueResult();
	}
		
	public RequestToken pesquisaToken(Session session, String token) {
		
		Query c = session.createQuery("select new RequestToken(secret,usuario.id) from RequestToken"
				+ " where token = :token");
		c.setParameter("token",token);
		RequestToken requestToken = (RequestToken) c.uniqueResult();
		Hibernate.initialize(requestToken);
		return requestToken;
	}
	
	public void alterar(Session session, RequestToken request) throws DAOException  {
		try{
			session =  HibernateHelper.currentSession();
			Transaction tx= session.beginTransaction();
			Query query = session.createQuery("update "+RequestToken.class.getName()+" set verificationCode = :verificationCode, usuarioidpk = :usuarioidpk" +
					" where id = :id");
			query.setParameter("id", request.getId());
			query.setParameter("verificationCode", request.getVerificationCode());
			query.setParameter("usuarioidpk", request.getUsuario().getId());
			query.executeUpdate();
			tx.commit();
			session.close();
		}catch (Exception e) {
			throw new DAOException(e.getMessage());
		}	
	}
	
	public boolean removerToken(String token) {
		RequestToken requestToken = pesquisaToken(token); 
		try {
				delete(requestToken.getId());
			} catch (DAOException e) {
				e.printStackTrace();
			}
			return true;
	}
}