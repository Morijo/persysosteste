package br.com.oauth.dao;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import br.com.dao.DAO;
import br.com.oauth.model.ConsumerSecret;

public class ConsumerSecretDAO extends DAO<ConsumerSecret> {

	public ConsumerSecretDAO() {
		super(ConsumerSecret.class);
	}
	
	public ConsumerSecret pesquisaConsumerSecret(Session session, Long consumerKey) {
		Criteria c = session.createCriteria(ConsumerSecret.class,"cs");
		c.setProjection(Projections.projectionList()
		        .add(Projections.property("cs.consumerSecret"), "consumerSecret")
		        .add(Projections.property("cs.consumerKey"), "consumerKey"))
		     	.setResultTransformer(Transformers.aliasToBean(ConsumerSecret.class));
		c.add(Restrictions.eq("cs.consumerKey", consumerKey));
		
		return (ConsumerSecret) c.uniqueResult();
	}

	public ConsumerSecret pesquisaConsumerKey(Session session, Long key) {
		
		Criteria c = session.createCriteria(ConsumerSecret.class);
		c.add(Restrictions.eq("consumerKey",  key ));
		ConsumerSecret consumer = (ConsumerSecret)c.uniqueResult();
		Hibernate.initialize(consumer);
		c = null;
		return consumer;
	}
	
}
