package br.com.principal.helper;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;

import br.com.principal.model.Aplicacao;
import br.com.produto.model.Produto;

/**
 * Classe HibernateHelper determina a configuracao com o banco de dados e auxilia no gerenciamento das sessoes.
 * @author Ricardo
 */

public class HibernateHelper{

	private final static SessionFactory sessionFactory;
	private static ServiceRegistry      serviceRegistry;
	private static Session			    session;

	static {
		try {

			Aplicacao dados = Aplicacao.getInstance();

			Configuration configuration = new Configuration();

			System.setProperty("hibernate.connection.driver_class", dados.getHibernateDriver());
			System.setProperty("hibernate.dialect", dados.getHibernateDialect());
			System.setProperty("hibernate.dialect","org.hibernate.spatial.dialect.postgis.PostgisDialect"); 
			System.setProperty("hibernate.connection.password",dados.getSenhaBanco());
			System.setProperty("hibernate.connection.username",dados.getUsuarioBanco());
			System.setProperty("hibernate.connection.url",dados.getUrl());
			System.setProperty("hibernate.show_sql","true");
			System.setProperty("hibernate.use_sql_comments","true");
			System.setProperty("hibernate.format_sql","true");
			System.setProperty("hibernate.hbm2ddl.auto","update");
			System.setProperty("hibernate.jdbc.batch_size","20");
			System.setProperty("hibernate.cache.use_second_level_cache","false");
			System.setProperty("hibernate.cache.region.factory_class","org.hibernate.cache.ehcache.EhCacheRegionFactory");
			System.setProperty("hibernate.cache.provider_class","org.hibernate.cache.EhCacheProvider");
			System.setProperty("hibernate.connection.pool_size","0"); 
			System.setProperty("hibernate.connection.release_mode","after_transaction");

			//Controle de session
			System.setProperty("hibernate.c3p0.acquire_increment","1");
			System.setProperty("hibernate.c3p0.max_statements","0");
			System.setProperty("hibernate.c3p0.max_size","1000");
			System.setProperty("hibernate.c3p0.min_size","1");
			System.setProperty("hibernate.c3p0.timeout","30");
			System.setProperty("hibernate.c3p0.idle_test_period","30");
			System.setProperty("hibernate.c3p0.preferredTestQuery","select 1");
			System.setProperty("hibernate.c3p0.unreturnedConnectionTimeout","30");
			System.setProperty("hibernate.c3p0.debugUnreturnedConnectionStackTraces","true");
			System.setProperty("hibernate.connection.provider_class","org.hibernate.connection.C3P0ConnectionProvider");

			configuration.setProperties(System.getProperties());

			configuration.configure();

			serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			configuration.buildSettings(serviceRegistry);
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

	}

	public static Session currentSession() throws HibernateException {  

		if(session == null){
			session = sessionFactory.openSession();
		}
		else if(!session.isOpen())
			session = sessionFactory.openSession();

		return session;  
	}

	public static Session openSession(Class<?> classe) {
		return sessionFactory.openSession();
	}

	public static void closeSession() { 

		if (session != null) {  
			session.flush();  
			session.close(); 
			session = null;
		} 
	}

	public static void closeSession(Session session) { 
		session.close();  
		session = null;
	}  

	public static void statistics(){
		Statistics stats = HibernateHelper.sessionFactory.getStatistics();

		double queryCacheHitCount  = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio =
				queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);

		System.out.print("Query Hit ratio:" + queryCacheHitRatio);

		EntityStatistics entityStats =
				stats.getEntityStatistics( Produto.class.getName() );
		long changes =
				entityStats.getInsertCount()
				+ entityStats.getUpdateCount()
				+ entityStats.getDeleteCount();
		System.out.print(Produto.class.getName() + " changed " + changes + "times"  );
	}

}