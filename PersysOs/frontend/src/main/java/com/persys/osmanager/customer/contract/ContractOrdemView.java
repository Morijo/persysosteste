package com.persys.osmanager.customer.contract;

import java.util.List;

import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.order.data.TransactionsContainerOrdemServicoData;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.ClienteObjeto;
import com.restmb.types.OrdemServico;
import com.restmb.types.Produto;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/** 
@author Ricardo Sabatine
@version Revision 1.1

Form para o contrato

@since 07/10/2013
 */
public class ContractOrdemView extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private Table tableOrdem = null;
	private TransactionsContainerOrdemServicoData data = new TransactionsContainerOrdemServicoData();
	private ClienteObjeto<?> clienteObjeto = null;

	private RestMBClient client = null;

	public ContractOrdemView(RestMBClient client, ClienteObjeto<?> clienteObjeto) {
		this.client = client;
		this.clienteObjeto = clienteObjeto;

		setWidth(AttrDim.FORM_WIDTH);
		setHeight(AttrDim.FORM_HEIGHT);

		setCompositionRoot(buildMainLayout());
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		Button buttonAdicionar = new Button("Adicionar Ordem",new Button.ClickListener() {
		
			private static final long serialVersionUID = 1L;

			// inline click-listener
			public void buttonClick(ClickEvent event) {
				
			}
		});

		buttonAdicionar.setWidth("-1px");
		buttonAdicionar.setHeight("-1px");
		buttonAdicionar.setVisible(true);
		buttonAdicionar.addStyleName("wide");
		buttonAdicionar.addStyleName("default");
		//mainLayout.addComponent(buttonAdicionar);
		//mainLayout.setComponentAlignment(buttonAdicionar, new Alignment(34));
		
		mainLayout.addComponent(modoTabela());
		mainLayout.setExpandRatio(tableOrdem, 1f);
		return mainLayout;
	}

	public Table modoTabela(){
		tableOrdem = new Table() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected String formatPropertyValue(Object rowId, Object colId,
					Property<?> property) {

				return super.formatPropertyValue(rowId, colId, property);
			}
		};
		tableOrdem.setSizeFull();
		tableOrdem.addStyleName("borderless");
		tableOrdem.setSelectable(false);
		tableOrdem.setColumnCollapsingAllowed(true);
		tableOrdem.setColumnReorderingAllowed(true);
		data.removeAllContainerFilters();
		tableOrdem.setContainerDataSource(data);

		tableOrdem.setVisibleColumns(new Object[] {"OS","Situacao","DataCriacao","DataConclusao"});
		
		tableOrdem.setImmediate(true);
		defaultTable();
		return tableOrdem;
	}

	public void defaultTable() {
		data.removeAllItems();
		try{
			Connection<OrdemServico> lista = Produto.listaAll(client,"/contrato/"+this.clienteObjeto.getId()+"/ordem",OrdemServico.class);

			for(List<OrdemServico> listaContrato : lista){
				for(OrdemServico clienteObjetoProduto : listaContrato){
					data.addTransaction(clienteObjetoProduto);
				}
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
