package com.persys.osmanager.contact;

import com.persys.osmanager.componente.interfaces.IFormWindows;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.restmb.types.Contato;
import com.restmb.types.Usuario;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ContactWindow extends Window implements IFormWindows<Contato> {

	private static final long serialVersionUID = 1L;
	private ContactView contactView;
	private IFormWindows<?> iFormWindows;

	public ContactWindow(RestMBClient client, Usuario<?> usuario, IFormWindows<?> iFormWindows, final int resultTag) {

		this.iFormWindows = iFormWindows;
	
		center();
		setModal(true);
		setWidth("1040px");
		setHeight("620px");
		setCaption("Selecione Endereço");
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setClosable(true);
	
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();
		
		TabSheet abas = new TabSheet();
		abas.setHeight("470px");
		abas.setWidth("100%");
		contactView = new ContactView(client,usuario,this,0);
		contactView.modoLivroContato();
		abas.addTab(contactView,"Lista de contatos");
		
		mainLayout.addComponent(abas);
		mainLayout.setExpandRatio(abas, 1.0f);
		setContent(mainLayout);
		
		addCloseListener(new CloseListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				try {
					commitWindows(resultTag);
				} catch (ViewException e1) {
					e1.getMessage();
				}
			}
		});
	}

	public Contato commit() throws ViewException {
		return (Contato) contactView.commit();
	}
	
	public void initData(Contato contato){
		contactView.initData((com.restmb.types.Contato) contato);
	}

	@Override
	public void commitWindows(int resultTag) throws ViewException {
		iFormWindows.commitWindows(resultTag);
		close();
	}

	@Override
	public void modoView() {}

	@Override
	public void modoAdd() {}

}
