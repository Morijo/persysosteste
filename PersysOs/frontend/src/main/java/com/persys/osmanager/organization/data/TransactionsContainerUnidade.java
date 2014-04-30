package com.persys.osmanager.organization.data;

import java.util.List;

import br.com.model.interfaces.IUnidade;

import com.persys.osmanager.componente.interfaces.ITransactionsContainerData;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.exception.ViewException;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Unidade;
import com.vaadin.data.util.BeanItemContainer;

public class TransactionsContainerUnidade extends com.vaadin.data.util.IndexedContainer
implements ITransactionsContainerData{

	private static final long serialVersionUID = 1L;

	public TransactionsContainerUnidade() {
		addContainerProperty("Id", Long.class, -1);
		addContainerProperty("Unidade", String.class, "");
		addContainerProperty("Cidade", String.class, "");
		addContainerProperty("Email", String.class, "");
		addContainerProperty("Codigo", String.class, "");
		addContainerProperty("Situação", String.class, "");

	}

	public void addTransaction(IUnidade unidade) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(unidade,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(IUnidade unidade, com.vaadin.data.Item item) {
		if (item != null) {
			item.getItemProperty("Id").setValue(unidade.getId());
			item.getItemProperty("Unidade").setValue(unidade.getNome());
			if(unidade.getEndereco() != null)
			item.getItemProperty("Cidade").setValue(unidade.getEndereco().getCidade());
			item.getItemProperty("Email").setValue(unidade.getEmail());
			item.getItemProperty("Codigo").setValue(unidade.getCodigo());
			if(unidade.getStatusModel() != null)
			item.getItemProperty("Situação").setValue(unidade.getStatusModel()==1 ? "Ativo" : "Inativo");
		}
	}

	@Override
	public void loadTableRest(RestMBClient client) throws ViewException {
		removeAllItems();
		try{
			Connection<Unidade> lista = Unidade.listaUnidadeHome(client);
			for(List<Unidade> l : lista){
				for(Unidade uni : l){
					addTransaction(uni);
				}
			}
		}catch (Exception e) {
			throw new ViewException("Sem Unidade");
		}
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		removeAllItems();
	}

	@Override
	public void loadTable(RestMBClient client, TransationsContainerTipo modo) throws ViewException {
		if(modo == TransationsContainerTipo.BD){
			loadTableDataBase(client);
		}else{
			try {
				loadTableRest(client);
			} catch (ViewException e) {
				throw new ViewException(e.getMessage());
			}
		}
	}

	public static BeanItemContainer<Unidade> listUnidade(RestMBClient client){

		BeanItemContainer<Unidade> listaBeans = new BeanItemContainer<Unidade>(Unidade.class);
		try{
			Connection<Unidade> lista = Unidade.listaUnidadeHome(client);
			for(List<Unidade> l : lista){
				for(Unidade uni : l){
					listaBeans.addBean(uni);
				}
			}
		}catch (Exception e) {}
		return listaBeans;
	}
}





