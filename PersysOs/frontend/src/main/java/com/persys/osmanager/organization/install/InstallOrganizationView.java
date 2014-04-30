package com.persys.osmanager.organization.install;

import java.util.ResourceBundle;

import org.hibernate.Session;
import org.vaadin.teemu.wizards.Wizard;

import br.com.empresa.model.Organizacao;
import br.com.principal.helper.HibernateHelper;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

public class InstallOrganizationView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Organizacao organizacao;
	private Session 	session;
	
	public InstallOrganizationView(ResourceBundle bundle){
		
		setSizeFull();
		addStyleName("login-layout");
		
		final CssLayout installPanel = new CssLayout();
		installPanel.addStyleName("login-panel");
    
        Wizard installWizard = new Wizard();
        installWizard.setUriFragmentEnabled(true);
        
        session = HibernateHelper.openSession(getClass());
        
        installWizard.addListener(new InstallWizardListener());
        installWizard.addStep(new OrganizationStepView(this), bundle.getString("organization"));
        installWizard.addStep(new AddressStepView(this), "address");
        installWizard.addStep(new UserStepView(this), "user");
        installWizard.addStep(new EndStepView(this), "end");
        
        installWizard.setHeight("600px");
        installWizard.setWidth("800px");
        installWizard.getNextButton().setCaption(bundle.getString("nextbutton"));
        installWizard.getBackButton().setCaption(bundle.getString("backbutton"));
        installWizard.getFinishButton().setCaption(bundle.getString("finishbutton"));
        installWizard.getCancelButton().setCaption(bundle.getString("cancelbutton"));
        
        installPanel.addComponent(installWizard);
        
    	addComponent(installPanel);
		setComponentAlignment(installPanel, Alignment.MIDDLE_CENTER);
		
		
	}

	public Organizacao getOrganizacao() {
		if(organizacao == null)
			organizacao = new Organizacao();
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public Session getSessionHibernate() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
}
