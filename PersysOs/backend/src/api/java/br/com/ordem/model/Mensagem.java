package br.com.ordem.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import br.com.exception.ModelException;
import br.com.funcionario.model.Funcionario;
import br.com.model.Model;
import br.com.model.PreconditionsModel;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="mensagem")
public class Mensagem extends Model<Mensagem> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "titulo", length=100)
	private String titulo = ""; 
	
	@Column(name = "fonte", length=100)
	private String fonte =""; //web telefone zabbix;
	
	@Column(name = "mensagem", length=5000)
	@Lob
	private String mensagem ="";;
	
	@Column(name = "tipo", length=100)
	private String tipo =""; 
	
	@Column(name = "ip")
	private String ipEndereco = ""; 
	
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "funcioanrioidpk", referencedColumnName="id"),
	})
	 private Funcionario funcionario;

	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "ordemidpk", referencedColumnName="id"),
	})
    private Ordem ordem;

	
	public Mensagem() {
		super();
		}


	public Mensagem(String mensagem) {
		super();
		this.mensagem = mensagem;
		}

	/**
	 * @return the fonte
	 */
	public String getFonte() {
		return fonte;
	}

	/**
	 * @param fonte the fonte to set
	 */
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	/**
	 * @return the ipEndereco
	 */
	public String getIpEndereco() {
		return ipEndereco;
	}

	/**
	 * @param ipEndereco the ipEndereco to set
	 */
	public void setIpEndereco(String ipEndereco) {
		this.ipEndereco = ipEndereco;
	}

	/**
	 * @return the ordem
	 */
	public Ordem getOrdem() {
		return ordem;
	}

	/**
	 * @param ordem the ordem to set
	 */
	public void setOrdem(Ordem ordem) {
		this.ordem = ordem;
	}

	
	/**
	 * @return the mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * @param mensagem the mensagem to set
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}


	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	/**
	 * @return the funcionario
	 */
	public Funcionario getFuncionario() {
		return funcionario;
	}


	/**
	 * @param funcionario the funcionario to set
	 */
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(mensagem, "Mensagem inv�lido");
	}


	@Override
	public String toString() {
		funcionario = null;
		return "Mensagem [fonte=" + fonte + ", mensagem=" + mensagem
				+ ", tipo=" + tipo + ", ipEndereco=" + ipEndereco
				;	
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fonte == null) ? 0 : fonte.hashCode());
		result = prime * result
				+ ((funcionario == null) ? 0 : funcionario.hashCode());
		result = prime * result
				+ ((ipEndereco == null) ? 0 : ipEndereco.hashCode());
		result = prime * result
				+ ((mensagem == null) ? 0 : mensagem.hashCode());
		result = prime * result + ((ordem == null) ? 0 : ordem.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Mensagem other = (Mensagem) obj;
		if (fonte == null) {
			if (other.fonte != null)
				return false;
		} else if (!fonte.equals(other.fonte))
			return false;
		if (funcionario == null) {
			if (other.funcionario != null)
				return false;
		} else if (!funcionario.equals(other.funcionario))
			return false;
		if (ipEndereco == null) {
			if (other.ipEndereco != null)
				return false;
		} else if (!ipEndereco.equals(other.ipEndereco))
			return false;
		if (mensagem == null) {
			if (other.mensagem != null)
				return false;
		} else if (!mensagem.equals(other.mensagem))
			return false;
		if (ordem == null) {
			if (other.ordem != null)
				return false;
		} else if (!ordem.equals(other.ordem))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}

	
	

}
