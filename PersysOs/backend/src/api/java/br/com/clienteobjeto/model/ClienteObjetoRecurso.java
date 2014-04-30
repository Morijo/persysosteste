package br.com.clienteobjeto.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.sun.xml.bind.CycleRecoverable;

import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.recurso.model.Recurso;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="clienteobjetorecurso")
public class ClienteObjetoRecurso extends Model<ClienteObjetoRecurso> implements Serializable , CycleRecoverable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "contratoidpk", referencedColumnName="id"),
	})
	@Cascade(CascadeType.ALL)
	private ClienteObjeto clienteOrdem = null;
	
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "recursoidpk", referencedColumnName="id"),
	})
	@Cascade(CascadeType.ALL)
	private Recurso recurso = null;
	
	@Column(name = "dataaquisicao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAquisicao = new Date();
	
	public ClienteObjetoRecurso(){}
	
	public ClienteObjetoRecurso(Recurso recurso, ClienteObjeto clienteOrdem, String status){
		super(ClienteObjetoRecurso.class);
		this.recurso  = recurso;
		this.clienteOrdem = clienteOrdem;
	}
	
	//GETTERS AND SETTERS
	public ClienteObjeto getClienteOrdem() {
		return clienteOrdem;
	}

	public void setClienteOrdem(ClienteObjeto clienteOrdem) {
		this.clienteOrdem = clienteOrdem;
	}

	public Date getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(Date dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return arg0;  
	}

	@Override
	public void valida() throws ModelException {
		if(getCodigo().isEmpty()){
			setCodigo("PCOR"+count(ClienteObjetoRecurso.class));
		}
	}
}
