package br.com.clienteobjeto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.sun.xml.bind.CycleRecoverable;

import br.com.cliente.model.Cliente;
import br.com.clienteobjeto.dao.ContratoDAO;
import br.com.contato.model.Contato;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.rest.represention.JsonDateAdapter;

/**
 * @author ricardosabatine
 *@version v1.0.0
 *Class Contrato ��� um objeto exclusivo para provedor
 */
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@XmlSeeAlso({ClienteObjetoProduto.class, ClienteObjetoRecurso.class})
@Entity
@Table(name="contrato")
@PrimaryKeyJoinColumn(name="id")
public class Contrato extends ClienteObjeto  implements Serializable , CycleRecoverable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@Column(name = "tipocontrato")
	private String tipoContrato = null;
	
	@Column(name = "fidelidade")
	private boolean fidelidade = false;
	
	@Column(name = "prorrogavel")
	private boolean prorrogavel =false;
	
	@Column(name = "dataassinatura")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date    dataAssinatura =null;
	
	@Column(name = "datavigenciainicio")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date    dataVigenciaInicio =null;
	
	@Column(name = "datavigenciafim")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date    dataVigenciaFim =null;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumn(name = "contatoid")
	@Cascade(CascadeType.PERSIST)
	private Contato contato = null;
	
	public Contrato(){
		super(Contrato.class);
	}

	public Contrato(Long id, String codigo){
		super(Contrato.class);
		this.setId(id);
		this.setCodigo(codigo);
	}

	public static String CONSTRUTOR = " (id, codigo, dataAssinatura,situacao,cliente.id, cliente.codigo, cliente.razaoNome, cliente.cnpjCpf) ";
	
	public Contrato(Long id, String codigo, Date dataAssinatura,String situacao, Long clienteId, String clienteCodigo, String clienteRazaoNome, String clienteCnpjCpf){
		super(Contrato.class);
		this.setId(id);
		this.setCodigo(codigo);
		this.setDataAssinatura(dataAssinatura);
		this.setSituacao(situacao);
		this.setCliente(new Cliente(clienteId,clienteCodigo,clienteRazaoNome,clienteCnpjCpf));
	}
	
	public String getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public boolean isFidelidade() {
		return fidelidade;
	}

	public void setFidelidade(boolean fidelidade) {
		this.fidelidade = fidelidade;
	}
	
	public boolean isProrrogavel() {
		return prorrogavel;
	}

	public void setProrrogavel(boolean prorrogavel) {
		this.prorrogavel = prorrogavel;
	}

	public Date getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public Date getDataVigenciaInicio() {
		return dataVigenciaInicio;
	}

	public void setDataVigenciaInicio(Date dataVigenciaInicio) {
		this.dataVigenciaInicio = dataVigenciaInicio;
	}

	public Date getDataVigenciaFim() {
		return dataVigenciaFim;
	}

	public void setDataVigenciaFim(Date dataVigenciaFim) {
		this.dataVigenciaFim = dataVigenciaFim;
	}
	
	public static ArrayList<Contrato> listaContrato(Session session, Integer inicial, Integer tamanho, String consumerSecret, Integer statusModel) throws ModelException{
		ContratoDAO contratoDAO = new ContratoDAO();
		try {
			return (ArrayList<Contrato>) contratoDAO.listaContrato(inicial, tamanho, consumerSecret,statusModel);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	public static ArrayList<Contrato> listaContratoCliente(Session session, Integer inicial, Integer tamanho, String consumerSecret, Long idCliente) throws ModelException{
		ContratoDAO contratoDAO = new ContratoDAO();
		try {
			return (ArrayList<Contrato>) contratoDAO.listaContratoCliente(inicial, tamanho, consumerSecret, idCliente);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void valida() throws ModelException {
		
		if(getCodigo().isEmpty())
			setCodigo("PCTR"+count(Contrato.class));
		
		super.valida();
		if(contato != null){
			contato.valida();
		}	
	}	
	
}	