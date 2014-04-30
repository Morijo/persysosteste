/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.persys.osmanager.organization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import com.vaadin.navigator.Navigator;
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

public class OrganizationMenuView extends HorizontalLayout implements View{

    private static final long serialVersionUID = 1L;
    private CssLayout menu;
	private CssLayout content;
	private Navigator nav = null;
	private HashMap<String, Button> viewNameToMenuButton = new HashMap<String, Button>();
	
    @Override
    public void enter(ViewChangeEvent event) {
       
       setSpacing(true);
       setSizeFull();
       Locale vmLocale = Locale.getDefault();
       ResourceBundle bundle = ResourceBundle.getBundle("com/persys/frontend/organization-menu",vmLocale);

	   menu = new CssLayout();
	   content = new CssLayout();

	   addStyleName("main-view");
       addComponent(new VerticalLayout() {
                   /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

				// Sidebar
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
      
        nav = new Navigator(this.getUI(), content);
		nav.addView("/minhaorganizacao", OrganizationView.class);
		nav.addView("/unidade", UnidadeView.class);
		nav.addView("/departamento", DepartamentoView.class);
		
        createNav(bundle.getString("minhaorganizacao"),"minhaorganizacao", OrganizationView.class);
        createNav(bundle.getString("unidade"),"unidade", UnidadeView.class);
	    createNav(bundle.getString("departamento"),"departamento", DepartamentoView.class);
	    
		nav.navigateTo("/minhaorganizacao");
		
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
				event.getButton().addStyleName("selected-invert");
                if (!nav.getState().equals("/"+ route))
					nav.navigateTo("/"+route);
			}
		});

		menu.addStyleName("no-vertical-drag-hints");
		menu.addComponent(b);
		viewNameToMenuButton.put("/"+route, b);
	}
    
	 public void createNav(String name, Component view){
	     Button b = new NativeButton(name.toUpperCase());
        b.setData(view);
        b.addClickListener(new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void buttonClick(ClickEvent event) {
           	 clearMenuSelection();
                event.getButton().addStyleName("selected-invert");
                content.removeAllComponents();
                content.addComponent((Component)event.getButton().getData());
            }
        });

       menu.addComponent(b);
 }
	 
	  private void clearMenuSelection() {
	        for (@SuppressWarnings("deprecation")
			Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
	            Component next = it.next();
	            if (next instanceof NativeButton) {
	                next.removeStyleName("selected-invert");
	            } else if (next instanceof DragAndDropWrapper) {
	                // Wow, this is ugly (even uglier than the rest of the code)
	                ((DragAndDropWrapper) next).iterator().next()
	                        .removeStyleName("selected-invert");
	            }
	        }
	    }
   
}
