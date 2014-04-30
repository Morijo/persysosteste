package com.persys.osmanager.organization.install;

import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;
import com.vaadin.server.Page;

public class InstallWizardListener implements WizardProgressListener{

	public void wizardCompleted(WizardCompletedEvent event) {
		Page.getCurrent().reload();
	}

	public void activeStepChanged(WizardStepActivationEvent event) {
		Page.getCurrent().setTitle(event.getActivatedStep().getCaption());
	}

	public void stepSetChanged(WizardStepSetChangedEvent event) {
		event.getWizard().getCancelButton().setVisible(false);
	}

	public void wizardCancelled(WizardCancelledEvent event) {
		Page.getCurrent().reload();
	}
}
