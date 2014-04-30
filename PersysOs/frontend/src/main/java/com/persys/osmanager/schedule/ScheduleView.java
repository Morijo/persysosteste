package com.persys.osmanager.schedule;

import java.util.List;
import com.persys.osmanager.dashboard.DashboardUI;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.OrdemServico;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;

public class ScheduleView extends HorizontalLayout implements View {

	private static final long serialVersionUID = 1L;

	private RestMBClient client;
	
	private ScheduleOrdemGeral scheduleOrdemGeral;
	
	@Override
	public void enter(ViewChangeEvent event) {
		client =  ((DashboardUI)getUI()).getClient();

		setSizeFull();
		addStyleName("schedule");
		
		TabSheet abas = new TabSheet();
		abas.setSizeFull();
		
		CentralAgendamentoView centralAgendamentoView = new CentralAgendamentoView(getUI(), client);
		abas.addTab(centralAgendamentoView, "Central Agendamento");

		scheduleOrdemGeral = new ScheduleOrdemGeral(getUI(), client);
		abas.addTab(scheduleOrdemGeral, "Agenda Geral");

		defaultTable();

		addComponent(abas);
	}

	@SuppressWarnings("deprecation")
	public void defaultTable() {
		try{
			Connection<OrdemServico> lista = OrdemServico.listaAll(client,"/ordem/agendamento",OrdemServico.class);
			for(List<OrdemServico> listaOrdemServico : lista){
				for(OrdemServico ordemServico : listaOrdemServico){
					if(ordemServico.getDataAgendamentoFim() == null){
						java.util.Calendar timeCalendar = java.util.Calendar.getInstance();
						timeCalendar.setTime(ordemServico.getDataAgendamentoInicio());
						timeCalendar.add(java.util.Calendar.MINUTE, 120);
						ordemServico.setDataAgendamentoFim(timeCalendar.getTime());
					}
					scheduleOrdemGeral.provider.addEvent((new ScheduleOrderEvent(ordemServico.getDataAgendamentoInicio(), ordemServico.getDataAgendamentoFim(),ordemServico)));
				}
			}
		}catch (Exception e) {}
		scheduleOrdemGeral.cal.requestRepaintAll();
	}
}
