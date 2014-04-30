package br.com.servico.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

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
import br.com.model.interfaces.IProcedimento;
import br.com.model.interfaces.IServico;
import br.com.model.interfaces.IServicoProcedimento;
import br.com.ordem.model.ServicoOrdem;
import br.com.servico.dao.ServicoProcedimentoDAO;
import br.com.servico.model.Servico;

import com.sun.xml.bind.CycleRecoverable;
/**
 *  <p> O modelo servicoProcedimento eh composto pelos seguintes campos </p>
 *  <p>obrigatorio boolean </p>
 *  <p>Código (String) tamanho (255) </p> 
 *  <p>servicoprocedimento Lista de procedimentos que um servico precisa executar</p>
 *  <br>Tabela no banco: servicoprocedimento </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see Procedimento
 * @see ServicoOrdem
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="servicoprocedimento")
public class ServicoProcedimento extends Model<ServicoProcedimento> implements Serializable, CycleRecoverable, IServicoProcedimento  {
	
	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}

	private static final long serialVersionUID = 1L;

	@Column(name = "obrigatorio")
	private Boolean obrigatorio; 

	@Column(name = "anexo")
	private Boolean anexo; 

	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "procedimentoidpk", referencedColumnName="id"),
	})
	private Procedimento procedimento;

	@ManyToOne
	@JoinColumn( name = "servicoidpk")
	private Servico servico;

	public static String CONSTRUTOR = "id,dataCriacao,dataAlteracao,statusModel, obrigatorio,anexo, procedimento.id, "
			+ "servico.id, procedimento.titulo, procedimento.codigo";

	public ServicoProcedimento(Long id, Date dataCriacao, Date dataAlteracao,int statusModel, boolean obrigatorio,
			boolean anexo, Long procedimentoId, 
			Long servicoId, String procedimentoNome, String procedimentoCodigo) {
		super(ServicoProcedimento.class);
		setId(id);
		setObrigatorio(obrigatorio);
		setAnexo(anexo);
		setDataAlteracao(dataAlteracao);
		setDataCriacao(dataCriacao);
		setStatusModel(statusModel);
		setServico(new Servico(servicoId, null, null,"", "",null, 1));
		setProcedimento(new Procedimento(procedimentoId,procedimentoNome,procedimentoCodigo));
	}

	public ServicoProcedimento(Long id, Date dataCriacao, Date dataUpdate, String titulo,
			String descricao, Procedimento procedimento) {
		super(ServicoProcedimento.class);
		this.procedimento = procedimento;
	}

	public ServicoProcedimento() {
		super(ServicoProcedimento.class);
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

	@Override
	public Boolean isObrigatorio() {
		return obrigatorio;
	}

	@Override
	public Boolean isAnexo() {
		return this.anexo;
	}

	@Override
	public void setAnexo(Boolean anexo) {
		this.anexo = anexo;
	}

	@Override
	public void setObrigatorio(Boolean obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public static ArrayList<ServicoProcedimento> listaServicoProcedimento(Long servicoId, String consumerKey) {
		ServicoProcedimentoDAO servicoProcedimentoDAO = new ServicoProcedimentoDAO();
		try {
			return (ArrayList<ServicoProcedimento>) servicoProcedimentoDAO.listaServicoProcedimento(servicoId, Long.parseLong(consumerKey));
		} catch (DAOException e) {
			return new ArrayList<ServicoProcedimento>();
		}
	}

	public boolean alteraServicoProcedimento() {
		ServicoProcedimentoDAO servicoProcedimentoDAO = new ServicoProcedimentoDAO();
		try {
			return servicoProcedimentoDAO.alteraServicoProcedimentoSituacao(this);
		} catch (DAOException e) {
			return false;
		}
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkNotNull(getConsumerSecret(), bundle.getString("consumersecretnotset"));
		setCodigo(getServico().getId()+"_"+getProcedimento().getId());
	}
	
	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
}
