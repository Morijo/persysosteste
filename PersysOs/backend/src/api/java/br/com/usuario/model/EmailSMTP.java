package br.com.usuario.model;

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IEmailSMTP;
import br.com.usuario.dao.UsuarioDAO;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name="emailsmtp")
public class EmailSMTP extends Model<EmailSMTP> implements IEmailSMTP{

	private String hostName =null; 
	private String pwd =null;
	private int port;
	private String userName=null;
	private String emailSend=null;

	public static String CONSTRUTOR = "id, hostName, pwd, port, userName, emailSend";
	
	public EmailSMTP(){}
	
	public EmailSMTP(Long id, String hostName,String pwd, int port, String userName, String emailSend){
		setId(id);
		this.hostName = hostName;
		this.pwd = pwd;
		this.port = port;
		this.userName = userName;
		this.emailSend = emailSend;
	}
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailSend() {
		return emailSend;
	}
	public void setEmailSend(String emailSend) {
		this.emailSend = emailSend;
	}

	@SuppressWarnings("deprecation")
	public void enviaEmailFormatoHtml(String emailTarget, String userNameTarget, String msg) throws EmailException, MalformedURLException {  

		HtmlEmail email = new HtmlEmail();  

		// adiciona uma imagem ao corpo da mensagem e retorna seu id  
		URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");  
		email.embed(url, "Persys");     

		// configura a mensagem para o formato HTML  
		email.setHtmlMsg("<html>Persys - <img ></html>");  

		// configure uma mensagem alternativa caso o servidor não suporte HTML  
		email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");  

		email.setHostName(this.hostName); // o servidor SMTP para envio do e-mail  "smtp.gmail.com" 
		email.addTo(emailTarget, userNameTarget); //destinatário  
		email.setFrom(this.emailSend, this.userName); // remetente  
		email.setSubject("OsManager -> Senha de acesso"); // assunto do e-mail  
		email.setMsg(msg); //conteudo do e-mail  
		email.setAuthentication(this.emailSend, this.pwd);  
		email.setSmtpPort(this.port);  //465
		email.setSSL(true);
		email.setTLS(true);

		email.send();  
	} 

	@SuppressWarnings("static-access")
	public static EmailSMTP pesquisaEmail(Long consumerKey){
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		EmailSMTP emailSMTP = usuarioDAO.pesquisaEmailSMTP(consumerKey);
		if(emailSMTP != null)
			return emailSMTP;
		else
			return new EmailSMTP();
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkNotNull(this.hostName, "Host Name invalid");
		PreconditionsModel.checkNotNull(this.port, "Port invalid");
		PreconditionsModel.checkNotNull(this.userName, "User Name invalid");
		PreconditionsModel.checkNotNull(this.emailSend, "Email Send invalid");
		setCodigo("PEMA"+countPorConsumerSecret(EmailSMTP.class,getConsumerSecret().getConsumerKey()));
		
	}

}
