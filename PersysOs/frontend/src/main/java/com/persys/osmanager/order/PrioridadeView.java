package com.persys.osmanager.order;

import java.util.Locale;
import java.util.ResourceBundle;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.order.data.TransactionsContainerPrioridadeData;
import com.restmb.RestMBClient;
import com.restmb.types.Prioridade;
import com.vaadin.data.Item;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 27/03/2013 View Para Prioridade em Ordem de Servico
 * <p>Dados Carregados apartir do RestMb</p>
 * <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/frontend/notification</p>
 */

public class PrioridadeView extends FormView{

	private static final long serialVersionUID = 1L;

	private TransactionsContainerPrioridadeData data 			= new TransactionsContainerPrioridadeData();
	private RestMBClient 		  client 		= null;
	private PrioridadeForm 		  prioridadeForm= null;
	private Item item = null;

	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",Locale.getDefault());
	}

	public PrioridadeView(RestMBClient client) {
		this.client = client;
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),bundle.getString("priority"),false);
		filterTable.setVisibleColumns(new Object[] { bundle.getString("code"), bundle.getString("color"), bundle.getString("priority"),bundle.getString("situation")});
		filterTable.setColumnExpandRatio(bundle.getString("priority"), 0.7f);
		}

	@Override
	public void editar() {
		try{
			Prioridade prioridade = (Prioridade) prioridadeForm.commit();
			prioridade.alterar(client);
			notificationTray(bundle.getString("alteration"),bundle.getString("success"));
			voltar();
		} catch (Exception e) {
			notificationError(bundle.getString("alteration"),
					bundle.getString("error") + e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		prioridadeForm = new PrioridadeForm();
		prioridadeForm.initData(new Prioridade());
		modoAdicionarView(prioridadeForm,bundle.getString("newpriority"));

	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),bundle.getString("wanttoremovethispriority"),bundle.getString("remove"),bundle.getString("cancel"),false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					Prioridade prioridade = (Prioridade) prioridadeForm.commit();
					prioridade.remover(client);
					notificationTray(bundle.getString("remove"),bundle.getString("success"));
					defaultTable();
					voltar();
				}catch(Exception e){
					notificationError(bundle.getString("remove"),bundle.getString("error") + e.getMessage());				
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
			Prioridade prioridade = (Prioridade) prioridadeForm.commit();
			prioridade.salvar(client);
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
	
		Prioridade prioridade = new Prioridade();
		prioridade = prioridade.pesquisa(client,id);
		prioridadeForm = new PrioridadeForm();
		prioridadeForm.initData(prioridade);
		modoVisualizarView(prioridadeForm, prioridade.getPrioridade());
	}

	@Override
	public void defaultTable() {
		data.loadTableRest(client);
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable,bundle.getString("priority"),false);
		defaultTable();
	}
}
