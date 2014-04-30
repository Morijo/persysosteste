package br.com.evento.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.dao.DAO;
import br.com.eventos.model.TipoEvento;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.HibernateHelper;
    
/**
 * Classe DAO para tratar consultas especificas
 * 
 * @author 
 * 
 */
    public class TipoEventoDAO extends DAO<TipoEvento> {
		
		public TipoEventoDAO() {
			super(TipoEvento.class);
		}
		
		public TipoEvento pesquisaTitulo(String titulo, ConsumerSecret consumerSecret) {
			Session session = HibernateHelper.openSession(DAO.class);
			Criteria criteria = session.createCriteria(TipoEvento.class);
			criteria.add(Restrictions.eq("titulo", titulo));
			criteria.add(Restrictions.eq("consumerSecret", consumerSecret));
        	TipoEvento tipoEvento = (TipoEvento) criteria.uniqueResult();
        	session.close();
        	return tipoEvento;
		}
	}
