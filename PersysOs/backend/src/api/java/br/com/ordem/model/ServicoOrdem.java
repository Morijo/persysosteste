package br.com.ordem.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.ordem.dao.ServicoOrdemDAO;
import br.com.servico.model.Servico;

import com.sun.xml.bind.CycleRecoverable;
/**
 *  <p> O modelo servicoOrdem eh composto pelos seguintes campos </p>
 *  <p>serivcoordemdescricao (String) tamanho (5000) </p>
 *  <p>Código (String) tamanho (255) </p> 
 *  <p>serivcoordemvalorservicoquantidade (BigDecimal) tamanho (19,2) </p>
 *  <p>serivcoordemvalorservicofinal (BigDecimal) tamanho (19,2) </p>
 *  <p>serivcoordemvalorservicounitario (BigDecimal) tamanho(19,2) </p>
 *  <p>servicoordem Lista de servicos que estao vinculados a uma ordem </p>
 *  <br>Tabela no banco: servicoordem </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see Servico
 
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="servicoordem")
public class ServicoOrdem extends Model<ServicoOrdem> implements Serializable , CycleRecoverable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "serivcoordemdescricao", length = 5000)
	private String descricao = ""; 
	
	@Column(name = "serivcoordemvalorservicofinal")
	private BigDecimal valorServicoFinal; 

	@Column(name = "serivcoordemvalorservicounitario")
	private BigDecimal valorUnitarioFinal; 

	@Column(name = "serivcoordemvalorservicoquantidade")
	private BigDecimal quantidade; 

	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "ordemidpk", referencedColumnName="id"),
	})
	private Ordem ordem;

	@ManyToOne
	@JoinColumn( name = "servicoidpk")
	private Servico servico;

	public static String CONSTRUTOR = "id,dataCriacao,dataAlteracao,statusModel, ordem.id, "
			+ "servico.id, servico.titulo, servico.descricao, servico.codigo, servico.statusModel";
	
	public ServicoOrdem(Long id, Date dataCriacao, Date dataUpdate, String titulo,
			String nota, Ordem ordem) {
		super(ServicoOrdem.class);
		this.ordem = ordem;
	}
	
	public ServicoOrdem(Long id, Date dataCriacao, Date dataAlteracao,int statusModel, Long ordemId, 
			Long servicoId, String servicoNome, String servicoDescricao, String servicoCodigo, int servicoStatus) {
		super(ServicoOrdem.class);
		setId(id);
		setDataAlteracao(dataAlteracao);
		setDataCriacao(dataCriacao);
		setStatusModel(statusModel);
		setServico(new Servico(servicoId, null, null,servicoCodigo, servicoNome,null, servicoStatus));
		getServico().setDescricao(servicoDescricao);
		setOrdem(new Ordem(ordemId));
	}

	public ServicoOrdem() {
		super(ServicoOrdem.class);
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

	public String getDescricao() {
		return descricao;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public BigDecimal getValorServicoFinal() {
		return valorServicoFinal;
	}

	public void setValorServicoFinal(BigDecimal valorServicoFinal) {
		this.valorServicoFinal = valorServicoFinal;
	}

	public BigDecimal getValorUnitarioFinal() {
		return valorUnitarioFinal;
	}

	public void setValorUnitarioFinal(BigDecimal valorUnitarioFinal) {
		this.valorUnitarioFinal = valorUnitarioFinal;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public static ArrayList<ServicoOrdem> listaServicoOrdem(Long idOrdem, int statusModel){
		ServicoOrdemDAO servicoDAO = new ServicoOrdemDAO();
		try {
			return (ArrayList<ServicoOrdem>) servicoDAO.listaServico(idOrdem,statusModel);
		} catch (DAOException e) {
			return new ArrayList<ServicoOrdem>();
		}
	}
	
	public void altera() throws ModelException{
		ServicoOrdemDAO servicoDAO = new ServicoOrdemDAO();
		try {
			servicoDAO.altera(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	

	public void remove() throws ModelException{
		ServicoOrdemDAO servicoDAO = new ServicoOrdemDAO();
		try {
			servicoDAO.remover(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkNotNull(getConsumerSecret(), "ConsumerSecret inválido");
		if(getCodigo() != null )
			if(!getCodigo().isEmpty())
				setCodigo(getServico().getId()+"_"+getOrdem().getId());
	}
}
