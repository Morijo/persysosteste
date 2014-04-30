package br.com.recurso.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.cliente.model.Cliente;
import br.com.dao.DAO;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.model.Chip;
	
public class ChipDAO extends DAO<Cliente> {
		
	public ChipDAO(Class<?> classe) {
		super( classe);
	}
	public ArrayList<Chip> listaChip(String fields, Integer statusModel){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Chip.class.getName()
					+ " ("+fields+") from "
					+ Chip.class.getName() 
					+ " where statusModel <= :statusModel");
			q.setParameter("statusModel", statusModel);
			@SuppressWarnings("unchecked")
			ArrayList<Chip> chip = (ArrayList<Chip>) q.list();
			tx.commit();
			return chip;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

}