package br.com.ordem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import com.sun.xml.bind.CycleRecoverable;
import br.com.contato.model.Contato;
import br.com.contato.model.Endereco;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.funcionario.model.Funcionario;
import br.com.model.Model;
import br.com.ordem.dao.AgendaOrdemFuncionarioDAO;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="agendaordemfuncionario")
public class AgendaOrdemFuncionario extends Model<AgendaOrdemFuncionario> implements Serializable , CycleRecoverable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "ordemidpk", referencedColumnName="id"),
		@JoinColumn(name = "consumersecretpfk", referencedColumnName = "consumerkeyid"),
	})
	private Ordem ordem;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({    
		@JoinColumn( name = "funcioanrioidpk", referencedColumnName="id"),
	})
	private Funcionario funcionario;

	
	public AgendaOrdemFuncionario() {
		super(AgendaOrdemFuncionario.class);
	}
	
	public AgendaOrdemFuncionario(Long id) {
		super(AgendaOrdemFuncionario.class);
		setId(id);
	}
	
	public static String CONSTRUTORL = "id, statusModel, ordem.id, ordem.codigo, ordem.statusModel, ordem.dataAgendamentoInicio,"
			+ "ordem.dataAgendamentoFim, funcionario.id";
	
	public AgendaOrdemFuncionario(Long id, Integer statusModel, Long ordemId, String ordemCodigo, Integer ordemStatusModel, 
			Date ordemDataInicio, Date ordemDataFim, Long funcionarioId) {
		super(AgendaOrdemFuncionario.class);
		setId(id);
		setStatusModel(statusModel);
		this.setOrdem(new Ordem(ordemId, ordemCodigo, ordemStatusModel, ordemDataInicio, ordemDataFim));
		this.setFuncionario(new Funcionario(funcionarioId, null, null));
	}
	
	public static String CONSTRUTOR = "id, ordem.id, ordem.codigo, ordem.assunto, ordem.situacaoOrdem.nome, ordem.clienteObjeto.cliente.razaoNome,"
			+ "ordem.contato.endereco.logradouro,ordem.contato.endereco.latitude, ordem.contato.endereco.longitude";
	
	public AgendaOrdemFuncionario(Long id, Long ordemId, String ordemCodigo, String assunto, String situacaoNome, String clienteRazao,
			String logradouro,Double latitude,Double longitude) {
		super(AgendaOrdemFuncionario.class);
		setId(id);
		this.ordem = new Ordem(ordemId, ordemCodigo, assunto, new Date(), new Date(), 0l, situacaoNome, new Date(), new Date() ,new Date(), 0l, "", 0l, clienteRazao, "");
		Contato contato = new Contato();
		Endereco endereco = new Endereco();
		endereco.setLogradouro(logradouro);
		endereco.setLatitude(latitude);
		endereco.setLongitude(longitude);
		contato.setEndereco(endereco);
		this.ordem.setContato(contato);
	}

	public Ordem getOrdem() {
		return ordem;
	}

	public void setOrdem(Ordem ordem) {
		this.ordem = ordem;
	}

	public Funcionario getFuncionario() {
		if(funcionario == null){
			funcionario = new Funcionario();
		}
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}

	@Override
	public void valida() throws ModelException {
		validador = funcionario.getId()+"_"+ordem.getId();
	}

	public void altera() throws ModelException {
		AgendaOrdemFuncionarioDAO agendaDAO = new AgendaOrdemFuncionarioDAO(br.com.ordem.model.AgendaOrdemFuncionario.class);
		try {
			agendaDAO.altera(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
		
	}
	
	public static ArrayList<AgendaOrdemFuncionario> listaOrdemFuncinario(Long idOrdem, String consumerKey) throws ModelException {
		AgendaOrdemFuncionarioDAO agendaDAO = new AgendaOrdemFuncionarioDAO(br.com.ordem.model.AgendaOrdemFuncionario.class);

		try {
			return agendaDAO.listaOrdemFuncinario(idOrdem, consumerKey);
		} catch (DAOException e) {
			throw new ModelException("not found");
		}
	}

	public static ArrayList<AgendaOrdemFuncionario> listaFuncinarioOrdem(Long idFuncionario,
			Date dataInicio, Date dataFim, String consumerKey) throws ModelException {
		AgendaOrdemFuncionarioDAO agendaDAO = new AgendaOrdemFuncionarioDAO(br.com.ordem.model.AgendaOrdemFuncionario.class);

		ArrayList<AgendaOrdemFuncionario> listaAgendaOrdem = new ArrayList<AgendaOrdemFuncionario>();
		try {
			listaAgendaOrdem = agendaDAO.listaFuncinarioOrdem(idFuncionario,
					dataInicio, dataFim, Long.parseLong(consumerKey));
		} catch (NumberFormatException e) {
			throw new ModelException("not found");
		} catch (DAOException e) {
			throw new ModelException("not found");
		}
	
		return listaAgendaOrdem;
	}
}
