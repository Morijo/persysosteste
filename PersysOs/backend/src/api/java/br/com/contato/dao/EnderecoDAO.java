package br.com.contato.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import br.com.contato.model.Endereco;
import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.principal.helper.HibernateHelper;

public class EnderecoDAO extends DAO<Endereco> {
	
	public EnderecoDAO() {
		super(Endereco.class);
	}
	
	public void altera(Endereco endereco) throws DAOException{
		Session session = HibernateHelper.openSession(ContatoDAO.class);
		
		try{
		session.beginTransaction();
		Query query = session.createQuery("update "
				+ endereco.getClass().getName()
				+ " set bairro = :bairro, cep = :cep, "
				+ " cidade = :cidade, estado = :estado, complemento = :complemento, "
				+ " numero = :numero, latitude = :latitude, logradouro = :logradouro, "
				+ " altura = :altura "
				+ " where id = :id");
		query.setParameter("bairro", endereco.getBairro());
		query.setParameter("cep", endereco.getCep());
		query.setParameter("cidade", endereco.getCidade());
		query.setParameter("estado", endereco.getEstado());
		query.setParameter("complemento", endereco.getComplemento());
		query.setParameter("logradouro", endereco.getLogradouro());
		query.setParameter("numero", endereco.getNumero());
		query.setParameter("logradouro", endereco.getLogradouro());
		query.setParameter("latitude", endereco.getLatitude());
		query.setParameter("altura", endereco.getAltura());
		query.setParameter("id", endereco.getId());
		query.executeUpdate();
		session.getTransaction().commit();
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}finally{
			session.close();
		}
	}

}