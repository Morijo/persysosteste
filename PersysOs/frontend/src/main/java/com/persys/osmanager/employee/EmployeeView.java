package com.persys.osmanager.employee;

import java.io.File;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.dashboard.DashboardUI;
import com.persys.osmanager.employee.data.TransactionsContainerEmployeeData;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.restmb.types.Funcionario;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class EmployeeView extends FormView implements View{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EmployeeMenu          menuFuncionario = null;
	private Funcionario           funcionario = null;
	private RestMBClient          client = null;
	private Item				  item;

	private TransactionsContainerEmployeeData data = new TransactionsContainerEmployeeData();
 
	@Override
	public void editar() {
		try{
			menuFuncionario.commit().alterar(client);
			notificationTray("Editar", "Sucesso");
		} catch (ViewException e) {
			notificationTray("Erro editar", e.getMessage());
		}catch (Exception e) {
			notificationTray("Erro editar", e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		menuFuncionario = new EmployeeMenu(client);
		menuFuncionario.initData(new Funcionario());
		menuFuncionario.modoAdd();
		modoAdicionarView(menuFuncionario,"Novo Empregado");
	}

	@Override
	public void remover(Object target) {
		messageSucess("Remover","Deseja remover?","Remover","Cancelar",false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					menuFuncionario.commit().remover(client);
					notificationTray("Remover", "Sucesso");
					voltar();
				}catch(Exception e){
					notificationError("Remover", "Erro " + e.getMessage() );
				}
			}
			@Override
			public void discard() {

			}

			@Override
			public void cancel() {

			}
		});
	}

	@Override
	public void salvar() {
		try{
			funcionario = menuFuncionario.commit();
			funcionario.salvar(client);
			notificationTray("Salvar", "Sucesso");
		}catch (Exception e) {
			if(e.getMessage().contains("senha")){
				notificationTray("Atenção", e.getMessage());
				voltar();
			}else{
				notificationTray("Erro", e.getMessage());
			}	
		}
	}

	@Override
	public void visualizar(Object target) {
		item = ((Item)target);
		Long id = (Long) item.getItemProperty("Id").getValue();
		this.funcionario = new Funcionario();
		this.funcionario = Funcionario.pesquisa(client,"/funcionario",Funcionario.class, id);
		menuFuncionario = new EmployeeMenu(client);
		menuFuncionario.initData(funcionario);
		modoVisualizarView(menuFuncionario, funcionario.getRazaoNome(),funcionario.getImagem());
	}

	@Override
	public void defaultTable() {
		data.removeAllItems();
		try{
			data.load(client, "/funcionario");
		}catch (Exception e) {}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable,"Empregados");
		defaultTable();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		client =  ((DashboardUI)getUI()).getClient();
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),"Empregado");
		filterTable.setVisibleColumns(new Object[] {"Perfil","Codigo","Nome","CPF/CNPJ", "Situação"});
		filterTable.setColumnExpandRatio("Nome", 1.0f);
		 
	}

	public void image(File file){
		super.image(file);
		try{
			funcionario.setImagem(client, file);
		}catch(Exception e){}
	}
}
