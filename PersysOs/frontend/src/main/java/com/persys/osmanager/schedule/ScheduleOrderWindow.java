package com.persys.osmanager.schedule;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IOrdem;

import com.persys.osmanager.customer.contract.ContractProdutoView;
import com.persys.osmanager.employee.EmployeeOrderView;
import com.persys.osmanager.resource.OrdemResourceView;
import com.persys.osmanager.service.OrdemServiceView;
import com.restmb.RestMBClient;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ScheduleOrderWindow extends Window {

	private static final long serialVersionUID = 1L;

	public ScheduleOrderWindow(final RestMBClient client, final IOrdem ordem) {

		Locale vmLocale = new Locale("pt","BR"); 
		ResourceBundle bundle = ResourceBundle.getBundle("com/persys/frontend/schedule",vmLocale);

		center();

		setWidth("800px");
		setHeight("600px");

		VerticalLayout verticalLayoutMain = new VerticalLayout();
		verticalLayoutMain.setSpacing(true);
		verticalLayoutMain.setMargin(true);
		verticalLayoutMain.setHeight("100%");


		setCaption(ordem.getCodigo());
		setContent(verticalLayoutMain);
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(true);
		setClosable(true);

		addStyleName("no-vertical-drag-hints");
		addStyleName("no-horizontal-drag-hints");

		TabSheet abas = new TabSheet();
		abas.addTab(abaDadosGeral(ordem, bundle, verticalLayoutMain),"Dados Gerais");

		abas.addTab(abaFuncionarioExecucao(client, ordem, bundle),"Empregado Execução");
		abas.addTab(new OrdemServiceView(client, ordem),"Serviços");
		abas.addTab(new ContractProdutoView(client, ordem.getClienteObjeto()),"Produtos");
		abas.addTab(new OrdemResourceView(client, ordem),"Recurso");

		verticalLayoutMain.addComponent(abas);

	}

	private CustomComponent abaFuncionarioExecucao(final RestMBClient client,
			final IOrdem ordem, ResourceBundle bundle) {

		return new EmployeeOrderView(client, ordem);
	}

	private HorizontalLayout abaDadosGeral(final IOrdem ordem,
			ResourceBundle bundle, VerticalLayout verticalLayoutMain) {

		HorizontalLayout details = new HorizontalLayout();
		details.setSpacing(true);
		details.setMargin(true);
		verticalLayoutMain.addComponent(details);

		FormLayout fields = new FormLayout();
		fields.setWidth("35em");
		fields.setSpacing(true);
		fields.setMargin(true);
		details.addComponent(fields);

		Label label;

		label = new Label(ordem.getClienteObjeto().getCliente().getCodigo()+" - "
				+ordem.getClienteObjeto().getCliente().getRazaoNome());
		label.setSizeUndefined();
		label.setCaption(bundle.getString("customer"));
		fields.addComponent(label);

		label = new Label(ordem.getClienteObjeto().getCliente().getCnpjCpf());
		label.setSizeUndefined();
		label.setCaption("CPF/CNPJ");
		fields.addComponent(label);

		label = new Label(ordem.getClienteObjeto().getCodigo());
		label.setSizeUndefined();
		label.setCaption("Contrato");
		fields.addComponent(label);

		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("dd/MM/yyyy hh:mm a");
		label = new Label(df.format(ordem.getDataCriacao()));
		label.setSizeUndefined();
		label.setCaption(bundle.getString("starttime"));
		fields.addComponent(label);
		try{
			label = new Label(df.format(ordem.getDataAgendamento()));
			label.setSizeUndefined();
			label.setCaption(bundle.getString("scheduletime"));
			fields.addComponent(label);
		}catch(Exception e){}

		try{
			label = new Label(df.format(ordem.getDataConclusao()));
			label.setSizeUndefined();
			label.setCaption(bundle.getString("endtime"));
			fields.addComponent(label);
		}catch(Exception e){}

		try{
			label = new Label(ordem.getSituacaoOrdem().getNome());
			label.setSizeUndefined();
			label.setCaption("Situação");
			fields.addComponent(label);
		}catch(Exception e){}

		label = new Label(ordem.getPrioridade().getPrioridade());
		label.setSizeUndefined();
		label.setCaption("Prioridade");
		fields.addComponent(label);

		label = new Label(ordem.getContato().getNome() );
		label.setSizeUndefined();
		label.setCaption("Contato");
		fields.addComponent(label);

		label = new Label(ordem.getContato().getTelefoneFixo() +" "+ ordem.getContato().getTelefoneMovel());
		label.setSizeUndefined();
		label.setCaption("Telefone");
		fields.addComponent(label);

		label = new Label(ordem.getContato().getEndereco().toString());
		label.setSizeUndefined();
		label.setCaption(bundle.getString("contact"));
		fields.addComponent(label);

		Label assunto = new Label();
		assunto.setValue(ordem.getAssunto());
		assunto.setCaption(bundle.getString("assunto"));
		fields.addComponent(assunto);

		return details;
	}

	public void commit(){}


}