package com.persys.osmanager.componente;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.vaadin.data.util.converter.StringToNumberConverter;
import com.vaadin.ui.TextField;

public class DecimalField extends TextField{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DecimalField(){
		super();
		StringToNumberConverter plainIntegerConverter = new StringToNumberConverter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected java.text.NumberFormat getFormat(Locale locale) {
				String formato = "R$ #,##0.00";  
				DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();
				decimalSymbols.setGroupingSeparator('.');
				decimalSymbols.setDecimalSeparator(',');
				DecimalFormat d = new DecimalFormat(formato);  
				d.setDecimalFormatSymbols(decimalSymbols);
				return d;
			};
		};
		setConverter(plainIntegerConverter);
		DecimalFormat formatter = new DecimalFormat();
		formatter.setMinimumFractionDigits(2); 
	}
}
