package com.persys.osmanager.service.data;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import br.com.exception.ModelException;
import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.RestMBClient;
import com.vaadin.data.util.BeanContainer;

public class TransactionsContainerProcedimento extends TransactionsContainer<br.com.model.interfaces.IProcedimento>{

	private static final long serialVersionUID = 1L;
	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}	
	public TransactionsContainerProcedimento() {
		addContainerProperty(bundle.getString("id"), Long.class, -1);
		addContainerProperty(bundle.getString("code"), String.class, "");
		addContainerProperty(bundle.getString("procedure"), String.class, "");
		addContainerProperty(bundle.getString("situation"), String.class, "");
		addContainerProperty("obj", Object.class, new Object());
	}

	public void addTransaction(br.com.model.interfaces.IProcedimento procedimento) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(procedimento,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IProcedimento procedimento, com.vaadin.data.Item item) {
		if (item != null) {
			item.getItemProperty(bundle.getString("id")).setValue(procedimento.getId());
			item.getItemProperty(bundle.getString("procedure")).setValue(procedimento.getTitulo());
			item.getItemProperty(bundle.getString("code")).setValue(procedimento.getCodigo());
			item.getItemProperty(bundle.getString("situation")).setValue(procedimento.getStatusModel()==1?bundle.getString("active"):bundle.getString("inactive"));
			item.getItemProperty("obj").setValue(procedimento);
		}
	}

	@Override
	public void loadTableRest(RestMBClient client) {}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		removeAllItems();
		ArrayList<br.com.servico.model.Procedimento> listaProcedimento;
		try {
			listaProcedimento = br.com.servico.model.Procedimento.pesquisalistaProcedimentoPorConstrutor(client.getOauth().getApiKey());
			for(br.com.model.interfaces.IProcedimento procedimento : listaProcedimento){
				addTransaction(procedimento);
			}
		} catch (ModelException e) {
			e.getMessage();
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
		beans.addBean(new SearchFormWindow.Parameter(bundle.getString("title"), bundle.getString("nameofprocedure"))); 
		beans.addBean(new SearchFormWindow.Parameter(bundle.getString("code"),bundle.getString("code"))); 
		
		return beans;
	}
}
