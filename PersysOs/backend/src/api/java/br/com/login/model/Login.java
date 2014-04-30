package br.com.login.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.model.Model;
import br.com.exception.ModelException;
import br.com.usuario.model.Usuario;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name = "login")
public class Login extends Model<Login> {

	private Date    dataEncerramento;
	private String  ip;
	private Boolean ativo;
	private String  sistemasOperacional;
	private String  navegador;
	private Integer resAltura;
	private Integer resLargura;
	
	@OneToOne
	private Usuario usuario;
	
	public Login(){}
	
	public Login(String navegador, String sistemasOperacional, String ip, Usuario usuario, Boolean ativo, Integer resAltura, Integer resLargura) {
		super();
		this.ip = ip;
		this.usuario = usuario;
		this.ativo = ativo;
		this.sistemasOperacional = sistemasOperacional;
		this.navegador = navegador;
		this.resAltura = resAltura;
		this.resLargura = resLargura;	
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Date getDataEncerramento() {
		return dataEncerramento;
	}

	public void setDataEncerramento(Date dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	
	public String getSistemasOperacional() {
		return sistemasOperacional;
	}

	public void setSistemasOperacional(String sistemasOperacional) {
		this.sistemasOperacional = sistemasOperacional;
	}

	public String getNavegador() {
		return navegador;
	}

	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}

	public int getResAltura() {
		return resAltura;
	}

	public void setResAltura(int resAltura) {
		this.resAltura = resAltura;
	}

	public int getResLargura() {
		return resLargura;
	}

	public void setResLargura(int resLargura) {
		this.resLargura = resLargura;
	}

	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Login other = (Login) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		return true;
	}

	@Override
	public void valida() throws ModelException {
		if(getCodigo().isEmpty()){
			setCodigo("PLOG"+countPorConsumerSecret(Login.class, getConsumerSecret().getConsumerKey()));
		}
	}
}
