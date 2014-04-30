 package com.persys.osmanager.schedule;

import java.util.List;

import com.restmb.Connection;
import com.restmb.Parameter;
import com.restmb.RestMBClient;
import com.restmb.types.OrdemServico;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.VerticalLayout;

public class ScheduleListOrder extends CustomComponent {

	private static final long serialVersionUID = 1L;
	
	public VerticalLayout verticalItens;
	
	private RestMBClient client;
	
	public ScheduleListOrder(RestMBClient client) {
		this.client = client;
	
		setCompositionRoot(buildMainLayout());

		defaultTable();
	}


	private VerticalLayout buildMainLayout() {
		// common part: create layout
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(true);
		mainLayout.setWidth("-1px");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(new MarginInfo(true, false, true, false));
		mainLayout.setSpacing(true);

		verticalItens = new VerticalLayout();
		verticalItens.setWidth("300px");
		verticalItens.setHeight("-1px");
		verticalItens.setSpacing(false);

		mainLayout.addComponent(verticalItens);

		return mainLayout;
	}
	
	public void createItem(OrdemServico ordemServico){

		ScheduleItemOrder scheduleItemOrder = new ScheduleItemOrder(client,ordemServico);
		scheduleItemOrder.setImmediate(false);
		scheduleItemOrder.setWidth("100.0%");
		scheduleItemOrder.setHeight("-1px");
		
		final DragAndDropWrapper scheduleItemOrderWrap = new DragAndDropWrapper(scheduleItemOrder);
		scheduleItemOrderWrap.setDragStartMode(DragStartMode.COMPONENT);
		scheduleItemOrderWrap.setData(ordemServico);
		verticalItens.addComponent(scheduleItemOrderWrap);

	}

	public void removeItem(DragAndDropWrapper scheduleItemOrderWrap){
		verticalItens.removeComponent(scheduleItemOrderWrap);
	}

	public void defaultTable() {
		try{
			Connection<OrdemServico> lista = OrdemServico.listaAll(client,"/ordem",OrdemServico.class,Parameter.with("agendada", false));
		
			for(List<OrdemServico> listaOrdemServico : lista){
				for(OrdemServico ordemServico : listaOrdemServico){
					createItem(ordemServico);
				}
			}
		}catch (Exception e) {}
	}

}
