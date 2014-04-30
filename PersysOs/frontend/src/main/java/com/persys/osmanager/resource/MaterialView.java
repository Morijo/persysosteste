package com.persys.osmanager.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.resource.data.TransactionsContainerResourceMaterial;
import com.restmb.RestMBClient;
import com.restmb.types.Material;
import com.vaadin.data.Item;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 18/02/2013 View Material
 */

public class MaterialView extends FormView {

	private static final long serialVersionUID = 1L;
	private RestMBClient      client = null;
	private Item              item = null;
	private MaterialForm 	  materialForm; 

	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}
	private final TransactionsContainerResourceMaterial data = new TransactionsContainerResourceMaterial();

	public MaterialView(RestMBClient client) {
		this.client = client;
		
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),bundle.getString("material"), false);
		filterTable.setVisibleColumns(bundle.getString("code"),bundle.getString("material"), bundle.getString("situation"),bundle.getString("measure")); 
		filterTable.setColumnExpandRatio(bundle.getString("material"), 0.7f);
	}

	@Override
	public void editar() {
		try {
			Material material = (Material) materialForm.commit();
			material.alterar(client);
			notificationTray(bundle.getString("alteration"),bundle.getString("success"));
			data.setItemProperty(material, item);
			voltar();
		} catch (Exception e) {
			notificationError(bundle.getString("alteration"),
					bundle.getString("error") + e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		materialForm = new MaterialForm(client);
		materialForm.initData(new Material());
		modoAdicionarView(materialForm, bundle.getString("newmaterial"));
	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),
				bundle.getString("wanttoremovethismaterial"),
				bundle.getString("remove"), bundle.getString("cancel"), false,
				true, true, new IMessage() {

					@Override
					public void ok() {
						try {
							Material material = (Material) materialForm.commit();
							material.remover(client);
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
			Material material = ((Material) materialForm.commit()).salvar(client);
			data.addTransaction(material);
			notificationTray(bundle.getString("save"),
					bundle.getString("success"));
			voltar();
		} catch (Exception e) {
			notificationTray(bundle.getString("errorsave"), e.getMessage());
		}
	}

	@Override
	public void visualizar(Object target) {
		try {
			item = ((Item) target);
			Long id = (Long) item.getItemProperty(bundle.getString("id")).getValue();
			Material material = new Material();
			material = material.pesquisa(client, id);
			materialForm = new MaterialForm(client);
			materialForm.initData(material);
			modoVisualizarView(materialForm, material.getMaterial());
		} catch (Exception e) {
		}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable, bundle.getString("material"), false);
	}

	@Override
	public void defaultTable() {
		data.loadTable(client, TransationsContainerTipo.BD);
	}
}