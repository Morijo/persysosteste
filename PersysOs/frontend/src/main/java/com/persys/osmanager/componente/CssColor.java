package com.persys.osmanager.componente;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class CssColor {

	public static CssLayout carregaCor(final String cor){

		CssLayout layout = new CssLayout() {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getCss(Component c) {
				if (c instanceof Label) {
					Color color = new Color(Integer.parseInt(cor));
					return "background: " + color.getCSS();
				}
				return null;
			}
		};
		layout.setSizeFull();
		@SuppressWarnings("deprecation")
		Label box = new Label("&nbsp;", Label.CONTENT_XHTML);
		box.setWidth("40px");
		box.setHeight("20px");
		layout.addComponent(box);

		return layout;
	}

}
