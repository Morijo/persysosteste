package com.persys.osmanager.customer;

import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.contact.ContactView;
import com.persys.osmanager.customer.contract.CustomerContractView;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.restmb.types.Cliente;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class CustomerMenu extends HorizontalLayout implements IForm<Cliente>{

	private CustomerForm 	clienteForm = null;
	private TabSheet abas;
	
	private Tab contatoTab;
	private Tab contratoTab;
	
	private RestMBClient client = null;

	public CustomerMenu(final RestMBClient client){
		this.client = client;
		setSizeFull();
		
	}

	@Override
	public Cliente commit() throws ViewException {
		try{
			return clienteForm.commit();
		}catch(Exception e){
			throw new ViewException(e.getMessage());
		}	
	}

	@Override
	public void initData(Cliente data) {
		removeAllComponents();
		abas  = null;
		abas = new TabSheet();
		addComponent(abas);
		
		clienteForm = new CustomerForm(client); 
		clienteForm.initData(data);
		
		ContactView contato = new ContactView(client, data);
		contato.modoLivroContato();

		abas.addTab(clienteForm , "Cliente");
		contatoTab = abas.addTab(contato, "Contato");
		contratoTab = abas.addTab(new CustomerContractView(client,data), "Contrato");
	}

	@Override
	public void modoView() {
		contatoTab.setVisible(true);
		contratoTab.setVisible(true);
		clienteForm.modoView();
	}

	@Override
	public void modoAdd() {
		contatoTab.setVisible(false);
		contratoTab.setVisible(false);
		clienteForm.modoAdd();
	}
}
