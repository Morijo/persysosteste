package br.com.ordem.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.sun.xml.bind.CycleRecoverable;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.interfaces.IOrdem;
import br.com.model.interfaces.IRecurso;
import br.com.model.interfaces.IRecursoOrdem;
import br.com.ordem.dao.RecursoOrdemDAO;
import br.com.recurso.model.Medida;
import br.com.recurso.model.Recurso;
import br.com.rest.represention.JsonDateAdapter;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="recursoordem")
public class RecursoOrdem extends Model<RecursoOrdem> implements Serializable, IRecursoOrdem, CycleRecoverable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "datacriacaoorigem")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataCriacaoOrigem = null;

	
	@Column(name = "quantidadeconsumida", nullable=false)
	private BigDecimal quantidadeConsumida  = new BigDecimal("0");

	@Column(name = "obs", length = 5000)
	private String obs = null; 

	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "ordemidpk", referencedColumnName="id"),
	})
	private Ordem ordem;
	
	@Transient
	@XmlElement(name="ordem")
	private Ordem ordemOrigem; //utilizado para mandar pela rede.

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn( name = "recursoidpk")
	private Recurso recurso;

	public static String CONSTRUTOR = "id,dataCriacao,dataAlteracao,dataCriacaoOrigem,statusModel,quantidadeConsumida, "
			+ "ordem.id, recurso.id, recurso.nome, recurso.codigo, "
			+ "recurso.statusModel, recurso.tipoRecurso, recurso.medida.nome";
	
	public RecursoOrdem(Long id, Date dataCriacao, Date dataAlteracao,Date dataCriacaoOrigem,int statusModel, BigDecimal quantidade, Long ordemId, 
			Long recursoId, String recursoNome, String recursoCodigo, int recursoStatusModel, String tipoRecurso, String nomeMedida) {
		super(RecursoOrdem.class);
		setId(id);
		setDataCriacaoOrigem(dataCriacaoOrigem);
		setDataCriacao(dataCriacao);
		setDataAlteracao(dataAlteracao);
		setStatusModel(statusModel);
		setQuantidadeConsumida(quantidade);
		setOrdem(new Ordem(ordemId));
		setRecurso(new Recurso(recursoId, recursoCodigo, recursoNome, recursoStatusModel, "", tipoRecurso));
		recurso.setMedida(new Medida(nomeMedida, null));
	}

	public RecursoOrdem() {
		super(RecursoOrdem.class);
	}

	/**
	 * @return the ordem
	 */
	public IOrdem getOrdem() {
		return ordem;
	}

	/**
	 * @param ordem the ordem to set
	 */
	public void setOrdem(IOrdem ordem) {
		this.ordem = (Ordem) ordem;
	}

	public String getObs() {
		return obs;
	}

	public BigDecimal getQuantidadeConsumida() {
		return quantidadeConsumida;
	}

	public void setQuantidadeConsumida(BigDecimal quantidadeConsumida) {
		this.quantidadeConsumida = quantidadeConsumida;
	}

	public IRecurso getRecurso() {
		return recurso;
	}

	public void setRecurso(IRecurso recurso) {
		this.recurso = (Recurso) recurso;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}
	
	public Ordem getOrdemOrigem() {
		return ordemOrigem;
	}

	public void setOrdemOrigem(Ordem ordemOrigem) {
		this.ordemOrigem = ordemOrigem;
	}

	public Date getDataCriacaoOrigem() {
		return dataCriacaoOrigem;
	}

	public void setDataCriacaoOrigem(Date dataCriacaoOrigem) {
		this.dataCriacaoOrigem = dataCriacaoOrigem;
	}
	
	public static ArrayList<RecursoOrdem> listaRecursoOrdem(Long idOrdem, int statusModel){
		RecursoOrdemDAO recursoOrdemDAO = new RecursoOrdemDAO();
		try {
			return (ArrayList<RecursoOrdem>) recursoOrdemDAO.listaRecurso(idOrdem,statusModel);
		} catch (DAOException e) {
			return new ArrayList<RecursoOrdem>();
		}
	}

	public void altera(Long consumerKey) throws ModelException{
		try{
			RecursoOrdemDAO recursoOrdemDAO = new  RecursoOrdemDAO();
			recursoOrdemDAO.altera(this,consumerKey);
		}catch(Exception e){
			throw new ModelException(e.getMessage());
		}
	}
	
	public void remove() throws ModelException{
		try{
			RecursoOrdemDAO recursoOrdemDAO = new  RecursoOrdemDAO();
			recursoOrdemDAO.remover(this);
		}catch(Exception e){
			throw new ModelException(e.getMessage());
		}
	}

	@Override
	public void valida() throws ModelException {
		if(dataCriacaoOrigem == null){
			dataCriacaoOrigem = new Date();
		}
		
		if(quantidadeConsumida == null){
			quantidadeConsumida = new BigDecimal("0.0");
		}
		if(dataCriacaoOrigem != null)
			setCodigo("POS"+dataCriacaoOrigem.getTime()+"_m_"+getCodigo());
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
