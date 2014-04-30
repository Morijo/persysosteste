package com.persys.osmanager.resource;

import java.math.BigDecimal;
import java.util.List;

import br.com.model.interfaces.IOrdem;
import br.com.model.interfaces.IRecurso;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.DialogWindow;
import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IFormWindows;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.resource.data.TransactionsContainerResource;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.oauth.service.ParameterList;
import com.restmb.types.OrdemServico;
import com.restmb.types.Recurso;
import com.restmb.types.RecursoOrdem;
import com.restmb.types.RestMbType;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/** 
@author Ricardo Sabatine
@version Revision 1.1

Form para o contrato

@since 07/10/2013
 */
public class OrdemResourceView extends CustomComponent implements IFormWindows<IRecurso>{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Table tableService = null;

	private TransactionsContainer data = new TransactionsContainer();
	private SearchFormWindow<IRecurso> serviceFormWindows = null;

	private IOrdem ordemServico = null;

	private RestMBClient client = null;

	public OrdemResourceView(RestMBClient client,IOrdem ordemServico) {
		this.client = client;
		this.ordemServico = ordemServico;

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

		Button buttonAdicionar = new Button("Adicionar Recurso",new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				modoAdd();
			}
		});

		buttonAdicionar.setWidth("-1px");
		buttonAdicionar.setHeight("-1px");
		buttonAdicionar.setVisible(true);
		buttonAdicionar.addStyleName("wide");
		buttonAdicionar.addStyleName("default");
		mainLayout.addComponent(buttonAdicionar);
		mainLayout.setComponentAlignment(buttonAdicionar, new Alignment(34));
		mainLayout.addComponent(modoTabela());
		mainLayout.setExpandRatio(tableService, 1f);

		return mainLayout;
	}

	public Table modoTabela(){
		tableService = new Table() {
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

		tableService.setVisibleColumns(new Object[] {"Código","Recurso","Quantidade","Medida","Situação"," ","  "});
		tableService.setColumnExpandRatio("Recurso", 1f);

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
		try{
			Connection<RecursoOrdem> lista = RestMbType.listaAll(client,"/ordem/"+this.ordemServico.getId()+"/recurso",RecursoOrdem.class);

			for(List<RecursoOrdem> listaRecurso : lista){
				for(RecursoOrdem recursoOrdem : listaRecurso){
					data.addTransaction(recursoOrdem);
				}
			}
		}catch (Exception e) {}
	}


	public class TransactionsContainer extends com.vaadin.data.util.IndexedContainer {

		private static final long serialVersionUID = 1L;

		public TransactionsContainer() {
			addContainerProperty("Id", Long.class, -1);
			addContainerProperty("Código", String.class, "");
			addContainerProperty("Recurso", String.class, "");
			addContainerProperty("Situação", ComboBox.class, new ComboBox());
			addContainerProperty("Quantidade", TextField.class, new TextField());
			addContainerProperty("Medida", String.class, "");
			addContainerProperty(" ", Button.class, new Button());
			addContainerProperty("  ", Button.class, new Button());

		}

		@SuppressWarnings("unchecked")
		public void addTransaction(RecursoOrdem recursoOrdem) {
			Object id = addItem();
			com.vaadin.data.Item item = getItem(id);
			if (item != null) {
				item.getItemProperty("Id").setValue(recursoOrdem.getId());
				item.getItemProperty("Código").setValue(recursoOrdem.getRecurso().getCodigo());
				item.getItemProperty("Recurso").setValue(recursoOrdem.getRecurso().getNome());

				TextField quantidade = new TextField();
				quantidade.setWidth("90");
				quantidade.setValue(recursoOrdem.getQuantidadeConsumida().toString());
				item.getItemProperty("Quantidade").setValue(quantidade);
				
				item.getItemProperty("Medida").setValue(recursoOrdem.getRecurso().getMedida().getNome());

				ComboBox comboBox = ComponenteFactory.createComboboxStatus(recursoOrdem.getStatusModel());
				comboBox.setWidth("80");
				item.getItemProperty("Situação").setValue(comboBox);

				Button save = new Button();
				save.setIcon(new ThemeResource("../reindeer/Icons/save.png"));
				save.setImmediate(true);
				save.setWidth("38px");
				save.setHeight("-1px");
				save.addStyleName("newicon");

				ItemView itemC = new ItemView(recursoOrdem, item);
				save.setData(itemC);
				save.addClickListener(new SaveListener());

				item.getItemProperty(" ").setValue(save);

				Button remove = new Button();
				remove.setIcon(new ThemeResource("../reindeer/Icons/delete.png"));
				remove.setImmediate(true);
				remove.setWidth("40px");
				remove.setHeight("-1px");
				remove.addStyleName("newicon");
				remove.setData(itemC);
				remove.addClickListener(new RemoveListener());

				item.getItemProperty("  ").setValue(remove);
			}
		}
	}

	private class SaveListener implements Button.ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			try{
				ItemView data = (ItemView) event.getButton().getData();

				RecursoOrdem recursoOrdem = data.recursoOrdem;

				ComboBox comboBoxStatus = (ComboBox) data.item.getItemProperty("Situação").getValue();
				TextField textFieldQuantidade = (TextField) data.item.getItemProperty("Quantidade").getValue();

				recursoOrdem.setStatusModel((Integer) comboBoxStatus.getValue());

				recursoOrdem.setQuantidadeConsumida(new BigDecimal(textFieldQuantidade.getValue()) );
				com.restmb.oauth.service.ParameterList headers = new ParameterList();
				headers.add("Content-Type", "application/json");
				client.publishChanges("/ordem/"+ordemServico.getId()+"/recurso/"+recursoOrdem.getId(), RecursoOrdem.class,recursoOrdem.toJson(),headers);
			}catch(Exception e ){}
		}        
	}

	private class RemoveListener implements Button.ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			ItemView data = (ItemView) event.getButton().getData();
			final RecursoOrdem recursoOrdem = data.recursoOrdem;
			DialogWindow.messageSucess(getUI(),"Remover","Deseja Remover","Remover","Cancelar",false,true,true,new IMessage() {

				@Override
				public void ok() {
					try{
						client.deleteObject("/ordem/"+ordemServico.getId()+"/recurso",recursoOrdem.getId());
						defaultTable();
					}catch(Exception e){
						Notification.show("Falha ao remover");
					}
				}
				@Override
				public void discard() {}

				@Override
				public void cancel() {}
			});
		}        
	}

	public void visualizar(Object objeto){

	}	

	@Override
	public IRecurso commit() {
		return null;
	}

	@Override
	public void initData(IRecurso data) {}

	@Override
	public void modoView() {
	}

	@Override
	public void modoAdd() {
		serviceFormWindows = new SearchFormWindow<IRecurso>(client, OrdemResourceView.this,0,"recurso/busca",Recurso.class,
				new TransactionsContainerResource(),"Código","Recurso","TipoRecurso");
		getUI().addWindow(serviceFormWindows);
	}


	@Override
	public void commitWindows(int resultTag) throws ViewException {
		try {
			IRecurso recurso = serviceFormWindows.commit();

			RecursoOrdem recursoOrdem = new RecursoOrdem();
			recursoOrdem.setRecurso((Recurso<?>)recurso);
			recursoOrdem.setQuantidadeConsumida(new BigDecimal(1));
			try{
				recursoOrdem = ((OrdemServico)ordemServico).adicionarRecursoOrdem(client, recursoOrdem);
				data.addTransaction(recursoOrdem);
			}catch (Exception e) {
				tableService.refreshRowCache();
				Notification.show("Falha, Tente mais tarde: " + e.getMessage());
			}

		} catch (ViewException e) {

		}
	}

	class ItemView{
		public RecursoOrdem recursoOrdem;
		public com.vaadin.data.Item item;

		public ItemView(RecursoOrdem recursoOrdem, com.vaadin.data.Item item){
			this.recursoOrdem = recursoOrdem;
			this.item = item;
		}
	}


}