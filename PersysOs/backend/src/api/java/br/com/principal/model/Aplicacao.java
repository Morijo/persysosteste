package br.com.principal.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.exception.DAOException;
import br.com.principal.helper.XMLHelper;

/**
 * Classe Aplicacao: principais informa��es referente a aplicacao
 * @author 
 * 
 */

@XmlRootElement(name = "aplicacao")
@XmlAccessorType(XmlAccessType.FIELD)
public class Aplicacao {

	@XmlElement(name = "id")
	private Long    id;
	@XmlElement(name = "data_criacao")
	private Date    dataCriacao;
	@XmlElement(name = "ativo")
	private Boolean ativo = false;
	@XmlElement(name = "app")
	private String  nomeAplicacao = "ospersys";
	@XmlElement(name = "versao")
	private String  versaoAplicacao = "1.0";
	@XmlElement(name = "sgbd")
	private String sgbdNome ="";
	@XmlElement(name = "url_banco")
	private String url;
	@XmlElement(name = "base_dado_banco")
	private String baseDados;
	@XmlElement(name = "usuario_banco")
	private String usuarioBanco;
	@XmlElement(name = "senha_banco")
	private String senhaBanco;
	@XmlElement(name = "sucesso_banco")
	private Boolean sucessoBanco;
	@XmlElement(name = "hibernate_driver_class")
	private String hibernateDriver;
	@XmlElement(name = "hibernate.dialect")
	private String hibernateDialect;
	@XmlElement(name = "endpoint")
	private String endPoint= "http://localhost:8080/OsManagerWeb/api";
	
	public static Aplicacao APLICACAO = null;

	@XmlTransient 
	public static String FILE_HOME="/opt/persys/app.xml";
	
	private Aplicacao(){}

	public static Aplicacao getInstance(){
		if(APLICACAO == null){
			return getAplicacao();
		}
		return new Aplicacao();
	}

	public static void setInstance(Aplicacao aplicacao){
		APLICACAO = aplicacao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getNomeAplicacao() {
		return nomeAplicacao;
	}

	public void setNomeAplicacao(String nomeAplicacao) {
		this.nomeAplicacao = nomeAplicacao;
	}

	public String getVersaoAplicacao() {
		return versaoAplicacao;
	}

	public void setVersaoAplicacao(String versaoAplicacao) {
		this.versaoAplicacao = versaoAplicacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsuarioBanco() {
		return usuarioBanco;
	}

	public void setUsuarioBanco(String usuarioBanco) {
		this.usuarioBanco = usuarioBanco;
	}

	public String getSenhaBanco() {
		return senhaBanco;
	}

	public void setSenhaBanco(String senhaBanco) {
		this.senhaBanco = senhaBanco;
	}

	public Boolean getSucessoBanco() {
		return sucessoBanco;
	}

	public void setSucessoBanco(Boolean sucessoBanco) {
		this.sucessoBanco = sucessoBanco;
	}

	public String getSgbdNome() {
		return sgbdNome;
	}

	public void setSgbdNome(String sgbdNome) {
		this.sgbdNome = sgbdNome;
	}

	public String getBaseDados() {
		return baseDados;
	}

	public void setBaseDados(String baseDados) {
		this.baseDados = baseDados;
	}

	public String getHibernateDriver() {
		return hibernateDriver;
	}

	public void setHibernateDriver(String hibernateDriver) {
		this.hibernateDriver = hibernateDriver;
	}

	public String getHibernateDialect() {
		return hibernateDialect;
	}

	public void setHibernateDialect(String hibernateDialect) {
		this.hibernateDialect = hibernateDialect;
	}
	
	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	@Override
	public String toString() {
		return "Aplicacao [id=" + id + ", dataCriacao=" + dataCriacao
				+ ", ativo=" + ativo + ", nomeAplicacao=" + nomeAplicacao
				+ ", versaoAplicacao=" + versaoAplicacao + "]";
	}

	public static boolean isAtivo(){

		Aplicacao app;
		try {
			app = (Aplicacao) XMLHelper.carregaArquivo(Aplicacao.class,FILE_HOME);
		} catch (JAXBException e) {
			app = criaArquivoPadrao(); 
		}
		if(app == null){
			app = criaArquivoPadrao(); 
		}

		return app.getAtivo();
	} 

	public static Aplicacao getAplicacao(){

		Aplicacao app;
		try {
			app = (Aplicacao) XMLHelper.carregaArquivo(Aplicacao.class, FILE_HOME);
		} catch (JAXBException e) {
			app = criaArquivoPadrao(); 
		}
		if(app == null){
			app = criaArquivoPadrao(); 
		}
		return app;
	}

	private static Aplicacao criaArquivoPadrao() {
		Aplicacao app;
		app = new Aplicacao();
		XMLHelper.geraArquivo(app, FILE_HOME);
		return app;
	} 


	public void salvar(){
		XMLHelper.geraArquivo(this, FILE_HOME);
	}

	/**
	 * Verifica a conex�o com o banco de dados
	 * 
	 * @return Returna true para ok e false para falha
	 * 
	 */
	public boolean testaConexao() throws DAOException{

		sucessoBanco = true;
		//jdbc:sqlserver://localhost:1433;databaseName=CRM_MIDIA
		String url             = getUrl();  
		String user            = getUsuarioBanco();  
		String pwd             = getSenhaBanco();  
		Connection con  = null;  

		try {  
			try {
				if(sgbdNome.equals("SqlServer 2008")){
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
					hibernateDriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
					hibernateDialect="org.hibernate.dialect.SQLServer2008Dialect";
				}else if(sgbdNome.equals("Mysql")){
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					hibernateDriver="com.mysql.jdbc.Driver";
					hibernateDialect="org.hibernate.dialect.MySQLDialect";
				}else if(sgbdNome.equals("Postgres")){
					Class.forName("org.postgresql.Driver").newInstance();
					hibernateDriver="org.postgresql.Driver";
					hibernateDialect="org.hibernate.dialect.PostgreSQLDialect";
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con = DriverManager.getConnection(url, user, pwd);  
			con.setAutoCommit(false); 
			con.close();
			sucessoBanco = true;

		} catch (ClassNotFoundException e) {  
			sucessoBanco = false;
			throw new DAOException("Ocorreu um erro no sistema ao se conectar em" + url  
					+ "\n, se o problema persistir contate o administrador:\n Driver não encontrado!");

		} catch (SQLException e) {  
			sucessoBanco = false;
			throw new DAOException("Ocorreu um erro no sistema, se o problema persistir contate o administrador:\n Erro na Conex�o com Banco\n"+ e);  

		}  
		return sucessoBanco;
	}

}
