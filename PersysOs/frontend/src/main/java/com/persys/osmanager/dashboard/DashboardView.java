/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.persys.osmanager.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import br.com.eventos.model.Alerta;
import br.com.exception.ModelException;
import br.com.principal.helper.FormatDateHelper;

import com.persys.osmanager.chart.SituacaoPorOrdemChart;
import com.persys.osmanager.order.data.TransactionsContainerOrdemServicoData;
import com.persys.osmanager.tracker.Tracker2View;
import com.restmb.RestMBClient;
import com.restmb.types.Usuario;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.RowHeaderMode;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DashboardView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	private Table ordemServicoTable;
	private final TransactionsContainerOrdemServicoData dataOrdemServico = new TransactionsContainerOrdemServicoData();
	private RestMBClient client = null;

	private final Label title = new Label("Meu Dashboard");
	private final TextArea notes = new TextArea("Notas","Digite suas anotações");
	private int eTagNotes = 0;

	public DashboardView() {

		client = ((DashboardUI)UI.getCurrent()).getClient(); 
		Usuario<?> usuario = Usuario.getHomeUsuarioDashboard(client);

		setHeight("100%");
		setWidth("100%");
		addStyleName("dashboard-view");


		HorizontalLayout top = new HorizontalLayout();
		top.setWidth("100%");
		top.setSpacing(true);
		top.addStyleName("toolbar");
		addComponent(top);

		try{
			if(!usuario.getDashboardNome().isEmpty())
				title.setValue(usuario.getDashboardNome());
		}catch(Exception e){
			title.setValue("Meu Dashboard");
		}
		title.setSizeUndefined();
		title.addStyleName("h1");
		top.addComponent(title);
		top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		top.setExpandRatio(title, 1);

		Button notify = createNotificacao(((DashboardUI)UI.getCurrent()).getNumNotification());
		top.addComponent(notify);
		top.setComponentAlignment(notify, Alignment.MIDDLE_LEFT);

		Button edit = editNameDashboard(top, title);
		top.setComponentAlignment(edit, Alignment.MIDDLE_LEFT);

		HorizontalLayout row = new HorizontalLayout();
		row.setHeight("100%");
		row.setWidth("100%");
		row.setMargin(new MarginInfo(true, true, false, true));
		row.setSpacing(true);

		Tracker2View tracker2View = new Tracker2View();
		row.addComponent(createPanel(tracker2View.maps()));
		tracker2View.defaultTableGeral(new Date());

		HorizontalLayout row2 = new HorizontalLayout();
		row2.setMargin(true);
		row2.setHeight("100%");
		row2.setWidth("100%");
		row2.setSpacing(true);

		createTableOrder();

		row2.addComponent(createPanel(ordemServicoTable));
		row2.addComponent(createPanel(new SituacaoPorOrdemChart()));
		row2.addComponent(createNotes(usuario));

		addComponent(row);
		setExpandRatio(row, 2.5f);

		addComponent(row2);
		setExpandRatio(row2, 2);

	}

	private void createTableOrder() {
		ordemServicoTable = new Table();
		ordemServicoTable.setCaption("Ordens de Serviços - Hoje");
		ordemServicoTable.setWidth("100%");
		ordemServicoTable.setPageLength(0);
		ordemServicoTable.addStyleName("plain");
		ordemServicoTable.addStyleName("borderless");
		ordemServicoTable.setSortEnabled(false);
		ordemServicoTable.setColumnAlignment("Revenue", Align.RIGHT);
		ordemServicoTable.setRowHeaderMode(RowHeaderMode.INDEX);
		ordemServicoTable.setContainerDataSource(dataOrdemServico);
		ordemServicoTable.setVisibleColumns((Object[]) new String[] { "OS", "Cliente",
		"Situacao"});

		defaultTable();
	}

	private CssLayout createNotes(Usuario<?> usuario) {
		try{
			if(!usuario.getNota().isEmpty())
				notes.setValue(usuario.getNota());
		}catch(Exception e){
			notes.setValue("Digite suas anotações");
		}

		eTagNotes = notes.getValue().hashCode();

		notes.setSizeFull();
		CssLayout panel = createPanel(notes);

		Button configure = new Button();
		configure.addStyleName("configure");
		configure.addStyleName("icon-upload");
		configure.addStyleName("icon-only");
		configure.addStyleName("borderless");
		configure.setDescription("Salvar");
		configure.addStyleName("small");
		configure.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				notificaAlteracao();
			}
		});

		panel.addComponent(configure);

		panel.addStyleName("notes");
		return panel;
	}

	private Button createNotificacao(String numberNotification) {
		Button notify = new Button(numberNotification);
		notify.setDescription("Notificações");
		notify.addStyleName("notifications");
		notify.addStyleName("unread");
		notify.addStyleName("icon-only");
		notify.addStyleName("icon-bell");
		notify.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				((DashboardUI) getUI()).clearDashboardButtonBadge();
				event.getButton().removeStyleName("unread");
				event.getButton().setDescription("Notifications");

				try {
					Alerta.setLida(client.getOauth().getApiKey(), 1);
				} catch (ModelException e) {}
				
				if (notifications != null && notifications.getUI() != null)
					notifications.close();
				else {
					buildNotifications(event);
					getUI().addWindow(notifications);
					notifications.focus();
					((CssLayout) getUI().getContent())
					.addLayoutClickListener(new LayoutClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void layoutClick(LayoutClickEvent event) {
							notifications.close();
							((CssLayout) getUI().getContent())
							.removeLayoutClickListener(this);
						}
					});
				}
			}
		});
		return notify;
	}

	private Button editNameDashboard(HorizontalLayout top, final Label title) {
		Button edit = new Button();
		edit.addStyleName("icon-edit");
		edit.addStyleName("icon-only");
		top.addComponent(edit);
		edit.setDescription("Edit Dashboard");
		edit.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				final Window w = new Window("Editar Dashboard");

				w.setModal(true);
				w.setClosable(false);
				w.setResizable(false);
				w.addStyleName("edit-dashboard");

				getUI().addWindow(w);

				w.setContent(new VerticalLayout() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					TextField name = new TextField("Nome do Dashboard");
					{
						addComponent(new FormLayout() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							{
								setSizeUndefined();
								setMargin(true);
								name.setValue(title.getValue());
								addComponent(name);
								name.focus();
								name.selectAll();
							}
						});

						addComponent(new HorizontalLayout() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							{
								setMargin(true);
								setSpacing(true);
								addStyleName("footer");
								setWidth("100%");

								Button cancel = new Button("Cancelar");
								cancel.addClickListener(new ClickListener() {
									/**
									 * 
									 */
									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(ClickEvent event) {
										w.close();
									}
								});
								cancel.setClickShortcut(KeyCode.ESCAPE, null);
								addComponent(cancel);
								setExpandRatio(cancel, 1);
								setComponentAlignment(cancel,
										Alignment.TOP_RIGHT);

								Button ok = new Button("Salvar");
								ok.addStyleName("wide");
								ok.addStyleName("default");
								ok.addClickListener(new ClickListener() {
									/**
									 * 
									 */
									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(ClickEvent event) {
										title.setValue(name.getValue());
										notificaAlteracao();
										w.close();
									}
								});
								ok.setClickShortcut(KeyCode.ENTER, null);
								addComponent(ok);
							}
						});

					}
				});

			}
		});
		return edit;
	}

	Window notifications;

	private void buildNotifications(ClickEvent event) {
		notifications = new Window("Notificações");
		VerticalLayout layatouNotifications = new VerticalLayout();
		layatouNotifications.setMargin(true);
		layatouNotifications.setSpacing(true);
		notifications.setContent(layatouNotifications);
		notifications.setWidth("300px");
		notifications.addStyleName("notifications");
		notifications.setClosable(false);
		notifications.setResizable(false);
		notifications.setDraggable(false);
		notifications.setPositionX(event.getClientX() - event.getRelativeX());
		notifications.setPositionY(event.getClientY() - event.getRelativeY());
		notifications.setCloseShortcut(KeyCode.ESCAPE, null);

		try {
			ArrayList<Alerta> alertas = (ArrayList<Alerta>) Alerta.listaAlerta(client.getOauth().getApiKey(),1);
			for(Alerta alerta : alertas){
				Label label = new Label(alerta.getUsuario().getRazaoNome() +": "+alerta.getMensagem(), ContentMode.HTML);
				layatouNotifications.addComponent(label);
			}
		} catch (ModelException e) {
			Label label = new Label("Falha ao carregar os alertas", ContentMode.HTML);
			layatouNotifications.addComponent(label);
		}
	}

	public void defaultTable() {

		Date hoje  = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hoje);
		calendar.add(Calendar.DAY_OF_YEAR,1);

		dataOrdemServico.removeAllItems();
		try{
			dataOrdemServico.load(client,"/ordem/agendamento?datainicio="+FormatDateHelper.formatTimeZoneBRRequest(hoje.getTime())+"&datafim="+FormatDateHelper.formatTimeZoneBRRequest(calendar.getTime().getTime()));
		}catch (Exception e) {}
	}

	private CssLayout createPanel(Component content) {
		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setHeight("100%");
		panel.addComponent(content);
		return panel;
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	private void notificaAlteracao() {
		@SuppressWarnings("rawtypes")
		Usuario<?> usuario = new Usuario();
		usuario.setDashboardNome(title.getValue());
		usuario.setNota(notes.getValue());

		Usuario.alteraDashboard(client, usuario);
	}

	@Override
	public void detach() {
		super.detach();
		if(eTagNotes != notes.getValue().hashCode())
			notificaAlteracao();
	}
}
