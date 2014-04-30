package com.persys.osmanager.install;

import java.util.Locale;
import java.util.ResourceBundle;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * Classe EtapaFinal no processo de instalacao
 * Cria um objeto aplicacao e salva as informações em um arquivo xml
 * @author 
 * 
 */
public class EndStepView implements WizardStep {

    private VerticalLayout content;
    
    private final static ResourceBundle bundle;
	static{
		 bundle = ResourceBundle.getBundle("com/persys/frontend/installend",Locale.getDefault());
	}
	
    public EndStepView(Wizard owner) {}

    public String getCaption() {
        return bundle.getString("entity");
    }
    
    public Component getContent() {
        content = new VerticalLayout();
    	content.addComponent(getText());
    
        return content;
    }

    @SuppressWarnings("deprecation")
	private Label getText() {
        return new Label(bundle.getString("message"),Label.CONTENT_XHTML);
    }

    public boolean onAdvance() {
    	try{
    		Notification.show(bundle.getString("messageend"));
    		return true;
    	}
    	catch(Exception e){ 
    		Notification.show(bundle.getString("messageerro"));
    		return false;
    	}
    }

    public boolean onBack() {
        return false;
    }
    
    
}