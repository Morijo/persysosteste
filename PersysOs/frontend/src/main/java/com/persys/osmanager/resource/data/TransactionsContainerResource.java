package com.persys.osmanager.resource.data;

import java.util.ArrayList;
import java.util.List;

import br.com.model.StatusModel;

import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Recurso;
import com.vaadin.data.util.BeanContainer;

public class TransactionsContainerResource extends TransactionsContainer<br.com.model.interfaces.IRecurso>{

	private static final long serialVersionUID = 1L;

	public TransactionsContainerResource() {
		addContainerProperty("Id", Long.class, -1);
		addContainerProperty("Código", String.class, "");
		addContainerProperty("Recurso", String.class, "");
		addContainerProperty("Situação", String.class, "");
		addContainerProperty("TipoRecurso", String.class, "");
		addContainerProperty("obj", Object.class, new Object());
	}

	public void addTransaction(br.com.model.interfaces.IRecurso recurso) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(recurso,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IRecurso recurso, com.vaadin.data.Item item) {
		try{
			if (item != null) {
				item.getItemProperty("Id").setValue(recurso.getId());
				item.getItemProperty("Recurso").setValue(recurso.getNome());
				item.getItemProperty("Código").setValue(recurso.getCodigo());
				item.getItemProperty("Situação").setValue(recurso.getStatusModel()==1?"Ativo":"Inativo");
				item.getItemProperty("TipoRecurso").setValue(recurso.getTipoRecurso().toUpperCase());
				item.getItemProperty("obj").setValue(recurso);
			}
		}catch(Exception e){}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
		try{
			Connection<Recurso<?>> lista = Recurso.listaAll(client,"/servico",Recurso.class);

			for(List<Recurso<?>> listaRecurso: lista){
				for(Recurso<?> recurso : listaRecurso){
					addTransaction(recurso);
				}
			}
		}catch (Exception e) {}
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		ArrayList<br.com.recurso.model.Recurso> listaRecurso = br.com.recurso.model.Recurso.listaRecurso(br.com.recurso.model.Recurso.CONSTRUTOR,StatusModel.ATIVO, client.getOauth().getApiKey());
		for(br.com.model.interfaces.IRecurso recurso : listaRecurso){
			addTransaction(recurso);
		}
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
		beans.addBean(new SearchFormWindow.Parameter("nome", "Nome do Recurso")); 
		beans.addBean(new SearchFormWindow.Parameter("codigo","Código")); 
		//beans.addBean(new SearchFormWindow.Parameter("tipo","Tipo do Recurso")); 

		return beans;
	}
}
