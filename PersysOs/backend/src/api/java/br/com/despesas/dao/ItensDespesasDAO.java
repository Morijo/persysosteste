package br.com.despesas.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.com.dao.DAO;
import br.com.despesas.model.ItensDespesas;
import br.com.principal.helper.HibernateHelper;
public class ItensDespesasDAO extends DAO<ItensDespesas> {
	
	public ItensDespesasDAO() {
		super(ItensDespesas.class);
	}
	
	public ItensDespesas pesquisaItensDespesasById(Long id) {
		System.out.print("pesquisaItensDespesasById : " + id);
		return (ItensDespesas) HibernateHelper.currentSession().load(ItensDespesas.class, id);
	}
	
	public ItensDespesas pesquisaItensDespesasByNome(String nome) {
		Criteria c = HibernateHelper.currentSession().createCriteria(ItensDespesas.class);
		c.add(Restrictions.ilike("nome", "%" + nome + "%"));

		return (ItensDespesas)c.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<ItensDespesas> pesquisaItensDespesass(String nome){
		Criteria c = HibernateHelper.currentSession().createCriteria(ItensDespesas.class);
		c.add(Restrictions.ilike("nome", "%" + nome + "%"));
		c.addOrder(Order.asc("nome"));
		
		return c.list();
	}
	
	/**
	 * Utilizando HQL 
	 * @param id
	 * @return
	 */
	public ItensDespesas buscaItensDespesas(Long id){
		Query q = HibernateHelper.currentSession().createQuery("select p from " + ItensDespesas.class.getName() + " as p where p.id like :id");
		
		q.setParameter("id", id);
		
		return (ItensDespesas)q.uniqueResult();
	}
}