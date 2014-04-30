package com.persys.osmanager.install;

import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;
import com.vaadin.server.Page;

public class InstallWizardListener implements WizardProgressListener{

	 public void wizardCompleted(WizardCompletedEvent event) {
	        endWizard("Finalizando!");
	    }

	    public void activeStepChanged(WizardStepActivationEvent event) {
	    	 Page.getCurrent().setTitle(event.getActivatedStep().getCaption());
	    }

	    public void stepSetChanged(WizardStepSetChangedEvent event) {
	    }

	    public void wizardCancelled(WizardCancelledEvent event) {
	        endWizard("Finalizando!");
	    }

	    private void endWizard(String message) {
	        Page.getCurrent().setLocation("");
        }

}
