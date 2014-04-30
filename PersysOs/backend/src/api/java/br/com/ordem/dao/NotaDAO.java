package br.com.ordem.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.dao.DAO;
import br.com.ordem.model.Nota;
import br.com.principal.helper.HibernateHelper;

public class NotaDAO extends DAO<Nota> {

	public NotaDAO() {
		super(Nota.class);
	}

	/**
	 * Consulta tdas as notas ativos de uma ordem de servi�o
	 * 
	 * 
	 * @param session  //sess�o com o banco de dados
	 * @param consumerSecret //chave da empresa
	 * @return List<ContratoDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<Nota> listaNotas(Long idOrdem){
		Session session = HibernateHelper.openSession(getClass());
		try{
			Transaction tx = session.beginTransaction();
			org.hibernate.Query query = session.createQuery("select new "
					+ Nota.class.getName()
					+ "("+Nota.CONSTRUTOR+ ") from "
					+ Nota.class.getName()
					+ " where ordem.id = :idOrdem ORDER BY id ASC");

			query.setParameter("idOrdem", idOrdem);
			List<Nota> lista = query.list();
			tx.commit();
			return lista;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return new ArrayList<Nota>();
		}finally{
			session.close();
			session = null;
		}
	}
}
