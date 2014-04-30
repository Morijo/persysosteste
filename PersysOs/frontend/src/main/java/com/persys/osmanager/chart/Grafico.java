package com.persys.osmanager.chart;

import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickEvent;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickHandler;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveEvent;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveHandler;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.renderers.series.PieRenderer;

import com.vaadin.ui.VerticalLayout;

public class Grafico extends VerticalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Grafico() {
    
	setSizeFull();
    setCaption("Desempenho - Hoje");	
    DataSeries dataSeries = new DataSeries()
    	.newSeries()
    	.add("none", 23)
    	.add("error", 0)
    	.add("click", 5)
    	.add("impression", 25);

    SeriesDefaults seriesDefaults = new SeriesDefaults()
    	.setRenderer(SeriesRenderers.PIE)
    	.setRendererOptions(
    		new PieRenderer()
    			.setShowDataLabels(true));

    org.dussan.vaadin.dcharts.options.Options options = new Options()
    	.setCaptureRightClick(true)
    	.setSeriesDefaults(seriesDefaults);

    DCharts chart = new DCharts();

    chart.setEnableChartDataMouseEnterEvent(true);
    chart.setEnableChartDataMouseLeaveEvent(true);
    chart.setEnableChartDataClickEvent(true);
    chart.setEnableChartDataRightClickEvent(true);

    chart.addHandler(new ChartDataMouseLeaveHandler() {
    	@Override
    	public void onChartDataMouseLeave(ChartDataMouseLeaveEvent event) {
    	}
    });

    chart.addHandler(new ChartDataClickHandler() {
    	@Override
    	public void onChartDataClick(ChartDataClickEvent event) {
    	}
    });

    chart.setDataSeries(dataSeries)
    	.setOptions(options)
    	.show();
    
    addComponent(chart);
    }

}