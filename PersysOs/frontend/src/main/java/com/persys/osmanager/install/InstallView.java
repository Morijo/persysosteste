package com.persys.osmanager.install;

import java.util.ResourceBundle;
import org.vaadin.teemu.wizards.Wizard;
import br.com.principal.model.Aplicacao;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

public class InstallView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InstallView(ResourceBundle bundle){
		
		setSizeFull();
		addStyleName("login-layout");
		
		final CssLayout instalacaoPanel = new CssLayout();
		instalacaoPanel.addStyleName("login-panel");
   
        Wizard instalacao = new Wizard();
        instalacao.setUriFragmentEnabled(true);
        instalacao.addListener(new InstallWizardListener());
        if(Aplicacao.getAplicacao().getSgbdNome().isEmpty()){
        	 instalacao.addStep(new DataBaseStepView(), bundle.getString("database"));
        }
        instalacao.addStep(new EndStepView(instalacao), bundle.getString("finish"));
        instalacao.setHeight("600px");
        instalacao.setWidth("800px");
        instalacao.getNextButton().setCaption(bundle.getString("nextbutton"));
        instalacao.getBackButton().setCaption(bundle.getString("backbutton"));
        instalacao.getFinishButton().setCaption(bundle.getString("finishbutton"));
        instalacao.getCancelButton().setCaption(bundle.getString("cancelbutton"));
        
        instalacaoPanel.addComponent(instalacao);
        
    	addComponent(instalacaoPanel);
		setComponentAlignment(instalacaoPanel, Alignment.MIDDLE_CENTER);
	}
}
