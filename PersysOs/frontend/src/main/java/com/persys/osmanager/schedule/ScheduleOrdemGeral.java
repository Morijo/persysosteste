package com.persys.osmanager.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import br.com.model.interfaces.IOrdem;
import br.com.principal.helper.FormatDateHelper;

import com.restmb.RestMBClient;
import com.restmb.exception.RestMBException;
import com.restmb.types.OrdemServico;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.TableTransferable;
import com.vaadin.ui.components.calendar.CalendarTargetDetails;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.BackwardEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.BackwardHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventResize;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.ForwardEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.ForwardHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.MoveEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClickHandler;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import com.vaadin.ui.components.calendar.handler.BasicWeekClickHandler;

public class ScheduleOrdemGeral extends HorizontalLayout{

	private static final long serialVersionUID = 1L;

	public OrderEventProvider provider = new OrderEventProvider();
	
	public CssLayout menuOrdem;

	private CssLayout content;

	private VerticalLayout calendarLayout;
	
	private Date currentMonthsFirstDate;

	private RestMBClient client;

	public Calendar cal;
	
	private enum Mode {MONTH, WEEK, DAY;}

	public Mode viewMode = Mode.DAY;

	public ScheduleListOrder scheduleListOrder;

	public java.util.Calendar calendar;
	
	private Label labelDiaAtual;
	
	private UI ui;
	
	public ScheduleOrdemGeral(UI ui, RestMBClient client){  
		this.ui = ui;
		this.client = client;
		
		setSizeFull();
		addStyleName("main-view");

		menuOrdem = new CssLayout();
		content = new CssLayout();

		content.setSizeFull();
		content.addComponent(buildCalendarView());
		addComponent(content);
		setExpandRatio(content, 1); 

		addComponent(new VerticalLayout() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			{
				addStyleName("sidebar-invert");
				setWidth(null);
				setHeight("100%");
				menuOrdem.addStyleName("menu");
				menuOrdem.setHeight("100%");
				addComponent(menuOrdem);
				setExpandRatio(menuOrdem, 1);
			}
		});
		scheduleListOrder = new ScheduleListOrder(client);
		menuOrdem.addComponent(scheduleListOrder);
	}

	
	public Component buildCalendarView() {
		calendarLayout = new VerticalLayout();
		calendarLayout.addStyleName("dummy");
		calendarLayout.setHeight("100%");
		calendarLayout.setMargin(true);
		calendarLayout.setImmediate(true);

		labelDiaAtual = new Label(FormatDateHelper.formatTimeZoneBRDATE(new Date().getTime()));
		labelDiaAtual.setStyleName("h1");

		initLayoutContent();

		cal = new Calendar(provider);
		cal.setWidth("100%");
		cal.setHeight("100%");
		cal.setImmediate(true);
		
		Locale ptBr = new Locale("pt", "BR"); //Locale para o Brasil
		cal.setLocale(ptBr);

		calendarLayout.addComponent(cal);
		calendarLayout.setExpandRatio(cal, 1);

		cal.setFirstVisibleHourOfDay(6);
		cal.setLastVisibleHourOfDay(20);

		calendar = new GregorianCalendar(ptBr);
		calendar.setTime(new Date());
	
		int rollAmount = calendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
		calendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
		resetTime(false);
		currentMonthsFirstDate = calendar.getTime();
		cal.setStartDate(currentMonthsFirstDate);
		calendar.add(GregorianCalendar.MONTH, 1);
		calendar.add(GregorianCalendar.DATE, -1);
		cal.setEndDate(calendar.getTime());

		addCalendarEventListeners();

		return calendarLayout;
	}

	private void initLayoutContent() {
		Button monthButton = new Button("Mês", new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				switchToMonthView();
			}
		});

		Button weekButton = new Button("Semana", new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				switchToWeekView();
			}
		});

		Button nextButton = new Button("Próximo", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				handleNextButtonClick();
			}
		});

		Button prevButton = new Button("Anterior", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				handlePreviousButtonClick();
			}
		});

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setWidth("-1");
		horizontalLayout.addComponent(prevButton);
		horizontalLayout.addComponent(monthButton);
		
		horizontalLayout.addComponent(labelDiaAtual);
		
		horizontalLayout.addComponent(weekButton);
		horizontalLayout.addComponent(nextButton);
		horizontalLayout.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
		horizontalLayout.setComponentAlignment(monthButton, Alignment.MIDDLE_CENTER);
		horizontalLayout.setComponentAlignment(weekButton, Alignment.MIDDLE_CENTER);
		horizontalLayout.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);

		monthButton.setVisible(true);
		weekButton.setVisible(true);

		calendarLayout.addComponent(horizontalLayout);
		calendarLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
	}


	private void addCalendarEventListeners() {

		cal.setHandler(new EventClickHandler() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void eventClick(EventClick event) {
				ScheduleOrderEvent scheduleOrderEvent = (ScheduleOrderEvent) event.getCalendarEvent();
				ui.addWindow(buildPopup(scheduleOrderEvent.ordem));
			}
		});

		cal.setHandler(new BackwardHandler() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void backward(BackwardEvent event) {
				System.out.println("Backward");
			}
		});

		cal.setHandler(new ForwardHandler() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void forward(ForwardEvent event) {
				System.out.println("Forward");
			}
		});

		cal.setDropHandler(new DropHandler() {

			private static final long serialVersionUID = -8939822725278862037L;
			public void drop(DragAndDropEvent event) {

				CalendarTargetDetails details = (CalendarTargetDetails) event
						.getTargetDetails();

				if(event.getTransferable() instanceof TableTransferable){
					TableTransferable transferable = (TableTransferable) event
							.getTransferable();

					createEvent(details, transferable);
				}else{
					createEvent(details, event.getTransferable());
				}

			}

			public AcceptCriterion getAcceptCriterion() {
				return AcceptAll.get();
			}

		});

		cal.setHandler(new BasicEventMoveHandler() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void eventMove(MoveEvent event) {
				super.eventMove(event);
				CalendarEvent calendarEvent = event.getCalendarEvent();

				if (calendarEvent instanceof ScheduleOrderEvent) {
					ScheduleOrderEvent editableEvent = (ScheduleOrderEvent) calendarEvent;

					Date newFromTime = event.getNewStart();

					long length = editableEvent.getEnd().getTime()
							- editableEvent.getStart().getTime();
					setDates(editableEvent, newFromTime,
							new Date(newFromTime.getTime() + length));

					buildPopup(editableEvent.ordem);

					agendaOrdem(editableEvent.ordem,editableEvent.getEnd(),editableEvent.getStart());

				}
			}

			protected void setDates(ScheduleOrderEvent event, Date start, Date end) {
				event.start = start;
				event.end = end;
			}
		});
		cal.setHandler(new BasicEventResizeHandler() {
		
			private static final long serialVersionUID = 1L;

		@SuppressWarnings("deprecation")
			@Override
			public void eventResize(EventResize event){
				super.eventResize(event);
				CalendarEvent calendarEvent = event.getCalendarEvent();
				if (calendarEvent instanceof ScheduleOrderEvent) {
					ScheduleOrderEvent editableEvent = (ScheduleOrderEvent) calendarEvent;
					Date newFromTime = event.getNewStart();

					long length = editableEvent.getEnd().getTime()
							- editableEvent.getStart().getTime();
					setDates(editableEvent, newFromTime,
							new Date(newFromTime.getTime() + length));

					buildPopup(editableEvent.ordem);

					System.out.println(event.getNewStart() +" "+event.getNewEndTime());
					agendaOrdem(editableEvent.ordem,event.getNewEnd(),newFromTime);
				}

			}

			@SuppressWarnings("unused")
			protected void setDates(ScheduleOrderEvent event, 
					Date start, Date end) {
				long eventLength = end.getTime() - start.getTime();
				super.setDates(event, start, end);
			}
		});

		cal.setHandler(new BasicWeekClickHandler() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void weekClick(final WeekClick event) {
				super.weekClick(event);
				//switchToWeekView();
			}
		});
	}

	private void handleNextButtonClick() {
		switch (viewMode) {
		case MONTH:
			nextMonth();
			break;
		case WEEK:
			nextWeek();
			break;
		case DAY:
			nextDay();
			break;
		}
	}

	private void handlePreviousButtonClick() {
		switch (viewMode) {
		case MONTH:
			previousMonth();
			break;
		case WEEK:
			previousWeek();
			break;
		case DAY:
			previousDay();
			break;
		}
	}

	private void nextMonth() {
		rollMonth(1);
	}

	private void previousMonth() {
		rollMonth(-1);
	}

	private void nextWeek() {
		rollWeek(1);
	}

	private void previousWeek() {
		rollWeek(-1);
	}

	private void nextDay() {
		rollDate(1);
	}

	private void previousDay() {
		rollDate(-1);
	}

	private void rollMonth(int direction) {
		calendar.setTime(currentMonthsFirstDate);
		calendar.add(GregorianCalendar.MONTH, direction);
		resetTime(false);
		currentMonthsFirstDate = calendar.getTime();
		cal.setStartDate(currentMonthsFirstDate);

		calendar.add(GregorianCalendar.MONTH, 1);
		calendar.add(GregorianCalendar.DATE, -1);
		
		resetCalendarTime(true);
		labelDiaAtual.setValue(FormatDateHelper.formatTimeZoneBRDATE(new Date().getTime()));
		
	}

	private void rollWeek(int direction) {

		calendar.add(GregorianCalendar.WEEK_OF_YEAR, direction);
		calendar.set(GregorianCalendar.DAY_OF_WEEK,
				calendar.getFirstDayOfWeek());
		resetCalendarTime(false);
		resetTime(true);
		calendar.add(GregorianCalendar.DATE, 6);
		cal.setEndDate(calendar.getTime());
		
	}

	private void rollDate(int direction) {

		calendar.add(GregorianCalendar.DATE, direction);
		resetCalendarTime(false);
		resetCalendarTime(true);
		
	}
	/*
	 * Switch the view to week view.
	 */
	public void switchToWeekView() {
		viewMode = Mode.WEEK;

		calendar = java.util.Calendar.getInstance();

		WeekClickHandler handler = (WeekClickHandler) cal
				.getHandler(WeekClick.EVENT_ID);
		handler.weekClick(new WeekClick(cal, calendar.get(java.util.Calendar.WEEK_OF_YEAR), calendar
				.get(GregorianCalendar.YEAR)));
		
	}

	/*
	 * Switch the Calendar component's start and end date range to the target
	 * month only. (sample range: 01.01.2010 00:00.000 - 31.01.2010 23:59.999)
	 */
	public void switchToMonthView() {
		viewMode = Mode.MONTH;

		calendar.setTime(currentMonthsFirstDate);
		cal.setStartDate(currentMonthsFirstDate);

		calendar.add(GregorianCalendar.MONTH, 1);
		calendar.add(GregorianCalendar.DATE, -1);
		resetCalendarTime(true);
		labelDiaAtual.setValue(FormatDateHelper.formatTimeZoneBRDATE(new Date().getTime()));
		
	}

	/*
	 * Switch to day view (week view with a single day visible).
	 */
	public void switchToDayView() {
		viewMode = Mode.DAY;
	}

	private void resetCalendarTime(boolean resetEndTime) {
		resetTime(resetEndTime);
		if (resetEndTime) {
			cal.setEndDate(calendar.getTime());
		} else {
			cal.setStartDate(calendar.getTime());
		}
		
		labelDiaAtual.setValue(FormatDateHelper.formatTimeZoneBRDATE(calendar.getTime().getTime()));
	}

	/*
	 * Resets the calendar time (hour, minute second and millisecond) either to
	 * zero or maximum value.
	 */
	private void resetTime(boolean max) {
		if (max) {
			calendar.set(GregorianCalendar.HOUR_OF_DAY,
					calendar.getMaximum(GregorianCalendar.HOUR_OF_DAY));
			calendar.set(GregorianCalendar.MINUTE,
					calendar.getMaximum(GregorianCalendar.MINUTE));
			calendar.set(GregorianCalendar.SECOND,
					calendar.getMaximum(GregorianCalendar.SECOND));
			calendar.set(GregorianCalendar.MILLISECOND,
					calendar.getMaximum(GregorianCalendar.MILLISECOND));
		} else {
			calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
			calendar.set(GregorianCalendar.MINUTE, 0);
			calendar.set(GregorianCalendar.SECOND, 0);
			calendar.set(GregorianCalendar.MILLISECOND, 0);
		}
	}

	@SuppressWarnings("deprecation")
	private void createEvent(CalendarTargetDetails details,
			Transferable transferable) {

		Date startTime = details.getDropTime();

		java.util.Calendar timeCalendar = details.getTargetCalendar().getInternalCalendar();
		timeCalendar.add(java.util.Calendar.MINUTE, 60);

		Date endTime = timeCalendar.getTime();

		OrdemServico ordem = (OrdemServico) ((DragAndDropWrapper) transferable.getSourceComponent()).getData();

		if(agendaOrdem(ordem,endTime,startTime)){
			((OrderEventProvider)details.getTargetCalendar().getEventProvider()).addEvent((new ScheduleOrderEvent(startTime, endTime, ordem)));
			scheduleListOrder.removeItem((DragAndDropWrapper) transferable.getSourceComponent());
			cal.requestRepaintAll();
		} 
	}

	private boolean agendaOrdem(IOrdem ordemServico, Date endTime, Date startTime){

		try{ 
			ordemServico.setDataAgendamentoInicio(startTime);
			ordemServico.setDataAgendamentoFim(endTime);
			((OrdemServico) ordemServico).adicionarAgendamento(client);
			buildPopup(ordemServico);
			return true;	
		}catch(RestMBException e){
			return false;
		}
	}

	class OrderEventProvider implements CalendarEventProvider {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public OrderEventProvider() {
			events = new ArrayList<CalendarEvent>();
		}

		private List<CalendarEvent> events = new ArrayList<CalendarEvent>();

		@Override
		public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
			return events;
		}

		public void addEvent(CalendarEvent OrderEvent) {
			events.add(OrderEvent);
		}
	}

	public Window buildPopup(final IOrdem ordem) {
		return new ScheduleOrderWindow(client, ordem);
	}

}
