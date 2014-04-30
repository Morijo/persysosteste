package com.persys.osmanager.order;

import br.com.model.interfaces.INota;

import com.restmb.RestMBClient;
import com.restmb.types.Nota;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class OrdemNotaViewWindows extends Window {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private br.com.model.interfaces.INota nota = null;	   
    private TextArea textArea = null;
    
    public OrdemNotaViewWindows(final INota nota,final RestMBClient client) {
    	this.nota = nota;
    	
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);

        setContent(verticalLayout);
        center();
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(true);

        addStyleName("no-vertical-drag-hints");
        addStyleName("no-horizontal-drag-hints");

        setCaption("Nota");
    
        verticalLayout.addComponent(createLayoutText());
        
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName("footer");
        footer.setWidth("100%");
        footer.setMargin(true);

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
				nota.setNota(textArea.getValue());
				((Nota) nota).salvar(client);
		        close();
            }
        });
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        verticalLayout.addComponent(footer);
    }
    
    public HorizontalLayout createLayoutText(){
    	HorizontalLayout details = new HorizontalLayout();
		details.setSpacing(true);
		details.setMargin(true);
		
		textArea = new TextArea();
		textArea.setCaption(nota.getTitulo());
		textArea.setValue(nota.getNota());
		details.addComponent(textArea);
		
    	return details;
    }
  
}