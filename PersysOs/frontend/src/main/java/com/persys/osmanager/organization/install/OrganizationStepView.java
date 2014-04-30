package com.persys.osmanager.organization.install;

import java.util.Locale;
import java.util.ResourceBundle;

import org.vaadin.teemu.wizards.WizardStep;

import br.com.empresa.model.Organizacao;
import br.com.principal.helper.HibernateHelper;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.organization.OrganizationForm;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * Classe de Usuario: cria e salva um usuario no banco de dados
 * 
 * @author 
 * 
 */
public class OrganizationStepView implements WizardStep {

	private VerticalLayout content;

	private final OrganizationForm organizationForm = new OrganizationForm();

	private InstallOrganizationView installOrganizationView	= null;
	
	private final static ResourceBundle bundle;

	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/installorg",Locale.getDefault());
	}
	
	public OrganizationStepView(InstallOrganizationView installOrganizationView){
		this.installOrganizationView = installOrganizationView;
	}

	public String getCaption() {
		return bundle.getString("entity");
	}

	public Component getContent() {

		content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		content.addStyleName("osmanager-view");

		organizationForm.initData(installOrganizationView.getOrganizacao());
		organizationForm.modoView();
		content.addComponent(ComponenteFactory.createPanel(organizationForm));

		return content;
	}

	public boolean onAdvance() {
		Organizacao organizacao = null;
		try {
			organizacao = (Organizacao) organizationForm.commit();
			organizacao.salvar(installOrganizationView.getSessionHibernate());
			installOrganizationView.setOrganizacao(organizacao);
			return true;
		} catch (Exception e) {
			Notification.show(e.getMessage());
			installOrganizationView.getSessionHibernate().close();
			installOrganizationView.setSession(HibernateHelper.openSession(getClass()));
			return false;
		} 
	}

	public boolean onBack() {
		return false;
	}
}