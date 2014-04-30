package com.persys.osmanager.product.data;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.tepi.filtertable.pagedrest.IPagedRest;

import com.github.wolfie.refresher.Refresher;
import com.persys.osmanager.componente.AsyncTransactionsContainer;
import com.restmb.Connection;
import com.restmb.Parameter;
import com.restmb.RestMBClient;
import com.restmb.types.Produto;
import com.restmb.types.RestMbType;

public class ProdutoLoadTask extends AsyncTransactionsContainer<Parameter> {
	private final static ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/product",
				Locale.getDefault());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RestMBClient client;
	private IPagedRest iPagedRest;
	private String uri;

	public ProdutoLoadTask(IPagedRest iPagedRest, String uri,
			RestMBClient client) {
		this.iPagedRest = iPagedRest;
		this.client = client;
		this.uri = uri;
	}

	@Override
	public void onPreExecute() {
		addContainerProperty(bundle.getString("id"), Long.class, -1);
        addContainerProperty(bundle.getString("name"), String.class, "");
        addContainerProperty(bundle.getString("code"), String.class, "");
        addContainerProperty(bundle.getString("obj"), Produto.class, "");
        
    }

	public void onPostExecute(List<Produto> lista) {
		for (Produto produto : lista) {
			addItemData(produto);
		}
	}

	@SuppressWarnings("unchecked")
	public void addItemData(Produto produto) {
		Object id = addItem();
		com.vaadin.data.Item item = getItem(id);
		if (item != null) {
			item.getItemProperty(bundle.getString("id")).setValue(produto.getId());
			item.getItemProperty(bundle.getString("name")).setValue(produto.getNome());
			item.getItemProperty(bundle.getString("code")).setValue(produto.getCodigo());
			item.getItemProperty(bundle.getString("obj")).setValue(produto);
		}

	}

	@Override
	public void doInBackGround(Parameter... parameters) {
		Connection<Produto> lista;

		lista = RestMbType.listaAll(client, uri, Produto.class, parameters);

		onPostExecute(lista.getData());
		while (lista.hasNext()) {
			lista = lista.fetchNextPage();
			onPostExecute(lista.getData());
		}
	}

	@Override
	public void refreshView(Refresher source) {
		iPagedRest.update(source);
	}
}
