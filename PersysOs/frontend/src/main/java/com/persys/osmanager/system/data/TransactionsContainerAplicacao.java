package com.persys.osmanager.system.data;

import java.util.ArrayList;
import br.com.usuario.model.AplicacaoAgente;
import com.persys.osmanager.componente.interfaces.ITransactionsContainerData;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.restmb.RestMBClient;
import com.vaadin.data.Item;

public class TransactionsContainerAplicacao extends com.vaadin.data.util.IndexedContainer
implements ITransactionsContainerData{

	private static final long serialVersionUID = 1L;

	public TransactionsContainerAplicacao() {
		addContainerProperty("Id", Long.class, -1);
		addContainerProperty("Nome", String.class, "");
	}

	public void addTransaction(br.com.model.interfaces.IUsuario AplicacaoAgente) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(AplicacaoAgente,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IUsuario AplicacaoAgente, com.vaadin.data.Item item) {
		if (item != null) {
			item.getItemProperty("Id").setValue(AplicacaoAgente.getId());
			item.getItemProperty("Nome").setValue(AplicacaoAgente.getNomeUsuario());
		}
	}

	@Override
	public void loadTableRest(RestMBClient client) {}

	public AplicacaoAgente toModel(Item item){
		AplicacaoAgente aplicacaoAgente = new AplicacaoAgente();
		aplicacaoAgente.setId((Long) item.getItemProperty("Id").getValue());
		return aplicacaoAgente;
	}

	public static ArrayList<AplicacaoAgente> listaAplicacaoAgentes(RestMBClient client){
		return AplicacaoAgente.listaAgentes(client.getOauth().getApiKey());	
	}

	@Override
	public void loadTable(RestMBClient client, TransationsContainerTipo modo) {
		if(modo == TransationsContainerTipo.BD){
			loadTableDataBase(client);
		}
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		removeAllItems();
		for(AplicacaoAgente aplicacaoAgente : listaAplicacaoAgentes(client))
			addTransaction(aplicacaoAgente);
	}
}
