package com.persys.osmanager.customer.contract;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.customer.data.TransactionsContainerContractCustomer;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.restmb.types.Contrato;
import com.vaadin.data.Item;

public class ContractView extends FormView {

	private static final long serialVersionUID = 1L;

	private final TransactionsContainerContractCustomer data = new TransactionsContainerContractCustomer();
	private RestMBClient client = null;
	private ContractForm contractForm = null;
	private Item item;
	
	public ContractView(RestMBClient client) {
		this.client = client;
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),"Contrato",false);
	}

	@Override
	public void editar() {
		try{
			contractForm.commit().alterar(client);
			notificationTray("Editar", "Sucesso");
			voltar();
		} catch (ViewException e) {
			notificationTray("Erro editar", e.getMessage());
		}catch (Exception e) {
			notificationTray("Erro editar", e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		contractForm = new ContractForm(client);
		contractForm.initData(new Contrato());
		contractForm.modoAdd();
		modoAdicionarView(contractForm,"Novo Contrato");
	}

	@Override
	public void remover(Object target) {
		messageSucess("Remover","Deseja remover?","Remover","Cancelar",false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					contractForm.commit().remover(client);
					notificationTray("Remover", "Sucesso");
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
	public void salvar() {
		try{
			contractForm.commit().salvar(client);
			notificationTray("Salvar", "Sucesso");
			contractForm.modoView();
		} catch (ViewException e) {
			notificationTray("Erro salvar", e.getMessage());
		}catch (Exception e) {
			notificationTray("Erro salvar", e.getMessage());
		}	
	}

	@Override
	public void visualizar(Object target) {
		item = ((Item)target);
		Long id = (Long) item.getItemProperty("Id").getValue();
		Contrato contrato = new Contrato();
		contrato = contrato.pesquisa(client, id);
		contractForm = new ContractForm(client);
		contractForm.initData(contrato);
		modoVisualizarView(contractForm, contrato.getCodigo());
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable,"Contrato");
		defaultTable();
	}

	@Override
	public void defaultTable() {
		data.removeAllItems();
		try{
			data.load(client, "/contrato");
		}catch (Exception e) {}
	}
}