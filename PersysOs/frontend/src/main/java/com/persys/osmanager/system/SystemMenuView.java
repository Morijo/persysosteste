/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.persys.osmanager.system;
import java.util.Iterator;

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

public class SystemMenuView extends HorizontalLayout implements View{

    private static final long serialVersionUID = 1L;
   
	private CssLayout menu;
	private CssLayout content;
 
    @Override
    public void enter(ViewChangeEvent event) {
    	
    	RestMBClient restMBClient =  ((DashboardUI)getUI()).getClient();
    	
    	setSpacing(true);
        setSizeFull();

        menu = new CssLayout();
    	content = new CssLayout();

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
           
             EmailView email = new EmailView(restMBClient);
             content.addComponent(email);
    	    
             createNav("Configuração Email","email",email);
             createNav("Configuração Grupo Usuário","grupo",new GrupoUsuarioView(restMBClient));
             createNav("Aplicação REST","Aplicação REST",new AplicacaoView(restMBClient));
             
    	}
    	
    	 public void createNav(String name, final String route, Component view){
     	     Button b = new NativeButton(name.toUpperCase());
             b.setData(view);
             b.addClickListener(new ClickListener() {
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
    	                ((DragAndDropWrapper) next).iterator().next()
    	                        .removeStyleName("selected-invert");
    	            }
    	        }
    	    }
    }
    

