package com.persys.osmanager.componente;

import java.io.File;
import java.net.URL;

import org.tepi.filtertable.paged.PagedFilterTable;
import org.vaadin.resetbuttonfortextfield.ResetButtonForTextField;

import com.github.wolfie.refresher.Refresher;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.componente.interfaces.IImage;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomTable.RowHeaderMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

public abstract class FormView extends VerticalSplitPanel implements IImage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Button buttonAdicionar;
	protected Button buttonSalvar;
	protected Button buttonEditar;
	protected Button buttonVoltar;
	protected Button buttonAlterarSenha;
	protected Button buttonDeletar;

	protected Label title;
	protected TextField filter;
	protected Image profilePic;
	protected PagedFilterTable<IndexedContainer> filterTable= null;

	private VerticalLayout modoLayoutAdd;
	private VerticalLayout modoLayoutTable;
	private VerticalLayout modoLayoutView;
	private Component      modoLayoutAtivo = null;


	public FormView()  {

		setWidth("100%");
		setHeight("100%");
		setSplitPosition(55, Unit.PIXELS);
		setLocked(true);
		setFirstComponent(buildHorizontalLayoutOne());

	}

	public FormView(String labelName)  {

		setWidth("100%");
		setHeight("100%");
		setSplitPosition(55, Unit.PIXELS);
		setLocked(true);
		setFirstComponent(buildHorizontalLayoutOne());

	}

	private void modoVisualizarViewInteral(Component component, String titleValue){

		if(getComponent() != null)
			removeComponent(getComponent());

		if(modoLayoutView != null){
			modoLayoutView.removeAllComponents();
			modoLayoutView.addComponent(component);
		}else{
			modoLayoutView = new VerticalLayout();
			modoLayoutView.addComponent(component);
		}

		if(component instanceof IForm<?>){
			((IForm<?>)component).modoView();
		}

		addComponent(modoLayoutView);
		setComponent(modoLayoutView);

		title.setVisible(true);
		title.setValue(titleValue);

		filter.setVisible(false);
		buttonAdicionar.setVisible(false); 	
		buttonDeletar.setVisible(true);
		buttonEditar.setVisible(true);
		buttonSalvar.setVisible(false);
		buttonVoltar.setVisible(true);
	}

	public void modoVisualizarView(Component component, String titleValue){
		modoVisualizarViewInteral(component, titleValue);
		profilePic.setVisible(false);

	}
	public void modoVisualizarView(Component component, String titleValue, String imagem){

		URL url= null;
		try {
			profilePic.setSource(null);
			url = new URL(imagem);
			url.openStream();
			profilePic.setSource(new ExternalResource(url));
		} catch (Exception ex) {
			profilePic.setSource(new ThemeResource("img/noimage.png"));
		}

		modoVisualizarViewInteral(component, titleValue);
		profilePic.setVisible(true);
		profilePic.setWidth("52px");
	}

	public void modoTabelaView(Component component, String titleValue, Boolean filterFlag){
		modoTabelaView(component, titleValue);
		filter.setVisible(filterFlag);
	}

	public void modoTabelaView(Component component, String titleValue){
		if(getComponent() != null)
			removeComponent(getComponent());

		if(modoLayoutTable != null)
			modoLayoutTable.addComponent(component);
		else{
			modoLayoutTable = new VerticalLayout();
			modoLayoutTable.addComponent(component);
		}

		addComponent(modoLayoutTable);
		setComponent(modoLayoutTable);

		title.setVisible(true);
		title.setValue(titleValue);
		profilePic.setVisible(false);
		filter.setVisible(true);
		buttonVoltar.setVisible(false);
		buttonEditar.setVisible(false);
		buttonSalvar.setVisible(false);
		buttonAdicionar.setVisible(true);
		buttonDeletar.setVisible(false);

	}

	public final void modoAdicionarView(Component component, String titleValue){
		if(getComponent() != null)
			removeComponent(getComponent());

		if(modoLayoutAdd != null){
			modoLayoutAdd.removeAllComponents();
			modoLayoutAdd.addComponent(component);
		}else{
			modoLayoutAdd = new VerticalLayout();
			modoLayoutAdd.addComponent(component);
		}

		if(component instanceof IForm<?>){
			((IForm<?>)component).modoAdd();
		}

		addComponent(modoLayoutAdd);
		setComponent(modoLayoutAdd);

		title.setVisible(true);
		title.setValue(titleValue);

		filter.setVisible(false);
		buttonAdicionar.setVisible(false); 	
		buttonDeletar.setVisible(false);
		buttonEditar.setVisible(false);
		buttonSalvar.setVisible(true);
		buttonVoltar.setVisible(true);
	}

	private HorizontalLayout buildHorizontalLayoutOne() {
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidth("100%");
		toolbar.setHeight("55px");
		toolbar.setSpacing(true);
		toolbar.setMargin(new MarginInfo(false, true, false, true));

		toolbar.addComponent(createLayoutLeft());

		HorizontalLayout cr = createLayoutRight();
		toolbar.addComponent(cr);
		toolbar.setComponentAlignment(cr, new Alignment(34));

		return toolbar;
	}

	private HorizontalLayout createLayoutLeft() {

		HorizontalLayout toolbarLeft = new HorizontalLayout();
		toolbarLeft.setWidth("130%");
		toolbarLeft.setHeight("55px");
		toolbarLeft.addStyleName("toolbar");
		toolbarLeft.setSpacing(true);

		profilePic = new Image(null,
				new ThemeResource("img/noimage.png"));
		profilePic.setWidth("52px");
		profilePic.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				com.persys.osmanager.componente.ImageWindows imageView = new com.persys.osmanager.componente.ImageWindows(FormView.this);
				getUI().addWindow(imageView);
			}
		}); 

		profilePic.setVisible(false);
		toolbarLeft.addComponent(profilePic);
		toolbarLeft.setComponentAlignment(profilePic, new Alignment(33));

		title = new Label();
		title.addStyleName("h1");
		title.setWidth("100%");
		toolbarLeft.addComponent(title);
		toolbarLeft.setComponentAlignment(title, new Alignment(33));
		toolbarLeft.setExpandRatio(title, 0.13f);

		return toolbarLeft;
	}

	private HorizontalLayout createLayoutRight() {
		HorizontalLayout toolbarRight = new HorizontalLayout();
		toolbarRight.setWidth("-1px");
		toolbarRight.setHeight("55px");
		toolbarRight.addStyleName("toolbar");
		toolbarRight.setSpacing(true);

		int[] modifierKey = {ModifierKey.SHIFT};
		
		filter = new TextField();
		filter.setInputPrompt("Filtro");
		ResetButtonForTextField.extend(filter);
		toolbarRight.addComponent(filter);
		toolbarRight.setExpandRatio(filter, 1);
		toolbarRight.setComponentAlignment(filter, new Alignment(34));

		buttonVoltar = createButtonOne("Voltar");
		buttonVoltar.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				voltar();
			}
		});
		toolbarRight.addComponent(buttonVoltar);
		toolbarRight.setComponentAlignment(buttonVoltar, new Alignment(34));
		toolbarRight.setExpandRatio(buttonVoltar, 1.0f);

		final ShortcutListener shortcutVoltar = new ShortcutListener("",
				KeyCode.V, modifierKey) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				voltar();
			}
		};
		buttonVoltar.addShortcutListener(shortcutVoltar);

		
		buttonAdicionar = createButtonOne("Adicionar");
		buttonAdicionar.addStyleName("default");
		buttonAdicionar.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				adicionar();
			}
		});
		toolbarRight.addComponent(buttonAdicionar);
		toolbarRight.setComponentAlignment(buttonAdicionar, new Alignment(34));

		final ShortcutListener shortcutAdicionar = new ShortcutListener("",
				KeyCode.N, modifierKey) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				adicionar();
			}
		};
		buttonAdicionar.addShortcutListener(shortcutAdicionar);
		
		buttonDeletar = createButtonOne("Remover");
		buttonDeletar.addStyleName("remove");
		buttonDeletar.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				remover(event.getSource());
			}
		});
		toolbarRight.addComponent(buttonDeletar);
		toolbarRight.setComponentAlignment(buttonDeletar, new Alignment(34));
		
		final ShortcutListener shortcutDeletar = new ShortcutListener("",
				KeyCode.D, modifierKey) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				remover(null);
			}
		};
		buttonDeletar.addShortcutListener(shortcutDeletar);
		
		buttonEditar = createButtonOne("Salvar Edição");
		buttonEditar.addStyleName("default");
		buttonEditar.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				editar();
			}
		});
		toolbarRight.addComponent(buttonEditar);
		toolbarRight.setComponentAlignment(buttonEditar, new Alignment(34));
		final ShortcutListener shortcutEditar = new ShortcutListener("",
				KeyCode.S,modifierKey) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				salvar();
			}
		};
		buttonEditar.addShortcutListener(shortcutEditar);

		
		buttonSalvar = createButtonOne("Salvar");
		buttonSalvar.addStyleName("default");
		buttonSalvar.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				salvar();
			}
		});
		toolbarRight.addComponent(buttonSalvar);
		toolbarRight.setComponentAlignment(buttonSalvar, new Alignment(34));
		
		final ShortcutListener shortcutSalvar = new ShortcutListener("",
				KeyCode.S,modifierKey) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				salvar();
			}
		};
		buttonSalvar.addShortcutListener(shortcutSalvar);

		
		return toolbarRight;
	}

	private Button createButtonOne(String caption) {
		Button button = new Button(caption);
		button.setWidth("-1px");
		button.setHeight("-1px");
		button.setVisible(false);
		button.addStyleName("wide");
		return button;
	}

	public abstract void editar();
	public abstract void adicionar();
	public abstract void remover(Object target);
	public abstract void salvar(); 
	public abstract void visualizar(Object target);
	public abstract void defaultTable();
	public abstract void voltar();

	public void setComponent(Component componet){
		this.modoLayoutAtivo = componet;
		setSecondComponent(componet);
	} 
	public Component getComponent(){
		return this.modoLayoutAtivo; 
	}

	public void image(File file){
		profilePic.setSource(new FileResource(file));
	}

	public Window messageSucess(String title,String messageValue,String titleOk,String titleDiscard, boolean cancelActive, boolean okActive, boolean discardActive, final IMessage messageSource){
		return DialogWindow.messageSucess(getUI(), title, messageValue, titleOk, titleDiscard, cancelActive, okActive, discardActive, messageSource);
	}

	public void notificationTray(String title, String value){
		Notification
		.show(title,
				value,
				Type.TRAY_NOTIFICATION);

	}

	public void notificationError(String title, String value){
		Notification
		.show(title,
				value,
				Type.ERROR_MESSAGE);

	}

	public void addExtensionS(Refresher refresher){
		addExtension(refresher);
	}

	protected VerticalLayout buildPagedFilterTable(IndexedContainer data) {

		filterTable = new PagedFilterTable<IndexedContainer>();
		filterTable.setWidth("100%");
		filterTable.setHeight("-1px");

		filterTable.setStyleName("table-persys");
		filterTable.setFilterBarVisible(true);
		filterTable.setImmediate(true);
		filterTable.setSelectable(true);
		filterTable.setMultiSelect(false);
		filterTable.setRowHeaderMode(RowHeaderMode.INDEX);
		filterTable.setFooterVisible(true);
		filterTable.setColumnCollapsingAllowed(true);
		filterTable.setColumnReorderingAllowed(true);

		filterTable.setContainerDataSource(data);
		filterTable.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Item item = filterTable.getFilterable().getItem(filterTable.getValue());
				visualizar(item);
			} 
		});

		int[] modifierKey = {ModifierKey.SHIFT};
		
		final ShortcutListener shortcutTable = new ShortcutListener("",
				KeyCode.T,modifierKey) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				filterTable.focus();
			}
		};
		filterTable.addShortcutListener(shortcutTable);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setStyleName(Reindeer.TABLE_BORDERLESS);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);

		mainLayout.addComponent(filterTable.createControls());
		mainLayout.addComponent(filterTable);


		filterTable.buttonReload.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				defaultTable();
			}

		});

		defaultTable();
		return mainLayout;
	}
}
