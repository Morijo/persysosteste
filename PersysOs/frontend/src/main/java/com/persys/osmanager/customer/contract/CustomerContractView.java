package com.persys.osmanager.customer.contract;

import com.persys.osmanager.customer.data.TransactionsContainerContractCustomer;
import com.restmb.RestMBClient;
import com.restmb.types.Cliente;
import com.restmb.types.Contrato;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CustomerContractView extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Table tableCustomer = null;
	TransactionsContainerContractCustomer data = new TransactionsContainerContractCustomer();
	Cliente cliente = null;
	RestMBClient client = null;

	public CustomerContractView(RestMBClient client, Cliente cliente) {
		this.cliente = cliente;
		this.client = client;
		setSizeFull();
		setMargin(true);
		modoTabela();
	}


	public void modoTabela(){
		tableCustomer = ContractTable.createTabela(new TextField(), data);
		tableCustomer.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				visualizar(tableCustomer.getItem(tableCustomer.getValue()));
			} 
		});

		addComponent(tableCustomer);
		defaultTable();
	}

	public void visualizar(Object target) {
		try{
			Long id = (Long) ((Item)target).getItemProperty("Id").getValue();
			Contrato contrato = new Contrato();
			contrato = contrato.pesquisa(client, id);
			getUI().addWindow(windowsView(contrato));
		}catch(Exception e){}
	}

	public Window windowsView(Contrato contrato){
		
		final MenuContract menuContrato = new MenuContract(client);
		menuContrato.initData(contrato);
		menuContrato.modoView();

		final Window windows = new Window("Contrato: "+ contrato.getId());
		windows.setModal(true);
		windows.setClosable(true);
		windows.setResizable(false);
		windows.addStyleName("edit-dashboard");
		windows.setHeight("500px");
		windows.setWidth("920px");
		windows.setContent(new VerticalLayout() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				setWidth("900px");
				addComponent(menuContrato);
			}
		});
		return windows;
	}

	public void defaultTable() {
		data.removeAllItems();
		try{
			data.load(client, "/cliente/"+cliente.getId()+"/contrato");
		}catch (Exception e) {

		}
	}

}
