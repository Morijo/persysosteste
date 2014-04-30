package com.persys.osmanager.order;

import java.net.URL;
import com.restmb.types.Anexo;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class OrdemAnexoViewWindows extends Window {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Anexo anexo = null;	   
   
    public OrdemAnexoViewWindows(Anexo anexo) {
    	this.anexo = anexo;
    	
        VerticalLayout l = new VerticalLayout();
        l.setSpacing(true);

        setContent(l);
        center();
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);

        addStyleName("no-vertical-drag-hints");
        addStyleName("no-horizontal-drag-hints");

        setCaption("Anexo");
        
        TabSheet abas = new TabSheet();
        abas.setWidth("560px");
        abas.setHeight("500px");
        
        abas.addTab(geral(), "Anexo");
          
        l.addComponent(abas);
  
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName("footer");
        footer.setWidth("100%");
        footer.setMargin(true);

        Button ok = new Button("Fechar");
        ok.addStyleName("wide");
        ok.addStyleName("default");
        ok.addClickListener(new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        l.addComponent(footer);
    }
    
    public HorizontalLayout geral(){
    	HorizontalLayout details = new HorizontalLayout();
		details.setSpacing(true);
		details.setMargin(true);
		
	      
		Image coverImage;
		URL url= null;
		try {
			url = new URL(anexo.getCaminho()+"?x=400&y=400");
			url.openStream();
			ExternalResource externalResource = new ExternalResource(url);
			coverImage = new Image("",externalResource);
		} catch (Exception ex) {
			coverImage = new Image("",new ThemeResource("img/profile-pic.png"));
		}
		coverImage.setWidth("400px");
		coverImage.setHeight("400px");
		details.addComponent(coverImage);
	        
		
		FormLayout fields = new FormLayout();
		fields.setWidth("35em");
		fields.setSpacing(true);
		fields.setMargin(true);
		details.addComponent(fields);

		Label label;

		label = new Label(anexo.getDescricao());
		label.setSizeUndefined();
		label.setCaption("Descrição");
		fields.addComponent(label);
		
	
    	return details;
    }
  
}