package com.persys.osmanager.resource;

import java.util.Locale;
import java.util.ResourceBundle;
import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.resource.data.TransactionsContainerResourceMedida;
import com.restmb.RestMBClient;
import com.restmb.types.Medida;
import com.vaadin.data.Item;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 18/02/2013 View Para Medida
 */

public class MedidaView extends FormView {

	private static final long serialVersionUID = 1L;
	private RestMBClient client = null;
	private Item item = null;
	private MedidaForm medidaForm = new MedidaForm();

	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}

	Medida IMedida = null;
	
	private TransactionsContainerResourceMedida data = new TransactionsContainerResourceMedida();

	public MedidaView(RestMBClient client) {
		this.client = client;
		setSizeFull();
		modoTabela();
	}

	public void modoTabela() {
		modoTabelaView(buildPagedFilterTable(data),
				bundle.getString("measure"), false);
		filterTable.setVisibleColumns(bundle.getString("code"),
				bundle.getString("measure"), bundle.getString("situation"));
		filterTable.setColumnExpandRatio(bundle.getString("measure"), 0.7f);
	}

	@Override
	public void editar() {
		try {
			Medida medida = (Medida) medidaForm.commit();
			medida.alterar(client);
			notificationTray(bundle.getString("alteration"),
					bundle.getString("success"));
			data.setItemProperty(medida, item);
			voltar();
		} catch (Exception e) {
			notificationError(bundle.getString("alteration"),
					bundle.getString("error") + e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		medidaForm.initData(new Medida());
		modoAdicionarView(medidaForm, bundle.getString("newmeasure"));
	}
	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),
				bundle.getString("wanttoremovethismeasure"),
				bundle.getString("remove"), bundle.getString("cancel"), false,
				true, true, new IMessage() {

					@Override
					public void ok() {
						try {
							Medida medida = (Medida) medidaForm.commit();
							medida.remover(client);
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
			Medida medida = ((Medida) medidaForm.commit()).salvar(client);
			notificationTray(bundle.getString("save"),
					bundle.getString("success"));
			data.addTransaction(medida);
			medidaForm.initData(medida);
			modoVisualizarView(medidaForm, medida.getNome());
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
			Medida medida = new Medida();
			medida = medida.pesquisa(client, id);
			medidaForm = new MedidaForm();
			medidaForm.initData(medida);
			modoVisualizarView(medidaForm, medida.getNome());
		} catch (Exception e) {
		}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable, bundle.getString("measure"), false);
	}

	@Override
	public void defaultTable() {
		data.loadTable(client, TransationsContainerTipo.BD);
	}
}