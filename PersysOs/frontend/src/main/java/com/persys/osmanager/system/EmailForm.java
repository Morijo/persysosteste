package com.persys.osmanager.system;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IEmailSMTP;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.exception.ViewException;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


public class EmailForm extends CustomComponent implements IForm<IEmailSMTP>{

	private static final long serialVersionUID = 1L;

	private TextField 		textFieldUsuario;
	private PasswordField 	textFieldSenha;
	private TextField 		textFieldEmailRemetente;
	private TextField 		textFieldHostName;
	private TextField 		textFieldSmtpPort;
	private IEmailSMTP 		emailSMTP = null;

	private final static ResourceBundle bundle;

	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/email",Locale.getDefault());
	}
	
	private final ComponenteFactory componenteFactory = new ComponenteFactory();


	public EmailForm() {

		setWidth("100.0%");
		setHeight("100.0%");
		setCompositionRoot(buildMainLayout());
	}

	private VerticalLayout buildMainLayout() {

		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		mainLayout.addComponent(buildHorizontalLayoutHostPort());
		mainLayout.addComponent(buildHorizontalLayoutEmailNomeDestinario());
		mainLayout.addComponent(buildHorizontalLayoutUsuarioSenha());

		return mainLayout;
	}

	private HorizontalLayout buildHorizontalLayoutEmailNomeDestinario() {

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setSpacing(true);

		textFieldHostName = componenteFactory.
				createTextFieldRequiredError(new TextField(), bundle.getString("hostname"), "Invalid");
		textFieldHostName.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldHostName.setHeight("-1px");
		horizontalLayout.addComponent(textFieldHostName);

		textFieldSmtpPort = componenteFactory.
				createTextFieldRequiredError(new TextField(), bundle.getString("port"), "Invalid");
		textFieldSmtpPort.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldSmtpPort.setHeight("-1px");
		horizontalLayout.addComponent(textFieldSmtpPort);

		return horizontalLayout;
	}

	private HorizontalLayout buildHorizontalLayoutHostPort() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setSpacing(true);

		textFieldEmailRemetente = componenteFactory.
				createTextFieldRequiredError(new TextField(), bundle.getString("emailsend"), "Invalid");
		textFieldEmailRemetente.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldEmailRemetente.setHeight("-1px");
		horizontalLayout.addComponent(textFieldEmailRemetente);

		return horizontalLayout;
	}

	private HorizontalLayout buildHorizontalLayoutUsuarioSenha() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);

		textFieldUsuario = componenteFactory.
				createTextFieldRequiredError(new TextField(), bundle.getString("user"), "Invalid");
		textFieldUsuario.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldUsuario.setHeight("-1px");
		horizontalLayout.addComponent(textFieldUsuario);

		textFieldSenha = new PasswordField();
		textFieldSenha.setCaption(bundle.getString("pwd"));
		textFieldSenha.setImmediate(true);
		textFieldSenha.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldSenha.setHeight("-1px");
		textFieldSenha.setNullRepresentation("");
		horizontalLayout.addComponent(textFieldSenha);
		return horizontalLayout;
	}

	@Override
	public IEmailSMTP commit() throws ViewException{
		try{
			componenteFactory.valida();
			emailSMTP.setHostName(textFieldHostName.getValue());
			emailSMTP.setEmailSend(textFieldEmailRemetente.getValue());
			emailSMTP.setUserName(textFieldUsuario.getValue());
			emailSMTP.setPwd(textFieldSenha.getValue());
			emailSMTP.setPort(Integer.parseInt(textFieldSmtpPort.getValue()));
			return emailSMTP;
		}catch (Exception e) {
			throw new ViewException(e.getMessage());
		}
	}

	@Override
	public void initData(IEmailSMTP data) {
		try{
			textFieldHostName.setValue(data.getHostName());
			textFieldEmailRemetente.setValue(data.getEmailSend());
			textFieldUsuario.setValue(data.getUserName());
			textFieldSenha.setValue(data.getPwd());
			textFieldSmtpPort.setValue(String.valueOf(data.getPort()));
		}catch(Exception e){}

		this.emailSMTP = data;
	}

	@Override
	public void modoView() {}

	@Override
	public void modoAdd() {
		textFieldHostName.setReadOnly(false);
	}
}
