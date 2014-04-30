package com.persys.osmanager.componente;

import com.persys.osmanager.componente.interfaces.IMessage;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class DialogWindow {

	public static Window messageSucess(UI ui, String title,String messageValue,String titleOk,String titleDiscard, boolean cancelActive, boolean okActive, boolean discardActive, final IMessage messageSource){
		VerticalLayout l = new VerticalLayout();
		l.setSpacing(true);


		final Window alert = new Window(title, l);
		alert.setModal(true);
		alert.setResizable(false);
		alert.setDraggable(false);
		alert.addStyleName("dialog");
		alert.setClosable(false);

		Label message = new Label(messageValue);
		l.addComponent(message);
		message.setWidth("300px");

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setWidth("100%");
		buttons.setSpacing(true);
		l.addComponent(buttons);

		Button discard = new Button(titleDiscard);
		discard.addStyleName("small");
		discard.setVisible(discardActive);
		discard.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				alert.close();
				messageSource.discard();
			}

		});
		buttons.addComponent(discard);
		buttons.setExpandRatio(discard, 1);

		Button cancel = new Button("Cancelar");
		cancel.setVisible(cancelActive);
		cancel.addStyleName("small");
		cancel.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				alert.close();
			}
		});
		buttons.addComponent(cancel);

		Button ok = new Button(titleOk);
		ok.setVisible(okActive);
		ok.addStyleName("default");
		ok.addStyleName("small");
		ok.addStyleName("wide");
		ok.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				alert.close();
				messageSource.ok();
			}
		});
		buttons.addComponent(ok);
		ok.focus();

		alert.addShortcutListener(new ShortcutListener("Cancel",
				KeyCode.ESCAPE, null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				alert.close();
				messageSource.cancel();
			}
		});

		ui.addWindow(alert);
		return alert;
	}

}
