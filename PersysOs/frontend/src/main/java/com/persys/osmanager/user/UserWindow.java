package com.persys.osmanager.user;

import br.com.exception.ModelException;
import br.com.usuario.model.Usuario;

import com.persys.osmanager.exception.ViewException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UserWindow extends Window{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private UserForm userForm = new UserForm();

	public UserWindow(Long idusuario){

		this.usuario = Usuario.pesquisaID(idusuario);

		center();

		setCaption(usuario.getNomeUsuario());
		setWidth("-1px");
		setHeight("-1px");
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setClosable(false);

		VerticalLayout verticalLayout = new VerticalLayout();

		userForm.initData(usuario);
		userForm.modoAdd();

		verticalLayout.addComponent(userForm);
		verticalLayout.addComponent(footerLayout());
		
		setContent(verticalLayout);
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
				try {
					usuario = (Usuario) userForm.commit();
				} catch (ViewException e) {}
				validaSenha();
			}
		});
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

		Button cancel = new Button("Fechar");
		cancel.addStyleName("wide");
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

	private void validaSenha(){

		try {
			usuario.alteraSenha();
			Notification.show("Senha alterada");
		} catch (ModelException e) {
			Notification.show(e.getMessage());
		}
	}
	
	public void modoPassword(){
		userForm.modoPassword();
	}
}

