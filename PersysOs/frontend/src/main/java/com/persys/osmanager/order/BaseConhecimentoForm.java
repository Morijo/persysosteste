package com.persys.osmanager.order;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IBaseConhecimento;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.ContainerUtils;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.organization.data.TransactionsContainerDepartamento;
import com.vaadin.annotations.AutoGenerated;
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
* @since 24/03/2013 View para Base de Conhecimento em Ordem
* <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/frontend/knowledgebase</p>
*/

public class BaseConhecimentoForm extends CustomComponent implements IForm<IBaseConhecimento>{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private static final long serialVersionUID   = 1L;
	private VerticalLayout    mainLayout 		 = null;
	private TextArea 	      textFieldAssunto   = null;
	private TextField 	   	  textFieldNome 	 = null;
	private ComboBox 	   	  comboBoxStatus 	 = null;
	private TextField 	      textFieldCodigo    = null;
	private final ComponenteFactory componenteFactory = new ComponenteFactory();
	private ComboBox comboBoxTipo;
	
	private final static ResourceBundle bundle;
	static{
		 bundle = ResourceBundle.getBundle("com/persys/frontend/knowledgebase",Locale.getDefault());
	}
	
	private IBaseConhecimento baseConhecimento = null;
	public BaseConhecimentoForm() {
		//inicializa rest cliente
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
		
		mainLayout.addComponent(buildHorizontalLayoutCodigoStatus());
		
		// textFieldNome
		textFieldNome = componenteFactory.createTextFieldRequiredError(
				new TextField(), bundle.getString("namebase"),
				bundle.getString("namebaseinvalid"),
				AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldNome.addShortcutListener(
		        new AbstractField.FocusShortcut(textFieldNome, KeyCode.ENTER,
		                                        ModifierKey.SHIFT));
		textFieldNome.focus();
		textFieldNome.setMaxLength(100);
			
		mainLayout.addComponent(textFieldNome);
		
		textFieldAssunto = componenteFactory.createTextAreaRequiredError(new TextArea(), bundle.getString("subject"),
				bundle.getString("subjectinvalid"),
				AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldAssunto.setMaxLength(1000);
		mainLayout.addComponent(textFieldAssunto);
		mainLayout.addComponent(buildHorizontalLayoutTipo());
		return mainLayout;
	}
	
	@SuppressWarnings("deprecation")
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutCodigoStatus() {
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

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutTipo() {
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		comboBoxTipo = componenteFactory.createComboBoxRequiredError(new ComboBox(), bundle.getString("type"),bundle.getString("typeinvalid"));
		comboBoxTipo.setContainerDataSource(TransactionsContainerDepartamento.listDepartmentTypes());
		comboBoxTipo.setNullSelectionAllowed(false);
		horizontalLayout.addComponent(comboBoxTipo);
		
		return horizontalLayout;
	}

	@Override
	public IBaseConhecimento commit() throws ViewException {
		try{
			textFieldNome.validate();
			componenteFactory.valida();
			
			baseConhecimento.setCodigo(textFieldCodigo.getValue());
			baseConhecimento.setTitulo(textFieldNome.getValue());
			baseConhecimento.setMensagem(textFieldAssunto.getValue().toString());
			baseConhecimento.setStatusModel((Integer)comboBoxStatus.getValue());
			baseConhecimento.setTipo(comboBoxTipo.getValue().toString());
			return baseConhecimento;
		}catch(Exception e){
			throw new ViewException(e.getMessage());
		}	
	}
	@Override
	public void initData(IBaseConhecimento data) {
		textFieldCodigo.setReadOnly(false);
		textFieldCodigo.setValue(data.getCodigo());
		textFieldNome.setValue(data.getTitulo());
		textFieldAssunto.setValue(data.getMensagem());
		comboBoxStatus.select(data.getStatusModel());
		
		if(data.getTipo() != null)
			comboBoxTipo.select(data.getTipo());
		
		this.baseConhecimento = data;
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