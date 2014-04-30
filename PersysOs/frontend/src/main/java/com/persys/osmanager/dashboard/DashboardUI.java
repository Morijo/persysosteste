package com.persys.osmanager.dashboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IGrupoUsuario;
import br.com.principal.helper.UrlHelper;
import br.com.usuario.model.GrupoUsuarioPermissao;

import com.persys.osmanager.componente.NotificationUpdater;
import com.persys.osmanager.customer.CustomerMenuView;
import com.persys.osmanager.data.NotificationWorker;
import com.persys.osmanager.data.MyConverterFactory;
import com.persys.osmanager.employee.EmployeeView;
import com.persys.osmanager.install.InstallView;
import com.persys.osmanager.login.LoginView;
import com.persys.osmanager.order.OrderMenuView;
import com.persys.osmanager.organization.OrganizationMenuView;
import com.persys.osmanager.product.ProdutoView;
import com.persys.osmanager.resource.RecursoMenuView;
import com.persys.osmanager.schedule.ScheduleView;
import com.persys.osmanager.service.ServiceMenuView;
import com.persys.osmanager.system.SystemMenuView;
import com.persys.osmanager.system.data.TransactionsContainerGrupo;
import com.persys.osmanager.tracker.Tracker2View;
import com.persys.osmanager.user.UserWindow;
import com.persys.osmanager.user.data.TransactionsContainerUser;
import com.persys.osmanager.vehicle.VehicleView;
import com.restmb.DefaultClient;
import com.restmb.RestMBClient;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.Transferable;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

@Push
@Theme("dashboard")
@Title("Persys Dashboard")
public class DashboardUI extends UI implements NotificationUpdater{

	private static final long serialVersionUID = 1L;

	public CssLayout root = new CssLayout();

	private HorizontalLayout menu = new HorizontalLayout();
	private CssLayout content = new CssLayout();
	private HashMap<String, Button> viewNameToMenuButton = new HashMap<String, Button>();
	private Navigator nav = null;
	private HelpManager helpManager = null;

	private String numNotification = "";
	private RestMBClient client = null;

	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/dashboard",Locale.getDefault());
	}

	@Override
	protected void init(VaadinRequest request) {

		if(!br.com.principal.model.Aplicacao.isAtivo()){
			root.addComponent(new InstallView(bundle));
		}
		else{
			configureView();
		}

		root.setSizeFull();

		setContent(root);
		addStyleName("login");
	}

	private void configureView() {

		getSession().setConverterFactory(new MyConverterFactory());

		client = new DefaultClient(UrlHelper.END_POINT_SERVICE);
		VaadinSession.getCurrent().setAttribute("clientREST", client);

		Locale BRAZIL = new Locale("pt","BR"); 
		setLocale(BRAZIL);

		root.addStyleName("root");

		Label bg = new Label();
		bg.setSizeUndefined();
		bg.addStyleName("login-bg");
		root.addComponent(bg);

		root.removeAllComponents();

		helpManager = new HelpManager();
		new LoginView(this,bundle,helpManager);
	}

	public void buildMainView(final br.com.model.interfaces.IUsuario usuario) {

		root.addComponent(new VerticalLayout() {
			private static final long serialVersionUID = 1L;

			{
				setSizeFull();
				addStyleName("main-view");
				addComponent(new HorizontalLayout() {
					private static final long serialVersionUID = 1L;

					{
						addStyleName("sidebar");
						setWidth("100%");
						setHeight(null);

						addComponent(new CssLayout() {

							private static final long serialVersionUID = 1L;

							{
								addStyleName("branding");
								Label logo = new Label(
										"OS Manager",
										ContentMode.HTML);
								logo.setStyleName("h1");
								logo.setSizeUndefined();
								addComponent(logo);
							}
						});

						addComponent(menu);
						setExpandRatio(menu, 1);

						addComponent(createMenuUser(usuario));
					}
				});
				addComponent(content);
				content.setSizeFull();
				content.addStyleName("view-content");
				setExpandRatio(content, 1);
			}

		});

		menu.removeAllComponents();

		createMenuNav(usuario);

		menu.addStyleName("menu");
		menu.setWidth("-1px");

		final NotificationWorker dbWorker = new NotificationWorker();
		dbWorker.fetchAndUpdateDataWith(this, client.getOauth().getApiKey());
		viewNameToMenuButton.get("/dashboard").setHtmlContentAllowed(true);

		String f = Page.getCurrent().getUriFragment();
		if (f != null && f.startsWith("!")) {
			f = f.substring(1);
		}
		nav.navigateTo("/dashboard");
		menu.getComponent(0).addStyleName("selected");

		nav.addViewChangeListener(new ViewChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				helpManager.closeAll();
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				View newView = event.getNewView();
				helpManager.showHelpFor(newView);
				if (autoCreateReport && newView instanceof ReportsView) {
					((ReportsView) newView).autoCreate(2, items, transactions);
				}
				autoCreateReport = false;
			}
		});

	}

	private VerticalLayout createMenuUser(
			final br.com.model.interfaces.IUsuario usuario) {
		return new VerticalLayout() {
			private static final long serialVersionUID = 1L;

			{
				setHeight("60px");
				setWidth("60px"); 

				Command cmd = new Command() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(
							MenuItem selectedItem) {
						Notification
						.show(selectedItem.getText());
						if(selectedItem.getText().contentEquals("Sair")){
							getUI().getPage().reload();
						}else{
							addWindow(new UserWindow(usuario.getId()));
						}
					}
				};
				MenuBar settings = new MenuBar();

				MenuItem settingsMenu;

				URL url= null;
				try {
					url = new URL(UrlHelper.END_POINT_SERVICE+"/funcionario/"+usuario.getId()+"/imagem?x=40&y=40");
					url.openStream();
					ExternalResource externalResource = new ExternalResource(url);
					settingsMenu = settings.addItem("",externalResource,null);
				} catch (Exception ex) {
					settingsMenu = settings.addItem("",new ThemeResource("img/profile-pic.png"),null);
				}
				settingsMenu.addItem(usuario.getNomeUsuario(), cmd);
				settingsMenu.addSeparator();
				settingsMenu.addItem("Sair", cmd);
				addComponent(settings);
				setComponentAlignment(settings, Alignment.MIDDLE_CENTER);

			}
		};
	}

	private void createMenuNav(br.com.model.interfaces.IUsuario usuario) {
		nav = new Navigator(this, content);
		setDashboard();

		IGrupoUsuario grupo = TransactionsContainerUser.pesquisaGrupoUsuario(usuario.getId());
		if(grupo.getAdministrado()){

			setOrganization();
			setOrder();
			setSchedule();
			setTracker();
			setEmployees();
			setCustomer();
			setResource();
			setVeiculo();
			setService();
			setProduct();
			setReport();
			setSystem();
		}else {
			ArrayList<GrupoUsuarioPermissao> grupoUsuarioPermissaos = 
					TransactionsContainerGrupo.listaPermissao(grupo.getId());

			if(grupoUsuarioPermissaos.get(1).getStatusModel() == 1)
				setOrganization();
			if(grupoUsuarioPermissaos.get(2).getStatusModel() == 1)
				setOrder();
			if(grupoUsuarioPermissaos.get(3).getStatusModel() == 1)
				setSchedule();
			if(grupoUsuarioPermissaos.get(5).getStatusModel() == 1)
				setTracker();
			if(grupoUsuarioPermissaos.get(5).getStatusModel() == 1)
				setEmployees();
			if(grupoUsuarioPermissaos.get(6).getStatusModel() == 1)
				setCustomer();
			if(grupoUsuarioPermissaos.get(7).getStatusModel() == 1)
				setResource();

			setVeiculo();

			if(grupoUsuarioPermissaos.get(8).getStatusModel() == 1)
				setService();
			if(grupoUsuarioPermissaos.get(9).getStatusModel() == 1)
				setProduct();
		}
	}

	private void setSystem() {
		nav.addView("/cog", SystemMenuView.class);
		createNav("Sistema","cog", SystemMenuView.class);
	}

	private void setReport() {
		nav.addView("/reports", ReportsView.class);
		createNav("Relatorio","reports", ReportsView.class);
	}

	private void setProduct() {
		nav.addView("/product", ProdutoView.class);
		createNav("Produto","product", ProdutoView.class);
	}

	private void setService() {
		nav.addView("/service", ServiceMenuView.class);
		createNav("Serviço","service", ServiceMenuView.class);
	}

	private void setResource() {
		nav.addView("/resource", RecursoMenuView.class);
		createNav("Recurso","resource", RecursoMenuView.class);
	}

	private void setCustomer() {
		nav.addView("/customer", CustomerMenuView.class);
		createNav("Cliente","customer", CustomerMenuView.class);
	}

	private void setEmployees() {
		nav.addView("/employees", EmployeeView.class);
		createNav("Empregado","employees", EmployeeView.class);
	}

	private void setTracker() {
		nav.addView("/tracker", Tracker2View.class);
		createNav("Rastreador","tracker", Tracker2View.class);
	}

	private void setSchedule() {
		nav.addView("/schedule", ScheduleView.class);
		createNav("Agenda","schedule", ScheduleView.class);
	}

	private void setOrder() {
		nav.addView("/order", OrderMenuView.class);
		createNav("Ordem de Serviço","order", OrderMenuView.class);
	}

	private void setOrganization() {
		nav.addView("/organization", OrganizationMenuView.class);
		createNav("Organização","organization", OrganizationMenuView.class);
	}

	private void setDashboard() {
		nav.addView("/dashboard", DashboardView.class);
		createNav("Dashboard","dashboard",DashboardView.class);
	}

	private void setVeiculo() {
		nav.addView("/fleet",VehicleView.class);
		createNav("Frota","fleet",VehicleView.class);
	}

	public void createNav(String name, final String route, final Class<? extends View> view){
		Button b = new NativeButton(name.toUpperCase());
		b.addStyleName("icon-" + route.toLowerCase());
		b.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				clearMenuSelection();
				event.getButton().addStyleName("selected");
				try{
					if (!nav.getState().equals("/"+ route))
						nav.navigateTo("/"+route);
				}catch (Exception e) {}	
			}
		});

		menu.addStyleName("no-vertical-drag-hints");
		menu.addComponent(b);
		viewNameToMenuButton.put("/"+route, b);
	}

	private Transferable items;

	private void clearMenuSelection() {
		for (@SuppressWarnings("deprecation")
		Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
			Component next = it.next();
			if (next instanceof NativeButton) {
				next.removeStyleName("selected");
			} else if (next instanceof DragAndDropWrapper) {
				// Wow, this is ugly (even uglier than the rest of the code)
				((DragAndDropWrapper) next).iterator().next()
				.removeStyleName("selected");
			}
		}
	}

	void updateReportsButtonBadge(String badgeCount) {
		viewNameToMenuButton.get("/reports").setHtmlContentAllowed(true);
		viewNameToMenuButton.get("/reports").setCaption(
				"Reports<span class=\"badge\">" + badgeCount + "</span>");
	}

	void clearDashboardButtonBadge() {
		viewNameToMenuButton.get("/dashboard").setCaption("Dashboard");
	}

	boolean autoCreateReport = false;
	Table transactions;

	public void openReports(Table t) {
		transactions = t;
		autoCreateReport = true;
		nav.navigateTo("/reports");
		clearMenuSelection();
		viewNameToMenuButton.get("/reports").addStyleName("selected");
	}

	HelpManager getHelpManager() {
		return helpManager;
	}

	public RestMBClient getClient() {
		return client;
	}

	public void setClient(RestMBClient client) {
		this.client = client;
	}
	
	public String getNumNotification() {
		return numNotification;
	}

	public void setNumNotification(String numNotification) {
		this.numNotification = numNotification;
	}

	@Override
	public void updateLabel(final String string) {
		access(new Runnable() {
			@Override
			public void run() {
				viewNameToMenuButton.get("/dashboard").setCaption(
						"Dashboard<span class=\"badge\">"+ string+ "</span>");
				numNotification = string;
			}
		});
	}

}
