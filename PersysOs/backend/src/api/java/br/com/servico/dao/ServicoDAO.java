package br.com.servico.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.dao.DAO;
import br.com.principal.helper.HibernateHelper;
import br.com.servico.model.Servico;
	
	public class ServicoDAO extends DAO<Servico> {
		
		public ServicoDAO(Class<?> classe) {
			super( classe);
		}
	
		@SuppressWarnings("unchecked")
		public List<Servico> pesquisaServicosAtivo(){
			Criteria c = HibernateHelper.currentSession().createCriteria(Servico.class);
			c.add(Restrictions.eq("status", "Ativo"));
			return c.list();
		}
	
	}