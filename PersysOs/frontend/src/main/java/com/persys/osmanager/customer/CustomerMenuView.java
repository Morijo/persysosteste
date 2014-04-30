package com.persys.osmanager.customer;

import java.util.Iterator;

import com.persys.osmanager.customer.contract.ContractView;
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

public class CustomerMenuView extends HorizontalLayout implements View{

	private static final long serialVersionUID = 1L;

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
		
		content.addComponent(new CustomerView(client));

		createNav("Cliente", content.getComponent(0));
		createNav("Contrato",new ContractView(client));
	
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
