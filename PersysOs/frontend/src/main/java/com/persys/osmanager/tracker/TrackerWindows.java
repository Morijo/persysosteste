package com.persys.osmanager.tracker;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import br.com.eventos.model.Evento;
import br.com.ordem.model.AgendaOrdemFuncionario;
import br.com.ordem.model.Ordem;
import br.com.principal.helper.FormatDateHelper;
import br.com.principal.helper.UrlHelper;
import com.persys.osmanager.chart.GraficoChart;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TrackerWindows extends Window {

	private static final long serialVersionUID = 1L;

	TransactionsContainerOrdem dataOrdem = new TransactionsContainerOrdem();
	TransactionsContainerEvento dataEvento = new TransactionsContainerEvento();
	TransactionsContainerVeiculo dataVeiculo = new TransactionsContainerVeiculo();
	br.com.funcionario.model.Funcionario funcionario = null;	   

	public TrackerWindows(br.com.funcionario.model.Funcionario funcionario) {
		this.funcionario = funcionario;

		VerticalLayout l = new VerticalLayout();
		l.setSpacing(true);

		setContent(l);
		center();
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setClosable(false);

		addStyleName("no-vertical-drag-hints");
		addStyleName("no-horizontal-drag-hints");

		setCaption(funcionario.getRazaoNome());

		TabSheet abas = new TabSheet();
		abas.setWidth("560px");
		abas.setHeight("300px");

		abas.addTab(geral(), "Geral");
		//abas.addTab(veiculo(), "Veículos");
		abas.addTab(ordemServico(), "Ordem Serviço");
		abas.addTab(evento(), "Eventos");
		abas.addTab(desempenho(), "Desempenho");
		//abas.addTab(desempenho(), "Mensagem");


		l.addComponent(abas);

		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName("footer");
		footer.setWidth("100%");
		footer.setMargin(true);

		Button ok = new Button("Fechar");
		ok.addStyleName("wide");
		ok.addStyleName("default");
		ok.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
		l.addComponent(footer);
	}

	public HorizontalLayout geral(){
		HorizontalLayout details = new HorizontalLayout();
		details.setSpacing(true);
		details.setMargin(true);

		Image coverImage;
		URL url= null;
		try {
			url = new URL(UrlHelper.END_POINT_SERVICE+"/funcionario/"+funcionario.getId()+"/imagem?x=60&y=60");
			url.openStream();
			ExternalResource externalResource = new ExternalResource(url);
			coverImage = new Image("",externalResource);
		} catch (Exception ex) {
			coverImage = new Image("",new ThemeResource("img/profile-pic.png"));
		}
		coverImage.setWidth("80px");
		coverImage.setHeight("80px");
		details.addComponent(coverImage);


		FormLayout fields = new FormLayout();
		fields.setWidth("35em");
		fields.setSpacing(true);
		fields.setMargin(true);
		details.addComponent(fields);

		Label label;

		label = new Label(funcionario.getRazaoNome());
		label.setSizeUndefined();
		label.setCaption("Nome");
		fields.addComponent(label);

		label = new Label(funcionario.getStatusModel() == 1?"Ativo":"Inativo");
		label.setSizeUndefined();
		label.setCaption("Situação");
		fields.addComponent(label);

		return details;
	}

	public VerticalLayout desempenho(){
		VerticalLayout layoutDesempenho = new VerticalLayout();
		layoutDesempenho.setSizeFull();
		layoutDesempenho.addComponent(new GraficoChart());
		return layoutDesempenho;
	}

	public VerticalLayout evento(){
		VerticalLayout layoutEvento = new VerticalLayout();
		layoutEvento.setSizeFull();
		final com.vaadin.ui.Table t = new Table();
		t.setSizeFull();
		t.addStyleName("borderless");
		t.setSelectable(true);
		t.setColumnCollapsingAllowed(true);
		t.setColumnReorderingAllowed(true);
		dataEvento.removeAllContainerFilters();
		t.setContainerDataSource(dataEvento);

		t.setColumnAlignment("Seats", Align.RIGHT);
		t.setColumnAlignment("Price", Align.RIGHT);

		t.setVisibleColumns(new Object[] {"Data","Evento", "Duração","Mensagem"});

		layoutEvento.addComponent(t);

		ArrayList<Evento> eventos = Evento.lista(funcionario.getId());
		for(Evento evento : eventos){
			dataEvento.addTransaction(evento);
		}

		return layoutEvento;
	}

	public VerticalLayout veiculo(){
		VerticalLayout layoutVeiculo = new VerticalLayout();
		layoutVeiculo.setSizeFull();

		final com.vaadin.ui.Table t = new Table();
		t.setSizeFull();
		t.addStyleName("borderless");
		t.setSelectable(true);
		t.setColumnCollapsingAllowed(true);
		t.setColumnReorderingAllowed(true);
		dataVeiculo.removeAllContainerFilters();
		t.setContainerDataSource(dataVeiculo);
		layoutVeiculo.addComponent(t);

		dataVeiculo.addTransaction( "Código","001");
		dataVeiculo.addTransaction( "Placa","XXX - 2034");
		dataVeiculo.addTransaction( "Horímetro","23232 km");
		dataVeiculo.addTransaction( "Velocidade média","40 km");
		dataVeiculo.addTransaction( "Velocidade máxima","80 km");
		dataVeiculo.addTransaction( "Início","8h30");
		dataVeiculo.addTransaction( "Fim"," Em uso ");

		return layoutVeiculo;
	}

	public VerticalLayout ordemServico(){
		VerticalLayout layoutOrdemServico = new VerticalLayout();
		layoutOrdemServico.setSizeFull();
		final com.vaadin.ui.Table t = new Table();
		t.setSizeFull();
		t.addStyleName("borderless");
		t.setSelectable(true);
		t.setColumnCollapsingAllowed(true);
		t.setColumnReorderingAllowed(true);
		dataOrdem.removeAllContainerFilters();
		t.setContainerDataSource(dataOrdem);

		t.setVisibleColumns(new Object[] { "Cliente", "Situação","Assunto"});

		layoutOrdemServico.addComponent(t);
	
		ArrayList<AgendaOrdemFuncionario> ordens = Ordem.listaOrdemAgendamentoPorFuncionario(0, 0, "", funcionario.getId(),new Date() , new Date());
		for(AgendaOrdemFuncionario orden : ordens){
			dataOrdem.addTransaction(orden.getOrdem().getClienteObjeto().getCliente().getRazaoNome(), orden.getOrdem().getDataAgendamento(), orden.getOrdem().getSituacaoOrdem().getNome(),orden.getOrdem().getAssunto());
		}
		return layoutOrdemServico;
	}

	public class TransactionsContainerOrdem extends com.vaadin.data.util.IndexedContainer {

		private static final long serialVersionUID = 1L;

		public TransactionsContainerOrdem() {
			addContainerProperty("Cliente", String.class, "");
			addContainerProperty("Situação", String.class, "");
			addContainerProperty("Assunto", String.class, "");
		}

		@SuppressWarnings("unchecked")
		public void addTransaction(String cliente, Date dataAgendamento, String situacao, String assunto) {
			Object id = addItem();
			com.vaadin.data.Item item = getItem(id);
			if (item != null) {
				item.getItemProperty("Cliente").setValue(cliente);
				item.getItemProperty("Situação").setValue(situacao);
				//item.getItemProperty("Data Agendamento").setValue(situacao);
				item.getItemProperty("Assunto").setValue(assunto);
			}

		}
	} 

	public class TransactionsContainerEvento extends com.vaadin.data.util.IndexedContainer {

		private static final long serialVersionUID = 1L;

		public TransactionsContainerEvento() {
			addContainerProperty("Evento", String.class, "");
			addContainerProperty("Data", String.class, "");
			addContainerProperty("Mensagem", String.class, "");
			addContainerProperty("Duração", String.class, "");
		}

		@SuppressWarnings("unchecked")
		public void addTransaction(Evento evento) {
			Object id = addItem();
			com.vaadin.data.Item item = getItem(id);
			if (item != null) {
				item.getItemProperty("Evento").setValue(evento.getTipoEvento().getTitulo());
				item.getItemProperty("Data").setValue(FormatDateHelper.formatTimeZoneUSToBR(evento.getDataInicio().getTime()));
				item.getItemProperty("Mensagem").setValue(evento.getMensagem());
				long minutes = TimeUnit.MILLISECONDS.toMinutes(evento.getDataFim().getTime()-evento.getDataInicio().getTime());
				String duracao = String.valueOf(minutes + " minutos");
				item.getItemProperty("Duração").setValue(duracao);
			}
		}
	} 

	public class TransactionsContainerVeiculo extends com.vaadin.data.util.IndexedContainer {

		private static final long serialVersionUID = 1L;

		public TransactionsContainerVeiculo() {
			addContainerProperty("Titulo", String.class, "");
			addContainerProperty("Descricao", String.class, "");
		}

		@SuppressWarnings("unchecked")
		public void addTransaction(String titulo, String descricao) {
			Object id = addItem();
			com.vaadin.data.Item item = getItem(id);
			if (item != null) {
				item.getItemProperty("Titulo").setValue(titulo);
				item.getItemProperty("Descricao").setValue(descricao);
			}
		}
	} 
}