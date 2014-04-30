package br.com.ordem.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import br.com.dao.DAO;
import br.com.exception.DAOException;
import br.com.ordem.model.AgendaOrdemFuncionario;
import br.com.ordem.model.Ordem;
import br.com.principal.helper.HibernateHelper;
import br.com.rest.hateoas.dto.OrdemDTO;

/**
 * @author ricardosabatine
 *
 */
public class OrdemDAO extends DAO<Ordem> {

	public OrdemDAO(Class<?> classe) {
		super(classe);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<OrdemDTO> listaOrdemAgendamento(Session session,
			Integer inicial, Integer tamanho, String consumerKey, Date dataInicio,
			Date dataFim) {
		Criteria criteria = projetaDado(session);
		criteria.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerKey)));
		criteria.add(Restrictions.ge("ordem.dataAgendamentoInicio", dataInicio));
		criteria.add(Restrictions.le("ordem.dataAgendamentoInicio", dataFim));
		criteria.addOrder(Order.desc("id"));
		return (ArrayList<OrdemDTO>) criteria.setFirstResult(inicial)
				.setMaxResults(tamanho).list();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<OrdemDTO> listaOrdemPorFuncionario(Session session,
			Integer inicial, Integer tamanho, String consumerKey, Long idFuncionario,
			Date dataInicio, Date dataFim) {

		Criteria criteria = projetaDado(session);
		criteria.createAlias("agendaOrdemFuncionario", "agendaOrdemFuncionario");

		criteria.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerKey)));
		criteria.add(Restrictions.eq("agendaOrdemFuncionario.funcionario.id",
				idFuncionario));
		criteria.add(Restrictions.ge("ordem.dataAgendamentoInicio", dataInicio));
		criteria.add(Restrictions.le("ordem.dataAgendamentoInicio", dataFim));
		criteria.addOrder(Order.desc("id"));

		return (ArrayList<OrdemDTO>) criteria.setFirstResult(inicial)
				.setMaxResults(tamanho).list();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<AgendaOrdemFuncionario> listaOrdemAgendamentoPorFuncionario(
			Integer inicial, Integer tamanho, String consumerKey, Long idFuncionario,
			Date dataInicio, Date dataFim) throws DAOException {

		Session session = null;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);


			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(dataFim);
			calendar2.set(Calendar.HOUR_OF_DAY, 23);
			calendar2.set(Calendar.MINUTE, 59);
			calendar2.set(Calendar.SECOND, 59);
			calendar2.set(Calendar.MILLISECOND, 59);			
			session = HibernateHelper.openSession(DAO.class);

			Query c = session.createQuery("select new "
					+ AgendaOrdemFuncionario.class.getName()
					+ " ("+AgendaOrdemFuncionario.CONSTRUTOR+") from "
					+ AgendaOrdemFuncionario.class.getName()
					+ " where funcionario.id = :idFuncionario and ordem.dataAgendamentoInicio <= :dataFim and ordem.dataAgendamentoInicio >= :dataInicio"
					+ " ORDER BY ordem.dataAgendamento ASC");
			c.setParameter("dataInicio", calendar.getTime());
			c.setParameter("dataFim",calendar2.getTime());
			c.setParameter("idFuncionario",idFuncionario);

			return (ArrayList<AgendaOrdemFuncionario>) c.list();
		} catch (HibernateException e) {
			throw new DAOException("");
		} finally {
			session.close();
			session = null;
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<AgendaOrdemFuncionario> listaOrdemAgendamentoGeral(
			Integer inicial, Integer tamanho, String consumerKey,
			Date dataInicio, Date dataFim) throws DAOException {

		Session session = null;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataInicio);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);


			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(dataFim);
			calendar2.set(Calendar.HOUR_OF_DAY, 23);
			calendar2.set(Calendar.MINUTE, 59);
			calendar2.set(Calendar.SECOND, 59);
			calendar2.set(Calendar.MILLISECOND, 59);

			session = HibernateHelper.openSession(DAO.class);

			Query c = session.createQuery("select new "
					+ AgendaOrdemFuncionario.class.getName()
					+ " ("+AgendaOrdemFuncionario.CONSTRUTOR+") from "
					+ AgendaOrdemFuncionario.class.getName()
					+ " where ordem.dataAgendamentoInicio <= :dataFim and  ordem.dataAgendamentoInicio >= :dataInicio"
					+ " ORDER BY ordem.dataAgendamentoInicio ASC");
			c.setParameter("dataInicio", calendar.getTime());
			c.setParameter("dataFim",calendar2.getTime());

			return (ArrayList<AgendaOrdemFuncionario>) c.list();
		} catch (HibernateException e) {
			throw new DAOException("");
		} finally {
			session.close();
			session = null;
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<OrdemDTO> listaOrdem(Session session, Integer inicial,
			Integer tamanho, String consumerKey, Boolean agendada) {
		Criteria criteria = projetaDado(session);
		criteria.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerKey)));
		criteria.addOrder(Order.desc("id"));

		if(agendada != null)
			criteria.add(Restrictions.eq("agendada", agendada));

		return (ArrayList<OrdemDTO>) criteria.setFirstResult(inicial)
				.setMaxResults(tamanho).list();
	}

	public OrdemDTO pesquisaOrdem(Session session, String consumerKey, Long idOrdem) {
		Criteria criteria = projetaDado(session);
		criteria.add(Restrictions.eq("consumerSecret.consumerKey", Long.parseLong(consumerKey)));
		criteria.add(Restrictions.eq("ordem.id", idOrdem));
		criteria.addOrder(Order.desc("id"));
		return (OrdemDTO) criteria.uniqueResult();
	}


	private Criteria projetaDado(Session session) {
		Criteria criteria = session
				.createCriteria(Ordem.class, "ordem")
				.createAlias("consumerSecret", "consumerSecret")
				.createAlias("clienteObjeto", "clienteObjeto")
				.createAlias("clienteObjeto.cliente", "cliente")
				.createAlias("clienteObjeto.unidadeGestora", "unidade")
				.createAlias("contato", "contato")
				.createAlias("contato.endereco", "endereco")
				.createAlias("ordem.situacaoOrdem", "situacaoOrdem")
				.createAlias("ordem.prioridade", "prioridade")
				.createAlias("ordem.departamento", "departamento")
				.createAlias("departamento.unidade", "unidadeDep")

				.setProjection(
						Projections
						.projectionList()
						.add(Projections.property("ordem.id"), "id")
						.add(Projections.property("ordem.codigo"),
								"codigo")
								.add(Projections
										.property("ordem.dataConclusao"),
										"dataConclusao")
										.add(Projections.property("ordem.dataCriacao"),
												"dataCriacao")
												.add(Projections
														.property("ordem.dataAgendamento"),
														"dataAgendamento")
														.add(Projections
																.property("ordem.agendada"),
																"agendada")
																.add(Projections
																		.property("ordem.dataAgendamentoFim"),
																		"dataAgendamentoFim")
																		.add(Projections
																				.property("ordem.dataAgendamentoInicio"),
																				"dataAgendamentoInicio")
																				.add(Projections.property("ordem.assunto"),
																						"assunto")
																						.add(Projections.property("ordem.informacaoAdicional"),
																								"informacaoAdicional")
																								.add(Projections.property("ordem.fonte"),
																										"fonte")
																										.add(Projections.property("clienteObjeto.id"),
																												"clienteObjetoId")
																												.add(Projections.property("clienteObjeto.codigo"),
																														"clienteObjetoCodigo")
																														.add(Projections.property("clienteObjeto.dataAssinatura"),
																																"clienteObjetoDataAssinatura")		
																																.add(Projections.property("cliente.razaoNome"),
																																		"razaoNome")
																																		.add(Projections.property("cliente.id"),
																																				"clienteId")
																																				.add(Projections.property("cliente.codigo"),
																																						"clienteCodigo")
																																						.add(Projections.property("cliente.cnpjCpf"),
																																								"clienteCnpjCpf")
																																								.add(Projections.property("contato.nome"),
																																										"contatoNome")
																																										.add(Projections.property("contato.id"),
																																												"contatoId")
																																												.add(Projections.property("endereco.id"),
																																														"enderecoId")
																																														.add(Projections.property("endereco.bairro"),
																																																"enderecoBairro")
																																																.add(Projections.property("endereco.cep"),
																																																		"enderecoCep")
																																																		.add(Projections.property("endereco.cidade"),
																																																				"enderecoCidade")
																																																				.add(Projections
																																																						.property("endereco.complemento"),
																																																						"enderecoComplemento")
																																																						.add(Projections.property("endereco.estado"),
																																																								"enderecoEstado")
																																																								.add(Projections.property("endereco.latitude"),
																																																										"enderecoLatitude")
																																																										.add(Projections.property("endereco.longitude"),
																																																												"enderecoLongitude")
																																																												.add(Projections
																																																														.property("endereco.logradouro"),
																																																														"enderecoLogradouro")
																																																														.add(Projections.property("endereco.numero"),
																																																																"enderecoNumero")
																																																																.add(Projections.property("unidade.id"),
																																																																		"unidadeId")
																																																																		.add(Projections.property("unidade.codigo"),
																																																																				"unidadeCodigo")
																																																																				.add(Projections
																																																																						.property("unidade.nomeUnidade"),
																																																																						"unidadeNome")
																																																																						.add(Projections
																																																																								.property("situacaoOrdem.id"),
																																																																								"situacaoOrdemId")
																																																																								.add(Projections
																																																																										.property("situacaoOrdem.nome"),
																																																																										"situacaoOrdemNome")
																																																																										.add(Projections
																																																																												.property("prioridade.id"),
																																																																												"prioridadeId")
																																																																												.add(Projections
																																																																														.property("prioridade.cor"),
																																																																														"prioridadeCor")
																																																																														.add(Projections
																																																																																.property("departamento.id"),
																																																																																"departamentoId")
																																																																																.add(Projections
																																																																																		.property("departamento.id"),
																																																																																		"departamentoId")
																																																																																		.add(Projections
																																																																																				.property("departamento.codigo"),
																																																																																				"departamentoCodigo")
																																																																																				.add(Projections
																																																																																						.property("departamento.nomeDepartamento"),
																																																																																						"departamentoNome")	
																																																																																						.add(Projections.property("unidadeDep.id"),
																																																																																								"unidadeDepId")
																																																																																								.add(Projections.property("unidadeDep.codigo"),
																																																																																										"unidadeDepCodigo")
																																																																																										.add(Projections.property("unidadeDep.nomeUnidade"),
																																																																																												"unidadeDepNome")
																																																																																												.add(Projections
																																																																																														.property("prioridade.prioridade"),
																																																																																														"prioridadeTitulo"))
																																																																																														.setResultTransformer(Transformers.aliasToBean(OrdemDTO.class));
		return criteria;
	}

	public Ordem pesquisaOrdemId(Long id) {
		return (Ordem) HibernateHelper.currentSession().load(Ordem.class, id);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Ordem> listaOrdem(Integer statusModel, String consumerKey) throws DAOException
	{
		Session session = null;
		try {
			session = HibernateHelper.openSession(DAO.class);

			Query c = session.createQuery("select new "
					+ Ordem.class.getName()
					+ " ("+Ordem.CONSTRUTORFULL+") from "
					+ Ordem.class.getName()
					+ " where statusModel <= :statusModel and consumerSecret.consumerKey = :consumerKey"
					+ " ORDER BY id DESC");
			c.setParameter("statusModel", statusModel);
			c.setParameter("consumerKey", Long.parseLong(consumerKey));

			return (ArrayList<Ordem>) c.list();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}	
	}

	public Ordem pesquisaOrdem(Long ordemId, Integer statusModel, String consumerKey) throws DAOException
	{
		Session session = null;
		try {
			session = HibernateHelper.openSession(DAO.class);

			Query c = session.createQuery("select new "
					+ Ordem.class.getName()
					+ " ("+Ordem.CONSTRUTORFULL+") from "
					+ Ordem.class.getName()
					+ " where id = :id and statusModel <= :statusModel and "
					+ "consumerSecret.consumerKey = :consumerKey");

			c.setParameter("id", ordemId);
			c.setParameter("statusModel", statusModel);
			c.setParameter("consumerKey", Long.parseLong(consumerKey));

			return (Ordem) c.uniqueResult();
		} catch (HibernateException e) {
			throw new DAOException(e.getMessage());
		} finally {
			session.close();
			session = null;
		}	
	}

	public boolean alteraDataAgendamento(Ordem ordemServico)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ ordemServico.getClass().getName()
							+ " set dataAgendamento = :dataAgendamento, situacaoOrdem = :situacaoOrdem, reagendamento = :reagendamento, "
							+ "  dataAgendamentoFim = :dataAgendamentoFim, dataAgendamentoInicio = :dataAgendamentoInicio, agendada = :agendada,"
							+ "  dataAlteracao = :dataAlteracao where id = :id");
			query.setParameter("dataAgendamento", ordemServico.getDataAgendamento());
			query.setParameter("dataAgendamentoInicio", ordemServico.getDataAgendamentoInicio());
			query.setParameter("dataAgendamentoFim", ordemServico.getDataAgendamentoFim());
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("situacaoOrdem", ordemServico.getSituacaoOrdem());
			query.setParameter("reagendamento", ordemServico.getReagendamento());
			query.setParameter("agendada", ordemServico.getAgendada());
			query.setParameter("id", ordemServico.getId());
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

	public boolean alteraOrdemCustomizado(Ordem ordemServico)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ ordemServico.getClass().getName()
							+ " set statusModel = :statusModel,"
							+ " assunto = :assunto, dataConclusao =:dataConclusao,"
							+ " fonte = :fonte, departamento = :departamento,"
							+ " prioridade = :prioridade, contato = :contato,"
							+ " clienteObjeto = :clienteObjeto, situacaoOrdem = :situacaoOrdem, "
							+ " dataAlteracao = :dataAlteracao"
							+ " where id = :id");
			query.setParameter("statusModel", ordemServico.getStatusModel());
			query.setParameter("assunto", ordemServico.getAssunto());
			query.setParameter("dataConclusao", ordemServico.getDataConclusao());
			query.setParameter("fonte", ordemServico.getFonte());
			query.setParameter("departamento", ordemServico.getDepartamento());
			query.setParameter("prioridade", ordemServico.getPrioridade());
			query.setParameter("contato", ordemServico.getContato());
			query.setParameter("clienteObjeto", ordemServico.getClienteObjeto());
			query.setParameter("situacaoOrdem", ordemServico.getSituacaoOrdem());
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", ordemServico.getId());
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

	public static boolean alteraDataAlteracao(Ordem ordemServico)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ ordemServico.getClass().getName()
							+ " set dataAlteracao = :dataAlteracao"
							+ " where id = :id");
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", ordemServico.getId());
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

	public static boolean alteraContrato(Ordem ordemServico)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ ordemServico.getClass().getName()
							+ " set dataAlteracao = :dataAlteracao, clienteObjeto = :clienteObjeto"
							+ " where id = :id");
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("clienteObjeto", ordemServico.getClienteObjeto());
			query.setParameter("id", ordemServico.getId());
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

	public static boolean alteraContato(Ordem ordemServico)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ ordemServico.getClass().getName()
							+ " set dataAlteracao = :dataAlteracao, contato = :contato"
							+ " where id = :id");
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("contato", ordemServico.getContato());
			query.setParameter("id", ordemServico.getId());
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

	public static boolean alteraFuncionarioExecucao(Ordem ordemServico)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ ordemServico.getClass().getName()
							+ " set dataAlteracao = :dataAlteracao, atribuida = :atribuida"
							+ " where id = :id");
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", ordemServico.getId());
			query.setParameter("atribuida", ordemServico.getAtribuida());
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

	public boolean alteraOrdemSituacao(Ordem ordemServico)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("update "
							+ ordemServico.getClass().getName()
							+ " set dataConclusao =:dataConclusao, situacaoOrdem = :situacaoOrdem,"
							+ " dataAlteracao = :dataAlteracao"
							+ " where id = :id");
			query.setParameter("dataConclusao", ordemServico.getDataConclusao());
			query.setParameter("situacaoOrdem", ordemServico.getSituacaoOrdem());
			query.setParameter("dataAlteracao", new Date());
			query.setParameter("id", ordemServico.getId());
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

	public static ArrayList<Object> contaOrdensPorSituacao(Date dataAtual)
			throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateHelper.openSession(DAO.class);
			transaction = session.beginTransaction();

			Query query = session
					.createSQLQuery("select s.nome, count(s.id) from ordem as o join situacaoordem s "+ 
							"on o.situacaoordem_id = s.id where o.status = 1 and o.dataagendamento > :data group by s.id");

			query.setParameter("data", dataAtual);
			@SuppressWarnings("unchecked")
			ArrayList<Object> lista = (ArrayList<Object>) query.list();

			transaction.commit();
			transaction = null;
			return lista;
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
