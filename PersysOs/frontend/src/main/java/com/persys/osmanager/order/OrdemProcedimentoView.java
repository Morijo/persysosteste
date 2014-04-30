package com.persys.osmanager.order;

import java.util.ArrayList;

import br.com.model.interfaces.IOrdem;
import br.com.model.interfaces.IOrdemProcedimento;
import br.com.ordem.model.OrdemProcedimento;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IFormWindows;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
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
public class OrdemProcedimentoView extends CustomComponent implements IFormWindows<IOrdemProcedimento>{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Table tableService = null;

	private TransactionsContainer data = new TransactionsContainer();
	
	private IOrdem ordem;

	RestMBClient client = null;
	
	public OrdemProcedimentoView(IOrdem ordem) {
		this.ordem = ordem;
	
		// top-level component properties
		setWidth(AttrDim.FORM_WIDTH);
		setHeight("100%");

		setCompositionRoot(buildMainLayout());

	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
	
		mainLayout.addComponent(modoTabela());
		mainLayout.setExpandRatio(tableService, 1f);
		
		return mainLayout;
	}

	public Table modoTabela(){
		tableService = new Table() {
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
		tableService.setSizeFull();
		tableService.addStyleName("borderless");
		tableService.setSelectable(true);
		tableService.setColumnCollapsingAllowed(true);
		tableService.setColumnReorderingAllowed(true);
		data.removeAllContainerFilters();
		tableService.setContainerDataSource(data);

		tableService.setVisibleColumns(new Object[] {"Id","Procedimento","Serviço","Obrigatório","Anexo","Situação"," "});
		tableService.setColumnExpandRatio("Procedimento", 1f);

		tableService.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				visualizar(tableService.getItem(tableService.getValue()));
			} 
		});

		tableService.setImmediate(true);
		defaultTable();
		return tableService;
	}

	public void defaultTable() {
		data.removeAllItems();
		ArrayList<OrdemProcedimento> servicoProcedimentos = OrdemProcedimento.listaOrdemProcedimento(ordem.getId());
		for(OrdemProcedimento ordemProcedimento : servicoProcedimentos){
			data.addTransaction(ordemProcedimento);
		}
	}

	public class TransactionsContainer extends com.vaadin.data.util.IndexedContainer {

		private static final long serialVersionUID = 1L;

		public TransactionsContainer() {
			addContainerProperty("Id", Long.class, -1);
			addContainerProperty("Serviço", String.class, "");
			addContainerProperty("Procedimento", String.class, "");
			addContainerProperty("Situação", ComboBox.class, new ComboBox());
			addContainerProperty("Obrigatório", CheckBox.class, new CheckBox());
			addContainerProperty("Anexo", CheckBox.class, new CheckBox());
			addContainerProperty(" ", Button.class, new Button());
		}

		@SuppressWarnings("unchecked")
		public void addTransaction(IOrdemProcedimento ordemProcedimento) {
			Object id = addItem();
			com.vaadin.data.Item item = getItem(id);
			if (item != null) {
				item.getItemProperty("Id").setValue(ordemProcedimento.getId());
				item.getItemProperty("Serviço").setValue(ordemProcedimento.getServico().getCodigo());
				
				item.getItemProperty("Procedimento").setValue(ordemProcedimento.getProcedimento().getTitulo());
				
				CheckBox checkBoxObrigatorio = new CheckBox();
				checkBoxObrigatorio.setValue(ordemProcedimento.isObrigatorio());
				item.getItemProperty("Obrigatório").setValue(checkBoxObrigatorio);
					
				CheckBox checkBoxAnexo = new CheckBox();
				checkBoxAnexo.setValue(ordemProcedimento.isAnexo());
				item.getItemProperty("Anexo").setValue(checkBoxAnexo);
				
				ComboBox comboBox = ComponenteFactory.createComboboxStatus(ordemProcedimento.getStatusModel());
				comboBox.setWidth("80");
				item.getItemProperty("Situação").setValue(comboBox);

				Button save = new Button();
				save.setIcon(new ThemeResource("../reindeer/Icons/save.png"));
				save.setImmediate(true);
				save.setWidth("38px");
				save.setHeight("-1px");
				save.addStyleName("newicon");

				ItemView itemC = new ItemView(ordemProcedimento, item);
				save.setData(itemC);
				save.addClickListener(new SaveListener());
				item.getItemProperty(" ").setValue(save);
				
				}
		}
	}
	
	private class SaveListener implements Button.ClickListener {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			ItemView data = (ItemView) event.getButton().getData();
			
			OrdemProcedimento ordemProcedimento = (OrdemProcedimento) data.ordemProcedimento;
			
			ComboBox comboBoxStatus = (ComboBox) data.item.getItemProperty("Situação").getValue();
			CheckBox checkBoxObrigatorio = (CheckBox) data.item.getItemProperty("Obrigatório").getValue();
			CheckBox checkBoxAnexo = (CheckBox) data.item.getItemProperty("Anexo").getValue();
			
			ordemProcedimento.setId((Long) data.item.getItemProperty("Id").getValue());
			ordemProcedimento.setStatusModel((Integer) comboBoxStatus.getValue());
			ordemProcedimento.setObrigatorio(checkBoxObrigatorio.getValue());
			ordemProcedimento.setAnexo(checkBoxAnexo.getValue());
			ordemProcedimento.alteraOrdemProcedimento();
		}        
	}
	
	public void visualizar(Object objeto){

	}	

	@Override
	public IOrdemProcedimento commit() {
		return null;
	}

	@Override
	public void initData(IOrdemProcedimento data) {}

	@Override
	public void modoView() {
	}

	@Override
	public void modoAdd() {}

	@Override
	public void commitWindows(int resultTag) throws ViewException {}
	
	class ItemView{
		public IOrdemProcedimento ordemProcedimento;
		public com.vaadin.data.Item item;
		
		public ItemView(IOrdemProcedimento ordemProcedimento, com.vaadin.data.Item item){
			this.ordemProcedimento = ordemProcedimento;
			this.item = item;
		}
	}


}