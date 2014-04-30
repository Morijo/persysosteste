package com.persys.osmanager.product.data;

import java.util.ArrayList;
import java.util.List;

import br.com.model.StatusModel;
import br.com.model.interfaces.IProduto;

import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.RestMbType;
import com.vaadin.data.util.BeanContainer;

public class TransactionsContainerProduct extends TransactionsContainer<IProduto>{

	private static final long serialVersionUID = 1L;

	public TransactionsContainerProduct() {
		addContainerProperty("Id", Long.class, -1);
		addContainerProperty("Produto", String.class, "");
		addContainerProperty("Código", String.class, "");
		addContainerProperty("Situação", String.class, "");
		addContainerProperty("obj", IProduto.class, "");

	}

	public void addTransaction(br.com.model.interfaces.IProduto produto) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(produto,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IProduto produto, com.vaadin.data.Item item) {
		if (item != null) {
			item.getItemProperty("Id").setValue(produto.getId());
			item.getItemProperty("Produto").setValue(produto.getNome());
			item.getItemProperty("Código").setValue(produto.getCodigo());
			item.getItemProperty("Situação").setValue(produto.getStatusModel()==1 ? "Ativo" : "Inativo");
			item.getItemProperty("obj").setValue(produto);
		}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
		loadRest(client);
	}

	public ArrayList<IProduto> loadRest(RestMBClient client) {
		ArrayList<IProduto> listaRest = new ArrayList<IProduto>();
		try{
			Connection<IProduto> listaConnection = RestMbType.listaAll(client,"/produto",IProduto.class);
			for(List<IProduto> lista : listaConnection){
				for(IProduto produto : lista){
					addTransaction(produto);
				}
			}
		}catch (Exception e) {
			return new ArrayList<IProduto>();
		}
		return listaRest;
	}

	public static ArrayList<br.com.produto.model.Produto> loadDataBase(RestMBClient client) {
		return br.com.produto.model.Produto.listaProduto(client.getOauth().getApiKey(), StatusModel.ATIVO);
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		removeAllItems();
		try{
			for(IProduto produto : loadDataBase(client)){
				addTransaction(produto);
			}
		}catch (Exception e) {}
	}

	@Override
	public void loadTable(RestMBClient client, TransationsContainerTipo modo) {
		if(modo == TransationsContainerTipo.BD){
			loadTableDataBase(client);
		}else{
			loadTableRest(client);
		}
	}
	
	public BeanContainer<Boolean, SearchFormWindow.Parameter> listParameter(){
		BeanContainer<Boolean, SearchFormWindow.Parameter> beans =
				new BeanContainer<Boolean, SearchFormWindow.Parameter>(SearchFormWindow.Parameter.class);
	
		beans.setBeanIdProperty("id");
		beans.addBean(new SearchFormWindow.Parameter("nome", "Nome do Produto")); 
		beans.addBean(new SearchFormWindow.Parameter("codigo","Código")); 
		
		return beans;
	}
}
