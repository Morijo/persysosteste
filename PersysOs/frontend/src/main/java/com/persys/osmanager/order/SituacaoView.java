package com.persys.osmanager.order;

import java.util.Locale;
import java.util.ResourceBundle;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.order.data.TransactionsContainerSituacaoData;
import com.restmb.RestMBClient;
import com.restmb.types.SituacaoOrdem;
import com.vaadin.data.Item;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 27/03/2013 View Para Situacao em Ordem de Servico
 * <p>Dados Carregados a partir do RestMb</p>
 * <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/frontend/notification</p>
 */

public class SituacaoView extends FormView{

	private static final long serialVersionUID = 1L;

	private TransactionsContainerSituacaoData data 	= new TransactionsContainerSituacaoData();
	private RestMBClient 		  client 		= null;
	private SituacaoForm 		  situacaoForm  = null;
	private Item item = null;
	
	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",Locale.getDefault());
	}

	public SituacaoView(RestMBClient client) {
		this.client = client;
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),bundle.getString("situation"),false);
		filterTable.setVisibleColumns(new Object[] { bundle.getString("code"), bundle.getString("ordersituation"), bundle.getString("situation")});
		filterTable.setColumnExpandRatio(bundle.getString("ordersituation"), 0.7f);
		}

	@Override
	public void editar() {
		try{
			SituacaoOrdem situacaoOrdem = (SituacaoOrdem) situacaoForm.commit();
			situacaoOrdem.alterar(client);
			notificationTray(bundle.getString("alteration"),bundle.getString("success"));
			voltar();
		} catch (Exception e) {
			notificationError(bundle.getString("alteration"),
					bundle.getString("error") + e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		situacaoForm = new SituacaoForm(client,data);
		situacaoForm.initData(new SituacaoOrdem());
		modoAdicionarView(situacaoForm,bundle.getString("situation"));
	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),bundle.getString("wanttoremovethissituation"),bundle.getString("remove"),bundle.getString("cancel"),false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					SituacaoOrdem situacaoOrdem = (SituacaoOrdem) situacaoForm.commit();
					situacaoOrdem.remover(client);
					notificationTray(bundle.getString("remove"),bundle.getString("success"));
					defaultTable();
					voltar();
				}catch(Exception e){
					notificationError(bundle.getString("remove"),bundle.getString("error") + e.getMessage());				
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
			SituacaoOrdem situacaoOrdem = (SituacaoOrdem) situacaoForm.commit();
			situacaoOrdem.salvar(client);
			notificationTray(bundle.getString("save"),bundle.getString("success"));
			voltar();
		}catch (Exception e) {
			notificationTray(bundle.getString("errorsave"), e.getMessage());
		}	
	}

	@Override
	public void visualizar(Object target) {
		item = ((Item)target);
		Long id = (Long) item.getItemProperty(bundle.getString("id")).getValue();
		SituacaoOrdem situacaoOrdem = new SituacaoOrdem();
		situacaoOrdem = situacaoOrdem.pesquisa(client,id);
		situacaoForm = new SituacaoForm(client,data);
		situacaoForm.initData(situacaoOrdem);
		situacaoForm.modoView();
		modoVisualizarView(situacaoForm, situacaoOrdem.getNome());
	}

	@Override
	public void defaultTable() {
		data.loadTable(client, TransationsContainerTipo.REST);
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable,bundle.getString("situation"),false);
		defaultTable();
	}
}
