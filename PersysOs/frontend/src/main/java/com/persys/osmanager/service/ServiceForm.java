package com.persys.osmanager.service;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IServico;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.ContainerUtils;
import com.persys.osmanager.componente.DecimalField;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
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
 * @since 18/02/2013 Form Servico
 */

public class ServiceForm extends CustomComponent implements IForm<IServico> {

	private static final long serialVersionUID = 1L;

	private TextArea textFieldDescricao;
	private TextField textFieldNome;
	private ComboBox comboBoxStatus;
	private TextField textFieldCodigo;
	private DecimalField textFieldValorServico;

	private Panel servicoProcedimentoPanel;
	private IServico servico = null;

	private final ComponenteFactory componenteFactory = new ComponenteFactory();
	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/service",
				Locale.getDefault());
	}

	private RestMBClient client = null;
	
	public ServiceForm(RestMBClient client) {
		
		this.client = client;
		
		setWidth("100.0%");
		setHeight("100.0%");

		setCompositionRoot(buildMainLayout());
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		mainLayout.addComponent(buildHorizontalLayoutCodigo());

		// textFieldNome
		textFieldNome = componenteFactory.createTextFieldRequiredError(
				new TextField(), bundle.getString("servicename"),
				bundle.getString("servicenameinvalid"),
				AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldNome.setMaxLength(100);
		textFieldNome.addShortcutListener(
		        new AbstractField.FocusShortcut(textFieldNome, KeyCode.ENTER,
		                                        ModifierKey.SHIFT));
		textFieldNome.focus();
		
		mainLayout.addComponent(textFieldNome);

		mainLayout.addComponent(buildHorizontalLayoutValor());

		textFieldDescricao = new TextArea();
		textFieldDescricao.setCaption(bundle.getString("description"));
		textFieldDescricao.setImmediate(false);
		textFieldDescricao.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldDescricao.setMaxLength(5000);
		textFieldDescricao.setHeight("-1px");
		mainLayout.addComponent(textFieldDescricao);

		servicoProcedimentoPanel = new Panel();
		servicoProcedimentoPanel.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		servicoProcedimentoPanel.setHeight("100%");
		mainLayout.addComponent(servicoProcedimentoPanel);

		return mainLayout;
	}

	@SuppressWarnings("deprecation")
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutCodigo() {
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
		textFieldCodigo.setMaxLength(255);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout_1.addComponent(textFieldCodigo);
		
		// comboBoxStatus
		comboBoxStatus = new ComboBox();
		comboBoxStatus.setCaption(bundle.getString("situation"));
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
	
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutValor() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);

		// textFieldCodigo
		textFieldValorServico = new DecimalField();
		textFieldValorServico.setCaption(bundle.getString("pricing"));
		textFieldValorServico.setImmediate(false);
		textFieldValorServico.setNullRepresentation("");
		textFieldValorServico.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldValorServico.setMaxLength(255);
		textFieldValorServico.setHeight("-1px");
		horizontalLayout.addComponent(textFieldValorServico);
		return horizontalLayout;
	}

	@Override
	public IServico commit() throws ViewException {
		try {
			textFieldNome.validate();
			servico.setCodigo(textFieldCodigo.getValue());
			servico.setTitulo(textFieldNome.getValue());
			servico.setDescricao(textFieldDescricao.getValue());
			try {
				servico.setValorServico(new BigDecimal(textFieldValorServico.getConvertedValue().toString()));
			}catch (Exception e) {
				throw new ViewException("Preço inválido");
			}
			servico.setStatusModel((Integer) comboBoxStatus.getValue());
			return servico;
		} catch (Exception e) {
			throw new ViewException(e.getMessage());
		}
	}

	@Override
	public void initData(IServico data) {

		textFieldCodigo.setReadOnly(false);
		textFieldCodigo.setValue(data.getCodigo());
		textFieldNome.setValue(data.getTitulo());
		textFieldDescricao.setValue(data.getDescricao());
		textFieldValorServico.setConvertedValue((Number) data.getValorServico());
		comboBoxStatus.select(data.getStatusModel());
		servicoProcedimentoPanel.setVisible(false);
		servicoProcedimentoPanel.setContent(new ServiceProcedimentoView(data,client));
		this.servico = data;


	}

	@Override
	public void modoView() {
		textFieldCodigo.setReadOnly(true);
		servicoProcedimentoPanel.setVisible(true);
	}

	@Override
	public void modoAdd() {
		textFieldCodigo.setReadOnly(true);
	}

}