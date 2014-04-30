package com.persys.osmanager.organization.install;

import java.util.Locale;
import java.util.ResourceBundle;
import org.vaadin.teemu.wizards.WizardStep;
import br.com.empresa.model.Organizacao;
import br.com.exception.ModelException;
import br.com.usuario.model.Usuario;
import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.install.data.InstallDB;
import com.persys.osmanager.user.UserForm;
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
public class UserStepView implements WizardStep {

	private VerticalLayout content;
	private final UserForm userForm = new UserForm();

	private final static ResourceBundle bundle;

	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/installuser",Locale.getDefault());
	}

	private InstallOrganizationView installOrganizationView	= null;

	public UserStepView(InstallOrganizationView installOrganizationView){
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
		content.addComponent(ComponenteFactory.createPanel(userForm));
		userForm.initData(new Usuario());
		userForm.modoAdd();

		return content;
	}

	public boolean onAdvance() {
		Organizacao organizacao = installOrganizationView.getOrganizacao();
		try {
			Usuario usuario = (Usuario) userForm.commit();
			try {
				InstallDB.createUser(Long.parseLong(organizacao.getCnpj()), usuario);
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
		return false;
	}

}