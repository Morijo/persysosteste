package com.persys.osmanager.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IMedida;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.ContainerUtils;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.exception.ViewException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 18/02/2013 Form para Medida
 */

public class MedidaForm extends CustomComponent implements IForm<IMedida>{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private static final long serialVersionUID = 1L;
	private VerticalLayout mainLayout;
	private TextArea textFieldDescricao;
	private TextField textFieldAbreviacao;
	private TextField textFieldNome;
	private ComboBox comboBoxStatus;
	private TextField textFieldCodigo;

	private IMedida medida = null;

	private final static ResourceBundle bundle;

	private final ComponenteFactory componenteFactory = new ComponenteFactory();

	static{
		 bundle = ResourceBundle.getBundle("com/persys/frontend/measure",Locale.getDefault());
		 
	}
	
	public MedidaForm() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

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
		textFieldNome = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("nameofmeasure"), bundle.getString("nameofmeasureinvalid"),
				AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldNome.setMaxLength(100);
		textFieldNome.addShortcutListener(
		        new AbstractField.FocusShortcut(textFieldNome, KeyCode.ENTER,
		                                        ModifierKey.SHIFT));
		textFieldNome.focus();
		mainLayout.addComponent(textFieldNome);
		mainLayout.addComponent(buildHorizontalLayout());

		textFieldDescricao = new TextArea();
		textFieldDescricao.setCaption(bundle.getString("description"));
		textFieldDescricao.setImmediate(false);
		textFieldDescricao.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldDescricao.setMaxLength(255);
		textFieldDescricao.setHeight("-1px");
		mainLayout.addComponent(textFieldDescricao);

		return mainLayout;
	}

	private HorizontalLayout buildHorizontalLayout() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setSpacing(true);

		// textFieldAbreviacao
		textFieldAbreviacao = componenteFactory.createTextFieldRequiredError(new TextField(), bundle.getString("abbreviation"),bundle.getString("abbreviationinvalid"),
				AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldAbreviacao.setMaxLength(50);
		horizontalLayout.addComponent(textFieldAbreviacao);

		return horizontalLayout;
	}

	@SuppressWarnings("deprecation")
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
		textFieldCodigo.setMaxLength(100);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout_1.addComponent(textFieldCodigo);

		// comboBoxStatus
		comboBoxStatus = new ComboBox();
		comboBoxStatus.setCaption(bundle.getString("situation"));
		comboBoxStatus.setImmediate(false);
		comboBoxStatus.setContainerDataSource(ContainerUtils.listaStatusBean());
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
	public IMedida commit() throws ViewException{
		try{
			textFieldNome.validate();
			medida.setCodigo(textFieldCodigo.getValue());
			medida.setAbreviacao(textFieldAbreviacao.getValue());
			medida.setNome(textFieldNome.getValue());
			medida.setObservacao(textFieldDescricao.getValue());
			medida.setStatusModel((Integer)comboBoxStatus.getValue());
			return medida;
		}catch (Exception e) {
			throw new ViewException(e.getMessage());
		}
	}

	@Override
	public void initData(IMedida data) {

		textFieldCodigo.setReadOnly(false);

		textFieldCodigo.setValue(data.getCodigo());
		textFieldAbreviacao.setValue(data.getAbreviacao());
		textFieldDescricao.setValue(data.getObservacao());
		textFieldNome.setValue(data.getNome());
		comboBoxStatus.select(data.getStatusModel());
		this.medida = data;

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