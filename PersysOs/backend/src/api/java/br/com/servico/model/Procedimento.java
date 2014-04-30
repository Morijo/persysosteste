package br.com.servico.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sun.xml.bind.CycleRecoverable;

import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IProcedimento;
import br.com.ordem.model.ServicoOrdem;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.model.Produto;

/**
 *  <p> O modelo Procedimento eh composto pelos seguintes campos </p>
 * 	<p>titulo (String) tamanho (100) campo obrigatorio </p>
 *  <p>nota(descricao) (String) tamanho (1000) </p> 
 *  <p>Código (String) tamanho (255) </p> 
 *  <br>Tabela no banco: procedimento </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see ServicoOrdem
 * @see ServicoProcedimento
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="procedimento")
public class Procedimento extends Model<Procedimento> implements Serializable , CycleRecoverable, IProcedimento  {
	private final static ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}
	
	public static final String CONSTRUTOR = "id,dataCriacao,dataAlteracao,codigo,titulo,descricao,statusModel";
	
	private static final long serialVersionUID = 1L;

	@Column(name = "titulo", length=100)
	private String titulo = null; 
	
	@Column(name = "nota", length = 1000)
	private String descricao = null; 
	
	public Procedimento(Long id, Date dataCriacao, Date dataAlteracao,String codigo, String titulo,
			String descricao, int statusModel) {
		super();
		setId(id);
		setDataCriacao(dataCriacao);
		setDataAlteracao(dataAlteracao);
		setCodigo(codigo);
		this.titulo = titulo;
		this.descricao = descricao;
		setStatusModel(statusModel);
	}
	
	public Procedimento(Long id, String titulo, String codigo) {
		super();
		setId(id);
		setTitulo(titulo);
		setCodigo(codigo);
	}

	public Procedimento() {
		super();
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
	
	@Override
	public Object onCycleDetected(Context arg0) {
		Procedimento procedimento = new Procedimento();
		return procedimento;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Procedimento> pesquisalistaProcedimentoPorConstrutor(String consumerKey) throws ModelException{
		Session session = HibernateHelper.openSession(Procedimento.class);
		Transaction tx = session.beginTransaction();
		try{
			return (ArrayList<Procedimento>) Procedimento.pesquisaListaPorConsumerSecret(session, 0, -1, CONSTRUTOR, consumerKey, Procedimento.class);
		}catch(Exception e){
			throw new ModelException(e.getMessage());
		}
		finally{
			tx.commit();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Procedimento> busca(String consumerSecret, String nomeTitulo, String codigo,
			Integer statusModel) throws ModelException {

		Session session = HibernateHelper.openSession(Produto.class.getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		
		try{
			ArrayList<Procedimento> servicoProcedimentoList = null;

			if(nomeTitulo.length() >= 3){
		    	parameter.add(ParameterDAO.with("titulo","%"+nomeTitulo+"%",ParameterDAOHelper.ILIKE));
		    }if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.ILIKE));
		    }
			parameter.add(ParameterDAO.with("statusModel",statusModel,ParameterDAOHelper.EQ));

			servicoProcedimentoList =(ArrayList<Procedimento>) Procedimento.pesquisaListaPorConsumerSecret(session,Procedimento.class,consumerSecret, parameter);
			tx.commit();

			return servicoProcedimentoList;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(titulo, bundle.getString("invalidprocedure"));
		PreconditionsModel.checkNotNull(getConsumerSecret(), bundle.getString("consumersecretnotset"));
		if(getCodigo().isEmpty()){
			setCodigo("PPROC"+countPorConsumerSecret(Procedimento.class,getConsumerSecret().getConsumerKey()));
		}
	}
}
