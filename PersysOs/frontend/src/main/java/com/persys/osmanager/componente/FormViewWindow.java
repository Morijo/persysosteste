package com.persys.osmanager.componente;

import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.restmb.types.RestMbType;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormViewWindow<T extends RestMbType<T>> extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IForm<T> formView = null;
	private T objeto;
	
	public FormViewWindow(final RestMBClient client, String nameForm, IForm formView, T objeto){
		
		this.formView = formView;
		
		center();
		setModal(true);
		setWidth("-1px");
		setHeight("-1px");
		setCaption(nameForm);
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setClosable(false);


		VerticalLayout verticalLayoutMain = new VerticalLayout();
		verticalLayoutMain.setSpacing(true);
		verticalLayoutMain.setMargin(true);
		setContent(verticalLayoutMain);
		
		formView.initData(objeto);
		formView.modoAdd();
		
		verticalLayoutMain.addComponent(formView);
		
		verticalLayoutMain.addComponent(footerLayout(client));
	}
	
	private HorizontalLayout footerLayout(final RestMBClient client) {
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
					objeto = formView.commit();
					objeto.salvar(client);
					Notification.show("Salvo com sucesso");
					close();
				} catch (ViewException e) {
					Notification.show(e.getMessage());
				}
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
				try{
					close();
				}catch(Exception e){}	
			}
		});
		footer.addComponent(cancel);
		footer.setComponentAlignment(cancel, Alignment.TOP_LEFT);
		return footer;
	}
	
	public T commit(){
		return objeto;
	}
}
