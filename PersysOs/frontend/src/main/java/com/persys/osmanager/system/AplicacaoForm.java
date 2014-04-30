package com.persys.osmanager.system;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.usuario.model.AplicacaoAgente;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.ContainerUtils;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.exception.ViewException;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AplicacaoForm extends CustomComponent implements IForm<AplicacaoAgente>{


	private static final long serialVersionUID   = 1L;
	private TextField 	   	  textFieldNome 	 = null;
	private ComboBox 	   	  comboBoxStatus 	 = null;
	private TextField 	      textFieldCodigo    = null;
	private TextField 		  textFieldChaveEmpresa;
	private TextField 		  textFieldChaveUsuario;
	private TextField 		  textFieldChaveUsuarioSecreta;

	private final ComponenteFactory componenteFactory = new ComponenteFactory();

	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/aplicacao",Locale.getDefault());
	}

	private AplicacaoAgente aplicacaoAgente = null;

	public AplicacaoForm() {

		setWidth("100.0%");
		setHeight("100.0%");
		setCompositionRoot(buildMainLayout());
	}

	private VerticalLayout buildMainLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		mainLayout.addComponent(buildHorizontalLayout());

		textFieldNome = componenteFactory.createTextFieldRequiredError(new TextField(),bundle.getString("nome"),"Nome",
				AttrDim.FORM_COM_SINGLE_WIDTH);

		mainLayout.addComponent(textFieldNome);
		mainLayout.addComponent(buildHorizontalLayoutChave());
		mainLayout.addComponent(buildHorizontalLayoutUsuarioChave());

		return mainLayout;
	}


	@SuppressWarnings("deprecation")
	private HorizontalLayout buildHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);

		// textFieldCodigo
		textFieldCodigo = new TextField();
		textFieldCodigo.setCaption(bundle.getString("codigo"));
		textFieldCodigo.setImmediate(false);
		textFieldCodigo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout.addComponent(textFieldCodigo);

		// comboBoxStatus
		comboBoxStatus = new ComboBox();
		comboBoxStatus.setCaption(bundle.getString("situacao"));
		comboBoxStatus.setImmediate(false);
		comboBoxStatus.setContainerDataSource(ContainerUtils.listaStatusBean());
		comboBoxStatus.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		comboBoxStatus.setHeight("-1px");
		comboBoxStatus.setContainerDataSource(AppData.listStatusInteger());
		comboBoxStatus.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_PROPERTY);
		comboBoxStatus.setItemCaptionPropertyId("statusNome");
		comboBoxStatus.setNullSelectionAllowed(false);
		horizontalLayout.addComponent(comboBoxStatus);

		return horizontalLayout;
	}

	private HorizontalLayout buildHorizontalLayoutChave() {
		// common part: create layout
		HorizontalLayout horizontalLayoutChave = new HorizontalLayout();
		horizontalLayoutChave.setImmediate(false);
		horizontalLayoutChave.setWidth("-1px");
		horizontalLayoutChave.setHeight("-1px");
		horizontalLayoutChave.setMargin(false);
		horizontalLayoutChave.setSpacing(true);

		textFieldChaveEmpresa = componenteFactory.createTextField(new TextField(), "Chave Empresa");
		textFieldChaveEmpresa.setMaxLength(14);
		horizontalLayoutChave.addComponent(textFieldChaveEmpresa);

		return horizontalLayoutChave;
	}

	private HorizontalLayout buildHorizontalLayoutUsuarioChave() {
		HorizontalLayout horizontalLayoutChave = new HorizontalLayout();
		horizontalLayoutChave.setImmediate(false);
		horizontalLayoutChave.setWidth("-1px");
		horizontalLayoutChave.setHeight("-1px");
		horizontalLayoutChave.setMargin(false);
		horizontalLayoutChave.setSpacing(true);

		textFieldChaveUsuario = componenteFactory.createTextField(new TextField(), "Usuário Token");
		textFieldChaveUsuario.setMaxLength(14);
		horizontalLayoutChave.addComponent(textFieldChaveUsuario);

		textFieldChaveUsuarioSecreta = componenteFactory.createTextField(new TextField(), "Usuário Token Secreto");
		horizontalLayoutChave.addComponent(textFieldChaveUsuarioSecreta);

		return horizontalLayoutChave;
	}

	@Override
	public AplicacaoAgente commit() throws ViewException {
		try{
			componenteFactory.valida();
			aplicacaoAgente.setCodigo(textFieldCodigo.getValue());
			aplicacaoAgente.setNomeUsuario(textFieldNome.getValue());
			aplicacaoAgente.setStatusModel((Integer)comboBoxStatus.getValue());
			return aplicacaoAgente;
		}catch(Exception e){
			throw new ViewException(e.getMessage());
		}	
	}

	@Override
	public void initData(AplicacaoAgente data) {

		this.aplicacaoAgente = data;

		textFieldCodigo.setValue(aplicacaoAgente.getCodigo());
		textFieldNome.setValue(aplicacaoAgente.getNomeUsuario());
		comboBoxStatus.select(aplicacaoAgente.getStatusModel());
		if(aplicacaoAgente.getConsumerSecret() != null && aplicacaoAgente.getAccessToken() != null){
			textFieldChaveEmpresa.setValue(aplicacaoAgente.getConsumerSecret().getConsumerKey().toString());
			textFieldChaveUsuario.setValue(aplicacaoAgente.getAccessToken().getToken());
			textFieldChaveUsuarioSecreta.setValue(aplicacaoAgente.getAccessToken().getSecret());
		}
	}

	@Override
	public void modoView() {
		textFieldCodigo.setReadOnly(true);
		textFieldChaveEmpresa.setReadOnly(true);
		textFieldChaveUsuario.setReadOnly(true);
		textFieldChaveUsuarioSecreta.setReadOnly(true);
		textFieldNome.setReadOnly(true);
	}

	@Override
	public void modoAdd() {
		textFieldCodigo.setReadOnly(false);
		textFieldChaveEmpresa.setVisible(false);
		textFieldChaveUsuario.setVisible(false);
		textFieldChaveUsuarioSecreta.setVisible(false);

	}

}
