package com.persys.osmanager.organization;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IUnidade;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.exception.ViewException;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class UnidadeForm extends CustomComponent implements IForm<IUnidade>{
	private static final long serialVersionUID = 1L;
	private TextField textFieldEmail;
	private TextField textFieldNome;
	private TextField textFieldTelefone;
	private TextField textFieldRamal;
	private ComboBox comboBoxStatus;
	private TextField textFieldCodigo;
	private final ComponenteFactory componenteFactory = new ComponenteFactory();

	private IUnidade unidade;

	private final static ResourceBundle bundle;
	private static String error = null;

	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/unit",Locale.getDefault());
		error = bundle.getString("requirederror");
	}

	public UnidadeForm() {
		setWidth(AttrDim.FORM_WIDTH);
		setHeight(AttrDim.FORM_HEIGHT);

		setCompositionRoot(buildMainLayout());
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("600px");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		mainLayout.addComponent(buildHorizontalLayoutCodigo());

		textFieldNome = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("name"), error,
				AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldNome.addShortcutListener(
		        new AbstractField.FocusShortcut(textFieldNome, KeyCode.ENTER,
		                                        ModifierKey.SHIFT));
		textFieldNome.focus();
		mainLayout.addComponent(textFieldNome);

		textFieldEmail = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("email"), error,
				AttrDim.FORM_COM_SINGLE_WIDTH);
		mainLayout.addComponent(textFieldEmail);

		mainLayout.addComponent(buildHorizontalLayoutTelefone());

		return mainLayout;
	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildHorizontalLayoutCodigo() {
		// common part: create layout
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		// textFieldCodigo
		textFieldCodigo = new TextField();
		textFieldCodigo.setCaption(bundle.getString("code"));
		textFieldCodigo.setImmediate(false);
		textFieldCodigo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout.addComponent(textFieldCodigo);

		// comboBoxStatus
		comboBoxStatus = componenteFactory.createComboBoxRequiredError(new ComboBox(), bundle.getString("status"), error);
		comboBoxStatus.setContainerDataSource(AppData.listStatusInteger());
		comboBoxStatus.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_PROPERTY);
		comboBoxStatus.setItemCaptionPropertyId("statusNome");
		comboBoxStatus.setNullSelectionAllowed(false);
		horizontalLayout.addComponent(comboBoxStatus);

		return horizontalLayout;
	}
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutTelefone() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		textFieldTelefone = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("phone"), error); 
		horizontalLayout.addComponent(textFieldTelefone);

		textFieldRamal = componenteFactory.createTextField(new TextField(), bundle.getString("extensionline")); 
		horizontalLayout.addComponent(textFieldRamal);

		return horizontalLayout;
	}

	private HorizontalLayout createHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		return horizontalLayout;
	}

	@Override
	public IUnidade commit() throws ViewException {
		try{
			componenteFactory.valida();

			unidade.setCodigo(unidade.getCodigo());
			unidade.setNome(textFieldNome.getValue());
			unidade.setEmail(textFieldEmail.getValue());
			unidade.setTelefone(textFieldTelefone.getValue());
			unidade.setRamal(textFieldRamal.getValue());
			unidade.setStatusModel((Integer)comboBoxStatus.getValue());

			return unidade;

		}catch(Exception e){
			throw new ViewException(e.getMessage());
		}
	}

	@Override
	public void initData(IUnidade data) {
		textFieldCodigo.setReadOnly(false);
		textFieldCodigo.setValue(data.getCodigo());
		textFieldNome.setValue(data.getNome());
		textFieldEmail.setValue(data.getEmail());
		textFieldTelefone.setValue(data.getTelefone());
		textFieldRamal.setValue(data.getRamal());
		comboBoxStatus.select(data.getStatusModel());
		unidade = data;
	}

	@Override
	public void modoView() {
		textFieldCodigo.setReadOnly(true);
	}

	@Override
	public void modoAdd() {
		textFieldCodigo.setReadOnly(true);
	}
}
