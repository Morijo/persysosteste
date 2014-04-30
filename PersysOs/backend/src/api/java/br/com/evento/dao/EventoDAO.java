package br.com.evento.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import br.com.dao.DAO;
import br.com.eventos.model.Evento;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.dto.EventoDTO;
import br.com.usuario.model.Usuario;

public class EventoDAO extends DAO<Evento> {


	public EventoDAO() {
		super(Evento.class);
	}

	public Evento pesquisaEventoID(Long id) {
		return (Evento) HibernateHelper.currentSession().load(Evento.class, id);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Evento> listaEvento(Usuario usuario) {
		Session session =HibernateHelper.openSession(DAO.class);

		try{
			Criteria criteria = session.createCriteria(Evento.class);
			criteria.add(Restrictions.eq("usuario", usuario));
			ArrayList<Evento> evento = (ArrayList<Evento>) criteria.list();
			return evento;
		}finally{
			session.close();
			session = null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Evento> listaEvento(Session session, Usuario usuario, Date iniData, Date fimData) {
		Criteria criteria = session.createCriteria(Evento.class);
		criteria.add(Restrictions.eq("usuario", usuario));
		criteria.add(Restrictions.ge("dataInicio", iniData));
		criteria.add(Restrictions.le("dataFim", fimData));
		criteria.addOrder(Order.asc("dataInicio"));
		return criteria.list();
	}

	public List<EventoDTO> listaUltimoEventoPorUsuario(Session session, String consumerSecret ) {

		@SuppressWarnings("unchecked")
		ArrayList<EventoDTO> eventoListaDAO = (ArrayList<EventoDTO>) session.createSQLQuery("select event.mensagem as mensagem, event.latitudeinicio as latitudeInicio,"+
				"event.longitudeinicio as longitudeInicio, event.latitudefim as latitudeFim,event.longitudefim as longitudeFim,"+
				"event.datainicio as dataInicio, event.datafim as dataFim, usuario1_.nomeusuario as nomeUsuario,"+
				" tipoevento2_.titulo as tipoEvento    from evento event " +
				" inner join " +
				" (select  max(ev.datainicio) as datainicio, ev.usuarioidpk as usuarioidpk from evento ev inner join usuario us on ev.usuarioidpk = us.id "+ 
				" group by ev.usuarioidpk) b on event.datainicio = b.datainicio and event.usuarioidpk = b.usuarioidpk"+
				" inner join"+
				"    consumersecret consumerse3_ "+
				"         on event.consumerkeyid=consumerse3_.idc "+
				" inner join"+
				"     tipoevento tipoevento2_ "+
				"         on event.tipoeventoid=tipoevento2_.id "+
				" inner join"+
				"    usuario usuario1_ "+
				"         on event.usuarioidpk=usuario1_.id"+
				" where"+
				"     consumerse3_.consumerkey= :consumerKey"+
				" group by"+
				"     event.usuarioidpk")
				.setResultTransformer( Transformers.aliasToBean(EventoDTO.class)).
				setParameter("consumerKey", consumerSecret).list();
		return eventoListaDAO;
	}

	@SuppressWarnings("unchecked")
	public List<EventoDTO> listaEvento(Session session, Usuario usuario, String iniData, String fimData, Integer tamanho) {
		ArrayList<EventoDTO> eventoListaDAO = (ArrayList<EventoDTO>) session.createSQLQuery("select event.mensagem as mensagem, event.latitudeinicio as latitudeInicio,"+
				"event.longitudeinicio as longitudeInicio, event.latitudefim as latitudeFim,event.longitudefim as longitudeFim,"+
				"event.datainicio as dataInicio, event.datafim as dataFim, usuario1_.nomeusuario as nomeUsuario,"+
				" tipoevento2_.titulo as tipoEvento    from evento event " +
				" inner join"+
				"     tipoevento tipoevento2_ "+
				"         on event.tipoeventoid=tipoevento2_.id "+
				" inner join"+
				"    usuario usuario1_ "+
				"         on event.usuarioidpk=usuario1_.id"+
				" where"+
				"  event.usuarioidpk = :idUsuario and event.datainicio > :dataInicio and event.datainicio < :dataInicioF")
				.setResultTransformer( Transformers.aliasToBean(EventoDTO.class)).
				setParameter("idUsuario", usuario.getId()).
				setParameter("dataInicio", iniData).
				setParameter("dataInicioF", fimData).
				setMaxResults(tamanho).list();

		return eventoListaDAO;
	}



	@SuppressWarnings("unchecked")
	public List<EventoDTO> listaEvento(Usuario usuario,Integer tamanho) {
		Session session =HibernateHelper.openSession(DAO.class);

		try{
			ArrayList<EventoDTO> eventoListaDAO = (ArrayList<EventoDTO>) session.createSQLQuery("select event.mensagem as mensagem, event.latitudeInicio as latitudeInicio,"+
					"event.longitudeinicio as longitudeInicio, event.latitudefim as latitudeFim,event.longitudefim as longitudeFim,"+
					"event.datainicio as dataInicio, event.datafim as dataFim, usuario1_.codigo as nomeUsuario,"+
					" tipoevento2_.titulo as tipoEvento    from evento event " +
					" inner join"+
					"     tipoevento tipoevento2_ "+
					"         on event.tipoeventoid=tipoevento2_.id "+
					" inner join"+
					"    usuario usuario1_ "+
					"         on event.usuarioidpk=usuario1_.id"+
					" where"+
					"  event.usuarioidpk = :idUsuario")
					.setResultTransformer( Transformers.aliasToBean(EventoDTO.class)).
					setParameter("idUsuario", usuario.getId()).
					setMaxResults(tamanho).list();

			return eventoListaDAO;
		}finally{
			session.close();
			session = null;
		}

	}
	
	public ArrayList<Evento> listaEventos(Long id){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Evento.class.getName()
					+ " ("+Evento.CONTRUTROR +") from "
					+ Evento.class.getName()
					+ " where  usuario.id = :id");

			q.setParameter("id", id);

			@SuppressWarnings("unchecked")
			ArrayList<Evento> eventos = (ArrayList<Evento>) q.list();
			tx.commit();
			return eventos;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}
}