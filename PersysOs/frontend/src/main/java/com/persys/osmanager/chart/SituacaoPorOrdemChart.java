package com.persys.osmanager.chart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import br.com.ordem.dao.OrdemDAO;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

public class SituacaoPorOrdemChart extends Chart {

	private static final long serialVersionUID = 1L;

	public SituacaoPorOrdemChart() {
        super(ChartType.PIE);

        setCaption("Ordens por situação");
        getConfiguration().setTitle("");
        getConfiguration().getChart().setType(ChartType.PIE);
        setWidth("100%");
        setHeight("90%");

        DataSeries series = new DataSeries();

        Date hoje  = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hoje);
		calendar.add(Calendar.DAY_OF_YEAR,1);

        try {
			ArrayList<Object> lista = OrdemDAO.contaOrdensPorSituacao(calendar.getTime());
			for(Object object : lista){
				String nome = (String) ((Object[])object)[0];
				BigInteger quantidade = (BigInteger) ((Object[])object)[1];
				series.add(new DataSeriesItem(nome, quantidade));
			}
		} catch (Exception e) {
			series.add(new DataSeriesItem("Agendada", 0));
		}
        
        getConfiguration().setSeries(series);
    }

}
