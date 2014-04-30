package com.persys.osmanager.organization.data;

import java.util.List;

import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Departamento;
import com.vaadin.data.util.BeanItemContainer;


public class TransactionsContainerDepartamentoData extends com.vaadin.data.util.IndexedContainer {

	private static final long serialVersionUID = 1L;

	public TransactionsContainerDepartamentoData(){
		addContainerProperty("ID", Long.class, -1);
		addContainerProperty("Nome", String.class, "");
		addContainerProperty("Codigo", String.class, "");
	}

	@SuppressWarnings("unchecked")
	public void addTransaction(Departamento situacaoOrdem) {
		Object id = addItem();
		com.vaadin.data.Item item = getItem(id);
		if (item != null) {
			item.getItemProperty("ID").setValue(situacaoOrdem.getId());
			item.getItemProperty("Nome").setValue(situacaoOrdem.getNomeDepartamento());
			item.getItemProperty("Codigo").setValue(situacaoOrdem.getCodigo());
		}
	}

	public void load(RestMBClient client, String resource){
		@SuppressWarnings("unchecked")
		Connection<Departamento> lista = Departamento.listaAll(client,resource,Departamento.class);

		for(List<Departamento> listaDepartamento : lista){
			for(Departamento situacaoOrdem : listaDepartamento){
				addTransaction(situacaoOrdem);
			}
		}
	}

	/**
	 * Lista em um Bean com os departamentos de uma unidade
	 * @param client
	 * @param resource
	 * @return BeanItemContainer<Departamento>
	 */
	public static BeanItemContainer<Departamento> loadBean(RestMBClient client, String resource){
		BeanItemContainer<Departamento> listaBeans = new BeanItemContainer<Departamento>(Departamento.class);

		try{
			Connection<Departamento> lista = Departamento.listaAll(client,resource,Departamento.class);

			for(List<Departamento> listaDepartamento : lista){
				for(Departamento departamento : listaDepartamento){
					listaBeans.addBean(departamento);
				}
			}
		}catch (Exception e) {
			return listaBeans;
		}
		return listaBeans;
	}
}