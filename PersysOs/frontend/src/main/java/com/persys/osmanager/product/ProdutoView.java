package com.persys.osmanager.product;

import java.util.Locale;
import java.util.ResourceBundle;
import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.dashboard.DashboardUI;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.product.data.TransactionsContainerProduct;
import com.restmb.RestMBClient;
import com.restmb.types.Produto;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 17/02/2013 View Product
 */

public class ProdutoView extends FormView implements View {

	private static final long serialVersionUID = 1L;
	private RestMBClient      client = null;
	private Item              item = null;
	private ProdutoForm 	  produtoForm = new ProdutoForm();

	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}
	private final TransactionsContainerProduct data = new TransactionsContainerProduct();

	@Override
	public void enter(ViewChangeEvent event) {
		client = ((DashboardUI) getUI()).getClient();
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),bundle.getString("product"), false);
		filterTable.setVisibleColumns(bundle.getString("code"),bundle.getString("product"), bundle.getString("situation"));
		filterTable.setColumnExpandRatio(bundle.getString("product"), 0.7f);
	}

	@Override
	public void editar() {
		try {
			Produto produto = (Produto) produtoForm.commit();
			produto.alterar(client);
			notificationTray(bundle.getString("alteration"),bundle.getString("success"));
			data.setItemProperty(produto, item);
			defaultTable();
			voltar();
		} catch (Exception e) {
			notificationError(bundle.getString("alteration"),
					bundle.getString("error") + e.getMessage());
		}
	}

	@Override
	public void adicionar() {
		produtoForm.initData(new Produto());
		modoAdicionarView(produtoForm, bundle.getString("newproduct"));
		defaultTable();
	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),
				bundle.getString("wanttoremovethisproduct"),
				bundle.getString("remove"), bundle.getString("cancel"), false,
				true, true, new IMessage() {

					@Override
					public void ok() {
						try {
							Produto produto = (Produto) produtoForm.commit();
							produto.remover(client);
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
			Produto produto = (Produto) produtoForm.commit();
			produto.salvar(client);
			data.addTransaction(produto);
			notificationTray(bundle.getString("save"),
					bundle.getString("success"));
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
			Long id = (Long) item.getItemProperty(bundle.getString("id")).getValue();
			Produto produto = new Produto();
			produto = produto.pesquisa(client, id);
			produtoForm.initData(produto);
			modoVisualizarView(produtoForm, produto.getNome());
		} catch (Exception e) {
		}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable, bundle.getString("product"), false);
		defaultTable();
	}

	@Override
	public void defaultTable() {
		data.loadTable(client, TransationsContainerTipo.BD);
	}
}