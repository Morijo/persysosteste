package com.persys.osmanager.customer;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.customer.data.TransactionsContainerCustomer;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.restmb.types.Cliente;
import com.vaadin.data.Item;

public class CustomerView extends FormView {

	private static final long serialVersionUID = 1L;

	private final TransactionsContainerCustomer data = new TransactionsContainerCustomer();
	private RestMBClient client = null;
	private CustomerMenu menuCliente = null;
	private Item item = null;
	
	public CustomerView(RestMBClient client){
		this.client = client;
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),"Cliente",false);
		filterTable.setVisibleColumns("Codigo","Nome","Cnpj/Cpf");
	}
	
	@Override
	public void visualizar(Object target) {
		try{
			item = ((Item)target);
			Long id = (Long) item.getItemProperty("Id").getValue();
			Cliente cliente = Cliente.pesquisa(client,"/cliente",Cliente.class, id);
			menuCliente = new CustomerMenu(client);
			menuCliente.initData(cliente);
			modoVisualizarView(menuCliente, cliente.getRazaoNome());
		}catch (Exception e) {}	
	}
	
	@Override
	public void adicionar() {
		menuCliente = new CustomerMenu(client);
		menuCliente.initData(new Cliente());
		modoAdicionarView(menuCliente,"Novo Cliente");
	}

	@Override
	public void remover(Object target) {
		messageSucess("Remover","Deseja remover?","Remover","Cancelar",false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					menuCliente.commit().remover(client);
					notificationTray("Remover", "Sucesso");
					defaultTable();
					voltar();
				}catch(Exception e){
					notificationError("Remover", "Erro " + e.getMessage() );
				}
			}
			@Override
			public void discard() {}

			@Override
			public void cancel() {}
		});
	}
	
	@Override
	public void editar() {
		try{
			menuCliente.commit().alterar(client);
			notificationTray("Editar", "Sucesso");
			defaultTable();
		} catch (ViewException e) {
			notificationTray("Erro editar", e.getMessage());
		}catch (Exception e) {
			notificationTray("Erro editar", e.getMessage());
		}
	}

	@Override
	public void salvar() {
		try{
			Cliente cliente = menuCliente.commit().salvar(client);
			notificationTray("Salvar", "Sucesso");
			data.addTransaction(cliente);
			menuCliente.initData(cliente);
			modoVisualizarView(menuCliente, cliente.getRazaoNome());
		} catch (ViewException e) {
			notificationTray("Erro salvar", e.getMessage());
		}catch (Exception e) {
			notificationTray("Erro salvar", e.getMessage());
		}
	}

	@Override
	public void defaultTable() {
		data.loadTableRest(client);
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable,"Cliente",false);
	}

}
