package com.persys.osmanager.tracker.component;

import java.util.Date;
import java.util.List;

import br.com.funcionario.model.Funcionario;

import com.persys.osmanager.tracker.interfaces.ITrackerMapListEvent;
import com.restmb.RestMBClient;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.VerticalLayout;

public class TrackerMapList extends CustomComponent  {

	private static final long serialVersionUID = 1L;
	private VerticalLayout verticalItens;
	private PopupDateField popupDateField;
	private ITrackerMapListEvent iTrackerMapListEvent;
	private RestMBClient client;
	
	public TrackerMapList(RestMBClient client, ITrackerMapListEvent iTrackerMapListEvent) {
		this.client = client;
		this.iTrackerMapListEvent = iTrackerMapListEvent;

		setWidth("100%");
		setHeight("-1px");
		setCompositionRoot(buildMainLayout());
	}

	private VerticalLayout buildMainLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(new MarginInfo(true, false, true, false));
		mainLayout.setSpacing(true);


		VerticalLayout verticalOptions = new VerticalLayout();
		verticalOptions.setWidth("100%");
		verticalOptions.setHeight("-1px");
		verticalOptions.setSpacing(true);
		verticalOptions.setMargin(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		
		CheckBox ordens = new CheckBox("Ordens");
		ordens.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				iTrackerMapListEvent.eventSelectOrder();
			}
		});
		
		CheckBox tracker = new CheckBox("Trajeto");
		tracker.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				iTrackerMapListEvent.eventSelectTracker();
			}
		});
		
		horizontalLayout.addComponent(ordens);
		horizontalLayout.addComponent(tracker);
		horizontalLayout.setSpacing(true);
		//verticalOptions.addComponent(horizontalLayout);

		popupDateField = new PopupDateField("Buscar por dada");
		popupDateField.setImmediate(true);
		popupDateField.setWidth("140px");
		popupDateField.setHeight("-1px");
		popupDateField.setValue(new Date());
		popupDateField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				iTrackerMapListEvent.eventDate(popupDateField.getValue());
			}
		});
		verticalOptions.addComponent(popupDateField);

		verticalItens = new VerticalLayout();
		verticalItens.setWidth("200px");
		verticalItens.setHeight("-1px");
		verticalItens.setSpacing(false);

		defaultTable();

		mainLayout.addComponent(verticalOptions);

		mainLayout.addComponent(verticalItens);

		return mainLayout;
	}

	public void defaultTable() {
		try{
			List<Funcionario> listaFuncionario = (List<Funcionario>) Funcionario.listaFuncionario(client.getOauth().getApiKey());
			for(Funcionario funcionario : listaFuncionario){
				TrackerMapItem trackerMapItem = new TrackerMapItem(funcionario.getRazaoNome());
				trackerMapItem.setData(funcionario);
				trackerMapItem.addLayoutClickListener(new LayoutClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void layoutClick(LayoutClickEvent event) {
						TrackerMapItem trackerMapItem = (TrackerMapItem) event.getComponent();
						selectionEmployees(event, trackerMapItem);
					}
				});
				verticalItens.addComponent(trackerMapItem);
			}
		}catch (Exception e) {}
	}

	private void selectionEmployees(LayoutClickEvent event,
			TrackerMapItem trackerMapItem) {
		Funcionario funcionario = (Funcionario) trackerMapItem.getData();
			if(trackerMapItem.getStyleName().contains("selecionado")){
				trackerMapItem.setStyleName("normal");
				iTrackerMapListEvent.eventUnSelectEmployees(funcionario.getId());
			}else{
				trackerMapItem.setStyleName("selecionado");
				iTrackerMapListEvent.eventSelectEmployees(funcionario.getId());
			}
		if(event.isDoubleClick()){
				iTrackerMapListEvent.eventSelectEmployees(funcionario);
		}
	}

	public PopupDateField getPopupDateField() {
		return popupDateField;
	}

	public void setPopupDateField(PopupDateField popupDateField) {
		this.popupDateField = popupDateField;
	}
	
}
