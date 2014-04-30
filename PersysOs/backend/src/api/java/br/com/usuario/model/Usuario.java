package br.com.usuario.model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.mail.EmailException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.sun.xml.bind.CycleRecoverable;
import br.com.usuario.dao.*;
import br.com.cliente.model.Cliente;
import br.com.contato.model.Contato;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.funcionario.model.Funcionario;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IAccessToken;
import br.com.oauth.model.AccessToken;
import br.com.principal.helper.HashHelper;
import br.com.principal.helper.HibernateHelper;

@XmlRootElement
@XmlSeeAlso({Funcionario.class, Cliente.class})
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@org.hibernate.annotations.DiscriminatorOptions(force=true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Usuario extends Model<Usuario> implements Serializable, CycleRecoverable, br.com.model.interfaces.IUsuario {

	@Transient
	private static final long serialVersionUID = 1L;

	@Column (name = "usuario", unique = true, nullable = false, length=50)
	private String nomeUsuario = null;

	@Column(name = "razaonome", length = 150)
	private String razaoNome = null;

	@Column(name = "fantasiasobrenome", length = 150)
	private String fantasiaSobrenome = null;

	@Column(name = "cnpjcpf", length = 20)
	private String cnpjCpf = null;

	@Column(name = "ierg", length = 20)
	private String ieRg = null;

	@XmlTransient
	@Column (name = "senha",length=200)
	private String senha = "";

	@Column(name = "emailprincipal")
	private String emailPrincipal = null;

	@Column(name = "nota", length = 2000)
	private String nota = null;

	@Column(name = "dashboardnome", length = 100)
	private String dashboardNome = null;

	@Column(name = "timezone", length = 100)
	private String timezone = null;

	@Column(name = "locale", length = 100)
	private String locale = null;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "grupousuarioidpk", referencedColumnName="id"),
	})
	private GrupoUsuario grupoUsuario;

	@XmlTransient
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "accesstokenidpk",updatable=true)
	private AccessToken accessToken;

	@XmlTransient
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "usuarioagendaidpk",updatable=true)
	private UsuarioAgenda usuarioAgenda;
	
	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "usuario", targetEntity = Contato.class, fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<Contato> contato = new ArrayList<Contato>();

	@XmlTransient
	@Column(name = "imageperfil")
	@Lob
	private byte[] imagemPerfil = null;

	public Usuario(){
		super(Usuario.class);
	}

	public Usuario(Class<?> modelClass){
		super(modelClass);
	}

	public Usuario(Long id){
		setId(id);
	}

	public Usuario(Long id, String razaoNome, String emailPrincipal, String senha, String nomeUsuario){
		setId(id);
		this.razaoNome = razaoNome;
		this.emailPrincipal = emailPrincipal;
		this.senha = senha;
		this.nomeUsuario = nomeUsuario;
	}
	
	public Usuario(Long id, String codigo, String razaoNome, String nomeUsuario){
		setId(id);
		this.razaoNome = razaoNome;
		setCodigo(codigo);
		this.nomeUsuario = nomeUsuario;
	}

	public Usuario(String nomeUsuario, String senha){
		this.nomeUsuario = nomeUsuario;
		this.senha		 = senha; 
	}

	public Usuario(Long id, String accessToken, String accessSecret ) {
		setId(id);
		this.setAccessToken(new AccessToken(accessToken,accessSecret));
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public IAccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(IAccessToken accessToken) {
		this.accessToken = (AccessToken) accessToken;
	}
	
	public Collection<Contato> getContato() {
		return contato;
	}

	public void setContato(Collection<Contato> contato) {
		this.contato = contato;
	}

	public void addContato(Contato contato) {
		if(this.contato == null)
			this.contato = new ArrayList<Contato>();
		this.contato.add(contato);
	}

	public byte[] getImagemPerfil() {
		return imagemPerfil;
	}

	public void setImagemPerfil(byte[] imagemPerfil) {
		this.imagemPerfil = imagemPerfil;
	}

	public String getRazaoNome() {
		return razaoNome;
	}

	public void setRazaoNome(String razaoNome) {
		this.razaoNome = razaoNome;
	}

	public String getFantasiaSobrenome() {
		return fantasiaSobrenome;
	}

	public void setFantasiaSobrenome(String fantasiaSobrenome) {
		this.fantasiaSobrenome = fantasiaSobrenome;
	}

	public String getCnpjCpf() {
		return cnpjCpf;
	}

	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}

	public String getIeRg() {
		return ieRg;
	}

	public void setIeRg(String ieRg) {
		this.ieRg = ieRg;
	}

	public String getEmailPrincipal() {
		return emailPrincipal;
	}

	public void setEmailPrincipal(String emailPrincipal) {
		this.emailPrincipal = emailPrincipal;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getDashboardNome() {
		return dashboardNome;
	}

	public void setDashboardNome(String dashboardNome) {
		this.dashboardNome = dashboardNome;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public br.com.model.interfaces.IGrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(br.com.model.interfaces.IGrupoUsuario grupoUsuario) {
		this.grupoUsuario = (GrupoUsuario) grupoUsuario;
	}
	
	public UsuarioAgenda getUsuarioAgenda() {
		return usuarioAgenda;
	}

	public void setUsuarioAgenda(UsuarioAgenda usuarioAgenda) {
		this.usuarioAgenda = usuarioAgenda;
	}

	public static boolean autentica(Session session, String nome, String senha){

		Usuario usuario = Usuario.autenticaUsuario(session,nome, senha);
		if(usuario != null){
			return true;
		}
		else return false;

	}

	public static boolean autentica(String nome, String senha){

		Session session = HibernateHelper.openSession(Usuario.class);

		try{
			Usuario usuario = Usuario.autenticaUsuario(session,nome, senha);
			if(usuario != null){
				return true;
			}
			else return false;
		}finally{
			session.close();
		}	
	}

	public static Usuario autenticaUsuario(String nome, String senha){

		Session session = HibernateHelper.openSession(Usuario.class);

		Usuario usuario = null;

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuario = usuarioDAO.autenticaUsuario(session, nome, HashHelper.shaCodigo(senha));
			return usuario;
		}
		catch (Exception e) {
			return null;
		}finally{
			session.close();
		}	

	}

	public static Usuario autenticaUsuario(Session session, String nome, String senha){
		Usuario usuario = null;

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuario = usuarioDAO.autenticaUsuario(session, nome, HashHelper.shaCodigo(senha));
			return usuario;
		}
		catch (Exception e) {
			return null;
		}

	}

	public static Usuario pesquisaAccessToken(Session session, String token){

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			return usuarioDAO.pesquisaAccessToken(session, token);
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Usuario pesquisaAccessTokenDashboard(Session session, String token){

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			return usuarioDAO.pesquisaAccessTokenDashboard(session, token);
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Usuario pesquisaID(Session session, long id){

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			return usuarioDAO.pesquisaID(session, id);
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Usuario pesquisaID(long id){

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			return usuarioDAO.pesquisaID(id);
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Object pesquisaAccessToken(String token, Class<?> obj){

		Object usuario = null;

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuario = usuarioDAO.pesquisaAccessToken(token, obj);
			return usuario;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static Object pesquisaAccessTokenReturnId(String token){

		Object usuario = null;

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuario = usuarioDAO.pesquisaAccessTokenReturnID(token);
			return usuario;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static Object pesquisaAccessTokenReturnId(Session session, String token){

		Object usuario = null;

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuario = usuarioDAO.pesquisaAccessTokenReturnID(session, token);
			return usuario;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Object pesquisaAccessToken(Session session, String token, Class<?> obj){

		Object usuario = null;

		try{
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuario = usuarioDAO.pesquisaAccessToken(session, token, obj);
			return usuario;
		}
		catch (Exception e) {
			return null;
		}
	}

	public void alteraSenha() throws ModelException{

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		try {
			try {
				setSenha(HashHelper.shaCodigo(senha));
			} catch (NoSuchAlgorithmException e) {}
			usuarioDAO.alteraSenha(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	public void alteraDashboard() throws ModelException{

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		try {
			usuarioDAO.alteraDashboard(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	public void salvaAccessToken(Session session, AccessToken accessToken) throws ModelException{

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		try {
			usuarioDAO.salvaAccessToken(session, this, accessToken);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	public static boolean verificaNomeUsuario(String nomeUsuario) throws ModelException, DAOException{
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Number count = usuarioDAO.verificaUserName(nomeUsuario);
		if(count.intValue() > 0){
			return false;
		}
		return true;
	}

	public static Usuario recuperaSenha(Usuario usuario) throws ModelException{
		try {
			if(usuario != null){
				String novaSenha = HashHelper.shortUUID();
				usuario.setSenha(novaSenha);
				usuario.alteraSenha();
				EmailSMTP.pesquisaEmail(usuario.getConsumerSecret().getConsumerKey()).enviaEmailFormatoHtml(usuario.emailPrincipal, usuario.razaoNome, "Usu��rio "
						+ usuario.nomeUsuario +" sua senha ��: "+ novaSenha);
				return usuario;
			}else{
				throw new ModelException("Erro ao enviar a senha por email. Em SISTEMA configure o email de envio ou defina a senha em ALTERAR SENHA");
			}
		} catch (MalformedURLException e) {
			throw new ModelException("Erro ao enviar a senha por email. Em SISTEMA configure o email de envio ou defina a senha em ALTERAR SENHA");
		} catch (EmailException e) {
			throw new ModelException("Erro ao enviar a senha por email. Em SISTEMA configure o email de envio ou defina a senha em ALTERAR SENHA");
		}
	}

	public static Usuario recuparaSenhaPorId(Long id, String consumerKey) throws ModelException{
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.pesquisa(id, consumerKey);
		return recuperaSenha(usuario);
	}
	
	public static void recuparaSenhaPorEmail(String email) throws ModelException{
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		ArrayList<Usuario> usuarios = usuarioDAO.pesquisaPorEmail(email);
		for(Usuario usuario :usuarios)
		  recuperaSenha(usuario);
	}

	public static byte[] carregaImage(Session session, String token) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.carregaImage(session, token);
	}
	
	public static byte[] carregaImage(Long id) throws DAOException {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.carregaImage(id);
	}
	
	public static AccessToken createTokenUser(Session session, Usuario usuario)throws ModelException {
		AccessToken token = null;
		if(usuario.getAccessToken() == null){ 
			token = new AccessToken();
			token.geraTokens();
			usuario.setAccessToken(token);
			usuario.salvaAccessToken(session,token);
		}else{
			token = (AccessToken) usuario.getAccessToken();
		}
		return token;
	}

	public static AccessToken createTokenUser(Usuario usuario)throws ModelException {
		Session session = HibernateHelper.openSession(Usuario.class);
		Transaction tx = session.beginTransaction();
		try{
			return createTokenUser(session, usuario);
		}finally{
			tx.commit();
			session.close();
			session = null;
			tx = null;
		}
	}
	
	public static void alteraImagem(byte[] imagemPerfil, Long id) throws ModelException{
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		try {
			usuarioDAO.alteraImagem(imagemPerfil, id);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	public static GrupoUsuario pesquisaGrupoUsuario(Long idUsuario){
		return UsuarioDAO.pesquisaGrupoUsuario(idUsuario);
	}

	@Override
	public String toString() {
		return "Usuario [id=" + getId() + ", nomeUsuario=" + nomeUsuario + ", senha=" + senha
				+"]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((nomeUsuario == null) ? 0 : nomeUsuario.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		if (nomeUsuario == null) {
			if (other.nomeUsuario != null)
				return false;
		} else if (!nomeUsuario.equals(other.nomeUsuario))
			return false;
		return true;
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(nomeUsuario, "Nome usu��rio inv��lido");
		PreconditionsModel.checkNotNull(getConsumerSecret(), "Consumer secret n��o definido");

		nomeUsuario = nomeUsuario.trim().replace(" ", "");

		if(cnpjCpf != null && !cnpjCpf.isEmpty())
		cnpjCpf = cnpjCpf.trim().replace(" ", "");


		if(fantasiaSobrenome != null && fantasiaSobrenome.isEmpty()){
			fantasiaSobrenome = razaoNome;
		}

		if(getContato() != null)
			if(getContato().isEmpty()){
				for(Contato c : getContato()){
					c.valida();
					c.setUsuario(this);
				}
			}	
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}