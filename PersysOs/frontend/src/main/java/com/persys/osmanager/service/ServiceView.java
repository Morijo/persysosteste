package com.persys.osmanager.service;

import java.util.Locale;
import java.util.ResourceBundle;
import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.service.data.TransactionsContainerService;
import com.restmb.RestMBClient;
import com.restmb.types.Servico;
import com.vaadin.data.Item;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 18/02/2013 View Para Servico
 */

public class ServiceView extends FormView {

	private static final long serialVersionUID = 1L;
	private RestMBClient client = null;
	private Item item = null;
	private ServiceForm serviceForm;

	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}

	private TransactionsContainerService data = new TransactionsContainerService();

	public ServiceView(RestMBClient client) {
		this.client = client;
		setSizeFull();
		modoTabela();
	}

	public void modoTabela() {
		modoTabelaView(buildPagedFilterTable(data),
				bundle.getString("service"), false);
		filterTable.setVisibleColumns(bundle.getString("code"),
				bundle.getString("service"), bundle.getString("situation"));
		filterTable.setColumnExpandRatio(bundle.getString("service"), 0.7f);
	}

	@Override
	public void editar() {
		try {
			Servico servico = (Servico) serviceForm.commit();
			servico.alterar(client);
			notificationTray(bundle.getString("alteration"),
					bundle.getString("success"));
			data.setItemProperty(servico, item);
			voltar();
		} catch (Exception e) {
			notificationError(bundle.getString("alteration"),
					bundle.getString("error") + e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		serviceForm = new ServiceForm(client);
		serviceForm.initData(new Servico());
		modoAdicionarView(serviceForm, bundle.getString("newservice"));
	}
	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),
				bundle.getString("wanttoremovethisservice"),
				bundle.getString("remove"), bundle.getString("cancel"), false,
				true, true, new IMessage() {

					@Override
					public void ok() {
						try {
							Servico servico = (Servico) serviceForm.commit();
							servico.remover(client);
							notificationTray(bundle.getString("remove"),
									bundle.getString("success"));
							defaultTable();
							voltar();
						} catch (Exception e) {
							notificationError(bundle.getString("remove"),
									bundle.getString("error") + e.getMessage());
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
		try {
			Servico servico = ((Servico) serviceForm.commit()).salvar(client);
			notificationTray(bundle.getString("save"),
					bundle.getString("success"));
			data.addTransaction(servico);
			serviceForm.initData(servico);
			modoVisualizarView(serviceForm, servico.getTitulo());
		} catch (ViewException e) {
			notificationTray(bundle.getString("errorsave"), e.getMessage());
		} catch (Exception e) {
			notificationTray(bundle.getString("errorsave"), e.getMessage());
		}
	}

	@Override
	public void visualizar(Object target) {
		try {
			item = ((Item) target);
			Long id = (Long) item.getItemProperty("Id").getValue();
			Servico servico = new Servico();
			servico = servico.pesquisa(client, id);
			serviceForm = new ServiceForm(client);
			serviceForm.initData(servico);
			modoVisualizarView(serviceForm, servico.getTitulo());
		} catch (Exception e) {
		}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable, bundle.getString("service"), false);
	}

	@Override
	public void defaultTable() {
		data.loadTableDataBase(client);
	}
}
