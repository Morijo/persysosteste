package com.persys.osmanager.install;

import java.util.Locale;
import java.util.ResourceBundle;

import org.vaadin.teemu.wizards.WizardStep;

import br.com.exception.DAOException;
import br.com.principal.model.Aplicacao;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.exception.ViewException;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * Classe cria as tabelas no banco de dados
 * 
 * @author Ricardo Sabatine
 * 
 */
public class DataBaseStepView implements WizardStep {
	
	private Boolean        permitir = false;
	private VerticalLayout mainLayout = null;
	private TextField      urlTextField = null;
	private TextField      portTextField = null;
    private TextField      baseDadoTextField = null;
 	private TextField      userTextField = null;
 	private PasswordField  senhaPasswordField = null;
 	private ComboBox       sgbdComboBox = null;
	
 	private final ComponenteFactory componenteFactory = new ComponenteFactory();

 	private final static ResourceBundle bundle;
	
	static{
		 bundle = ResourceBundle.getBundle("com/persys/frontend/installdb",Locale.getDefault());
	}
	
  	@SuppressWarnings("deprecation")
	public Component getContent() {
      
    	mainLayout = new VerticalLayout();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        
        //carrega mensagem de error
        String error = bundle.getString("requirederror");
        
        createComponet(error);
        
    	mainLayout.addComponent(new Label(bundle.getString("message"),Label.CONTENT_XHTML));
        
        HorizontalLayout horizontalLayoutDB = new HorizontalLayout();
        horizontalLayoutDB.setSizeFull();
        horizontalLayoutDB.addComponent(sgbdComboBox);
        horizontalLayoutDB.addComponent(baseDadoTextField);
    	mainLayout.addComponent(horizontalLayoutDB);
        
        HorizontalLayout horizontalLayoutIpPort = new HorizontalLayout();
        horizontalLayoutIpPort.setSizeFull();
        horizontalLayoutIpPort.addComponent(urlTextField);
        horizontalLayoutIpPort.addComponent(portTextField);
    	mainLayout.addComponent(horizontalLayoutIpPort);
    	
        HorizontalLayout horizontalLayoutUser = new HorizontalLayout();
        horizontalLayoutUser.setSizeFull();
        horizontalLayoutUser.addComponent(userTextField);
        horizontalLayoutUser.addComponent(senhaPasswordField);
    	mainLayout.addComponent(horizontalLayoutUser);
        
        return mainLayout;
    }

	private void createComponet(String error) {
		//cria os componentes
        sgbdComboBox       = componenteFactory.createComboBoxRequiredError(new ComboBox(), bundle.getString("sgbd"),error);
        baseDadoTextField  = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("database"), error);
        urlTextField       = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("url"),error); 
        portTextField       = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("port"),error); 
        userTextField      = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("user"), error); 
        senhaPasswordField = (PasswordField) componenteFactory.createAbstractFieldRequiredError(new PasswordField(), bundle.getString("pwd"),error);  
        
    	//carrega combobox com opções
    	sgbdComboBox.addItem("Mysql");
        sgbdComboBox.addItem("SqlServer 2008");
        sgbdComboBox.addItem("Postgres");
	}

    public String getCaption() {
        return bundle.getString("entity");
    }
   
    public boolean onAdvance() {
    	
    	try {
			componenteFactory.valida();
		} catch (ViewException e1) {
			Notification.show(e1.getMessage());
			return false;
		}
    	
    	try{
		    Aplicacao app = Aplicacao.getInstance();
		    app.setUsuarioBanco(userTextField.getValue().toString());
		    app.setSenhaBanco(senhaPasswordField.getValue().toString());
		    app.setBaseDados(baseDadoTextField.getValue().toString());
		    app.setSgbdNome(sgbdComboBox.getValue().toString());
		    app.setAtivo(true);
    		
		    try{
			    if(sgbdComboBox.getValue().toString().equals("SqlServer 2008")){
			    	app.setUrl("jdbc:sqlserver://"+urlTextField.getValue().toString()+":"+portTextField.getValue().toString()+";databaseName="+baseDadoTextField.getValue().toString());
			    }else if(sgbdComboBox.getValue().toString().equals("Mysql")){
			    	app.setUrl("jdbc:mysql://"+urlTextField.getValue().toString()+":"+portTextField.getValue().toString()+"/"+baseDadoTextField.getValue().toString());
			    }else if(sgbdComboBox.getValue().toString().equals("Postgres")){
			    	app.setUrl("jdbc:postgresql://"+urlTextField.getValue().toString()+":"+portTextField.getValue().toString()+"/"+baseDadoTextField.getValue().toString());
			    }
		   
		    	app.testaConexao();
				
		    	if(app.getSucessoBanco()){
		    		  app.salvar();
		    		  permitir = true;  
				}
			}catch (DAOException e) {
				Notification.show("Falha" + e.getMessage());
			}
		}catch (Exception e) {
			Notification.show("Falha" + e.getMessage());
		}
    	
    	return permitir;
    }

    public boolean onBack() {
        return false;
    }
}