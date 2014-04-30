package br.com.usuario.dao;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import br.com.usuario.model.AplicacaoAgente;
import br.com.usuario.model.EmailSMTP;
import br.com.usuario.model.GrupoUsuario;
import br.com.usuario.model.GrupoUsuarioPermissao;
import br.com.usuario.model.Usuario;
import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.funcionario.model.Funcionario;
import br.com.oauth.model.AccessToken;
import br.com.principal.helper.HibernateHelper;

/**
 * Classe DAO para tratar consultas especificas
 * 
 * @author 
 * 
 */
public class UsuarioDAO extends DAO<Usuario> {

	public UsuarioDAO() {
		super(Usuario.class);
	}

	public Usuario autenticaUsuario(Session session, String nome, String senha) {
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
				.add(Projections.property("consumerSecret.consumerKey"), "consumerId"));
		criteria.add(Restrictions.eq("nomeUsuario", nome));
		criteria.add(Restrictions.eq("senha", senha));
		criteria.setResultTransformer(Transformers.aliasToBean(Usuario.class));
		Usuario usuario= (Usuario) criteria.uniqueResult();
		return usuario;
	}

	public byte[] carregaImage(Session session, String token) {
		Criteria criteria = session.createCriteria(Usuario.class).createAlias("accessToken", "accessToken");
		criteria.setProjection(Projections.projectionList().add(Projections.property("imagemPerfil")));
		criteria.add(Restrictions.eq("accessToken.token",token));
		byte[] imagem= (byte[]) criteria.uniqueResult();
		return imagem;
	}

	public byte[] carregaImage(Long id) throws DAOException {
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction transaction = session.beginTransaction();
		
		try{
			Criteria criteria = session.createCriteria(Usuario.class);
			criteria.setProjection(Projections.projectionList().add(Projections.property("imagemPerfil")));
			criteria.add(Restrictions.eq("id",id));
			byte[] imagem= (byte[]) criteria.uniqueResult();
			transaction.commit();
			return imagem;
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DAOException(e.getMessage());
		} finally {
			transaction = null;
			session.close();
			session = null;
		}
	}

	public Usuario pesquisaAccessToken(Session session, String token) {
		Criteria criteria = session.createCriteria(Usuario.class,"usuario")
				.createAlias("accessToken", "accessToken");
		projecaoUsuario(criteria);
		criteria.add(Restrictions.eq("accessToken.token",token));
		criteria.setResultTransformer(Transformers.aliasToBean(Usuario.class));
		return (Usuario) criteria.uniqueResult();
	}


	public Usuario pesquisaAccessTokenDashboard(Session session, String token) {
		Criteria criteria = session.createCriteria(Usuario.class,"usuario")
				.createAlias("accessToken", "accessToken");
		projecaoUsuarioDashboard(criteria);
		criteria.add(Restrictions.eq("accessToken.token",token));
		criteria.setResultTransformer(Transformers.aliasToBean(Usuario.class));
		return (Usuario) criteria.uniqueResult();
	}

	public Usuario pesquisaID(Session session, long id) {
		Criteria criteria = session.createCriteria(Usuario.class,"usuario");
		projecaoUsuario(criteria);
		criteria.add(Restrictions.eq("id",id));
		criteria.setResultTransformer(Transformers.aliasToBean(Usuario.class));
		return (Usuario) criteria.uniqueResult();
	}

	public Usuario pesquisaID(long id) {
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();
		try{
			Criteria criteria = session.createCriteria(Usuario.class,"usuario");
			projecaoUsuario(criteria);
			criteria.add(Restrictions.eq("id",id));
			criteria.setResultTransformer(Transformers.aliasToBean(Usuario.class));
			Usuario usuario = (Usuario) criteria.uniqueResult();
			tx.commit();
			return usuario;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	private void projecaoUsuario(Criteria criteria) {
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("usuario.id"), "id")
				.add(Projections.property("usuario.dataAlteracao"), "dataAlteracao")
				.add(Projections.property("usuario.dataCriacao"), "dataCriacao")
				.add(Projections.property("usuario.statusModel"), "statusModel")
				.add(Projections.property("usuario.permitidoAlterar"), "permitidoAlterar")
				.add(Projections.property("usuario.permitidoExcluir"), "permitidoExcluir")
				.add(Projections.property("usuario.codigo"), "codigo")
				.add(Projections.property("usuario.nomeUsuario"), "nomeUsuario")
				.add(Projections.property("usuario.razaoNome"), "razaoNome")
				.add(Projections.property("usuario.fantasiaSobrenome"), "fantasiaSobrenome")
				.add(Projections.property("usuario.cnpjCpf"), "cnpjCpf")
				.add(Projections.property("usuario.ieRg"), "ieRg")
				.add(Projections.property("usuario.emailPrincipal"), "emailPrincipal")
				.add(Projections.property("usuario.nota"), "nota")
				.add(Projections.property("usuario.dashboardNome"), "dashboardNome")
				.add(Projections.property("usuario.timezone"), "timezone")
				.add(Projections.property("usuario.locale"), "locale")
				.add(Projections.property("usuario.consumerSecret"), "consumerSecret"));
	}

	private void projecaoUsuarioDashboard(Criteria criteria) {
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("usuario.id"), "id")
				.add(Projections.property("usuario.nota"), "nota")
				.add(Projections.property("usuario.dashboardNome"), "dashboardNome")
				.add(Projections.property("usuario.timezone"), "timezone")
				.add(Projections.property("usuario.locale"), "locale")
				.add(Projections.property("usuario.consumerSecret"), "consumerSecret"));
	}

	public Usuario pesquisa(Long id, String consumerKey){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Usuario.class.getName()
					+ "(id,razaoNome,emailPrincipal,senha,nomeUsuario) from "
					+ Funcionario.class.getName()
					+ " where consumerSecret.consumerKey = :cs and id = :id");

			q.setParameter("cs", Long.parseLong(consumerKey));
			q.setParameter("id", id);

			Usuario usuario = (Usuario) q.uniqueResult();
			tx.commit();
			return usuario;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public ArrayList<Usuario> pesquisaPorEmail(String email){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ Usuario.class.getName()
					+ "(id,razaoNome,emailPrincipal,senha,nomeUsuario) from "
					+ Funcionario.class.getName()
					+ " where emailPrincipal = :email and statusModel = 1");

			q.setParameter("email", email);
			@SuppressWarnings("unchecked")
			ArrayList<Usuario> usuarios = (ArrayList<Usuario>) q.list();
			tx.commit();
			return usuarios;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public Number verificaUserName(String nomeUsuario){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select count(*) from "
					+ Usuario.class.getName()
					+ " where nomeUsuario  = :nomeUsuario ");
			q.setParameter("nomeUsuario", nomeUsuario);
			Number count = (Number) q.uniqueResult();
			tx.commit();
			return count;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public Usuario pesquisaAccessTokenReturnID(Session session, String token){
		Usuario usuario = pesquisaAccessTokenId(session, token);
		return usuario;
	}

	public Usuario pesquisaAccessTokenReturnID(String token){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();
		try{
			Usuario usuario = pesquisaAccessTokenId(session, token);
			tx.commit();
			return usuario;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	private Usuario pesquisaAccessTokenId(Session session, String token) {
		Query q = session.createQuery("select new "
				+ Usuario.class.getName()
				+ "(id,codigo,razaoNome,nomeUsuario) from "
				+ Usuario.class.getName()
				+ " where accessToken.token = :token");

		q.setParameter("token",token);
		Usuario usuario = (Usuario) q.uniqueResult();
		return usuario;
	}


	@SuppressWarnings("deprecation")
	public Object pesquisaAccessToken(Session session, String token, Class<?> obj){
		Criteria c = session.createCriteria(obj);
		c.createAlias("accessToken", "cs",CriteriaSpecification.LEFT_JOIN); 
		c.add(Restrictions.eq("cs.token",  token));
		return c.uniqueResult();

	}

	@SuppressWarnings("deprecation")
	public Object pesquisaAccessToken(String token, Class<?> obj){
		Session session = HibernateHelper.currentSession();

		Criteria c = session.createCriteria(obj);
		c.createAlias("accessToken", "cs",CriteriaSpecification.LEFT_JOIN); 
		c.add(Restrictions.eq("cs.token",  token));
		return c.uniqueResult();

	}

	public void salvaAccessToken(Session session, Usuario usuario, AccessToken accessToken) throws DAOException{
		try{
			accessToken.salvar();

			Query query = session
					.createQuery("update "
							+ usuario.getClass().getName()
							+ " set accessToken =:accessToken "
							+ " where id = :id");
			query.setParameter("accessToken", accessToken);
			query.setParameter("id", usuario.getId());
			query.getQueryString();
			int id = query.executeUpdate();
			System.out.print(id);
		}catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	public boolean alteraSenha(Usuario usuario)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ usuario.getClass().getName()
							+ " set senha =:senha,"
							+ " dataAlteracao = :dataAlteracao"
							+ " where id = :id");
			query.setParameter("senha", usuario.getSenha());
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", usuario.getId());
			query.getQueryString();
			query.executeUpdate();

			transaction.commit();
			transaction = null;
			return true;
		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}
	}

	public boolean alteraDashboard(Usuario usuario)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ usuario.getClass().getName()
							+ " set dashboardNome =:dashboardNome,"
							+ " timezone = :timezone,"
							+ " nota = :nota,"
							+ " locale =:locale"
							+ " where id = :id");
			query.setParameter("dashboardNome", usuario.getDashboardNome());
			query.setParameter("locale", usuario.getLocale());
			query.setParameter("timezone", usuario.getTimezone());
			query.setParameter("nota", usuario.getNota());
			query.setParameter("id", usuario.getId());
			query.getQueryString();
			query.executeUpdate();

			transaction.commit();
			transaction = null;
			return true;
		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}
	}

	public static ArrayList<GrupoUsuarioPermissao> pesquisaGrupoPermissao(Long idGrupo){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query query = session.createQuery("select new "
					+ GrupoUsuarioPermissao.class.getName()
					+ "(id,permissao.id,permissao.nome,statusModel) from "
					+ GrupoUsuarioPermissao.class.getName()
					+ " where grupoUsuario.id = :id");

			query.setParameter("id", idGrupo);

			@SuppressWarnings("unchecked")
			ArrayList<GrupoUsuarioPermissao> grupo = (ArrayList<GrupoUsuarioPermissao>) query.list();
			tx.commit();
			return grupo;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public static GrupoUsuario pesquisaGrupoUsuario(Long idUsuario){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query q = session.createQuery("select new "
					+ GrupoUsuario.class.getName()
					+ "(grupoUsuario.id,grupoUsuario.codigo,grupoUsuario.nome,grupoUsuario.administrador) from "
					+ Usuario.class.getName()
					+ " where id = :id");

			q.setParameter("id", idUsuario);

			GrupoUsuario grupo = (GrupoUsuario) q.uniqueResult();
			tx.commit();
			return grupo;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public static EmailSMTP pesquisaEmailSMTP(Long consumerKey){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query query = session.createQuery("select new "
					+ EmailSMTP.class.getName()
					+ " ("+EmailSMTP.CONSTRUTOR +") from "
					+ EmailSMTP.class.getName()
					+ " where consumerSecret.consumerKey = :consumerKey");

			query.setParameter("consumerKey", consumerKey);

			EmailSMTP emailSMTP = (EmailSMTP) query.uniqueResult();
			tx.commit();
			return emailSMTP;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public ArrayList<AplicacaoAgente> listaAplicacaoAgente(Long consumerKey){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query query = session.createQuery("select new "
					+ AplicacaoAgente.class.getName()
					+ " ("+AplicacaoAgente.CONSTRUTOR +") from "
					+ AplicacaoAgente.class.getName()
					+ " where consumerSecret.consumerKey = :consumerKey");

			query.setParameter("consumerKey", consumerKey);

			@SuppressWarnings("unchecked")
			ArrayList<AplicacaoAgente> aplicacaoAgentes = (ArrayList<AplicacaoAgente>) query.list();
			tx.commit();
			return aplicacaoAgentes;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public AplicacaoAgente pesquisaAplicacaoAgente(Long id, Long consumerKey){
		Session session = HibernateHelper.openSession(DAO.class);
		Transaction tx = session.beginTransaction();

		try{
			Query query = session.createQuery("select new "
					+ AplicacaoAgente.class.getName()
					+ " ("+AplicacaoAgente.CONSTRUTOR +") from "
					+ AplicacaoAgente.class.getName()
					+ " where id = :id and consumerSecret.consumerKey = :consumerKey");

			query.setParameter("consumerKey", consumerKey);
			query.setParameter("id", id);

			AplicacaoAgente aplicacaoAgente = (AplicacaoAgente) query.uniqueResult();
			tx.commit();
			return aplicacaoAgente;
		} finally {
			tx = null;
			session.close();
			session = null;
		}
	}

	public static boolean alteraPermissao(GrupoUsuarioPermissao grupoUsuarioPermissao)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+  GrupoUsuarioPermissao.class.getName()
							+ " set statusModel =:statusModel "
							+ " where grupoUsuario.id = :grupoUsuarioId and permissao.id = :permissaoId");
			query.setParameter("statusModel", grupoUsuarioPermissao.getStatusModel());
			query.setParameter("grupoUsuarioId", grupoUsuarioPermissao.getGrupoUsuario().getId());
			query.setParameter("permissaoId", grupoUsuarioPermissao.getPermissao().getId());
			query.getQueryString();
			query.executeUpdate();

			transaction.commit();
			transaction = null;
			return true;
		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}
	}

	public static boolean alteraGrupoUsuario(GrupoUsuario grupoUsuario)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+  GrupoUsuario.class.getName()
							+ " set statusModel =:statusModel, nome =:nome, administrador = :administrador"
							+ " where id = :grupoUsuarioId");
			query.setParameter("statusModel", grupoUsuario.getStatusModel());
			query.setParameter("nome", grupoUsuario.getNome());
			query.setParameter("administrador", grupoUsuario.getAdministrado());
			query.setParameter("grupoUsuarioId", grupoUsuario.getId());
			query.getQueryString();
			query.executeUpdate();

			transaction.commit();
			transaction = null;
			return true;
		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}
	}

	public boolean alteraImagem(byte[] imagemPerfil, Long id)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+  Usuario.class.getName()
							+ " set imagemPerfil = :imagemPerfil"
							+ " where id = :id");
			query.setParameter("imagemPerfil", imagemPerfil);
			query.setParameter("id", id);
			query.getQueryString();
			query.executeUpdate();

			transaction.commit();
			transaction = null;
			return true;
		} catch (HibernateException e) {
			transaction.rollback();
			transaction = null;
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}
	}
}
