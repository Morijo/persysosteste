package br.com.model.interfaces;

public interface IEmailSMTP {

	public String getHostName();
	public void setHostName(String hostName);
	public String getPwd();
	public void setPwd(String pwd);
	public int getPort();
	public void setPort(int port);
	public String getUserName();
	public void setUserName(String userName);
	public String getEmailSend();
	public void setEmailSend(String emailSend);
}
