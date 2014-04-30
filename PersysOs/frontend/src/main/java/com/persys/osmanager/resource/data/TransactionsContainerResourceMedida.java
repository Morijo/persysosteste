package com.persys.osmanager.resource.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IMedida;

import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Medida;
import com.restmb.types.RestMbType;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 26/02/2013 Container de Dados Para Medida
 */
public class TransactionsContainerResourceMedida extends TransactionsContainer<br.com.model.interfaces.IMedida>{
	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/measure",
				Locale.getDefault());
	}

	private static final long serialVersionUID = 1L;


	public TransactionsContainerResourceMedida() {
		addContainerProperty(bundle.getString("id"), Long.class, -1);
		addContainerProperty(bundle.getString("entity"), String.class, "");
		addContainerProperty(bundle.getString("abbreviation"), String.class, "");
		addContainerProperty(bundle.getString("code"), String.class, "");
		addContainerProperty(bundle.getString("situation"), String.class, "");
		addContainerProperty("obj", Object.class, new Object());
	}

	public void addTransaction(br.com.model.interfaces.IMedida medida) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(medida,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IMedida medida, com.vaadin.data.Item item) {
		try{
			if (item != null) {
				item.getItemProperty(bundle.getString("id")).setValue(medida.getId());
				item.getItemProperty(bundle.getString("entity")).setValue(medida.getNome());
				item.getItemProperty(bundle.getString("abbreviation")).setValue(medida.getAbreviacao());
				item.getItemProperty(bundle.getString("code")).setValue(medida.getCodigo());
				item.getItemProperty(bundle.getString("situation")).setValue(medida.getStatusModel()==1?"Ativo":"Inativo");
				item.getItemProperty("obj").setValue(medida);
			}
		}catch(Exception e){}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		loadRest(client);
	}

	public ArrayList<IMedida> loadRest(RestMBClient client) {
		ArrayList<IMedida> listaRest = new ArrayList<IMedida>();
		try{
			Connection<IMedida> listaConnection = RestMbType.listaAll(client,"/medida",IMedida.class);
			for(List<IMedida> lista : listaConnection){
				for(IMedida medida : lista){
					addTransaction(medida);
				}
			}
		}catch (Exception e) {
			return new ArrayList<IMedida>();
		}
		return listaRest;
	}

	public static ArrayList<br.com.recurso.model.Medida> loadDataBase(RestMBClient client) {
		return br.com.recurso.model.Medida.pesquisalistaMedidaPorConstrutor(client.getOauth().getApiKey());
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		try{
			for(IMedida medida : loadDataBase(client)){
				addTransaction(medida);
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

	public static BeanItemContainer<Medida> listMedida(RestMBClient client){

		BeanItemContainer<Medida> listaBeans = new BeanItemContainer<Medida>(Medida.class);
		try{
			Connection<Medida> lista = Medida.listaMedida(client);
			for(List<Medida> l : lista){
				for(Medida medida : l){
					listaBeans.addBean(medida);
				}
			}
		}catch (Exception e) {
			e.getMessage();
		}
		return listaBeans;
	}
	
	public BeanContainer<Boolean, SearchFormWindow.Parameter> listParameter(){
		return null;
	}
}
