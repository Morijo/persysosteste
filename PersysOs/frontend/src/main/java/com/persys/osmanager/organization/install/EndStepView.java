package com.persys.osmanager.organization.install;

import org.vaadin.teemu.wizards.WizardStep;
import br.com.empresa.model.Organizacao;
import com.persys.osmanager.install.data.InstallDB;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**

 * Classe de Usuario: cria e salva um usuario no banco de dados
 * 
 * @author 
 * 
 */
public class EndStepView implements WizardStep {

	private VerticalLayout content;
	
	private InstallOrganizationView installOrganizationView	= null;

	public EndStepView(InstallOrganizationView installOrganizationView){
		this.installOrganizationView = installOrganizationView;
	}
	
	public String getCaption() {
		return "Conclusão";
	}

	public Component getContent() {

		content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		content.addStyleName("osmanager-view");

		content.addComponent(getText());
	    	
		return content;
	}
	
	 @SuppressWarnings("deprecation")
		private Label getText() {
	        return new Label("Fim!!",Label.CONTENT_XHTML);
	    }
	
	public boolean onAdvance() {
		Organizacao organizacao = installOrganizationView.getOrganizacao();
		try{
			InstallDB.createUnidade(organizacao,Long.parseLong(organizacao.getCnpj()));
			InstallDB.createPermissao(Long.parseLong(organizacao.getCnpj()));
			InstallDB.createStatusOrder(Long.parseLong(organizacao.getCnpj()));
			InstallDB.medidas(Long.parseLong(organizacao.getCnpj()));
			InstallDB.tipoEvento(Long.parseLong(organizacao.getCnpj()));
			}catch(Exception e){
				e.getMessage();
			}
		return true;
	}

	public boolean onBack() {
		return false;
	}

}