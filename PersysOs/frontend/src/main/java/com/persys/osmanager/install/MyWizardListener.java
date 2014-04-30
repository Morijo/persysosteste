package com.persys.osmanager.install;

import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;
import com.vaadin.server.Page;

public class MyWizardListener implements WizardProgressListener{

	 public void wizardCompleted(WizardCompletedEvent event) {
	        endWizard("Instalação finalizada!");
	    }

	    public void activeStepChanged(WizardStepActivationEvent event) {
	        // display the step caption as the window title
	    	 Page.getCurrent().setTitle(event.getActivatedStep().getCaption());
	    }

	    public void stepSetChanged(WizardStepSetChangedEvent event) {
	        // NOP, not interested on this event
	    }

	    public void wizardCancelled(WizardCancelledEvent event) {
	        endWizard("Install Cancelled!");
	    }

	    private void endWizard(String message) {
	        Page.getCurrent().setLocation("");
        }

}
