package br.com.rest.hateoas.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import br.com.funcionario.model.Funcionario;
import br.com.produto.model.Produto;
import br.com.rest.represention.JsonDateAdapter;
import br.com.usuario.model.Usuario;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD)
public class ClienteObjetoProdutoDTO {

	private Long id;
	private int statusModel=0;
	
	private ContratoDTO clienteOrdem = new ContratoDTO();
	
	private Produto produto = new Produto();
	
	@XmlJavaTypeAdapter(value = JsonDateAdapter.class)
	private Date dataAquisicao = null;
	
	private Usuario funcionario = new Funcionario();
	
	public ContratoDTO getClienteOrdem() {
		return clienteOrdem;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public void setStatusModel(int statusModel){
		this.statusModel = statusModel;
	}

	public void setClienteOrdem(Long id) {
		this.clienteOrdem.setId(id);
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

	public Usuario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	public String getFuncionarioNome() {
		return this.funcionario.getRazaoNome();
	}

	public void setFuncionarioNome(String funcionarioNome) {
		this.funcionario.setRazaoNome(funcionarioNome);
	}

	public Long getFuncionarioId() {
		return this.funcionario.getId();
	}

	public void setFuncionarioId(Long funcionarioId) {
		this.funcionario.setId(funcionarioId);
	}

	protected String getProdutoNome() {
		return this.produto.getNome();
	}

	protected void setProdutoNome(String produtoNome) {
		this.produto.setNome(produtoNome);
	}

	protected Long getProdutoId() {
		return this.produto.getId();
	}

	protected void setProdutoId(Long produtoId) {
		this.produto.setId(produtoId);
	}
	
	protected void setProdutoCodigo(String produtoCodigo) {
		this.produto.setCodigo(produtoCodigo);
	}

	@Override
	public String toString() {
		return "ClienteObjetoProdutoDTO [clienteOrdem=" + clienteOrdem
				+ ", produto=" + produto + ", dataAquisicao=" + dataAquisicao
				+ ", funcionario=" + funcionario + "]";
	}
	
	
	
}
