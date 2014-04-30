package com.persys.osmanager.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IDispositivo;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.data.AppData;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 17/02/2013 Form Dispositivo
 */

public class DispositivoForm extends CustomComponent implements
		IForm<IDispositivo> {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	private static final long serialVersionUID = 1L;

	@AutoGenerated
	private VerticalLayout mainLayout;
	private TextArea textFieldDescricao;
	private TextField textFieldMarca;
	private TextField textFieldEtiqueta;
	private TextField textFieldNome;
	private ComboBox comboBoxStatus;
	private TextField textFieldCodigo;
	private TextField textFieldModelo;
	private TextField textFieldIMEI;
	private Panel ChipPanel;

	private final ComponenteFactory componenteFactory = new ComponenteFactory();
	private IDispositivo dispositivo = null;
	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/device",
				Locale.getDefault());
	}

	public DispositivoForm() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		mainLayout.addComponent(buildHorizontalLayout_1());

		// textFieldNome
		textFieldNome = componenteFactory.createTextFieldRequiredError(
				new TextField(), bundle.getString("devicename"),
				bundle.getString("devicenameinvalid"),
				AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldNome.setMaxLength(100);
		textFieldNome.addShortcutListener(
		        new AbstractField.FocusShortcut(textFieldNome, KeyCode.ENTER,
		                                        ModifierKey.SHIFT));
		textFieldNome.focus();
		
		mainLayout.addComponent(textFieldNome);

		mainLayout.addComponent(buildHorizontalLayoutIMEI());
		mainLayout.addComponent(buildHorizontalLayout());

		// textFieldNome
		textFieldDescricao = new TextArea();
		textFieldDescricao.setCaption(bundle.getString("description"));
		textFieldDescricao.setImmediate(false);
		textFieldDescricao.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldDescricao.setMaxLength(255);
		textFieldDescricao.setHeight("-1px");
		mainLayout.addComponent(textFieldDescricao);

		ChipPanel = new Panel();
		ChipPanel.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		ChipPanel.setHeight("100%");
		mainLayout.addComponent(ChipPanel);

		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setSpacing(true);

		// textFieldEtiqueta
		textFieldEtiqueta = new TextField();
		textFieldEtiqueta.setCaption(bundle.getString("label"));
		textFieldEtiqueta.setImmediate(false);
		textFieldEtiqueta.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldEtiqueta.setMaxLength(100);
		textFieldEtiqueta.setHeight("-1px");
		horizontalLayout.addComponent(textFieldEtiqueta);

		// textFieldMarca
		textFieldMarca = new TextField();
		textFieldMarca.setCaption(bundle.getString("brand"));
		textFieldMarca.setImmediate(false);
		textFieldMarca.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldMarca.setMaxLength(100);
		textFieldMarca.setHeight("-1px");
		horizontalLayout.addComponent(textFieldMarca);

		return horizontalLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutIMEI() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setSpacing(true);

		// textFieldModelo
		textFieldModelo = new TextField();
		textFieldModelo.setCaption(bundle.getString("model"));
		textFieldModelo.setImmediate(false);
		textFieldModelo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldModelo.setMaxLength(100);
		textFieldModelo.setHeight("-1px");
		horizontalLayout.addComponent(textFieldModelo);

		// textFieldIMEI
		textFieldIMEI = componenteFactory.createTextFieldRequiredError(
				new TextField(), bundle.getString("imei"),
				bundle.getString("imeiinvalid"), AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldIMEI.setMaxLength(100);
		horizontalLayout.addComponent(textFieldIMEI);

		return horizontalLayout;
	}

	@SuppressWarnings("deprecation")
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		HorizontalLayout horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);

		// textFieldCodigo
		textFieldCodigo = new TextField();
		textFieldCodigo.setCaption(bundle.getString("code"));
		textFieldCodigo.setImmediate(false);
		textFieldCodigo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCodigo.setMaxLength(200);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout_1.addComponent(textFieldCodigo);

		// comboBoxStatus
		comboBoxStatus = new ComboBox();
		comboBoxStatus.setCaption(bundle.getString("situation"));
		comboBoxStatus.setImmediate(false);
		comboBoxStatus.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		comboBoxStatus.setHeight("-1px");
		comboBoxStatus.setContainerDataSource(AppData.listStatusInteger());
		comboBoxStatus.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_PROPERTY);
		comboBoxStatus.setItemCaptionPropertyId("statusNome");
		comboBoxStatus.setNullSelectionAllowed(false);
		horizontalLayout_1.addComponent(comboBoxStatus);

		return horizontalLayout_1;
	}

	@Override
	public IDispositivo commit() {
		try {
			textFieldNome.validate();
			dispositivo.setNome(textFieldNome.getValue());
			dispositivo.setCodigo(textFieldCodigo.getValue());
			dispositivo.setEtiqueta(textFieldEtiqueta.getValue());
			dispositivo.setMarca(textFieldMarca.getValue());
			dispositivo.setIMEI(textFieldIMEI.getValue());
			dispositivo.setDescricao(textFieldDescricao.getValue());
			dispositivo.setModelo(textFieldModelo.getValue());
			dispositivo.setStatusModel((Integer) comboBoxStatus.getValue());

			return dispositivo;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void initData(IDispositivo data) {

		textFieldCodigo.setReadOnly(false);

		textFieldCodigo.setValue(data.getCodigo());
		textFieldEtiqueta.setValue(data.getEtiqueta());
		textFieldDescricao.setValue(data.getDescricao());
		textFieldMarca.setValue(data.getMarca());
		textFieldNome.setValue(data.getNome());
		textFieldIMEI.setValue(data.getIMEI());
		textFieldModelo.setValue(data.getModelo());
		comboBoxStatus.select(data.getStatusModel());
		ChipPanel.setVisible(true);
		ChipPanel.setContent(new DispositivoChipView(data));

		this.dispositivo = data;

	}

	@Override
	public void modoView() {
		textFieldCodigo.setReadOnly(true);
		ChipPanel.setVisible(true);
	}

	@Override
	public void modoAdd() {
		textFieldCodigo.setReadOnly(true);
	}

}