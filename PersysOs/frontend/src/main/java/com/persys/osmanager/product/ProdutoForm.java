package com.persys.osmanager.product;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IProduto;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.ContainerUtils;
import com.persys.osmanager.componente.DecimalField;
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
 * @since 17/02/2013
 * Form Product
 * Campo obrigatorio Nome do Produto
 */

public class ProdutoForm extends CustomComponent implements IForm<IProduto> {
	
	private static final long serialVersionUID = 1L;

	private TextArea textAreaDescricao;
	private TextField textFieldMarca;
	private TextField textFieldCodigoBarra;
	private TextField textFieldNome;
	private ComboBox comboBoxStatus;
	private TextField textFieldCodigo;
	private DecimalField textFieldValor;

	private final ComponenteFactory componenteFactory = new ComponenteFactory();

	private IProduto produto = null;

	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/product",
				Locale.getDefault());
	}

	public ProdutoForm() {
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

		mainLayout.addComponent(buildHorizontalLayoutStatus());

		textFieldNome = componenteFactory.createTextFieldRequiredError(
				new TextField(), bundle.getString("productname"),bundle.getString("productnameinvalid"),
				AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldNome.setMaxLength(100);
		textFieldNome.addShortcutListener(
		        new AbstractField.FocusShortcut(textFieldNome, KeyCode.ENTER,
		                                        ModifierKey.SHIFT));
		textFieldNome.focus();
		mainLayout.addComponent(textFieldNome);


		mainLayout.addComponent(buildHorizontalLayout());

		mainLayout.addComponent(buildHorizontalLayoutValor());

		textAreaDescricao = new TextArea();
		textAreaDescricao.setCaption(bundle.getString("description"));
		textAreaDescricao.setImmediate(false);
		textAreaDescricao.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textAreaDescricao.setMaxLength(250);
		textAreaDescricao.setHeight("-1px");
		mainLayout.addComponent(textAreaDescricao);

		return mainLayout;
	}

	private HorizontalLayout buildHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setSpacing(true);

		textFieldCodigoBarra = new TextField();
		textFieldCodigoBarra.setCaption(bundle.getString("barcode"));
		textFieldCodigoBarra.setImmediate(false);
		textFieldCodigoBarra.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCodigoBarra.setMaxLength(30);
		textFieldCodigoBarra.setHeight("-1px");
		horizontalLayout.addComponent(textFieldCodigoBarra);

		textFieldMarca = new TextField();
		textFieldMarca.setCaption(bundle.getString("brand"));
		textFieldMarca.setImmediate(false);
		textFieldMarca.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldMarca.setMaxLength(100);
		textFieldMarca.setHeight("-1px");
		horizontalLayout.addComponent(textFieldMarca);

		return horizontalLayout;
	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildHorizontalLayoutStatus() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);

		textFieldCodigo = new TextField();
		textFieldCodigo.setCaption(bundle.getString("code"));
		textFieldCodigo.setImmediate(false);
		textFieldCodigo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCodigo.setMaxLength(255);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout.addComponent(textFieldCodigo);

		comboBoxStatus = new ComboBox();
		comboBoxStatus.setCaption(bundle.getString("situation"));
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

	private HorizontalLayout buildHorizontalLayoutValor() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);

		textFieldValor = new DecimalField();
		textFieldValor.setCaption(bundle.getString("pricing"));
		textFieldValor.setImmediate(false);
		textFieldValor.setNullRepresentation("");
		textFieldValor.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldValor.setHeight("-1px");
		horizontalLayout.addComponent(textFieldValor);
		return horizontalLayout;
	}

	@Override
	public IProduto commit() throws ViewException {
		try {
			textFieldNome.validate();
			produto.setCodigo(textFieldCodigo.getValue());
			produto.setCodigoBarra(textFieldCodigoBarra.getValue());
			produto.setMarca(textFieldMarca.getValue());
			produto.setNome(textFieldNome.getValue());
			produto.setDescricao(textAreaDescricao.getValue());
			produto.setStatusModel((Integer) comboBoxStatus.getValue());
			try {
				produto.setValor(new BigDecimal(textFieldValor.getConvertedValue().toString()));
			}catch (Exception e) {
				throw new ViewException("Preço inválido");
			}
			return produto;
		} catch (Exception e) {
			throw new ViewException(e.getMessage());
		}
	}

	@Override
	public void initData(IProduto data) {

		textFieldCodigo.setReadOnly(false);
		textFieldCodigo.setValue(data.getCodigo());
		textFieldCodigoBarra.setValue(data.getCodigoBarra());
		textAreaDescricao.setValue(data.getDescricao());
		textFieldMarca.setValue(data.getMarca());
		textFieldNome.setValue(data.getNome());
		comboBoxStatus.select(data.getStatusModel());
		textFieldValor.setConvertedValue(data.getValor());
		produto = data;

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
