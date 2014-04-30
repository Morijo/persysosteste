package br.com.servico.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;

import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.ordem.model.ServicoOrdem;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;
import br.com.rest.resources.exception.PreconditionsREST;
import br.com.servico.dao.ServicoDAO;

import com.sun.xml.bind.CycleRecoverable;
/**
 *  <p> O modelo servico eh composto pelos seguintes campos </p>
 * 	<p>titulo (String) tamanho (100) campo obrigatorio </p>
 *  <p>Descricao (String) tamanho (5000) </p> 
 *  <p>Código (String) tamanho (255) </p> 
 *  <p>ValorServico (BigDecimal) tamanho (19,2) </p> 
 *  <br>Tabela no banco: servico </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see ServicoOrdem
 * @see ServicoProcedimento
 */
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="servico")
public class Servico extends Model<Servico> implements Serializable, CycleRecoverable, br.com.model.interfaces.IServico{
	private final static ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}
	
	public static final String CONSTRUTOR = "id,dataCriacao,dataAlteracao,codigo,titulo,valorServico,statusModel";
	private static final long serialVersionUID = 1L;

	@Column(name = "titulo", length=100)
	private String titulo = null; 

	@Column(name = "descricao", length = 5000)
	private String descricao = null; 
	
	@Column(name = "valor")
	private BigDecimal valorServico = new BigDecimal(0.0);

	@XmlTransient
	@OneToMany(mappedBy = "servico", targetEntity = ServicoOrdem.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<ServicoOrdem> servicoOrdem = new ArrayList<ServicoOrdem>();

	@OneToMany(mappedBy = "servico", targetEntity = ServicoProcedimento.class, fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<ServicoProcedimento> servicoProcedimento = new ArrayList<ServicoProcedimento>();


	public Servico(Long id, Date dataCriacao, Date dataAlteracao, String codigo, String titulo,BigDecimal valorServico, Integer statusModel) {
		super(Servico.class);
		this.setId(id);
		this.setDataCriacao(dataCriacao);
		this.setDataAlteracao(dataAlteracao);
		this.setCodigo(codigo);
		this.setStatusModel(statusModel);
		this.titulo = titulo;
		this.valorServico = valorServico;
	}

	public Servico(Long id) {
		super(Servico.class);
		setId(id);
	}
	
	public Servico() {
		super(Servico.class);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Collection<ServicoOrdem> getServicoOrdem() {
		return servicoOrdem;
	}

	public void setServicoOrdem(Collection<ServicoOrdem> servicoOrdem) {
		this.servicoOrdem = servicoOrdem;
	}
	
	public BigDecimal getValorServico() {
		return valorServico;
	}

	public void setValorServico(BigDecimal valorServico) {
		this.valorServico = valorServico;
	}

	public Collection<ServicoProcedimento> getServicoProcedimento() {
		return servicoProcedimento;
	}

	public void setServicoProcedimento(
			Collection<ServicoProcedimento> servicoProcedimento) {
		this.servicoProcedimento = servicoProcedimento;
	}

	public static List<Servico> listaServicoAtivo(){
		ServicoDAO serviceDAO = new ServicoDAO(Servico.class);
		return  serviceDAO.pesquisaServicosAtivo();
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Servico> pesquisalistaServicoPorConstrutor(String consumerKey){
		Session session = HibernateHelper.openSession(Servico.class);
		Transaction tx = session.beginTransaction();
		try{
			return (ArrayList<Servico>) Servico.pesquisaListaPorConsumerSecret(session, 0, -1, CONSTRUTOR,consumerKey, Servico.class);
		}finally{
			tx.commit();
			session.close();
		}
	}
	/**
	 * 	A busca pode ser realizada por nome, codigo e ser filtrado por statusModel 
	 * @author ricardosabatine, jpmorijo	
	 * @version 1.0.0
	*/
	@SuppressWarnings("unchecked")
	public static ArrayList<Servico> busca(String consumerKey, String nomeServico, String codigo,
			Integer stauts) throws ModelException {

		Session session = HibernateHelper.openSession(Produto.class.getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		
		try{
			ArrayList<Servico> produtoServico = null;

			if(nomeServico.length() >= 3){
		    	parameter.add(ParameterDAO.with("titulo","%"+nomeServico+"%",ParameterDAOHelper.ILIKE));
		    }else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.ILIKE));
		    }else{
		    	PreconditionsREST.error(bundle.getString("invalidcondition"));
		    }
	
			parameter.add(ParameterDAO.with("statusModel",stauts,ParameterDAOHelper.EQ));

			produtoServico =(ArrayList<Servico>) Servico.pesquisaListaPorConsumerSecret(session,Servico.class,consumerKey, parameter);
			tx.commit();

			return produtoServico;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}
	
	@Override
	public void valida() throws ModelException {
		if(this.titulo.length() < 3){
			PreconditionsModel.checkNotNull(null, bundle.getString("invalidservicename"));
		}
		PreconditionsModel.checkNotNull(getConsumerSecret(), bundle.getString("consumersecretnotset"));
		if(getCodigo().isEmpty()){
			setCodigo("PSER"+countPorConsumerSecret(Servico.class,getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
	
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result
				+ ((servicoOrdem == null) ? 0 : servicoOrdem.hashCode());
		result = prime
				* result
				+ ((servicoProcedimento == null) ? 0 : servicoProcedimento
						.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
		Servico other = (Servico) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (servicoOrdem == null) {
			if (other.servicoOrdem != null)
				return false;
		} else if (!servicoOrdem.equals(other.servicoOrdem))
			return false;
		if (servicoProcedimento == null) {
			if (other.servicoProcedimento != null)
				return false;
		} else if (!servicoProcedimento.equals(other.servicoProcedimento))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}
	
	
}
