package com.persys.osmanager.order;

import java.util.Locale;
import java.util.ResourceBundle;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.order.data.TransactionsContainerOrdemServicoData;
import com.persys.osmanager.order.table.FilterDecoratorOrdem;
import com.persys.osmanager.order.table.FilterGeneratorOrdem;
import com.restmb.RestMBClient;
import com.restmb.types.OrdemServico;
import com.vaadin.data.Item;

public class OrdemView extends FormView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final TransactionsContainerOrdemServicoData data = new TransactionsContainerOrdemServicoData();
	private RestMBClient client = null;
	private OrdemForm ordemForm = null;

	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/ordem",Locale.getDefault());
	}

	public OrdemView(RestMBClient client) {
		this.client =  client;
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),bundle.getString("entidade"));
		filterTable.setVisibleColumns((Object[]) new String[] { "OS", "Cliente",
				"Situacao", "DataCriacao", "DataConclusao", "Endereco" });
		filterTable.setFilterDecorator(new FilterDecoratorOrdem());
		filterTable.setFilterGenerator(new FilterGeneratorOrdem());
	}

	@Override
	public void editar() {
		try{
			ordemForm.modoView();
			ordemForm.commit().alterar(client);
			notificationTray("Editar", "Sucesso");
		} catch (Exception e) {
			notificationTray("Erro editar", e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		ordemForm = new OrdemForm(client,bundle);
		ordemForm.initData(new OrdemServico());
		ordemForm.modoAdd();
		modoAdicionarView(ordemForm,bundle.getString("nova"));
	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remover"),bundle.getString("removerpergunta"),bundle.getString("remover"),"Cancelar",false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					ordemForm.commit().remover(client);
					notificationTray(bundle.getString("remover"), "Sucesso");
					voltar();
				}catch(Exception e){
					notificationError(bundle.getString("remover"), "Erro " + e.getMessage() );
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
		OrdemServico ordem = null;
		try{
			ordemForm.modoAdd();
			ordem = ordemForm.commit();
			ordem = ordem.salvar(client);
			notificationTray("Salvar", "Sucesso");
			try{
				ordem = ordem.pesquisa(client, ordem.getId());
				ordemForm.initData(ordem);
				modoVisualizarView(ordemForm,ordem.getCodigo());
			}catch (Exception e) {
				voltar();
			}
		}catch (Exception e) {
			notificationTray("Erro salvar", e.getMessage());
		}
	}

	@Override
	public void visualizar(Object target) {
		
			Long id = (Long) ((Item)target).getItemProperty("ID").getValue();
			OrdemServico ordemServico = new OrdemServico();
			ordemServico = ordemServico.pesquisa(client, id);
			ordemForm = new OrdemForm(client,bundle);
			ordemForm.initData(ordemServico);
			ordemForm.modoView();
			modoVisualizarView(ordemForm,ordemServico.getCodigo());
	
	}

	@Override
	public void voltar() {
		defaultTable();
		modoTabelaView(filterTable,bundle.getString("entidade"));
	}


	@Override
	public void defaultTable() {
		data.loadTableDataBase(client);
	}

}
