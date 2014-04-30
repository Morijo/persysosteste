package com.persys.osmanager.order.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.model.interfaces.IOrdem;
import br.com.ordem.model.Ordem;

import com.persys.osmanager.componente.SearchFormWindow.Parameter;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.OrdemServico;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;

/**
 * @author ricardosabatine
 *
 */
/**
 * @author ricardosabatine
 *
 */
/**
 * @author ricardosabatine
 *
 */
public class TransactionsContainerOrdemServicoData extends TransactionsContainer<IOrdem> {

	private static final long serialVersionUID = 1L;

	public TransactionsContainerOrdemServicoData() {
		addContainerProperty("ID", Long.class, null);
		addContainerProperty("OS", String.class, null);
		addContainerProperty("Cliente", String.class, null);
		addContainerProperty("Situacao",String.class, null);
		addContainerProperty("DataCriacao", Date.class, null);
		addContainerProperty("DataConclusao", Date.class, null);
		addContainerProperty("Endereco", String.class, null);

	}

	@SuppressWarnings("unchecked")
	public void addTransaction(IOrdem ordemServico) {
		Object id = addItem();
		com.vaadin.data.Item item = getItem(id);
		if (item != null) {
			item.getItemProperty("ID").setValue(ordemServico.getId());
			item.getItemProperty("OS").setValue(ordemServico.getCodigo());

			 if(ordemServico.getClienteObjeto() != null)
			  item.getItemProperty("Cliente").setValue(ordemServico.getClienteObjeto().getCliente().getRazaoNome());

			item.getItemProperty("Situacao").setValue(ordemServico.getSituacaoOrdem().getNome());
			item.getItemProperty("DataCriacao").setValue(ordemServico.getDataCriacao());

			if(ordemServico.getDataConclusao() != null)
				item.getItemProperty("DataConclusao").setValue(ordemServico.getDataConclusao());

			 if(ordemServico.getContato() != null)
				 item.getItemProperty("Endereco").setValue(ordemServico.getContato().getEndereco().toString());

		}
	}

	/**
	 * Lista em um Bean as ordem
	 * @param client
	 * @param resource
	 * @return BeanItemContainer<OrdemServico>
	 */
	public static BeanItemContainer<OrdemServico> loadBean(RestMBClient client, String resource){
		BeanItemContainer<OrdemServico> listaBeans = new BeanItemContainer<OrdemServico>(OrdemServico.class);
		Connection<OrdemServico> lista = OrdemServico.listaAll(client,resource,OrdemServico.class);

		for(List<OrdemServico> listaOrdemServico : lista){
			for(OrdemServico ordemServico : listaOrdemServico){
				listaBeans.addBean(ordemServico);
			}
		}
		return listaBeans;
	}

	@Override
	public void loadTable(RestMBClient client,
			com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo modo) {
	
	}

	public void load(RestMBClient client, String resource) {
		removeAllItems();
		try{
			Connection<OrdemServico> lista = OrdemServico.listaAll(client,resource,OrdemServico.class);
			for(List<OrdemServico> listaOrdemServico : lista){
				for(OrdemServico ordemServico : listaOrdemServico){
					addTransaction(ordemServico);
				}
			}
		}catch(Exception e){}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
		try{
			Connection<OrdemServico> lista = OrdemServico.listaAll(client,"/ordem",OrdemServico.class);
			for(List<OrdemServico> listaOrdemServico : lista){
				for(OrdemServico ordemServico : listaOrdemServico){
					addTransaction(ordemServico);
				}
			}
		}catch(Exception e){}
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		removeAllItems();
		try{
			ArrayList<Ordem> lista = Ordem.listaOrdem(2, client.getOauth().getApiKey());
			for(Ordem ordem : lista){
				addTransaction(ordem);
			}
		}catch(Exception e){}
	}

	@Override
	public BeanContainer<Boolean, Parameter> listParameter() {
		return null;
	}
	
	/**
	  * Retorna os tipos de ordem
	  * Web
	  * Telefone
	  * Balcão
	  * Email
	  * Interno
	 * @return BeanItemContainer<String>
	 */
	public static BeanItemContainer<String> listFonteOrdem(){
		 BeanItemContainer<String> beans =
			        new BeanItemContainer<String>(String.class);
			    
		beans.addBean("Web"); 
		beans.addBean("Telefone"); 
		beans.addBean("Email"); 
		beans.addBean("Balcão"); 
		beans.addBean("Interno"); 
		
		return beans;
	}
}
