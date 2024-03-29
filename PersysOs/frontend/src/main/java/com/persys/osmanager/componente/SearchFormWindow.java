package com.persys.osmanager.componente;

import java.util.List;

import org.tepi.filtertable.paged.PagedFilterTable;

import br.com.model.interfaces.IModel;

import com.persys.osmanager.componente.interfaces.IFormWindows;
import com.persys.osmanager.data.TransactionsContainer;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomTable.RowHeaderMode;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SearchFormWindow<T extends IModel> extends Window{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	private PagedFilterTable filterTable = null;
	private Button buttonBuscar;
	private TextField textFieldBusca;
	
	public  IFormWindows<?> formOrigin;
	private RestMBClient client = null;
	private OptionGroup group;
	private Object[] visibleColumns;
	private TransactionsContainer<?> data;
	private String recurso;
	private Class<?> classe;
	private int resultTag;
	private int modo = 1;
	
	public SearchFormWindow( 
			RestMBClient client,
			IFormWindows<?> formOrigin,
			int resultTag,
			String recurso,
			Class<?> classe,
			TransactionsContainer<?> data, 
			String... visibleColumns) {
		
		this.formOrigin = formOrigin;
		this.client = client;
		this.data = data;
		this.visibleColumns = visibleColumns;
		this.classe = classe;
		this.recurso = recurso;
		this.resultTag = resultTag;

		center();
		setCaption("Busca");
		setWidth("800px");
		setHeight("600px");;
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setClosable(false);
		addStyleName("no-vertical-drag-hints");
		addStyleName("no-horizontal-drag-hints");

		setContent(buildMainLayout());

	}

	@SuppressWarnings("deprecation")
	private VerticalSplitPanel buildMainLayout() {
		VerticalSplitPanel mainLayout = new VerticalSplitPanel();
		mainLayout.setImmediate(false);
		mainLayout.setLocked(true);
		mainLayout.setSplitPosition(90, Sizeable.UNITS_PERCENTAGE);
		mainLayout.setFirstComponent(createTable());
		mainLayout.setSecondComponent(footerLayout());
		
		return mainLayout;
	}

	@SuppressWarnings("rawtypes")
	private VerticalLayout createTable() {
		filterTable = new PagedFilterTable();
		filterTable.setStyleName("table-persys");
		filterTable.setFilterBarVisible(true);
		filterTable.setImmediate(true);
		filterTable.setSelectable(true);
		filterTable.setMultiSelect(false);
		filterTable.setRowHeaderMode(RowHeaderMode.INDEX);
		filterTable.setColumnCollapsingAllowed(true);
		filterTable.setColumnReorderingAllowed(true);
		filterTable.setWidth("100%");
		filterTable.setHeight("-1px");
		filterTable.setContainerDataSource(data);
		filterTable.setVisibleColumns(visibleColumns);
		VerticalLayout mainLayoutTable = new VerticalLayout();
		mainLayoutTable.setStyleName(Reindeer.TABLE_BORDERLESS);
		mainLayoutTable.setSpacing(true);
		mainLayoutTable.setMargin(true);

		mainLayoutTable.addComponent(buildHorizontalLayout());
		mainLayoutTable.addComponent(buildHorizontalLayoutOptions());
		mainLayoutTable.addComponent(filterTable);

		return mainLayoutTable;
	}

	private HorizontalLayout footerLayout() {
		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName("footer");
		footer.setWidth("100%");
		footer.setHeight("-1px");
		
		footer.setMargin(true);
		footer.setSpacing(true);

		Button ok = new Button("Salvar");
		ok.addStyleName("wide");
		ok.addStyleName("default");
		ok.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try{
					formOrigin.commitWindows(resultTag);
					close();
				} catch (ViewException e) {
				}
			}
		});
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

		Button cancel = new Button("Fechar");
		cancel.addStyleName("wide");
		cancel.addStyleName("delete");
		cancel.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		footer.addComponent(cancel);
		footer.setComponentAlignment(cancel, Alignment.TOP_LEFT);
		return footer;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setCaption("Parametros da Busca");
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("100%");
		horizontalLayout.setHeight("100%");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);

		// textFieldBusca
		textFieldBusca = new TextField();
		textFieldBusca.setImmediate(false);
		textFieldBusca.setInputPrompt("Termo da busca");
		textFieldBusca.setWidth("100%");
		textFieldBusca.setHeight("-1px");
		horizontalLayout.addComponent(textFieldBusca);
		horizontalLayout.setExpandRatio(textFieldBusca, 1.0f);

		buttonBuscar = new Button();
		buttonBuscar.setImmediate(false);
		buttonBuscar.setIcon(new ThemeResource("../reindeer/Icons/find2.png"));
		buttonBuscar.setWidth("60px");
		buttonBuscar.setHeight("-1px");

		final ShortcutListener shortcutBuscar = new ShortcutListener("",
				KeyCode.ENTER, null) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				buttonBuscar.click();
			}
		};

		buttonBuscar.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				defaultTable();
			}
		});
		buttonBuscar.addShortcutListener(shortcutBuscar);

		horizontalLayout.addComponent(buttonBuscar);

		return horizontalLayout;
	}

	@AutoGenerated
	private VerticalLayout buildHorizontalLayoutOptions() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setImmediate(false);
		verticalLayout.setWidth("100%");
		verticalLayout.setHeight("-1px");
		verticalLayout.setMargin(false);
		verticalLayout.setSpacing(true);
		verticalLayout.setStyleName("normal");

		group = new OptionGroup();
		group.setContainerDataSource(data.listParameter());
		group.setItemCaptionPropertyId("name");
		group.select(((List<?>)group.getItemIds()).get(0));
		verticalLayout.addComponent(group);

		return verticalLayout;
	}
	
	@SuppressWarnings("unchecked")
	public T commit() throws ViewException {
		try{
			Item item = filterTable.getFilterable().getItem(filterTable.getValue());
			return (T) item.getItemProperty("obj").getValue();
		}catch (Exception e) {
			throw new ViewException("Parâmetros invalidos");
		}
	}

	public void initData(String term){
		textFieldBusca.setValue(term);
	}

	public void defaultTable() {
		try{
			data.searchRest(client,recurso,classe,textFieldBusca.getValue().toString(), group.getValue().toString());
		}catch(Exception e){
			Notification.show("Sem resultado",Type.WARNING_MESSAGE);
		}
	}
	
	public int getModo() {
		return modo;
	}

	public void setModo(int modo) {
		this.modo = modo;
	}


	public static class Parameter{

		private String id;
		private String name;

		public Parameter(String id, String name){
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
}
