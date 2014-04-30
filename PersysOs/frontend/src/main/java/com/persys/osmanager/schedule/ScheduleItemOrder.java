package com.persys.osmanager.schedule;

import br.com.model.interfaces.IOrdem;
import br.com.principal.helper.FormatDateHelper;

import com.restmb.RestMBClient;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ColorPickerArea;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

public class ScheduleItemOrder extends CustomComponent {

	private static final long serialVersionUID = 1L;
	
	public ScheduleItemOrder(final RestMBClient client, IOrdem ordemServico) {
		buildMainLayout(client, ordemServico);
		
		setWidth("100%");
		setHeight("100px");
		setStyleName("itemlistmaps");

		setCompositionRoot(buildMainLayout(client, ordemServico));
	}

	private AbsoluteLayout buildMainLayout(final RestMBClient client, final IOrdem ordemServico) {

		AbsoluteLayout mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100px");
	
		Label labelOS = new Label();
		labelOS.setStyleName("h2");
		labelOS.setImmediate(false);
		labelOS.setWidth("-1px");
		labelOS.setHeight("-1px");
		labelOS.setValue(ordemServico.getCodigo());
		labelOS.setDescription("Referente ao contrato: "+ordemServico.getClienteObjeto().getCodigo()+
				"</br> Assunto: " + ordemServico.getAssunto());
		mainLayout.addComponent(labelOS, "top:-8.0px;left:20.0px;");

		// labelCliente
		Label labelCliente = new Label();
		labelCliente.setImmediate(false);
		labelCliente.setWidth("-1px");
		labelCliente.setHeight("-1px");
		labelCliente.setValue(ordemServico.getClienteObjeto().getCliente().getRazaoNome());
		labelCliente.setDescription(ordemServico.getClienteObjeto().getCliente().getCodigo()
				+" Cpf/Cnpj: "+ ordemServico.getClienteObjeto().getCliente().getCnpjCpf());
		mainLayout.addComponent(labelCliente, "top:35.0px;left:20.0px;");

		Label labelCidade = new Label();
		labelCidade.setImmediate(false);
		labelCidade.setWidth("280px");
		labelCidade.setHeight("-1px");
		labelCidade.setValue(ordemServico.getContato().getEndereco().getCidade());
		labelCidade.setDescription(ordemServico.getContato().getEndereco().toString());
		mainLayout.addComponent(labelCidade, "top:60.0px;left:20.0px;");

		// labelDataCriacao
		Label labelDataCriacao = new Label();
		labelDataCriacao.setImmediate(false);
		labelDataCriacao.setWidth("-1px");
		labelDataCriacao.setHeight("-1px");
		labelDataCriacao.setDescription("Data Criação");
		labelDataCriacao.setValue(FormatDateHelper.formatTimeZoneBRDATE(ordemServico.getDataCriacao().getTime()));
		mainLayout.addComponent(labelDataCriacao, "top:4.0px;left:200.0px;");

		// colorPickerArea_1
		ColorPickerArea colorPickerArea = new ColorPickerArea();
		colorPickerArea.setImmediate(false);
		colorPickerArea.setWidth("10px");
		colorPickerArea.setHeight("92px");
		colorPickerArea.setDescription(ordemServico.getPrioridade().getPrioridade());
		colorPickerArea.setColor(new Color(Integer.parseInt(ordemServico.getPrioridade().getCor())));
		mainLayout.addComponent(colorPickerArea, "top:4.0px;left:4.0px;");
				
		mainLayout.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 1L;
			public void layoutClick(LayoutClickEvent event) {
				UI.getCurrent().addWindow(new ScheduleOrderWindow(client, ordemServico));
			}
		});
		
		return mainLayout;
	}
	
}
