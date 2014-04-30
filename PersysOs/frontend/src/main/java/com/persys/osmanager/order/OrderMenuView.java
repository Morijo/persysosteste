/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.persys.osmanager.order;
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

public class OrderMenuView extends HorizontalLayout implements View{

    private static final long serialVersionUID = 1L;
   
	private CssLayout menu;
	private CssLayout content;
 
    @Override
    public void enter(ViewChangeEvent event) {
    	RestMBClient client =  ((DashboardUI)getUI()).getClient();
    	
    	setSpacing(true);
        setSizeFull();

        menu = new CssLayout();
    	content = new CssLayout();

    	setSizeFull();
        addStyleName("main-view");
        addComponent(new VerticalLayout() {
            /**
			 * 
			 */
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
           
             OrdemView ordemView = new OrdemView(client);
             content.addComponent(ordemView);
    	    
             createNav("Ordem","ordem", ordemView);
             createNav("Prioridade","prioridade", new PrioridadeView(client));
             createNav("Situação","situacao", new SituacaoView(client));
             createNav("Base de Conhecimento","base-conhecimento", new BaseConhecimentoView(client));
                  	
    	}
    	
    	 public void createNav(String name, final String route, Component view){
     	     Button buttonOptions = new NativeButton(name.toUpperCase());
             buttonOptions.setData(view);
             buttonOptions.addClickListener(new ClickListener() {
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

            menu.addComponent(buttonOptions);
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
    

