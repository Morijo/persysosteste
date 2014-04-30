package br.com.despesas.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import br.com.dao.DAO;
import br.com.despesas.model.Despesas;
import br.com.principal.helper.HibernateHelper;

	public class DespesasDAO extends DAO<Despesas> {
		
		public DespesasDAO() {
			super(Despesas.class);
		}
		
		public Despesas pesquisaDespesasById(Long id) {
			return (Despesas) HibernateHelper.currentSession().load(Despesas.class, id);
		}
		
		public Despesas pesquisaDespesasByNome(String nome) {
			Criteria c = HibernateHelper.currentSession().createCriteria(Despesas.class);
			c.add(Restrictions.ilike("nome", "%" + nome + "%"));

			return (Despesas)c.uniqueResult();
		}
		
		@SuppressWarnings("unchecked")
		public List<Despesas> pesquisaDespesass(Long cpf){
			Criteria c = HibernateHelper.currentSession().createCriteria(Despesas.class);
			c.add( Restrictions.eq("usuario.cpf", cpf ));
			return c.list();
		}
		
	
		/**
		 * Utilizando HQL 
		 * @param id
		 * @return
		 */
		public Despesas buscaDespesas(Long id){
			Query q = HibernateHelper.currentSession().createQuery("select p from " + Despesas.class.getName() + " as p where p.id like :id");
			
			q.setParameter("id", id);
			
			return (Despesas)q.uniqueResult();
		}
			
	}
