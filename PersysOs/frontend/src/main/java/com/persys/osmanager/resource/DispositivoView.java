package com.persys.osmanager.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.resource.data.TransactionsContainerResourceDispositivo;
import com.restmb.RestMBClient;
import com.restmb.types.Dispositivo;
import com.vaadin.data.Item;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 17/02/2013 View para Dispositivo
 */

public class DispositivoView extends FormView {

	private static final long serialVersionUID = 1L;
	private RestMBClient client = null;
	private Item item = null;
	private DispositivoForm dispositivoForm = new DispositivoForm();

	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}
	Dispositivo IDispositivo = null;

	private final TransactionsContainerResourceDispositivo data = new TransactionsContainerResourceDispositivo();

	public DispositivoView(RestMBClient client) {
		this.client = client;
		setSizeFull();
		modoTabela();
	}

	public void modoTabela() {
		modoTabelaView(buildPagedFilterTable(data), 
				bundle.getString("device"),	false);
		filterTable.setVisibleColumns(bundle.getString("code"),
				bundle.getString("imei"), bundle.getString("situation"));
		filterTable.setColumnExpandRatio(bundle.getString("imei"), 0.7f);
		buttonAdicionar.setVisible(true);
	}

	@Override
	public void editar() {
		try {
			Dispositivo dispositivo = (Dispositivo) dispositivoForm.commit();
			dispositivo.alterar(client);
			notificationTray(bundle.getString("alteration"),
					bundle.getString("success"));
			data.setItemProperty(dispositivo, item);
			voltar();
		} catch (Exception e) {
			notificationError(bundle.getString("alteration"),
					bundle.getString("error") + e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		dispositivoForm.initData(new Dispositivo());
		modoAdicionarView(dispositivoForm, bundle.getString("newdevice"));
	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),
				bundle.getString("wanttoremovethisdevice"),
				bundle.getString("remove"), bundle.getString("cancel"), false,
				true, true, new IMessage() {

					@Override
					public void ok() {
						try {
							Dispositivo dispositivo = (Dispositivo) dispositivoForm
									.commit();
							dispositivo.remover(client);
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
			Dispositivo dispositivo = ((Dispositivo) dispositivoForm.commit())
					.salvar(client);
			notificationTray(bundle.getString("save"),
					bundle.getString("success"));
			data.addTransaction(dispositivo);
			dispositivoForm.initData(dispositivo);
			modoVisualizarView(dispositivoForm, dispositivo.getNome());
			defaultTable();
			voltar();
		} catch (Exception e) {
			notificationTray(bundle.getString("errorsave"), e.getMessage());
		}
	}

	@Override
	public void visualizar(Object target) {
		try {
			item = ((Item) target);
			Long id = (Long) item.getItemProperty(bundle.getString("id"))
					.getValue();
			Dispositivo dispositivo = new Dispositivo();
			dispositivo = dispositivo.pesquisa(client, id);
			dispositivoForm = new DispositivoForm();
			dispositivoForm.initData(dispositivo);
			modoVisualizarView(dispositivoForm, dispositivo.getNome());
		} catch (Exception e) {
		}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable, bundle.getString("device"), false);
	}

	@Override
	public void defaultTable() {
		data.loadTable(client, TransationsContainerTipo.BD);
	}
}
