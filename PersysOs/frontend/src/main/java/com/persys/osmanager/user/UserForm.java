package com.persys.osmanager.user;

import java.util.Locale;
import java.util.ResourceBundle;

import org.vaadin.csvalidation.CSValidator;

import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.usuario.model.Usuario;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.exception.ViewException;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class UserForm extends CustomComponent implements IForm<br.com.model.interfaces.IUsuario> {

	private static final long serialVersionUID = 1L;

	private TextField textFieldSenhaConfirmacao;
	private TextField textFieldSenha;
	private TextField textFieldUsuario;
	private TextField textFieldEmail;

	HorizontalLayout horizontalLayoutSenha = new HorizontalLayout();

	private final static ResourceBundle bundle;
	private final ComponenteFactory componenteFactory = new ComponenteFactory();
	private static String errorMessage;

	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/user",Locale.getDefault());
		errorMessage = bundle.getString("requirederror");
	}

	private br.com.model.interfaces.IUsuario usuario;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public UserForm() {
		setWidth(AttrDim.FORM_WIDTH);
		setHeight(AttrDim.FORM_HEIGHT);
		setImmediate(true);
		setCompositionRoot(buildMainLayout());
	}

	private VerticalLayout buildMainLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		textFieldUsuario = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("nick"), errorMessage, AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldUsuario.setImmediate(true);
		final CSValidator validator = new CSValidator();
		validator.extend(textFieldUsuario);
		
		mainLayout.addComponent(textFieldUsuario);

		textFieldEmail = componenteFactory.createTextFieldEmailRequiredError(new TextField(), "Email", errorMessage, AttrDim.FORM_COM_SINGLE_WIDTH);
		mainLayout.addComponent(textFieldEmail);

		mainLayout.addComponent(buildHorizontalLayoutSenha());

		return mainLayout;
	}

	private HorizontalLayout buildHorizontalLayoutSenha() {
		horizontalLayoutSenha.setImmediate(false);
		horizontalLayoutSenha.setWidth("-1px");
		horizontalLayoutSenha.setHeight("-1px");
		horizontalLayoutSenha.setMargin(false);
		horizontalLayoutSenha.setSpacing(true);

		textFieldSenha = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("pwd"), errorMessage);
		horizontalLayoutSenha.addComponent(textFieldSenha);

		textFieldSenhaConfirmacao = componenteFactory.createTextField(new TextField(), bundle.getString("pwdc"));
		horizontalLayoutSenha.addComponent(textFieldSenhaConfirmacao);

		return horizontalLayoutSenha;
	}

	@Override
	public br.com.model.interfaces.IUsuario commit() throws ViewException {
		try{
			if(!textFieldSenha.getValue().toString().contentEquals(textFieldSenhaConfirmacao.getValue().toString()))
				throw new ViewException(bundle.getString("messagepwd"));

			componenteFactory.valida();

			if(usuario == null){
				usuario = new Usuario();
			}
			usuario.setEmailPrincipal(textFieldEmail.getValue());
			usuario.setNomeUsuario(textFieldUsuario.getValue());
			usuario.setSenha(textFieldSenha.getValue());
			return usuario;

		}catch (Exception e) {
			throw new ViewException(e.getMessage());
		}
	}

	@Override
	public void initData(br.com.model.interfaces.IUsuario data) {
		textFieldUsuario.setValue(data.getNomeUsuario());
		textFieldEmail.setValue(data.getEmailPrincipal());
		usuario = data;
	}

	public void modoPassword(){
		textFieldUsuario.setReadOnly(true);
		textFieldEmail.setVisible(false);
	}
	
	@Override
	public void modoView() {
		horizontalLayoutSenha.setVisible(false);
	}

	@Override
	public void modoAdd() {
		textFieldUsuario.setReadOnly(false);
	}

}
