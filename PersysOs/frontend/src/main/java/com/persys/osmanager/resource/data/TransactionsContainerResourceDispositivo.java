package com.persys.osmanager.resource.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Dispositivo;
import com.vaadin.data.util.BeanContainer;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 26/02/2013 Container de Dados Para Dispositivo
 */
public class TransactionsContainerResourceDispositivo extends TransactionsContainer<br.com.model.interfaces.IDispositivo>{

	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/device",
				Locale.getDefault());
	}

	private static final long serialVersionUID = 1L;

	public TransactionsContainerResourceDispositivo() {

		addContainerProperty(bundle.getString("id"), Long.class, -1);
		addContainerProperty(bundle.getString("imei"), String.class, "");
		addContainerProperty(bundle.getString("code"), String.class, "");
		addContainerProperty(bundle.getString("situation"), String.class, "");
		addContainerProperty("obj", Object.class, new Object());
	}

	public void addTransaction(br.com.model.interfaces.IDispositivo dispositivo) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(dispositivo,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IDispositivo dispositivo, com.vaadin.data.Item item) {
		try{
			if (item != null) {
				item.getItemProperty(bundle.getString("id")).setValue(dispositivo.getId());
				item.getItemProperty(bundle.getString("imei")).setValue(dispositivo.getIMEI());
				item.getItemProperty(bundle.getString("code")).setValue(dispositivo.getCodigo());
				item.getItemProperty(bundle.getString("situation")).setValue(dispositivo.getStatusModel()==1?"Ativo":"Inativo");
				item.getItemProperty("obj").setValue(dispositivo);

			}
		}catch(Exception e){}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
		try{
			Connection<Dispositivo> lista = Dispositivo.listaAll(client,"/dispositivo",Dispositivo.class);

			for(List<Dispositivo> listaDispositivo: lista){
				for(Dispositivo dispositivo : listaDispositivo){
					addTransaction(dispositivo);
				}
			}
		}catch (Exception e) {}
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		ArrayList<br.com.recurso.model.Dispositivo> listaDispositivo = br.com.recurso.model.Dispositivo.listaDispositivoPorConstrutor(client.getOauth().getApiKey());
		for(br.com.model.interfaces.IDispositivo dispositivo : listaDispositivo){
			addTransaction(dispositivo);
		}
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
		beans.addBean(new SearchFormWindow.Parameter("IMEI",bundle.getString("imei"))); 
		beans.addBean(new SearchFormWindow.Parameter("codigo",bundle.getString("code"))); 

		return beans;
	}

}
