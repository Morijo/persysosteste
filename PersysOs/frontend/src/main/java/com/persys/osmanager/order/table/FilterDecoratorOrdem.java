package com.persys.osmanager.order.table;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Locale;

import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;

@SuppressWarnings("serial")
public class FilterDecoratorOrdem implements org.tepi.filtertable.FilterDecorator, Serializable {

    @Override
    public String getEnumFilterDisplayName(Object propertyId, Object value) {
        
         return null;
    }

    @Override
    public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
       return null;
    }

    @Override
    public String getFromCaption() {
        return "Data Início:";
    }

    @Override
    public String getToCaption() {
        return "Data Final:";
    }

    @Override
    public String getSetCaption() {
        // use default caption
        return "Ajustar";
    }

    @Override
    public String getClearCaption() {
        // use default caption
        return "Limpar";
    }

    @Override
    public boolean isTextFilterImmediate(Object propertyId) {
        // use text change events for all the text fields
        return true;
    }

    @Override
    public int getTextChangeTimeout(Object propertyId) {
        // use the same timeout for all the text fields
        return 500;
    }

    @Override
    public String getAllItemsVisibleString() {
        return "";
    }

    @Override
    public Resolution getDateFieldResolution(Object propertyId) {
        return Resolution.DAY;
    }

    public DateFormat getDateFormat(Object propertyId) {
        return DateFormat.getDateInstance(DateFormat.SHORT, new Locale("fi",
                "FI"));
    }

    @Override
    public boolean usePopupForNumericProperty(Object propertyId) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getDateFormatPattern(Object propertyId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NumberFilterPopupConfig getNumberFilterPopupConfig() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public Resource getEnumFilterIcon(Object propertyId, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
		// TODO Auto-generated method stub
		return null;
	}
}