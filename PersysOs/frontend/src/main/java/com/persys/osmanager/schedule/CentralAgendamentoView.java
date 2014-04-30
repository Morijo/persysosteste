
package com.persys.osmanager.schedule;

import java.util.Date;

import com.persys.osmanager.tracker.Tracker2View;
import com.persys.osmanager.tracker.TrackerWindows;
import com.persys.osmanager.tracker.component.TrackerMapList;
import com.persys.osmanager.tracker.interfaces.ITrackerMapListEvent;
import com.restmb.RestMBClient;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class CentralAgendamentoView extends VerticalLayout implements View, ITrackerMapListEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Tracker2View tracker2View;
	
	public CentralAgendamentoView(UI ui, RestMBClient client) {
	
		setHeight("100%");
		setWidth("100%");
		
		HorizontalLayout row = new HorizontalLayout();
		row.setHeight("100%");
		row.setWidth("100%");
		row.setMargin(new MarginInfo(true, true, false, true));
		row.setSpacing(true);
		
		ScheduleOrdemGeral scheduleOrdemGeral = new ScheduleOrdemGeral(ui, client);
		row.addComponent(createPanel(scheduleOrdemGeral.buildCalendarView()));

		HorizontalLayout row2 = new HorizontalLayout();
		row2.setMargin(true);
		row2.setHeight("100%");
		row2.setWidth("100%");
		row2.setSpacing(true);

		ScheduleListOrder scheduleListOrder = new ScheduleListOrder(client);
		scheduleListOrder.verticalItens.setWidth("500px");
		row2.addComponent(createPanel(scheduleListOrder));
		
		tracker2View = new Tracker2View();
		row2.addComponent(createPanel(tracker2View.maps()));
		
		TrackerMapList trackerMapList = new TrackerMapList(client, this);
		trackerMapList.getPopupDateField().setVisible(false);
		row2.addComponent(createPanel(trackerMapList));

		addComponent(row);
		setExpandRatio(row, 2.5f);

		addComponent(row2);
		setExpandRatio(row2, 2);

	}
	
	private CssLayout createPanel(Component content) {
		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setHeight("100%");
		panel.addComponent(content);
		return panel;
	}

	@Override
	public void eventDate(Date newDate) {
		tracker2View.dataSelecionada = newDate;
		tracker2View.defaultTable(tracker2View.dataSelecionada);
	}

	@Override
	public void eventSelectOrder() {
		tracker2View.defaultTable(tracker2View.dataSelecionada);
	}

	@Override
	public void eventSelectTracker() {
		tracker2View.mapsRoute();
	}

	@Override
	public void eventSelectEmployees(Long id) {
		tracker2View.idUsuario = id;
		for(GoogleMapMarker marker : tracker2View.list)
			tracker2View.googleMap.removeMarker(marker);
		
		tracker2View.defaultTable(tracker2View.dataSelecionada);
	}

	@Override
	public void eventUnSelectEmployees(Long id) {
		//Notification.show("Funcionario deselecionado" + id);
	}
	
	@Override
	public void eventSelectEmployees(br.com.funcionario.model.Funcionario funcionario) {
		getUI().addWindow(new TrackerWindows(funcionario));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
