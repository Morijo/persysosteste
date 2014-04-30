package br.com.recurso.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.dao.DAO;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.model.Dispositivo;

public class DispositivoDAO extends DAO<br.com.recurso.model.Dispositivo> {
	
	public DispositivoDAO( Class<?> classe) {
		super(classe);
	}
	public ArrayList<Dispositivo> listaDispositivo(String fields, Integer statusModel){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Dispositivo.class.getName()
					+ " ("+fields+") from "
					+ Dispositivo.class.getName() 
					+ " where statusModel <= :statusModel");
			q.setParameter("statusModel", statusModel);
			@SuppressWarnings("unchecked")
			ArrayList<Dispositivo> dispositivo = (ArrayList<Dispositivo>) q.list();
			tx.commit();
			return dispositivo;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

}