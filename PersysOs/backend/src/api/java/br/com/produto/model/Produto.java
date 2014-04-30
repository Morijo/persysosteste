package br.com.produto.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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

import com.sun.xml.bind.CycleRecoverable;

import br.com.clienteobjeto.model.ClienteObjetoProduto;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.oauth.model.ConsumerSecret;
import br.com.principal.helper.HibernateHelper;
import br.com.produto.dao.ProdutoDAO;

/**
 *  <p> O modelo produto eh composto pelos seguintes campos </p>
 * 	<p>Nome (String) campo obrigatorio, tamanho 100 caracteres </p>
 *  <p>Descricao (String) tamanho 250 </p> 
 *  <p>Marca (String) tamanho 100 </p>
 *  <p>CodigoBarra (String) tamanho 30 </p>
 *  <p>Valor (BigDecimal) tamanho (19,2) </p> 

 *  <p>ContratoProduto Lista de contrato que esse produto está vinculado </p>
 *  <br>Tabela no banco: produto </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see ClienteObjetoProduto
 */
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="produto")
public class Produto extends Model<Produto> implements Serializable, CycleRecoverable, br.com.model.interfaces.IProduto {
	
	private final static ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle.getBundle("com/persys/backend/notification",
				Locale.getDefault());
	}

	public static final String CONSTRUTOR = "id,codigo,nome,valor,statusModel";
	
	private static final long serialVersionUID = 1L;

	@Column(name = "nome", length = 100)
	private String nome = null;

	@Column(name = "descricao", length = 250)
	private String descricao = null;

	@Column(name = "marca",length = 100)
	private String marca = null;

	@Column(name = "codigobarra", length = 30)
	private String codigoBarra = null;

	@Column(name = "valor")
	private BigDecimal valor = new BigDecimal(0.0);

	@XmlTransient
	@OneToMany(mappedBy = "produto", targetEntity = ClienteObjetoProduto.class, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Collection<ClienteObjetoProduto> contratoProduto = new ArrayList<ClienteObjetoProduto>();

	public Produto(){
		super(Produto.class);
	}

	public Produto(Long id, String codigo, String nome){
		super(Produto.class);
		setId(id);
		setCodigo(codigo);
		this.nome = nome;
	}

	public Produto(Long id, String codigo, String nome, BigDecimal valor, Integer statusModel){
		super(Produto.class);
		setId(id);
		setCodigo(codigo);
		setStatusModel(statusModel);
		this.valor = valor;
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}
	
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Collection<ClienteObjetoProduto> getContratoProduto() {
		return contratoProduto;
	}
	public void setContratoProduto(Collection<ClienteObjetoProduto> contratoProduto) {
		this.contratoProduto = contratoProduto;
	}

	public static Produto pesquisaProdutoID(ConsumerSecret consumerKey, Long id){
		Produto produto = null;
		try{
			ProdutoDAO equipamentoDAO = new ProdutoDAO( Produto.class);
			produto = equipamentoDAO.pesquisaProdutoID(consumerKey, id);
			equipamentoDAO = null;
			return produto;
		}
		catch (Exception e) {
			return null;
		}	
	}

	public static ArrayList<Produto> listaProduto(String consumerKey, Integer statusModel){
		try{
			ProdutoDAO produtoDAO = new ProdutoDAO( Produto.class);
			return (ArrayList<Produto>) produtoDAO.listaProduto(Long.parseLong(consumerKey), statusModel);
		}
		catch (Exception e) {
			return new ArrayList<Produto>();
		}	
	}

	/**
	 * 	A busca pode ser realizada por nome, codigo e ser filtrado por statusModel 
	 * @author ricardosabatine, jpmorijo	
	 * @version 1.0.0
	*/
	@SuppressWarnings("unchecked")
	public static ArrayList<Produto> busca(String consumerKey, String nomeProduto, String codigo,
			Integer statusModel) throws ModelException {

		Session session = HibernateHelper.openSession(Produto.class.getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		
		try{
			ArrayList<Produto> produtoLista = null;

			if(nomeProduto.length() >= 3){
				parameter.add(ParameterDAO.with("nome","%"+nomeProduto+"%",ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo","%"+codigo+"%",ParameterDAOHelper.ILIKE));
			}else{
				throw new ModelException(bundle.getString("invalidcondition"));
			}

			parameter.add(ParameterDAO.with("statusModel",statusModel,ParameterDAOHelper.EQ));
			produtoLista =(ArrayList<Produto>) Produto.pesquisaListaPorConsumerSecret(session,Produto.class, consumerKey, parameter);
			tx.commit();
			return produtoLista;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(nome, bundle.getString("invalidproductname"));
		PreconditionsModel.checkNotNull(getConsumerSecret(), bundle.getString("consumersecretnotset"));
		if(getCodigo().isEmpty()){
			setCodigo("PPRO"+countPorConsumerSecret(Produto.class,getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}	
}
