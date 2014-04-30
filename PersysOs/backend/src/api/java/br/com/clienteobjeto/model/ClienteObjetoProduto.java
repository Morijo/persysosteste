package br.com.clienteobjeto.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.sun.xml.bind.CycleRecoverable;

import br.com.clienteobjeto.dao.ContratoDAO;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.produto.model.Produto;
import br.com.rest.represention.JsonDateAdapter;
import br.com.usuario.model.Usuario;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="clienteobjetoproduto")
public class ClienteObjetoProduto extends Model<ClienteObjetoProduto> implements Serializable , CycleRecoverable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "contratoidpk", referencedColumnName="id"),
	})
	private ClienteObjeto clienteObjeto = null;

	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "produtoidpk", referencedColumnName="id"),
	})
	private Produto produto = null;

	@Column(name = "dataaquisicao")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAquisicao = new Date();

	@OneToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "usuarioidpk")
	@Cascade(CascadeType.PERSIST)
	private Usuario usuario = null;

	public ClienteObjetoProduto(){}

	public ClienteObjetoProduto(Produto produto, Contrato contrato, String status){
		super(ClienteObjetoProduto.class);
		this.produto  = produto;
	}

	public static String CONSTRUTOR = " (id, statusModel, dataAquisicao, clienteObjeto.id, produto.id, produto.nome, produto.codigo, "
			+ "usuario.id, usuario.nomeUsuario, usuario.razaoNome) ";

	public ClienteObjetoProduto(Long id, Integer statusModel, Date dataAquisicao, Long idCliente, Long idProduto, String nomeProduto,
			String codigoProduto, Long idUsuario, String nomeUsuario, String razaoNomeUsuario){
		super(ClienteObjetoProduto.class);
		setId(id);
		setStatusModel(statusModel);
		setDataAquisicao(dataAquisicao);
		setClienteOrdem(new ClienteObjeto(idCliente,null, null));
		setProduto(new Produto(idProduto, codigoProduto, nomeProduto));
		setUsuario(new Usuario(idUsuario, null, razaoNomeUsuario, nomeUsuario));
	}

	public ClienteObjeto getClienteOrdem() {
		return clienteObjeto;
	}

	public void setClienteOrdem(ClienteObjeto clienteOrdem) {
		this.clienteObjeto = clienteOrdem;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Date getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(Date dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public static List<ClienteObjetoProduto> listaContratoProduto(Long clienteObjeto ,String consumerSecret) throws ModelException{
		ContratoDAO contratoDao = new ContratoDAO();
		try {
			return contratoDao.listaContratoProduto(clienteObjeto, consumerSecret);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}	

	public boolean alteraSituacao(String consumerSecret) throws ModelException{
		ContratoDAO contratoDao = new ContratoDAO();
		try {
			return contratoDao.alteraSituacao(this, consumerSecret);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return new ClienteObjetoProduto();  
	}

	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkNotNull(produto, "Produto inválido");
		PreconditionsModel.checkNotNull(getConsumerSecret().getConsumerKey(), "ConsumerKey Inválid");

		if(getCodigo().isEmpty()){
			setCodigo("PCOP"+countPorConsumerSecret(ClienteObjetoProduto.class,getConsumerSecret().getConsumerKey()));
		}
	}


}
