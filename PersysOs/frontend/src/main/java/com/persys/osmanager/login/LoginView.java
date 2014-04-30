package com.persys.osmanager.login;

import java.util.ResourceBundle;

import br.com.exception.ServiceException;
import br.com.model.PreconditionsModel;
import com.persys.osmanager.dashboard.DashboardUI;
import com.persys.osmanager.dashboard.HelpManager;
import com.persys.osmanager.dashboard.HelpOverlay;
import com.persys.osmanager.organization.install.InstallOrganizationView;
import com.persys.osmanager.user.data.TransactionsContainerUser;
import com.restmb.RestMBClient;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class LoginView extends Window{

	private static final long serialVersionUID = 1L;

	private DashboardUI dDashboardUI;
	private ResourceBundle bundle;
	private HelpManager helpManager;
	private VerticalLayout   fieldsUserPwdSign = new VerticalLayout();
	private HorizontalLayout fieldsEmailRestore = new HorizontalLayout();
	private Button bunttonRestorePwd;
	private Label labelError;

	public LoginView(DashboardUI dashboardUI, ResourceBundle bundle, HelpManager helpManager){
		this.dDashboardUI = dashboardUI;
		this.bundle = bundle;
		this.helpManager = helpManager;

		addStyleName("login");

		buildLoginView();
	}

	private void buildLoginView() {

		final VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setSizeFull();
		loginLayout.addStyleName("login-layout");
		dDashboardUI.root.addComponent(loginLayout);

		final CssLayout loginPanel = new CssLayout();
		loginPanel.addStyleName("login-panel");

		createLoginTitle(loginPanel);
		createLayoutUserPwdSign(loginLayout,loginPanel);
		createLayoutEmailRestore(loginLayout,loginPanel);
		createErrorRestore(loginPanel);

		loginLayout.addComponent(loginPanel);
		loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	}

	private void createLoginTitle(final CssLayout loginPanel) {
		HorizontalLayout labels = new HorizontalLayout();
		labels.setWidth("100%");
		labels.setMargin(true);
		labels.addStyleName("labels");
		loginPanel.addComponent(labels);


		Label welcome = new Label( bundle.getString("entity"));
		welcome.setSizeUndefined();
		welcome.addStyleName("h4");
		labels.addComponent(welcome);
		labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

		Label title = new Label(bundle.getString("title"));
		title.setSizeUndefined();
		title.addStyleName("h2");
		title.addStyleName("light");
		labels.addComponent(title);
		labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);
	}

	private void createErrorRestore(final CssLayout loginPanel) {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();

		labelError = new Label("",ContentMode.HTML);
		labelError.setSizeUndefined();
		labelError.addStyleName("light");
		labelError.addStyleName("error");
		labelError.setVisible(false);

		verticalLayout.addComponent(labelError);
		verticalLayout.addComponent(layoutRestoreNewAccount());
	
		loginPanel.addComponent(verticalLayout);
	}

	private HorizontalLayout layoutRestoreNewAccount() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		
		Button bunttonNewAccount = new Button(bundle.getString("newaccount"));
		bunttonNewAccount.addStyleName(Reindeer.BUTTON_LINK);
		bunttonNewAccount.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				dDashboardUI.root.removeAllComponents();
				dDashboardUI.root.addComponent(new InstallOrganizationView(bundle));
			}
		});
		horizontalLayout.addComponent(bunttonNewAccount);
		horizontalLayout.setComponentAlignment(bunttonNewAccount, Alignment.BOTTOM_LEFT);

		
		bunttonRestorePwd = new Button(bundle.getString("passwordrestore"));
		bunttonRestorePwd.addStyleName(Reindeer.BUTTON_LINK);
		bunttonRestorePwd.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fieldsUserPwdSign.setVisible(false);
				fieldsEmailRestore.setVisible(true);
				bunttonRestorePwd.setVisible(false);
				labelError.setVisible(false);
			}
		});
		horizontalLayout.addComponent(bunttonRestorePwd);
		horizontalLayout.setComponentAlignment(bunttonRestorePwd, Alignment.BOTTOM_RIGHT);
		return horizontalLayout;
	}

	private void createLayoutUserPwdSign(
			final VerticalLayout loginLayout, final CssLayout loginPanel) {

		fieldsUserPwdSign.setSpacing(true);
		fieldsUserPwdSign.setMargin(true);
		fieldsUserPwdSign.addStyleName("fields");

		final TextField username = new TextField();
		username.setInputPrompt(bundle.getString("username"));
		username.setWidth("280px");
		username.setHeight("32px");
		username.focus();
		fieldsUserPwdSign.addComponent(username);

		final PasswordField password = new PasswordField();
		password.setInputPrompt(bundle.getString("password"));
		password.setWidth("280px");
		password.setHeight("32px");
		fieldsUserPwdSign.addComponent(password);

		final Button signin = new Button(bundle.getString("sign"));
		signin.addStyleName("default");
		signin.setWidth("280px");
		signin.setHeight("40px");

		fieldsUserPwdSign.addComponent(signin);
		fieldsUserPwdSign.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

		final ShortcutListener enter = new ShortcutListener(bundle.getString("sign"),
				KeyCode.ENTER, null) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				signin.click();
			}
		};

		signin.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validaLogin(loginLayout, loginPanel, username, password);
			}
		});

		signin.addShortcutListener(enter);
		loginPanel.addComponent(fieldsUserPwdSign);

	}

	private void createLayoutEmailRestore(
			final VerticalLayout loginLayout, final CssLayout loginPanel) {

		fieldsEmailRestore.setSpacing(true);
		fieldsEmailRestore.setMargin(true);
		fieldsEmailRestore.setVisible(false);

		fieldsEmailRestore.addStyleName("fields");

		final TextField email = new TextField();
		email.setInputPrompt(bundle.getString("email"));
		email.setWidth("280px");
		email.setHeight("32px");
		email.focus();
		fieldsEmailRestore.addComponent(email);

		final Button send = new Button(bundle.getString("send"));
		send.addStyleName("default");
		send.setHeight("32px");
		fieldsEmailRestore.addComponent(send);
		fieldsEmailRestore.setComponentAlignment(send, Alignment.BOTTOM_LEFT);

		send.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try{
					PreconditionsModel.checkValidEmail(email.getValue().toString(),bundle.getString("emailinvalid"));
					br.com.usuario.model.Usuario.recuparaSenhaPorEmail(email.getValue().toString());
					Notification.show(bundle.getString("msgpwd"));
					backLogin();
				}catch(Exception e){
					Notification.show(bundle.getString("msgpwdfail"));
					labelError.setValue(e.getMessage());
					labelError.setVisible(true);
				}	
			}
		});

		final Button cancel = new Button(bundle.getString("cancelbutton"));
		cancel.addStyleName("cancel");
		cancel.setHeight("32px");
		fieldsEmailRestore.addComponent(cancel);

		cancel.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				backLogin();
			}
		});
		loginPanel.addComponent(fieldsEmailRestore);
	}


	private void validaLogin(final VerticalLayout loginLayout,
			final CssLayout loginPanel, final TextField username,
			final PasswordField password) {

		try {
			RestMBClient restMBClient = dDashboardUI.getClient();
			entrar(loginLayout,TransactionsContainerUser.
					login(restMBClient,username.getValue(), password.getValue(),TransactionsContainerUser.MODOBD));
			
		} catch (ServiceException e1) {
			createMsgHelpLogin(e1.getMessage());//cria a windows Persys Login
			labelError.setVisible(true);
			labelError.setValue(e1.getMessage()); //mensagem em vermelho
			username.focus();
		}
	}

	private void entrar(final VerticalLayout loginLayout, br.com.model.interfaces.IUsuario usuario) {
		if(usuario.getStatusModel() == 1){
			helpManager.closeAll();
			removeStyleName("login");
			dDashboardUI.root.removeComponent(loginLayout);
			dDashboardUI.buildMainView(usuario);
		}else{
			createMsgHelpLogin("Usuário inativo");
		}	
	}

	private void createMsgHelpLogin(String msg) {
		helpManager.closeAll();
		HelpOverlay helpOverlay = helpManager
				.addOverlay(
						"Persys Login",
						"<p>"+ msg +
						"<p>"+ bundle.getString("help") +" <a href=\"http://persys.com.br\">Persys TI</a>.</p>",
						"login");

		helpOverlay.center();
		
		dDashboardUI.addWindow(helpOverlay);
	}

	private void backLogin() {
		fieldsUserPwdSign.setVisible(true);
		fieldsEmailRestore.setVisible(false);
		bunttonRestorePwd.setVisible(true);
		labelError.setVisible(false);
	}
}
