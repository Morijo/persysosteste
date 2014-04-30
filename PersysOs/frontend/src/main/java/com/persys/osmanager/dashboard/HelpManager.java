/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.persys.osmanager.dashboard;

import java.util.ArrayList;
import java.util.List;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class HelpManager {

     private List<HelpOverlay> overlays = new ArrayList<HelpOverlay>();

    public HelpManager() {
    }

    public void closeAll() {
        for (HelpOverlay overlay : overlays) {
            overlay.close();
        }
        overlays.clear();
    }

    public void showHelpFor(View view) {
        // showHelpFor(view.getClass());
    }

    public HelpOverlay addOverlay(String caption, String text, String style) {
        HelpOverlay o = new HelpOverlay();
        o.setCaption(caption);
        o.addComponent(new Label(text, ContentMode.HTML));
        o.setStyleName(style);
        // ui.addWindow(o);
        overlays.add(o);
        return o;
    }

}
