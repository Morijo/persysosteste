package br.com.clienteobjeto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.sun.xml.bind.CycleRecoverable;

import br.com.cliente.model.Cliente;
import br.com.clienteobjeto.dao.ContratoDAO;
import br.com.empresa.model.Unidade;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.StatusModel;
import br.com.model.interfaces.ICliente;
import br.com.model.interfaces.IClienteObjeto;
import br.com.ordem.model.Ordem;

/**
 * @author ricardosabatine
 * @since v1.0.0
 * ClienteObjeto ��� uma classe generica para atender os diferentes tipos de contratos.
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@XmlSeeAlso({Contrato.class})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DynamicInsert
public class ClienteObjeto extends Model<ClienteObjeto> implements  CycleRecoverable, IClienteObjeto  {

	@Column(name = "descricao")
	private String descricaoObjeto;
	
	@Column(name = "valortotal")
	private BigDecimal valorTotal = new BigDecimal("0.00");

	@Column(name = "valormensal")
	private BigDecimal valorMensal = new BigDecimal("0.00");
	
	@Column(name = "situacao")  //vencido, suspenso, cancelado, car���ncia, normal, especial
	private String situacao = null;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@PrimaryKeyJoinColumn 
	private Cliente cliente = null;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumns({    
		@JoinColumn( name = "unidadeidpk", referencedColumnName="id",updatable=false),
	})
	private Unidade unidadeGestora = null;
	
	@XmlTransient
	@OneToMany(mappedBy = "clienteObjeto", targetEntity = ClienteObjetoProduto.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<ClienteObjetoProduto> contratoProduto = new ArrayList<ClienteObjetoProduto>();
	
	@XmlTransient
	@OneToMany(mappedBy = "clienteOrdem", targetEntity = ClienteObjetoRecurso.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<ClienteObjetoRecurso> contratoRecurso = new ArrayList<ClienteObjetoRecurso>();
	
	public ClienteObjeto(Long id, String codigo, String descricaoObjeto, String cliente) {
		setId(id);
		setCodigo(codigo);
		this.descricaoObjeto = descricaoObjeto;
		this.cliente.setRazaoNome(cliente);
	}
	
	public ClienteObjeto(Long id, String codigo,Long clienteId, String clienteRazao,
			String clienteCnpjCpf) {
		setId(id);
		setCodigo(codigo);
		this.setCliente(new Cliente(clienteId, codigo, clienteRazao, clienteCnpjCpf, null));
	}
	
	public ClienteObjeto(Long id, String codigo, String descricaoObjet) {
		setId(id);
		setCodigo(codigo);
		this.descricaoObjeto = descricaoObjet;
	}
	
	public ClienteObjeto(Class<?> modelClass){
		super(modelClass);
	}
	
	public ClienteObjeto(){}
	
	public Collection<ClienteObjetoProduto> getContratoProduto() {
		return contratoProduto;
	}

	public void setContratoProduto(Collection<ClienteObjetoProduto> contratoProduto) {
		this.contratoProduto = contratoProduto;
	}

	public void addProduto(ClienteObjetoProduto contratoProduto) throws ModelException {
		contratoProduto.setDataAquisicao(new Date());
		contratoProduto.setStatusModel(StatusModel.ATIVO);
		contratoProduto.setClienteOrdem(this);
		contratoProduto.valida();
		this.contratoProduto.add(contratoProduto);
	}
	
	public Collection<ClienteObjetoRecurso> getContratoRecurso() {
		return contratoRecurso;
	}

	public void setContratoRecurso(Collection<ClienteObjetoRecurso> contratoRecurso) {
		this.contratoRecurso = contratoRecurso;
	}

	
	public void setUnidadeGestora(Unidade unidadeGestora) {
		this.unidadeGestora = unidadeGestora;
	}
	
	public Unidade getUnidadeGestora() {
		return unidadeGestora;
	}
	
	public ICliente getCliente() {
		return cliente;
	}
	public void setCliente(ICliente cliente) {
		this.cliente = (Cliente) cliente;
	}
	
	
	public String getDescricaoObjeto() {
		return descricaoObjeto;
	}
	
	public void setDescricaoObjeto(String descricaoObjeto) {
		this.descricaoObjeto = descricaoObjeto;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(String valorTotal) {
		this.valorTotal = new BigDecimal(valorTotal);
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public BigDecimal getValorMensal() {
		return valorMensal;
	}
	public void setValorMensal(String valorMensal) {
		this.valorMensal = new BigDecimal(valorMensal);
	}
	
	public void setValorMensal(BigDecimal valorMensal) {
		this.valorMensal = valorMensal;
	}
	
	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public static ArrayList<Ordem> listaContratoOrdem(Long idContrato, String consumerKey) throws ModelException{
		ContratoDAO contratoDAO = new ContratoDAO();
		try {
			return contratoDAO.listaContratoOrdem(idContrato, consumerKey);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(descricaoObjeto, "Descri����o objeto inv��lido");
		PreconditionsModel.checkNotNull(cliente, "Cliente inv��lido");
		PreconditionsModel.checkNotNull(unidadeGestora, "Unidade gestora inv��lida");
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}	
}
