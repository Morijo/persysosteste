package com.persys.osmanager.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.resource.data.TransactionsContainerResourceEquipamento;
import com.restmb.RestMBClient;
import com.restmb.types.Equipamento;
import com.vaadin.data.Item;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 18/02/2013 View Para Equipamento
 */

public class EquipamentoView extends FormView {

	private static final long serialVersionUID = 1L;
	private RestMBClient client = null;
	private Item item = null;
	private EquipamentoForm equipamentoForm = new EquipamentoForm();

	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}

	Equipamento IEquipamento = null;

	private TransactionsContainerResourceEquipamento data = new TransactionsContainerResourceEquipamento();

	public EquipamentoView(RestMBClient client) {
		this.client = client;
		setSizeFull();
		modoTabela();
	}

	public void modoTabela() {
		modoTabelaView(buildPagedFilterTable(data),
				bundle.getString("equipment"), false);
		filterTable.setVisibleColumns(bundle.getString("code"),
				bundle.getString("equipment"), bundle.getString("situation"));
		filterTable.setColumnExpandRatio(bundle.getString("equipment"), 0.7f);
	}

	@Override
	public void editar() {
		try {
			Equipamento equipamento = (Equipamento) equipamentoForm.commit();
			equipamento.alterar(client);
			notificationTray(bundle.getString("alteration"),
					bundle.getString("success"));
			data.setItemProperty(equipamento, item);
			voltar();
		} catch (Exception e) {
			notificationError(bundle.getString("alteration"),
					bundle.getString("error") + e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		equipamentoForm.initData(new Equipamento());
		modoAdicionarView(equipamentoForm, bundle.getString("newequipment"));
	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),
				bundle.getString("wanttoremovethisequipment"),
				bundle.getString("remove"), bundle.getString("cancel"), false,
				true, true, new IMessage() {

					@Override
					public void ok() {
						try {
							Equipamento equipamento = (Equipamento) equipamentoForm
									.commit();
							equipamento.remover(client);
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
			Equipamento equipamento = ((Equipamento) equipamentoForm.commit())
					.salvar(client);
			notificationTray(bundle.getString("save"),
					bundle.getString("success"));
			data.addTransaction(equipamento);
			equipamentoForm.initData(equipamento);
			modoVisualizarView(equipamentoForm, equipamento.getEquipamento());
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
			Equipamento equipamento = new Equipamento();
			equipamento = equipamento.pesquisa(client, id);
			equipamentoForm = new EquipamentoForm();
			equipamentoForm.initData(equipamento);
			modoVisualizarView(equipamentoForm, equipamento.getEquipamento());
		} catch (Exception e) {
		}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable, bundle.getString("equipment"), false);
	}

	@Override
	public void defaultTable() {
		data.loadTable(client, TransationsContainerTipo.BD);
	}

}
