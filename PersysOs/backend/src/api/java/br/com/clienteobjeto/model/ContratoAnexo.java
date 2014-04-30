package br.com.clienteobjeto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.com.exception.ModelException;
import br.com.model.Model;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@DynamicInsert
public class ContratoAnexo extends Model<ClienteObjeto>{

	/**
	* 
	*/
	@Column(name = "tamanho")
	private Integer tamanho = null;
	
	@Column(name = "arquivo")
	private byte[] arquivo = null;
	
	@Column(name = "tipo")
	private byte[] tipo = null;
	
	@Column(name = "descricao")
	private boolean descricao =true;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumns({    
		@JoinColumn( name = "clienteobjetoidpk", referencedColumnName="id",updatable=false),
	})
	private ClienteObjeto clienteObjeto = null;
	
	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public boolean isDescricao() {
		return descricao;
	}

	public void setDescricao(boolean descricao) {
		this.descricao = descricao;
	}

	public ClienteObjeto getClienteObjeto() {
		return clienteObjeto;
	}

	public void setClienteObjeto(ClienteObjeto clienteObjeto) {
		this.clienteObjeto = clienteObjeto;
	}

	@Override
	public void valida() throws ModelException {
		if(getCodigo().isEmpty()){
			setCodigo("PCOA"+count(ContratoAnexo.class));
		}
	}
	
	
}
