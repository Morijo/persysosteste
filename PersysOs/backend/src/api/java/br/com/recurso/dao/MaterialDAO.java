package br.com.recurso.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.dao.DAO;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.model.Material;

public class MaterialDAO extends DAO<br.com.recurso.model.Material> {
	
	public MaterialDAO( Class<?> classe) {
		super(classe);
		}
	
	public ArrayList<Material> listaMaterial(String fields, Integer statusModel){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Material.class.getName()
					+ " ("+fields+") from "
					+ Material.class.getName() 
					+ " where statusModel <= :statusModel");
			q.setParameter("statusModel", statusModel);
			@SuppressWarnings("unchecked")
			ArrayList<Material> material = (ArrayList<Material>) q.list();
			tx.commit();
			return material;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

}

