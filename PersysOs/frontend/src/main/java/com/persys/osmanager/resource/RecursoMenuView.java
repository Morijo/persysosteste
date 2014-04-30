package com.persys.osmanager.resource;

import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import com.persys.osmanager.dashboard.DashboardUI;
import com.restmb.RestMBClient;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 17/02/2013 Menu View Para Recurso
 */

public class RecursoMenuView extends HorizontalLayout implements View{

	private static final long serialVersionUID = 1L;

	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/resource",
				Locale.getDefault());
	}

	private CssLayout menu = new CssLayout();
	private CssLayout content = new CssLayout();

	@Override
	public void enter(ViewChangeEvent event) {

		RestMBClient client =  ((DashboardUI)getUI()).getClient();
		buildLayoutMain();
		buildMenu(client);
	}

	private void buildLayoutMain() {
		setSpacing(true);
		setSizeFull();
		addStyleName("main-view");
		addComponent(new VerticalLayout() {
			private static final long serialVersionUID = 1L;

			{
				addStyleName("sidebar-invert");
				setWidth(null);
				setHeight("100%");
				menu.addStyleName("menu");
				menu.setHeight("100%");
				addComponent(menu);
				setExpandRatio(menu, 1);

			}
		});

		addComponent(content);
		content.setSizeFull();
		setExpandRatio(content, 1);  

	}

	private void buildMenu(RestMBClient client) {
		try{
		content.addComponent(new MaterialView(client));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		createNav(bundle.getString("material"), content.getComponent(0));
		createNav(bundle.getString("equipment"),new EquipamentoView(client));
		createNav(bundle.getString("device"), new DispositivoView(client));
		createNav(bundle.getString("measure"), new MedidaView(client));
		menu.getComponent(0).addStyleName("selected-invert");
	}

	public void createNav(String name, Component view){
		Button buttonSelection = new NativeButton(name.toUpperCase());
		buttonSelection.setData(view);
		buttonSelection.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				clearMenuSelection();
				event.getButton().addStyleName("selected-invert");
				content.removeAllComponents();
				content.addComponent((Component)event.getButton().getData());
			}
		});

		menu.addComponent(buttonSelection);
	}

	private void clearMenuSelection() {
		for (@SuppressWarnings("deprecation")
		Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
			Component next = it.next();
			if (next instanceof NativeButton) {
				next.removeStyleName("selected-invert");
			} else if (next instanceof DragAndDropWrapper) {
				((DragAndDropWrapper) next).iterator().next()
				.removeStyleName("selected-invert");
			}
		}
	}
}
