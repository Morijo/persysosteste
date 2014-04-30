package com.persys.osmanager.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IMaterial;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.FormViewWindow;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.resource.data.TransactionsContainerResourceMedida;
import com.restmb.RestMBClient;
import com.restmb.types.Medida;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 14/03/2013 Form Para Material
 */
public class MaterialForm extends CustomComponent implements IForm<IMaterial>{

	private static final long serialVersionUID = 1L;

	private TextArea 		textFieldDescricao;
	private TextField 		textFieldMarca;
	private TextField 		textFieldEtiqueta;
	private TextField 		textFieldMaterial;
	private ComboBox 		comboBoxStatus;
	private TextField 		textFieldCodigo;
	private ComboBox 		comboBoxMedida;
	private TextField 		textFieldModelo;

	private final ComponenteFactory componenteFactory = new ComponenteFactory();

	private RestMBClient client = null;
	private IMaterial material = null;

	private final static ResourceBundle bundle;

	static{
		 bundle = ResourceBundle.getBundle("com/persys/frontend/material",Locale.getDefault());
	}
	
	public MaterialForm(RestMBClient client) {

		this.client = client;

		setWidth("100.0%");
		setHeight("100.0%");
		setCompositionRoot(	buildMainLayout());
	}
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		mainLayout.addComponent(buildHorizontalLayoutStatusCode());

		textFieldMaterial = componenteFactory.createTextFieldRequiredError(
				new TextField(), bundle.getString("namematerial"),
				bundle.getString("namematerialinvalid"),
				AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldMaterial.setMaxLength(100);
		textFieldMaterial.addShortcutListener(
		        new AbstractField.FocusShortcut(textFieldMaterial, KeyCode.ENTER,
		                                        ModifierKey.SHIFT));
		textFieldMaterial.focus();
		
		mainLayout.addComponent(textFieldMaterial);

		mainLayout.addComponent(buildHorizontalLayoutMedida());
		mainLayout.addComponent(buildHorizontalLayoutMarcaModelo());

		// textFieldMaterial
		textFieldDescricao = new TextArea();
		textFieldDescricao.setCaption(bundle.getString("description"));
		textFieldDescricao.setImmediate(false);
		textFieldDescricao.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldDescricao.setMaxLength(255);
		textFieldDescricao.setHeight("-1px");
		mainLayout.addComponent(textFieldDescricao);

		return mainLayout;
	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildHorizontalLayoutStatusCode() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		// textFieldCodigo
		textFieldCodigo = new TextField();
		textFieldCodigo.setCaption(bundle.getString("code"));
		textFieldCodigo.setImmediate(false);
		textFieldCodigo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCodigo.setMaxLength(100);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout.addComponent(textFieldCodigo);

		// comboBoxStatus
		comboBoxStatus = new ComboBox();
		comboBoxStatus.setCaption(bundle.getString("situation"));
		comboBoxStatus.setContainerDataSource(AppData.listStatusInteger());
		comboBoxStatus.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		comboBoxStatus.setHeight("-1px");
		comboBoxStatus.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_PROPERTY);
		comboBoxStatus.setItemCaptionPropertyId("statusNome");
		comboBoxStatus.setNullSelectionAllowed(false);
		horizontalLayout.addComponent(comboBoxStatus);
		return horizontalLayout;
	}

	private HorizontalLayout buildHorizontalLayoutMarcaModelo() {
		// common part: create layout
		HorizontalLayout horizontalLayoutMarcaModelo = createHorizontalLayout();

		textFieldMarca = new TextField();
		textFieldMarca.setCaption(bundle.getString("brand"));
		textFieldMarca.setImmediate(false);
		textFieldMarca.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldMarca.setMaxLength(100);
		textFieldMarca.setHeight("-1px");
		horizontalLayoutMarcaModelo.addComponent(textFieldMarca);

		textFieldModelo = new TextField();
		textFieldModelo.setCaption(bundle.getString("model"));
		textFieldModelo.setImmediate(false);
		textFieldModelo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldModelo.setMaxLength(100);
		textFieldModelo.setHeight("-1px");
		horizontalLayoutMarcaModelo.addComponent(textFieldModelo);
		return horizontalLayoutMarcaModelo;
	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildHorizontalLayoutMedida() {
		// common part: create layout
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		// textFieldEtiqueta
		textFieldEtiqueta = new TextField();
		textFieldEtiqueta.setCaption(bundle.getString("barcode"));
		textFieldEtiqueta.setImmediate(false);
		textFieldEtiqueta.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldEtiqueta.setMaxLength(100);
		textFieldEtiqueta.setHeight("-1px");
		horizontalLayout.addComponent(textFieldEtiqueta);

		HorizontalLayout horizontalLayoutMedida = new HorizontalLayout();
		horizontalLayoutMedida.setWidth("-1px");
		horizontalLayoutMedida.setHeight("-1px");
		horizontalLayoutMedida.setMargin(false);
		horizontalLayoutMedida.setSpacing(false);

		comboBoxMedida = componenteFactory.createComboBoxRequiredError(
				new ComboBox(), bundle.getString("measure"),
				bundle.getString("measureinvalid"));
		comboBoxMedida.setWidth(AttrDim.FORM_COM_DOUBLE_ICON_WIDTH);
		comboBoxMedida.setNullSelectionAllowed(false);
		comboBoxMedida.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_PROPERTY);
		comboBoxMedida.setItemCaptionPropertyId("nome");
		comboBoxMedida.setContainerDataSource(TransactionsContainerResourceMedida.listMedida(client));

		Button buttonNovaMedida = new Button();
		buttonNovaMedida
				.setIcon(new ThemeResource("../reindeer/Icons/plus.png"));
		buttonNovaMedida.setImmediate(true);
		buttonNovaMedida.setWidth("30px");
		buttonNovaMedida.setHeight("-1px");
		buttonNovaMedida.addStyleName("newicon");

		buttonNovaMedida.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window window = new FormViewWindow<Medida>(client,bundle.getString("newmeasure"), new MedidaForm(), new Medida());
				getUI().addWindow(window);
				window.addCloseListener(new CloseListener() {
				private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						loadMedida();
					}
				});
			}
		});
		horizontalLayoutMedida.addComponent(comboBoxMedida);
		horizontalLayoutMedida.addComponent(buttonNovaMedida);
		horizontalLayoutMedida.setComponentAlignment(buttonNovaMedida, Alignment.BOTTOM_CENTER);
		horizontalLayout.addComponent(horizontalLayoutMedida);

		return horizontalLayout;
	}

	private HorizontalLayout createHorizontalLayout() {
		HorizontalLayout horizontalLayoutMarcaModelo = new HorizontalLayout();
		horizontalLayoutMarcaModelo.setImmediate(false);
		horizontalLayoutMarcaModelo.setWidth("-1px");
		horizontalLayoutMarcaModelo.setHeight("-1px");
		horizontalLayoutMarcaModelo.setMargin(false);
		horizontalLayoutMarcaModelo.setSpacing(true);
		return horizontalLayoutMarcaModelo;
	}

	@Override
	public IMaterial commit() throws ViewException{
			Medida medidaCombo = null;
		try{
			textFieldMaterial.validate();
			material.setCodigo(textFieldCodigo.getValue());
			material.setEtiqueta(textFieldEtiqueta.getValue());
			material.setMarca(textFieldMarca.getValue());
			material.setMaterial(textFieldMaterial.getValue());
			material.setModelo(textFieldModelo.getValue());
			material.setDescricao(textFieldDescricao.getValue());
			material.setStatusModel((Integer)comboBoxStatus.getValue());

			medidaCombo = new Medida();
			
			medidaCombo.setId(((Medida)comboBoxMedida.getValue()).getId());

			material.setMedida(medidaCombo);

			return material;
		}catch (Exception e) {
			throw new ViewException(e.getMessage());
		}finally{
			medidaCombo = null;
		}
	}

	@Override
	public void initData(IMaterial data) {

		textFieldCodigo.setReadOnly(false);

		textFieldCodigo.setValue(data.getCodigo());
		textFieldEtiqueta.setValue(data.getEtiqueta());
		textFieldDescricao.setValue(data.getDescricao());
		textFieldMarca.setValue(data.getMarca());
		textFieldMaterial.setValue(data.getMaterial());
		textFieldModelo.setValue(data.getModelo());
		comboBoxStatus.select(data.getStatusModel());
		comboBoxMedida.select(data.getMedida());
		this.material = data;

	}

	@Override
	public void modoView() {
		textFieldCodigo.setReadOnly(true);
	}

	@Override
	public void modoAdd() {
		textFieldCodigo.setReadOnly(true);
	}
	
	private void loadMedida(){
		comboBoxMedida.getContainerDataSource().removeAllItems();
		comboBoxMedida.setContainerDataSource(TransactionsContainerResourceMedida.listMedida(client));
	}
}