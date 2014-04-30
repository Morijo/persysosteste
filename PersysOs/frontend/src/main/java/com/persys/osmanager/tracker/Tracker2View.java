/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.persys.osmanager.tracker;

import java.util.ArrayList;
import java.util.Date;

import br.com.eventos.model.Evento;
import br.com.ordem.model.AgendaOrdemFuncionario;
import br.com.ordem.model.Ordem;

import com.persys.osmanager.dashboard.DashboardUI;
import com.persys.osmanager.tracker.component.TrackerMapList;
import com.persys.osmanager.tracker.interfaces.ITrackerMapListEvent;
import com.restmb.RestMBClient;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.GoogleMapPolyline;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class Tracker2View extends VerticalLayout implements View, ITrackerMapListEvent {

	private static final long serialVersionUID = 1L;
	
	private RestMBClient client = null;
	
	public Long idUsuario;
	public GoogleMap googleMap = new GoogleMap(new LatLon(-22.1968012524283, -49.95513378974191), 10.0, "");
	public ArrayList<GoogleMapMarker> list = new ArrayList<GoogleMapMarker>();
	public Date dataSelecionada = new Date(); 

	public Tracker2View() {}

	private CssLayout createPanel(Component content) {
		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel-tracker ");
		panel.setHeight("100%");
		panel.addComponent(content);
		return panel;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		client =  ((DashboardUI)getUI()).getClient();
		setHeight("100%");
		setWidth("100%");

		HorizontalLayout mapsLayout = new HorizontalLayout();
		mapsLayout.setHeight("800px");
		mapsLayout.setWidth("100%");
		mapsLayout.setMargin(new MarginInfo(true, true, false, true));
		mapsLayout.setSpacing(true);
		mapsLayout.addComponent(createPanel(maps()));

		addComponent(mapsLayout);
		setExpandRatio(mapsLayout, 3f);

		HorizontalLayout optionsLayout = new HorizontalLayout();
		optionsLayout.setMargin(true);
		optionsLayout.setHeight("100%");
		optionsLayout.setWidth("200px");
		optionsLayout.setSpacing(true);

		optionsLayout.addComponent(createPanel(new TrackerMapList(client, this)));

		addComponent(optionsLayout);
		setExpandRatio(optionsLayout, 5);
		setComponentAlignment(optionsLayout, Alignment.MIDDLE_RIGHT);
	}

	public GoogleMap maps(){
		googleMap.setSizeFull();
		googleMap.setMinZoom(4.0);
		googleMap.setMaxZoom(16.0);
		mapsRoute();
		return googleMap;
	}

	public void createMark(GoogleMap googleMap, String name, String urlIcon,Double lat, Double lon) {
		GoogleMapMarker marker = googleMap.addMarker(name, new LatLon(
				lat, lon), true, urlIcon);

		list.add(marker);
		
		googleMap.addMarkerClickListener(new MarkerClickListener() {
			@Override
			public void markerClicked(GoogleMapMarker clickedMarker) {
				Notification.show(clickedMarker.getCaption());
			}
		});

		googleMap.addMapMoveListener(new MapMoveListener() {
			@Override
			public void mapMoved(double zoomLevel, LatLon center,
					LatLon boundsNE, LatLon boundsSW) {
			}
		});

		googleMap.addMarkerDragListener(new MarkerDragListener() {
			public void markerDragged(GoogleMapMarker draggedMarker,
					LatLon newPosition) {
			}
		});
	}

	public void mapsRoute(){
			ArrayList<LatLon> points = new ArrayList<LatLon>();
			points.add(new LatLon(-22.1968012524283, -49.92513378974191));
			points.add(new LatLon(-22.2068012524283, -49.93513478974191));
			points.add(new LatLon(-22.2168014524283, -49.94513678974191));
			points.add(new LatLon(-22.2268012524283, -49.95513878974191));
			points.add(new LatLon(-22.2368015524283, -49.96513978974191));

			GoogleMapPolyline overlay = new GoogleMapPolyline(
					points, "#d31717", 0.8, 10);
			googleMap.addPolyline(overlay);
	}
	
	public void defaultTable(Date data) {

		ArrayList<AgendaOrdemFuncionario> ordens = Ordem.listaOrdemAgendamentoPorFuncionario(0, 0, "", idUsuario,new Date(), new Date());
		for(AgendaOrdemFuncionario orden : ordens){
			createMark(googleMap, orden.getOrdem().getCodigo(),"", orden.getOrdem().getContato().getEndereco().getLatitude(), orden.getOrdem().getContato().getEndereco().getLongitude());
		}
	}
	
	public void defaultTableGeral(Date data) {

		ArrayList<AgendaOrdemFuncionario> ordens = Ordem.listaOrdemAgendamento(0, 0, "", new Date(), new Date());
		for(AgendaOrdemFuncionario orden : ordens){
			createMark(googleMap, orden.getOrdem().getCodigo(),"", orden.getOrdem().getContato().getEndereco().getLatitude(), orden.getOrdem().getContato().getEndereco().getLongitude());
		}
	}

	@Override
	public void eventDate(Date newDate) {
		dataSelecionada = newDate;
		defaultTable(dataSelecionada);
	}

	@Override
	public void eventSelectOrder() {
		defaultTable(dataSelecionada);
	}

	@Override
	public void eventSelectTracker() {
		mapsRoute();
	}

	@Override
	public void eventSelectEmployees(Long id) {
		idUsuario = id;
		for(GoogleMapMarker marker : list)
			googleMap.removeMarker(marker);
		
		defaultTable(dataSelecionada);
	}

	@Override
	public void eventUnSelectEmployees(Long id) {
		//Notification.show("Funcionario deselecionado" + id);
	}
	
	@Override
	public void eventSelectEmployees(br.com.funcionario.model.Funcionario funcionario) {
		getUI().addWindow(new TrackerWindows(funcionario));
	}
}
