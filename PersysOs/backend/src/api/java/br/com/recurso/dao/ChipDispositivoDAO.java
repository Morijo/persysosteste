package br.com.recurso.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.cliente.model.Cliente;
import br.com.dao.DAO;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.model.ChipDispositivo;

public class ChipDispositivoDAO extends DAO<Cliente> {
		
	public ChipDispositivoDAO(Class<?> classe) {
		super( classe);
	}

	public ArrayList<ChipDispositivo> listaChipDispositivo(String fields, Integer statusModel){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ ChipDispositivo.class.getName()
					+ " ("+fields+") from "
					+ ChipDispositivo.class.getName() 
					+ " where statusModel <= :statusModel");
			q.setParameter("statusModel", statusModel);
			@SuppressWarnings("unchecked")
			ArrayList<ChipDispositivo> chipDispositivo = (ArrayList<ChipDispositivo>) q.list();
			tx.commit();
			return chipDispositivo;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

}
