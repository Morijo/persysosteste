package com.persys.osmanager.componente;

import java.util.ArrayList;

import org.vaadin.addons.maskedtextfield.MaskedTextField;
import org.vaadin.csvalidation.CSValidator;

import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.exception.ViewException;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class ComponenteFactory {

	private ArrayList<AbstractField<?>> listFields = new ArrayList<AbstractField<?>>();

	public AbstractField<?> createAbstractFieldRequiredError(AbstractField<?> abstractField, String caption,String errorMessage){
		abstractField.setRequired(true);
		abstractField.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		abstractField.setCaption(caption);
		abstractField.setRequiredError(errorMessage+": "+caption);
		abstractField.setImmediate(true);
		listFields.add(abstractField);
		return abstractField;
	}

	public TextField createTextField(TextField textField, String caption){
		textField.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textField.setCaption(caption);
		textField.setImmediate(true);
		textField.setNullRepresentation("");
		listFields.add(textField);
		return textField;
	}

	public TextField createTextFieldRequiredError(TextField textField, String caption,String errorMessage){
		textField.setRequired(true);
		textField.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textField.setCaption(caption);
		textField.setRequiredError(errorMessage+": "+caption);
		textField.setImmediate(true);
		textField.setNullRepresentation("");
		final CSValidator validator = new CSValidator();
		validator.extend(textField);
		validator.setJavaScript("if (value.length <= 0 ) \"  " + errorMessage + "\";");
		validator.setErrorMessage(errorMessage+": "+caption);
		listFields.add(textField);
		return textField;
	}

	public TextField createTextFieldRequiredError(TextField textField, String caption,String errorMessage,String width){
		textField.setRequired(true);
		textField.setWidth(width);
		textField.setCaption(caption);
		textField.setRequiredError(errorMessage+": "+caption);
		textField.setImmediate(true);
		textField.setNullRepresentation("");
		listFields.add(textField);
		final CSValidator validator = new CSValidator();
		validator.extend(textField);
		validator.setJavaScript("if (value.length == 0 ) \"  " + errorMessage + "\";");
		validator.setErrorMessage(errorMessage+": "+caption);
		return textField;
	}
	public TextArea createTextAreaRequiredError(TextArea textArea, String caption,String errorMessage,String width){
		textArea.setRequired(true);
		textArea.setWidth(width);
		textArea.setCaption(caption);
		textArea.setRequiredError(errorMessage+": "+caption);
		textArea.setImmediate(true);
		listFields.add(textArea);
		final CSValidator validator = new CSValidator();
		validator.extend(textArea);
		validator.setJavaScript("if (value.length <= 0 ) \"  " + errorMessage + "\";");
		validator.setErrorMessage(errorMessage+": "+caption);
		return textArea;
	}
	
	public MaskedTextField createTextCpfRequiredError(MaskedTextField textCpf, String caption,String errorMessage,String width){
		textCpf.setRequired(true);
		textCpf.setWidth(width);
		textCpf.setCaption(caption);
		textCpf.setRequiredError(errorMessage);
		textCpf.setImmediate(false);
		textCpf.setNullRepresentation("");
		listFields.add(textCpf);
		final CSValidator validator = new CSValidator();
		validator.extend(textCpf);
		validator.setJavaScript("if (value.length <= 0 ) \"  " + errorMessage + "\";");
		validator.setErrorMessage(errorMessage);
		return textCpf;
	}
	
	public TextField createTextFieldEmailRequiredError(TextField textField, String caption,String errorMessage,String width){
		textField.setRequired(true);
		textField.setWidth(width);
		textField.setCaption(caption);
		textField.setRequiredError(errorMessage);
		textField.setImmediate(true);
		textField.setNullRepresentation("");
		listFields.add(textField);
		final CSValidator validator = new CSValidator();
		validator.extend(textField);
		validator.setRegExp(PreconditionsView.EMAIL_PATTERN);
		validator.setErrorMessage(errorMessage);
		return textField;
	}

	public ComboBox createComboBoxRequiredError(ComboBox comboBox, String caption,String errorMessage){
		comboBox.setRequired(true);
		comboBox.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		comboBox.setCaption(caption);
		comboBox.setRequiredError(errorMessage);
		comboBox.setImmediate(true);
		listFields.add(comboBox);
		return comboBox;
	}

	public static CssLayout createPanel(Component content) {
		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();
		panel.addComponent(content);
		return panel;
	}

	public void addField(AbstractField<?> abstractField){
		listFields.add(abstractField);
	}

	@SuppressWarnings("deprecation")
	public static ComboBox createComboboxStatus(Integer status) {
		ComboBox comboBoxStatus = new ComboBox();
		comboBoxStatus.setContainerDataSource(AppData.listStatusInteger());
		comboBoxStatus.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_PROPERTY);
		comboBoxStatus.setItemCaptionPropertyId("statusNome");
		comboBoxStatus.setNullSelectionAllowed(false);
		comboBoxStatus.setValue(status);
		return comboBoxStatus;
	}

	public void valida() throws ViewException{
		for(AbstractField<?> bField : listFields){
			try{
				bField.validate();
			}catch (Exception e) {
				throw new ViewException(e.getMessage());
			}
		}
	}

	
}
