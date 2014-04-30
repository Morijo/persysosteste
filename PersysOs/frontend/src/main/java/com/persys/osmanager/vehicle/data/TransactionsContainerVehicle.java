package com.persys.osmanager.vehicle.data;

import java.util.ArrayList;
import java.util.List;
import br.com.model.interfaces.IVeiculo;
import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.RestMbType;
import com.vaadin.data.util.BeanContainer;

public class TransactionsContainerVehicle extends TransactionsContainer<IVeiculo>{

	private static final long serialVersionUID = 1L;

	public TransactionsContainerVehicle() {
		addContainerProperty("Id", Long.class, -1);
		addContainerProperty("Veiculo", String.class, "");
		addContainerProperty("Código", String.class, "");
		addContainerProperty("Situação", String.class, "");
		addContainerProperty("obj", IVeiculo.class, "");

	}

	public void addTransaction(br.com.model.interfaces.IVeiculo veiculo) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(veiculo,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IVeiculo veiculo, com.vaadin.data.Item item) {
		if (item != null) {
			item.getItemProperty("Id").setValue(veiculo.getId());
			item.getItemProperty("Veiculo").setValue(veiculo.getNome());
			item.getItemProperty("Placa").setValue(veiculo.getPlaca());
			item.getItemProperty("Situação").setValue(veiculo.getStatusModel()==1 ? "Ativo" : "Inativo");
			item.getItemProperty("obj").setValue(veiculo);
		}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
		loadRest(client);
	}

	public ArrayList<IVeiculo> loadRest(RestMBClient client) {
		ArrayList<IVeiculo> listaRest = new ArrayList<IVeiculo>();
		try{
			Connection<IVeiculo> listaConnection = RestMbType.listaAll(client,"/recurso/veiculo",IVeiculo.class);
			for(List<IVeiculo> lista : listaConnection){
				for(IVeiculo Veiculo : lista){
					addTransaction(Veiculo);
				}
			}
		}catch (Exception e) {
			return new ArrayList<IVeiculo>();
		}
		return listaRest;
	}

	public static ArrayList<br.com.frota.model.Veiculo> loadDataBase(RestMBClient client) {
		return br.com.frota.model.Veiculo.lista(client.getOauth().getApiKey());
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		removeAllItems();
		try{
			for(IVeiculo Veiculo : loadDataBase(client)){
				addTransaction(Veiculo);
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
		beans.addBean(new SearchFormWindow.Parameter("nome", "Nome do Veiculo")); 
		beans.addBean(new SearchFormWindow.Parameter("codigo","Código")); 
		
		return beans;
	}
}
