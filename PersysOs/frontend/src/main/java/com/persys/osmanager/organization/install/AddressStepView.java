package com.persys.osmanager.organization.install;

import java.util.Locale;
import java.util.ResourceBundle;

import org.vaadin.teemu.wizards.WizardStep;

import br.com.contato.model.Endereco;
import br.com.empresa.model.Organizacao;
import br.com.exception.ModelException;

import com.persys.osmanager.address.AddressForm;
import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.exception.ViewException;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**

 * Classe de Usuario: cria e salva um usuario no banco de dados
 * 
 * @author 
 * 
 */
public class AddressStepView implements WizardStep {

	private VerticalLayout content;
	private final AddressForm addressForm = new AddressForm();
	
	private final static ResourceBundle bundle;
	
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/installaddress",Locale.getDefault());
	}
	
	private Endereco    endereco       = null;
	private InstallOrganizationView installOrganizationView	= null;

	public AddressStepView(InstallOrganizationView installOrganizationView){
		this.installOrganizationView = installOrganizationView;
	}
	
	public String getCaption() {
		return bundle.getString("entity");
	}

	@SuppressWarnings("deprecation")
	public Component getContent() {

		content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		content.addStyleName("osmanager-view");
		content.addComponent(new Label(bundle.getString("message"),Label.CONTENT_XHTML));
		addressForm.initData(new Endereco());
		content.addComponent(ComponenteFactory.createPanel(addressForm));
	    	
		return content;
	}
	
	public boolean onAdvance() {
		Organizacao organizacao = installOrganizationView.getOrganizacao();
	 	try {
			endereco = (Endereco) addressForm.commit();
			try {
				endereco.setConsumerId(Long.parseLong(organizacao.getCnpj()));
	            endereco.salvar();
	    		organizacao.setEndereco(endereco);
	    		Organizacao.alteraEndereco(organizacao.getId(), endereco);
	     	} catch (ModelException e) {
	     		Notification.show(e.getMessage());
	     		return false;
			}
		} catch (ViewException e) {
			Notification.show(e.getMessage());
	     	return false;
		}
	       
		return true;
	}

	public boolean onBack() {
		return true;
	}

}