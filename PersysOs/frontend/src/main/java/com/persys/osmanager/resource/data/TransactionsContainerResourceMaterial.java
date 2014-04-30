package com.persys.osmanager.resource.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import br.com.model.interfaces.IMaterial;
import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.RestMbType;
import com.vaadin.data.util.BeanContainer;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 26/02/2013 Container de Dados Para Material
 */
public class TransactionsContainerResourceMaterial extends TransactionsContainer<br.com.model.interfaces.IMaterial>{
		private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/material",
				Locale.getDefault());
	}

	private static final long serialVersionUID = 1L;

	public TransactionsContainerResourceMaterial() {

	    addContainerProperty(bundle.getString("id"), Long.class, -1);
		addContainerProperty(bundle.getString("entity"), String.class, "");
		addContainerProperty(bundle.getString("measure"), String.class, "");
		addContainerProperty(bundle.getString("code"), String.class, "");
		addContainerProperty(bundle.getString("situation"), String.class, "");
		addContainerProperty("obj", Object.class, new Object());
}
	
	@Override
	public void addTransaction(br.com.model.interfaces.IMaterial material) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(material,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IMaterial material, com.vaadin.data.Item item) {
		try{
			if (item != null) {
				item.getItemProperty(bundle.getString("id")).setValue(material.getId());
				item.getItemProperty(bundle.getString("entity")).setValue(material.getMaterial());
				item.getItemProperty(bundle.getString("measure")).setValue(material.getMedida().getNome());
				item.getItemProperty(bundle.getString("code")).setValue(material.getCodigo());
				item.getItemProperty(bundle.getString("situation")).setValue(material.getStatusModel()==1?"Ativo":"Inativo");
				item.getItemProperty("obj").setValue(material);
	

			}
		}catch(Exception e){}
	}
	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
		loadRest(client);
	}

	public ArrayList<IMaterial> loadRest(RestMBClient client) {
		ArrayList<IMaterial> listaRest = new ArrayList<IMaterial>();
		try{
			Connection<IMaterial> listaConnection = RestMbType.listaAll(client,"/material",IMaterial.class);
			for(List<IMaterial> lista : listaConnection){
				for(IMaterial material : lista){
					addTransaction(material);
				}
			}
		}catch (Exception e) {
			return new ArrayList<IMaterial>();
		}
		return listaRest;
	}

	public static ArrayList<br.com.recurso.model.Material> loadDataBase(RestMBClient client) {
		return br.com.recurso.model.Material.pesquisalistaMaterialPorConstrutor(client.getOauth().getApiKey());
	}
	
	@Override
	public void loadTableDataBase(RestMBClient client) {
		try{
			for(IMaterial material : loadDataBase(client)){
				addTransaction(material);
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
	
		beans.setBeanIdProperty("id");
		beans.addBean(new SearchFormWindow.Parameter("material",bundle.getString("namematerial"))); 
		beans.addBean(new SearchFormWindow.Parameter("codigo",bundle.getString("code"))); 
		
		return beans;
	}
}
