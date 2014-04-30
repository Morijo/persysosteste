package com.persys.osmanager.organization.data;

import java.util.List;

import br.com.model.interfaces.IDepartamento;

import com.persys.osmanager.componente.interfaces.ITransactionsContainerData;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.exception.ViewException;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Departamento;
import com.vaadin.data.util.BeanItemContainer;


public class TransactionsContainerDepartamento extends com.vaadin.data.util.IndexedContainer
implements ITransactionsContainerData {

	private static final long serialVersionUID = 1L;

	public TransactionsContainerDepartamento(){
		addContainerProperty("Id", Long.class, -1);
		addContainerProperty("Nome", String.class, "");
		addContainerProperty("Tipo", String.class, "");
		addContainerProperty("Unidade", String.class, "");
		addContainerProperty("Telefone", String.class, "");
		addContainerProperty("Codigo", String.class, "");
	}

	@SuppressWarnings("unchecked")
	public void addTransaction(IDepartamento departamento) {
		Object id = addItem();
		com.vaadin.data.Item item = getItem(id);
		if (item != null) {
			item.getItemProperty("Id").setValue(departamento.getId());
			item.getItemProperty("Nome").setValue(departamento.getNomeDepartamento());
			item.getItemProperty("Tipo").setValue(departamento.getTipo());
			item.getItemProperty("Unidade").setValue(departamento.getUnidade().getNome());
			item.getItemProperty("Telefone").setValue(departamento.getTelefone());
			item.getItemProperty("Codigo").setValue(departamento.getCodigo());
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

	@Override
	public void loadTable(RestMBClient client, TransationsContainerTipo modo) throws ViewException {}

	@Override
	public void loadTableRest(RestMBClient client) throws ViewException {
		removeAllItems();
		try{
			Connection<IDepartamento> lista = Departamento.listaAll(client,"/departamento", Departamento.class);
			for(List<IDepartamento> listaDepartamento : lista){
				for(IDepartamento departamento : listaDepartamento){
					addTransaction(departamento);
				}
			}
		}catch(Exception e){
			throw new ViewException("Sem Departamento");
		}

	}

	@Override
	public void loadTableDataBase(RestMBClient client) {}
	
	 /**
	  * Retorna os tipos de departamentos
	  * Externo
	  * Interno
	 * @return BeanItemContainer<String>
	 */
	 public static BeanItemContainer<String> listDepartmentTypes(){
			BeanItemContainer<String> beans =
				        new BeanItemContainer<String>(String.class);
				    
			beans.addBean("Externo"); 
			beans.addBean("Interno"); 
			 
			return beans;
		}
}