package br.com.ordem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.interfaces.IAnexo;
import br.com.model.interfaces.IOrdem;
import br.com.model.interfaces.IOrdemProcedimento;
import br.com.model.interfaces.IProcedimento;
import br.com.model.interfaces.IServico;
import br.com.ordem.dao.OrdemProcedimentoDAO;
import br.com.servico.model.Procedimento;
import br.com.servico.model.Servico;

import com.sun.xml.bind.CycleRecoverable;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="ordemprocedimento")
public class OrdemProcedimento extends Model<OrdemProcedimento> implements Serializable , CycleRecoverable, IOrdemProcedimento  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "obrigatorio")
	private Boolean obrigatorio = null; 

	@Column(name = "anexo")
	private Boolean temAnexo = null; 

	@Column(name = "verificado")
	private Boolean verificado = null; 

	
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "procedimentoidpk", referencedColumnName="id"),
	})
	private Procedimento procedimento;

	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "ordemidpk", referencedColumnName="id"),
	})
	private Ordem ordem;


	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "anexoidpk", referencedColumnName="id"),
	})
	private Anexo anexo;

	
	@OneToOne
	private Servico servico;

	public static String CONSTRUTOR = "id,dataCriacao,dataAlteracao,statusModel, obrigatorio, temAnexo, "
			+ "servico.id, servico.titulo, servico.codigo, ordem.id,procedimento.id, procedimento.titulo, procedimento.codigo";

	public OrdemProcedimento(Long id, Date dataCriacao, Date dataAlteracao,int statusModel, boolean obrigatorio,
			boolean anexo, Long servicoId, String servicoTitulo, String servicoCodigo, Long ordemId,
			Long procedimentoId, String procedimentoNome, String procedimentoCodigo) {
		super(OrdemProcedimento.class);
		setId(id);
		setObrigatorio(obrigatorio);
		setAnexo(anexo);
		setDataAlteracao(dataAlteracao);
		setDataCriacao(dataCriacao);
		setStatusModel(statusModel);
		setServico(new Servico(servicoId, null, null,servicoCodigo, servicoTitulo,null, 1));
		setOrdem(new Ordem(ordemId));
		setProcedimento(new Procedimento(procedimentoId,procedimentoNome,procedimentoCodigo));
	}

	public OrdemProcedimento() {
		super(OrdemProcedimento.class);
	}

	public IProcedimento getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(IProcedimento procedimento) {
		this.procedimento = (Procedimento) procedimento;
	}

	public void setServico(IServico servico) {
		this.servico = (Servico) servico;
	}

	@Override
	public IServico getServico() {
		return this.servico;
	}
	
	public void setObrigatorio(Boolean obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public IOrdem getOrdem() {
		return ordem;
	}

	public void setOrdem(IOrdem ordem) {
		this.ordem = (Ordem) ordem;
	}

	@Override
	public IAnexo getAnexo() {
		return anexo;
	}

	@Override
	public void setAnexo(IAnexo anexo) {
		this.anexo = (Anexo) anexo;
	}

	@Override
	public Boolean isObrigatorio() {
		return obrigatorio;
	}

	@Override
	public Boolean isAnexo() {
		return this.temAnexo;
	}

	@Override
	public void setAnexo(Boolean anexo) {
		this.temAnexo = anexo;
	}

	@Override
	public Boolean isVerificado() {
		return verificado;
	}

	@Override
	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}

	
	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}

	@Override
	public void valida() throws ModelException {
		setCodigo(ordem.getId()+"_"+servico.getId()+"_"+procedimento.getId());
		if(temAnexo == null){
			temAnexo = false;
		}
		if(verificado == null){
			verificado = false;
		}
		if(obrigatorio == null){
			obrigatorio = false;
		}
		
	}

	public static ArrayList<OrdemProcedimento> listaOrdemProcedimento(Long ordemId) {
		OrdemProcedimentoDAO servicoProcedimentoDAO = new OrdemProcedimentoDAO();
		try {
			return (ArrayList<OrdemProcedimento>) servicoProcedimentoDAO.listaOrdemProcedimento(ordemId);
		} catch (DAOException e) {
			return new ArrayList<OrdemProcedimento>();
		}
	}
	
	public static ArrayList<OrdemProcedimento> listaOrdemProcedimento(Long ordemId, Long servicoId) {
		OrdemProcedimentoDAO servicoProcedimentoDAO = new OrdemProcedimentoDAO();
		try {
			return (ArrayList<OrdemProcedimento>) servicoProcedimentoDAO.listaOrdemProcedimento(ordemId, servicoId);
		} catch (DAOException e) {
			return new ArrayList<OrdemProcedimento>();
		}
	}

	public static boolean alteraOrdemProcedimentoSituacao(Long ordemId, Long servicoId, int status){
		OrdemProcedimentoDAO  servicoProcedimentoDAO = new OrdemProcedimentoDAO();
		try {
			return servicoProcedimentoDAO.alteraOrdemProcedimentoSituacao(ordemId, servicoId, status);
		} catch (DAOException e) {
			return false;
		}
	}	
	public boolean alteraOrdemProcedimento() {
		OrdemProcedimentoDAO  servicoProcedimentoDAO = new OrdemProcedimentoDAO();
		try {
			return servicoProcedimentoDAO.alteraOrdemProcedimentoSituacao(this);
		} catch (DAOException e) {
			return false;
		}
	}
}
