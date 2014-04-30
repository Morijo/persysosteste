package com.persys.osmanager.customer.data;

import java.util.ArrayList;
import java.util.List;

import br.com.exception.ServiceException;

import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.Parameter;
import com.restmb.RestMBClient;
import com.restmb.types.Cliente;
import com.restmb.types.RestMbType;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;

public class TransactionsContainerCustomer extends TransactionsContainer<br.com.model.interfaces.ICliente>{

	private static final long serialVersionUID = 1L;
	public TransactionsContainerCustomer() {
		addContainerProperty("Id", Long.class, -1);
		addContainerProperty("Nome", String.class, "");
		addContainerProperty("Codigo", String.class, "");
		addContainerProperty("Cnpj/Cpf", String.class, "");
		addContainerProperty("obj", Object.class, new Object());
	}

	public void addTransaction(br.com.model.interfaces.ICliente cliente) {
		Object idItem = addItem();
		com.vaadin.data.Item item = getItem(idItem);
		setItemProperty(cliente, item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.ICliente cliente, com.vaadin.data.Item item) {
		if (item != null) {
			item.getItemProperty("Id").setValue(cliente.getId());
			item.getItemProperty("Nome").setValue(cliente.getRazaoNome());
			item.getItemProperty("Codigo").setValue(cliente.getCodigo());
			item.getItemProperty("Cnpj/Cpf").setValue(cliente.getCnpjCpf());
			item.getItemProperty("obj").setValue(cliente);
		}
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
		try{
			loadRest(client);
		}catch (Exception e) {}
	}

	public ArrayList<Cliente> loadRest(RestMBClient client) {
		ArrayList<Cliente> listaRest = new ArrayList<Cliente>();
		try{
			Connection<Cliente> listaConnection = Cliente.listaAll(client,"/cliente",Cliente.class);
			for(List<Cliente> lista : listaConnection){
				for(Cliente cliente : lista){
					addTransaction(cliente);
				}
			}
		}catch (Exception e) {
			return new ArrayList<Cliente>();
		}
		return listaRest;
	}

	public static ArrayList<Cliente> loadDataBase() {
		return new ArrayList<Cliente>();
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		removeAllItems();
		try{
			for(Cliente cliente : loadDataBase()){
				addTransaction(cliente);
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
	
	public void searchRest(RestMBClient client, String termo, String parametro) throws ServiceException{
		removeAllItems();
		if(!termo.isEmpty())
			try{
				Connection<Cliente> listaBusca = RestMbType.listaAll(client,"/cliente/busca", Cliente.class, Parameter.with(parametro, termo));
				for(List<Cliente> listaCliente : listaBusca){
					for(Cliente cliente : listaCliente){
						addTransaction(cliente);
					}
				}
			}catch (Exception e) {
				throw new ServiceException("Nenhum Resultado encontrado");
			}
	}
	
	public BeanContainer<Boolean, SearchFormWindow.Parameter> listParameter(){
		BeanContainer<Boolean, com.persys.osmanager.componente.SearchFormWindow.Parameter> beans =
				new BeanContainer<Boolean, SearchFormWindow.Parameter>(SearchFormWindow.Parameter.class);
	
		beans.setBeanIdProperty("id");
		beans.addBean(new SearchFormWindow.Parameter("razao", "Razão Social/Nome")); 
		beans.addBean(new SearchFormWindow.Parameter("codigo","Código")); 
		beans.addBean(new SearchFormWindow.Parameter("cnpjCpf","Cnjp/Cpf")); 
		
		return beans;
	}
	
	 /**
	  * Retorna os tipos de cliente
	  * Físico
	  * Jurídico
	 * @return BeanItemContainer<String>
	 */
	public static BeanItemContainer<String> listStatusCustomer(){
		 BeanItemContainer<String> beans =
			        new BeanItemContainer<String>(String.class);
			    
		beans.addBean("Físico"); 
		beans.addBean("Jurídico"); 
		
		return beans;
	}

}

