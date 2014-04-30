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
import com.persys.osmanager.tracker.component.TrackerMapList;
import com.persys.osmanager.tracker.interfaces.ITrackerMapListEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.GoogleMapPolygon;
import com.vaadin.tapio.googlemaps.client.GoogleMapPolyline;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class TrackerView extends HorizontalLayout implements View, ITrackerMapListEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CssLayout menu;
	private CssLayout content;


	@Override
	public void enter(ViewChangeEvent event) {

		setSizeFull();
	
		menu = new CssLayout();
		content = new CssLayout();

		setSizeFull();

		addStyleName("main-view");
		addComponent(new VerticalLayout() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				addStyleName("sidebar-invert");
				setWidth(null);
				setHeight("100%");
				menu.addStyleName("menu");
				menu.setHeight("100%");
				addComponent(menu);
				setExpandRatio(menu, 1);

			}
		});

		addComponent(content);
		content.setSizeFull();
		setExpandRatio(content, 1);        


		content.addComponent(createMaps());
		menu.addComponent(new TrackerMapList(null, this));
		
	}

	public VerticalLayout createMaps(){

		VerticalLayout layoutMaps = new VerticalLayout();
		layoutMaps.setSizeFull();



		/*
        Button moveCenterButton = new Button(
                "Move over Luonnonmaa (60.447737, 21.991668), zoom 12",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        googleMap.setCenter(new LatLon(60.447737, 21.991668));
                        googleMap.setZoom(12.0);
                    }
                });
       // buttonLayoutRow1.addComponent(moveCenterButton);

        Button limitCenterButton = new Button(
                "Limit center between (60.619324, 22.712753), (60.373484, 21.945083)",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        googleMap.setCenterBoundLimits(new LatLon(60.619324,
                                22.712753), new LatLon(60.373484, 21.945083));
                        event.getButton().setEnabled(false);
                    }
                });
       // buttonLayoutRow1.addComponent(limitCenterButton);

        Button limitVisibleAreaButton = new Button(
                "Limit visible area between (60.494439, 22.397835), (60.373484, 21.945083)",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        googleMap.setVisibleAreaBoundLimits(new LatLon(
                                60.494439, 22.397835), new LatLon(60.420632,
                                22.138626));
                        event.getButton().setEnabled(false);
                    }
                });
       // buttonLayoutRow1.addComponent(limitVisibleAreaButton);

        Button addPolyOverlayButton = new Button("Add overlay over Luonnonmaa",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        ArrayList<LatLon> points = new ArrayList<LatLon>();
                        points.add(new LatLon(60.484715, 21.923706));
                        points.add(new LatLon(60.446636, 21.941387));
                        points.add(new LatLon(60.422496, 21.99546));
                        points.add(new LatLon(60.427326, 22.06464));
                        points.add(new LatLon(60.446467, 22.064297));

                        GoogleMapPolygon overlay = new GoogleMapPolygon(points,
                                "#ae1f1f", 0.8, "#194915", 0.5, 3);
                        googleMap.addPolygonOverlay(overlay);
                        event.getButton().setEnabled(false);
                    }
                });
       // buttonLayoutRow2.addComponent(addPolyOverlayButton);

        Button addPolyLineButton = new Button("Draw line from Turku to Raisio",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        ArrayList<LatLon> points = new ArrayList<LatLon>();
                        points.add(new LatLon(60.448118, 22.253738));
                        points.add(new LatLon(60.455144, 22.24198));
                        points.add(new LatLon(60.460222, 22.211939));
                        points.add(new LatLon(60.488224, 22.174602));
                        points.add(new LatLon(60.486025, 22.169195));

                        GoogleMapPolyline overlay = new GoogleMapPolyline(
                                points, "#d31717", 0.8, 10);
                        googleMap.addPolyline(overlay);
                        event.getButton().setEnabled(false);
                    }
                });
        //buttonLayoutRow2.addComponent(addPolyLineButton);

        Button changeToTerrainButton = new Button("Change to terrain map",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        googleMap.setMapType(GoogleMap.MapType.Terrain);
                        event.getButton().setEnabled(false);
                    }
                });
       // buttonLayoutRow2.addComponent(changeToTerrainButton);

        Button changeControls = new Button("Remove street view control",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        googleMap.removeControl(GoogleMapControl.StreetView);
                        event.getButton().setEnabled(false);
                    }
                });
        //buttonLayoutRow2.addComponent(changeControls);
		 */
		layoutMaps.addComponent(mapsTracker());

		return layoutMaps;
	}


	public GoogleMap maps(){
		GoogleMap googleMap = new GoogleMap(new LatLon(-22.1968012524283, -49.95513378974191), 10.0, "");
		googleMap.setSizeFull();
		googleMap.addMarker("Ricardo Sabatine", new LatLon(
				-22.1968012524283, -49.95513378974191), true, "");

		googleMap.addMarkerClickListener(new MarkerClickListener() {
			@Override
			public void markerClicked(GoogleMapMarker clickedMarker) {
				createWindow();
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

		googleMap.setMinZoom(4.0);
		googleMap.setMaxZoom(16.0);
		return googleMap;
	}

	public VerticalLayout mapsTracker(){
		VerticalLayout layoutMapsTracker = new VerticalLayout();
		layoutMapsTracker.setSizeFull();

		GoogleMap googleMap = maps();

		layoutMapsTracker.addComponent(googleMap);
		layoutMapsTracker.setExpandRatio(googleMap, 1.0f);

		return layoutMapsTracker;
	}


	public VerticalLayout mapsRoute(){

		VerticalLayout layoutMapsRota = new VerticalLayout();
		layoutMapsRota.setSizeFull();

		GoogleMap googleMap = maps();

		ArrayList<LatLon> points = new ArrayList<LatLon>();
		points.add(new LatLon(-22.1968012524283, -49.92513378974191));
		points.add(new LatLon(-22.2068012524283, -49.93513378974191));
		points.add(new LatLon(-22.2168012524283, -49.94513378974191));
		points.add(new LatLon(-22.2268012524283, -49.95513378974191));
		points.add(new LatLon(-22.2368012524283, -49.96513378974191));

		GoogleMapPolyline overlay = new GoogleMapPolyline(
				points, "#d31717", 0.8, 10);
		googleMap.addPolyline(overlay);

		layoutMapsRota.addComponent(googleMap);
		layoutMapsRota.setExpandRatio(googleMap, 1.0f);

		return layoutMapsRota;
	}


	public VerticalLayout mapsArea(){
		VerticalLayout layoutMapsArea = new VerticalLayout();
		layoutMapsArea.setSizeFull();

		GoogleMap googleMap = maps();

		ArrayList<LatLon> points = new ArrayList<LatLon>();
		points.add(new LatLon(-22.1968012524283, -49.92513378974191));
		points.add(new LatLon(-22.2068012524283, -49.93513378974191));
		points.add(new LatLon(-22.2168012524283, -49.94513378974191));
		points.add(new LatLon(-22.2268012524283, -49.95513378974191));
		points.add(new LatLon(-22.2368012524283, -49.96513378974191));

		GoogleMapPolygon overlay = new GoogleMapPolygon(points,
				"#ae1f1f", 0.8, "#194915", 0.5, 3);
		googleMap.addPolygonOverlay(overlay);

		layoutMapsArea.addComponent(googleMap);
		layoutMapsArea.setExpandRatio(googleMap, 1.0f);

		return layoutMapsArea;
	}

	private void createWindow() {
		System.out.println("");
	}

	@Override
	public void eventDate(Date newDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventSelectOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventSelectTracker() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventSelectEmployees(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventUnSelectEmployees(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventSelectEmployees(br.com.funcionario.model.Funcionario funcionario) {
		// TODO Auto-generated method stub
		
	}
}
