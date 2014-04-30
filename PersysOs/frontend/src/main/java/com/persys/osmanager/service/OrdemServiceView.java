package com.persys.osmanager.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import br.com.model.interfaces.IOrdem;
import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.DialogWindow;
import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IFormWindows;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.service.data.TransactionsContainerService;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.oauth.service.ParameterList;
import com.restmb.types.OrdemServico;
import com.restmb.types.RestMbType;
import com.restmb.types.Servico;
import com.restmb.types.ServicoOrdem;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 18/02/2013 View para ordem serviço
 */
public class OrdemServiceView extends CustomComponent implements IFormWindows<ServicoOrdem>{

	private static final long serialVersionUID = 1L;
	
	private Table tableService = null;
	private TransactionsContainer data = new TransactionsContainer();
	private SearchFormWindow<Servico> serviceFormWindows = null;
	private IOrdem ordemServico = null;
	private RestMBClient client = null;

	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}

	public OrdemServiceView(RestMBClient client, IOrdem ordemServico) {
		this.client = client;
		this.ordemServico = ordemServico;
	
		// top-level component properties
		setWidth(AttrDim.FORM_WIDTH);
		setHeight(AttrDim.FORM_HEIGHT);

		setCompositionRoot(buildMainLayout());
	}

	private VerticalLayout buildMainLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
	
		// buttonSalvar
		Button buttonAdicionar = new Button(bundle.getString("addservice"),new Button.ClickListener() {
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

		tableService.setVisibleColumns(new Object[] {bundle.getString("code"),bundle.getString("service"),bundle.getString("dateofassignment"),bundle.getString("situation")," ","  "});
		tableService.setColumnExpandRatio(bundle.getString("service"), 1f);

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
			Connection<ServicoOrdem> lista = RestMbType.listaAll(client,"/ordem/"+this.ordemServico.getId()+"/servico",ServicoOrdem.class);

			for(List<ServicoOrdem> listaServico : lista){
				for(ServicoOrdem servicoOrdem : listaServico){
					data.addTransaction(servicoOrdem);
				}
			}
		}catch (Exception e) {}
	}


	public class TransactionsContainer extends com.vaadin.data.util.IndexedContainer {

		private static final long serialVersionUID = 1L;

		public TransactionsContainer() {
			addContainerProperty(bundle.getString("id"), Long.class, -1);
			addContainerProperty(bundle.getString("code"), String.class, "");
			addContainerProperty(bundle.getString("service"), String.class, "");
			addContainerProperty(bundle.getString("situation"), ComboBox.class, new ComboBox());
			addContainerProperty(bundle.getString("dateofassignment"), Date.class, new Date());
			addContainerProperty(" ", Button.class, new Button());
			addContainerProperty("  ", Button.class, new Button());

		}

		@SuppressWarnings("unchecked")
		public void addTransaction(ServicoOrdem servicoOrdem) {
			Object id = addItem();
			com.vaadin.data.Item item = getItem(id);
			if (item != null) {
				item.getItemProperty(bundle.getString("id")).setValue(servicoOrdem.getServico().getId());
				item.getItemProperty(bundle.getString("code")).setValue(servicoOrdem.getServico().getCodigo());
				item.getItemProperty(bundle.getString("service")).setValue(servicoOrdem.getServico().getTitulo());
				item.getItemProperty(bundle.getString("dateofassignment")).setValue(servicoOrdem.getDataCriacao());
						
				ComboBox comboBox = ComponenteFactory.createComboboxStatus(servicoOrdem.getStatusModel());
				comboBox.setWidth("80");
				item.getItemProperty(bundle.getString("situation")).setValue(comboBox);

				Button save = new Button();
				save.setIcon(new ThemeResource("../reindeer/Icons/save.png"));
				save.setImmediate(true);
				save.setWidth("38px");
				save.setHeight("-1px");
				save.addStyleName("newicon");

				ItemView itemC = new ItemView(servicoOrdem, item);
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
			ItemView data = (ItemView) event.getButton().getData();
			
			ServicoOrdem servicoOrdem = data.servicoOrdem;
			
			ComboBox comboBoxStatus = (ComboBox) data.item.getItemProperty(bundle.getString("situation")).getValue();
		
			servicoOrdem.setStatusModel((Integer) comboBoxStatus.getValue());
			
			com.restmb.oauth.service.ParameterList headers = new ParameterList();
			headers.add("Content-Type", "application/json");
			client.publishChanges("/ordem/"+ordemServico.getId()+"/servico/"+servicoOrdem.getId(), ServicoOrdem.class,servicoOrdem.toJson(),headers);		
		}        
	}
	
	private class RemoveListener implements Button.ClickListener {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			ItemView data = (ItemView) event.getButton().getData();
			final ServicoOrdem servicoOrdem = data.servicoOrdem;
			DialogWindow.messageSucess(getUI(),bundle.getString("remove"),bundle.getString("wanttoremove"),bundle.getString("remove"),bundle.getString("cancel"),false,true,true,new IMessage() {

				@Override
				public void ok() {
					try{
						client.deleteObject("/ordem/"+ordemServico.getId()+"/servico",servicoOrdem.getId());
					}catch(Exception e){}
					defaultTable();
					
				}
				@Override
				public void discard() {}

				@Override
				public void cancel() {}
			});
		}        
	}

	public void visualizar(Object objeto){}	

	@Override
	public ServicoOrdem commit() {
		return null;
	}

	@Override
	public void initData(ServicoOrdem data) {}

	@Override
	public void modoView() {
	}

	@Override
	public void modoAdd() {
		serviceFormWindows = new SearchFormWindow<Servico>(client, OrdemServiceView.this,0,"servico/busca",Servico.class,
				new TransactionsContainerService(),bundle.getString("code"),bundle.getString("service"));
		getUI().addWindow(serviceFormWindows);
	}


	@Override
	public void commitWindows(int resultTag) throws ViewException {
		try {
			Servico servico = serviceFormWindows.commit();

			try{
				ServicoOrdem servicoOrdem = ((OrdemServico)ordemServico).adicionarServicoOrdem(client, servico);
				data.addTransaction(servicoOrdem);
			}catch (Exception e) {
				tableService.refreshRowCache();
				Notification.show(bundle.getString("failedtrylater") + e.getMessage());
			}

		} catch (ViewException e) {

		}
	}
	
	class ItemView{
		public ServicoOrdem servicoOrdem;
		public com.vaadin.data.Item item;
		
		public ItemView(ServicoOrdem servicoOrdem, com.vaadin.data.Item item){
			this.servicoOrdem = servicoOrdem;
			this.item = item;
		}
	}


}
