package br.com.recurso.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import br.com.dao.DAO;
import br.com.dao.ParameterDAO;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.model.Recurso;

public class RecursoDAO extends DAO<br.com.recurso.model.Recurso> {
	
	public RecursoDAO( Class<?> classe) {
		super(classe);
	}
	
	public ArrayList<Recurso> listaRecurso(String fields, Integer statusModel, Long consumerKey){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Recurso.class.getName()
					+ " ("+fields+") from "
					+ Recurso.class.getName() 
					+ " where statusModel <= :statusModel and consumerSecret.consumerKey = :consumerKey");
			q.setParameter("statusModel", statusModel);
			q.setParameter("consumerKey", consumerKey);
			@SuppressWarnings("unchecked")
			ArrayList<Recurso> recursos = (ArrayList<Recurso>) q.list();
			tx.commit();
			return recursos;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Recurso> listaRecurso(Session session,
			String cs, ArrayList<ParameterDAO> parameters) {

		Criteria criteria = session
				.createCriteria(Recurso.class, "recurso")
				.setProjection(
						Projections
								.projectionList()
								.add(Projections.property("recurso.id"),
										"id")
								.add(Projections
										.property("recurso.nome"),
										"nome")
								.add(Projections.property("recurso.codigo"),
										"codigo")
								.add(Projections.property("recurso.tipoRecurso"),
										"tipoRecurso")) .setResultTransformer(
						Transformers.aliasToBean(Recurso.class));
		
		ParameterDAO.createRestrictions(criteria, parameters);
		return (ArrayList<Recurso>) criteria.list();
	}
}