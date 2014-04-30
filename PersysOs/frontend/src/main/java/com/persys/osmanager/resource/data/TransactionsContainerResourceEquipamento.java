package com.persys.osmanager.resource.data;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IEquipamento;

import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.RestMBClient;
import com.vaadin.data.util.BeanContainer;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 26/02/2013 Container de Dados Para Equipamento
 */
public class TransactionsContainerResourceEquipamento extends TransactionsContainer<br.com.model.interfaces.IEquipamento>{
	
	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/equipment",
				Locale.getDefault());
	}
	
	private static final long serialVersionUID = 1L;
	
	
	public TransactionsContainerResourceEquipamento() {
    	addContainerProperty(bundle.getString("id"), Long.class, -1);
        addContainerProperty(bundle.getString("entity"), String.class, "");
        addContainerProperty(bundle.getString("code"), String.class, "");
		addContainerProperty(bundle.getString("situation"), String.class, "");
		addContainerProperty("obj", Object.class, new Object());
	}

	public void addTransaction(br.com.model.interfaces.IEquipamento equipamento) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(equipamento,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IEquipamento equipamento, com.vaadin.data.Item item) {
		try{
			if (item != null) {
				item.getItemProperty(bundle.getString("id")).setValue(equipamento.getId());
                item.getItemProperty(bundle.getString("entity")).setValue(equipamento.getEquipamento());
                item.getItemProperty(bundle.getString("code")).setValue(equipamento.getCodigo());
				item.getItemProperty(bundle.getString("situation")).setValue(equipamento.getStatusModel()==1?"Ativo":"Inativo");
				item.getItemProperty("obj").setValue(equipamento);
			}
		}catch(Exception e){}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
	}

	public static ArrayList<br.com.recurso.model.Equipamento> loadDataBase(RestMBClient client) {
		return br.com.recurso.model.Equipamento.pesquisalistaEquipamentoPorConstrutor(client.getOauth().getApiKey());
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		try{
			for(IEquipamento equipamento : loadDataBase(client)){
				addTransaction(equipamento);
			}
		}catch (Exception e) {}
	}

	@Override
	public void loadTable(RestMBClient client, TransationsContainerTipo modo) {
		removeAllItems();
		if(modo == TransationsContainerTipo.BD){
			loadTableDataBase(client);
		}else{
			loadTableRest(client);
		}
	}

	public BeanContainer<Boolean, SearchFormWindow.Parameter> listParameter(){
		BeanContainer<Boolean, SearchFormWindow.Parameter> beans =
				new BeanContainer<Boolean, SearchFormWindow.Parameter>(SearchFormWindow.Parameter.class);
	
		beans.setBeanIdProperty(bundle.getString("id"));
		beans.addBean(new SearchFormWindow.Parameter("nome", bundle.getString("equipmentname"))); 
		beans.addBean(new SearchFormWindow.Parameter("numeroSerie", bundle.getString("serialnumber"))); 
		beans.addBean(new SearchFormWindow.Parameter("codigo",bundle.getString("code"))); 

		return beans;
	}

}