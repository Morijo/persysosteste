package com.persys.osmanager.customer.contract;
import java.util.Iterator;

import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.contact.ContactView;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.restmb.types.Contrato;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class MenuContract extends HorizontalLayout implements IForm<Contrato>{
	

	Button bDados    = null;
	Button bRecursos = null;
	Button bProduto  = null;
	
	Contrato 			  contrato     = null;
	ContractForm 		  contratoForm = null;
	ContractRecursoView   contratoRecursoView = null;
	ContractProdutoView   contratoProdutoView = null;

	private CssLayout menu;
	private CssLayout content;
	
	ContactView     contatoViewView	 = null;

	RestMBClient client = null;

	public MenuContract(RestMBClient client){
		
		this.client = client;
		
		menu = new CssLayout();
		content = new CssLayout();

		setSizeFull();
        addStyleName("main-view");
        addComponent(new VerticalLayout() {
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
          
         bDados = new NativeButton("    Dados Gerais   ".toUpperCase());
         bDados.addClickListener(new ClickListener() {
             @Override
             public void buttonClick(ClickEvent event) {
            	 clearMenuSelection();
                 event.getButton().addStyleName("selected-invert");
                 content.removeAllComponents();
                 content.addComponent(contratoForm);
             }
         });
        
         menu.addComponent(bDados);
         
         bRecursos = new NativeButton("  Recursos   ".toUpperCase());
         bRecursos.addClickListener(new ClickListener() {
             @Override
             public void buttonClick(ClickEvent event) {
            	 clearMenuSelection();
                 event.getButton().addStyleName("selected-invert");
                 content.removeAllComponents();
                 content.addComponent(contratoRecursoView);
             }
         });
         menu.addComponent(bRecursos);
         
         bProduto = new NativeButton("  Produtos   ".toUpperCase());
         bProduto.addClickListener(new ClickListener() {
             @Override
             public void buttonClick(ClickEvent event) {
            	 clearMenuSelection();
                 event.getButton().addStyleName("selected-invert");
                 content.removeAllComponents();
                 content.addComponent(contratoProdutoView);
             }
         });
         menu.addComponent(bProduto);
         
        menu.getComponent(0).addStyleName("selected-invert");
	     
	}
	
	 public void createNav(String name, final String route, Component view){
 	     Button b = new NativeButton(name.toUpperCase());
         b.setData(view);
         b.addClickListener(new ClickListener() {
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

	@Override
	public Contrato commit() throws ViewException {
		Contrato contrato = null;
		
		try{
			contrato = contratoForm.commit();
			return contrato;
		}catch(Exception e){
			Notification.show("Erro: "+e.getMessage());
			throw new ViewException(e.getMessage());
		}	
	}

	@Override
	public void initData(Contrato data) {
		
		content.removeAllComponents();
		
		contratoForm = null;
		contatoViewView = null;
		
		contratoForm = new ContractForm(client); 
     	contratoForm.initData(data);
     	content.addComponent(contratoForm);
        
        contratoRecursoView = new ContractRecursoView(client,data);
        contratoProdutoView = new ContractProdutoView(client,data);
     	contrato = data;
	}

	@Override
	public void modoView() {
		bRecursos.setVisible(true);
		bProduto.setVisible(true);
		contratoForm.modoAdd();
	}

	@Override
	public void modoAdd() {
		bRecursos.setVisible(false);
		bProduto.setVisible(false);
	}
}
