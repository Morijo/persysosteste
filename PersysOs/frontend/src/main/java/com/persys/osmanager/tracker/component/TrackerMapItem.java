package com.persys.osmanager.tracker.component;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

public class TrackerMapItem extends AbsoluteLayout{

	private static final long serialVersionUID = 1L;
	private Label label;
	
	public TrackerMapItem(String title) {
		
		setWidth("500px");
		setHeight("40px");
		setStyleName("itemlistmaps");
	
		label = new Label();
		label.setImmediate(false);
		label.setWidth("100.0%");
		label.setHeight("-1px");
		label.setValue(title);
		addComponent(label, "top:12.0px;right:0.0px;left:50.0px;");
		
	    Image profilePic = new Image();
        profilePic.setSource(new ThemeResource("img/profile-pic.png"));
        profilePic.setWidth("40px");
        
		addComponent(profilePic, "top:1.0px;left:1.0px;");
		
	}

}
