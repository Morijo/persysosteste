package com.persys.osmanager.service.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Servico;
import com.vaadin.data.util.BeanContainer;

public class TransactionsContainerService extends TransactionsContainer<br.com.model.interfaces.IServico>{

	private static final long serialVersionUID = 1L;
	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}
	public TransactionsContainerService() {
		addContainerProperty(bundle.getString("id"), Long.class, -1);
		addContainerProperty(bundle.getString("code"), String.class, "");
		addContainerProperty(bundle.getString("service"), String.class, "");
		addContainerProperty(bundle.getString("situation"), String.class, "");
		addContainerProperty("obj", Object.class, new Object());
	}

	public void addTransaction(br.com.model.interfaces.IServico servico) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(servico,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IServico servico, com.vaadin.data.Item item) {
		if (item != null && servico != null && !servico.getTitulo().isEmpty()) {
			item.getItemProperty(bundle.getString("id")).setValue(servico.getId());
			item.getItemProperty(bundle.getString("code")).setValue(servico.getCodigo());
			item.getItemProperty(bundle.getString("service")).setValue(servico.getTitulo());
			item.getItemProperty(bundle.getString("situation")).setValue(servico.getStatusModel()==1?bundle.getString("active"):bundle.getString("inactive"));
			item.getItemProperty("obj").setValue(servico);
		}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
		try{
			Connection<Servico> lista = Servico.listaAll(client,"/servico",Servico.class);

			for(List<Servico> listaService : lista){
				for(Servico service : listaService){
					addTransaction(service);
				}
			}
		}catch (Exception e) {}
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		removeAllItems();
		ArrayList<br.com.servico.model.Servico> listaService = br.com.servico.model.Servico.pesquisalistaServicoPorConstrutor(client.getOauth().getApiKey());
		for(br.com.model.interfaces.IServico service : listaService){
			addTransaction(service);
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
		beans.addBean(new SearchFormWindow.Parameter("titulo", bundle.getString("servicename"))); 
		beans.addBean(new SearchFormWindow.Parameter("codigo",bundle.getString("code"))); 
		
		return beans;
	}
}
