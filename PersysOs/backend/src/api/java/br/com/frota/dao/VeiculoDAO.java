package br.com.frota.dao;

import java.util.List;

import org.hibernate.Query;

import br.com.dao.DAO;
import br.com.frota.model.Veiculo;
import br.com.principal.helper.HibernateHelper;

public class VeiculoDAO extends DAO<Veiculo> {
	
	public VeiculoDAO() {
		super(Veiculo.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Veiculo> lista(String consumerKey) {
		Query c = HibernateHelper.currentSession().createQuery("select new "
				+ Veiculo.class.getName()
				+ " ("+Veiculo.CONSTRUTOR+") from "
				+ Veiculo.class.getName()
				+ " where consumerSecret.consumerKey = :cs and statusModel < 2 ORDER BY id ASC");
		c.setParameter("cs", Long.parseLong(consumerKey));
		return  (List<Veiculo>) c.list();
	}
	
}