package com.restmb.types;

import java.util.Date;

import com.restmb.RestMB;
import com.restmb.RestMBClient;



public class ClienteObjetoProduto extends RestMbType<ClienteObjetoProduto> {

	@RestMB
	private ClienteObjeto<?> clienteOrdem = null;
	@RestMB
	private Produto produto = null;
	@RestMB
	private Date dataAquisicao = null;
	@RestMB
	private Usuario<?> usuario = null;

	public ClienteObjetoProduto(){
		super("/contrato/produto",ClienteObjetoProduto.class);
	}
	
	public ClienteObjeto<?> getClienteOrdem() {
		return clienteOrdem;
	}

	public void setClienteOrdem(ClienteObjeto<?> clienteOrdem) {
		this.clienteOrdem = clienteOrdem;
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

	public Usuario<?> getFuncionario() {
		return usuario;
	}

	public void setFuncionario(Funcionario usuarioFuncionario) {
		this.usuario = usuarioFuncionario;
	}
	
	public static ClienteObjetoProduto getProduto(RestMBClient client, Long  idClienteObjetoProduto){
		return client.fetchObject("/contrato/produto/"+idClienteObjetoProduto,ClienteObjetoProduto.class);
	}
	
	public static boolean deleta(RestMBClient client, Long  idClienteObjetoProduto){
		return 	ClienteObjetoProduto.deletar(client,"/contrato/produto", idClienteObjetoProduto);
	}
}
