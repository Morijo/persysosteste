package br.com.produto.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import br.com.dao.DAO;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;

public class ProdutoDAO extends DAO<Produto> {

	public ProdutoDAO(Class<?> classe) {
		super( classe);
	}

	public Produto pesquisaProdutoID(ConsumerSecret consumerKey, Long id) {
		Criteria criterio = HibernateHelper.currentSession().createCriteria(Produto.class);
		criterio.add(Restrictions.eq("id",id));
		criterio.add(Restrictions.eq("consumerSecret",consumerKey));
		return (Produto) criterio.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Produto> listaProduto(Long consumerKey, Integer statusModel) {
		Query queryProduto = HibernateHelper.currentSession().createQuery("select new "
				+ Produto.class.getName()
				+ " ("+Produto.CONSTRUTOR+") from "
				+ Produto.class.getName()
				+ " where consumerSecret.consumerKey = :cs and statusModel <= :statusModel ORDER BY id ASC");
		queryProduto.setParameter("cs", consumerKey);
		queryProduto.setParameter("statusModel", statusModel);
		
		return  (List<Produto>) queryProduto.list();
	}
	
}