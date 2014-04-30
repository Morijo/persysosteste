package com.persys.osmanager.resource.data;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import br.com.model.StatusModel;
import br.com.model.interfaces.IChip;
import com.google.gwt.user.client.ui.CheckBox;
import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.RestMBClient;
import com.vaadin.data.util.BeanContainer;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 26/02/2013 Container de Dados Para Chip
 */
public class TransactionsContainerResourceChip extends TransactionsContainer<br.com.model.interfaces.IChip>{

	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/chip",
				Locale.getDefault());
	}

	private static final long serialVersionUID = 1L;

	public TransactionsContainerResourceChip() {
		addContainerProperty(bundle.getString("id"), Long.class, -1);
        addContainerProperty(bundle.getString("operator"), String.class, "");
        addContainerProperty(bundle.getString("number"), Long.class, "");
        addContainerProperty(bundle.getString("main"), CheckBox.class, "");
        addContainerProperty(bundle.getString("situation"), String.class, "");
        addContainerProperty("obj", Object.class, new Object());
	}

	@Override
	public void addTransaction(IChip model) {
	}
	
	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IChip chip, com.vaadin.data.Item item) {
		try{
			if (item != null) {
		    	item.getItemProperty(bundle.getString("id")).setValue(chip.getId());
                item.getItemProperty(bundle.getString("number")).setValue(chip.getDddNumero());
                item.getItemProperty(bundle.getString("operator")).setValue(chip.getOperadora());
                item.getItemProperty(bundle.getString("main")).setValue(chip.getPrincipal());
                item.getItemProperty(bundle.getString("situation")).setValue(chip.getStatusModel()==1?"Ativo":"Inativo");
                item.getItemProperty("obj").setValue(chip);

			}
		}catch(Exception e){}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		ArrayList<br.com.recurso.model.Chip> listaChip = br.com.recurso.model.Chip.listaChip(br.com.recurso.model.Chip.CONSTRUTOR,StatusModel.ATIVO);
		for(br.com.model.interfaces.IChip chip : listaChip){
		addTransaction(chip);
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

		beans.setBeanIdProperty(bundle.getString("id"));
		beans.addBean(new SearchFormWindow.Parameter("nome",bundle.getString("cellnumber"))); 
		beans.addBean(new SearchFormWindow.Parameter("codigo",bundle.getString("code"))); 
	
		return beans;
	}

}
