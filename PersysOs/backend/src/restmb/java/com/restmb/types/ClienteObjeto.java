package com.restmb.types;

import java.math.BigDecimal;
import java.util.List;

import br.com.model.interfaces.ICliente;
import br.com.model.interfaces.IClienteObjeto;

import com.restmb.DefaultJsonMapper;
import com.restmb.RestMB;
import com.restmb.RestMBClient;
import com.restmb.oauth.service.ParameterList;

public class ClienteObjeto<T> extends RestMbType<T> implements IClienteObjeto{

	@RestMB("descricaoObjeto")
	private String descricaoObjeto ="";
	
	@RestMB("valorTotal")
	private BigDecimal valorTotal = new BigDecimal("0.00");

	@RestMB("valorMensal")
	private BigDecimal valorMensal = new BigDecimal("0.00");
	
	@RestMB
	private Cliente cliente = null;
	
	@RestMB
	private Unidade unidadeGestora = null;

	@RestMB
	private List<ClienteObjetoProduto> contratoProduto = null;

	@RestMB ("situacao")  //vencido, suspenso, cancelado, car��ncia, normal, especial,encerrado
	private String situacao = "";
	
	@RestMB ("motivaEncerramento")
	private String motivoEncerramento = null;
	
	public ClienteObjeto() {}
	 
	public ClienteObjeto(String resourcePath, Class<T> paClass) {
		super(resourcePath, paClass);
	}
	public String getDescricaoObjeto() {
		return descricaoObjeto;
	}

	public void setDescricaoObjeto(String descricaoObjeto) {
		this.descricaoObjeto = descricaoObjeto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorMensal() {
		return valorMensal;
	}

	public void setValorMensal(BigDecimal valorMensal) {
		this.valorMensal = valorMensal;
	}

	public ICliente getCliente() {
		return cliente;
	}

	public void setCliente(ICliente cliente) {
		this.cliente = (Cliente) cliente;
	}

	public Unidade getUnidadeGestora() {
		return unidadeGestora;
	}

	public void setUnidadeGestora(Unidade unidadeGestora) {
		this.unidadeGestora = unidadeGestora;
	}

	public List<ClienteObjetoProduto> getContratoProduto() {
		return contratoProduto;
	}

	public void setContratoProduto(List<ClienteObjetoProduto> contratoProduto) {
		this.contratoProduto = contratoProduto;
	}
	
	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getMotivoEncerramento() {
		return motivoEncerramento;
	}

	public void setMotivoEncerramento(String motivoEncerramento) {
		this.motivoEncerramento = motivoEncerramento;
	}
	
	@Override
	public void setValorTotal(String valorTotal) {}

	@Override
	public void setValorMensal(String valorMensal) {}

	public ClienteObjetoProduto adicionaProduto(RestMBClient client, ClienteObjetoProduto clienteOrdem){
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publish("/contrato/"+getId()+"/produto",ClienteObjetoProduto.class,json.toJson(clienteOrdem),headers);
		
	}
	
	public static ClienteObjetoProduto alterarProduto(RestMBClient client,ClienteObjetoProduto clienteOrdem, Long  idClienteObjetoProduto){
		DefaultJsonMapper json = new DefaultJsonMapper();
		com.restmb.oauth.service.ParameterList headers = new ParameterList();
		headers.add("Content-Type", "application/json");
		return client.publishChanges("/contrato/produto/"+idClienteObjetoProduto,ClienteObjetoProduto.class,json.toJson(clienteOrdem),headers);
		
	}
	
	@Override
	public String toString() {
		return getCodigo();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		ClienteObjeto<?> other = (ClienteObjeto<?>) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		return true;
	}
}
