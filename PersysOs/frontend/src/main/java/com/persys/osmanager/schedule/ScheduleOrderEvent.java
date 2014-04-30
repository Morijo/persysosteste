package com.persys.osmanager.schedule;

import java.util.Date;

import br.com.model.interfaces.IOrdem;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;

public class ScheduleOrderEvent implements CalendarEvent,  EditableCalendarEvent {

	 private static final long serialVersionUID = 1L;
	 Date start;
	 Date end;
	 String caption;
	 String styleName;
	 String description;
	 IOrdem ordem;

	 public ScheduleOrderEvent(Date start, Date end, IOrdem ordem) {
		 this.start = start;
		 this.end = end;
		 this.caption = ordem.getCodigo();
		 this.description = ordem.getClienteObjeto().getCodigo() +" - "+ ordem.getClienteObjeto().getCliente().getRazaoNome()+
				 " </br> Assunto: "+ ordem.getAssunto()+
		 		 " </br> Situação: "+ ordem.getSituacaoOrdem().getNome();
		 this.ordem = ordem;
		 
		 com.vaadin.server.Page.Styles styles = Page.getCurrent().getStyles();
		 
		 Color color = new Color(Integer.parseInt(ordem.getPrioridade().getCor()));
			
		 String cor = "color:"+color.getCSS();
		 
		 styles.add(".v-calendar .v-calendar-event-mycolor"+ordem.getPrioridade().getId()+" { "+ cor + "; }"); 
		 //styles.add(".dashboard .schedule .v-calendar-event-"+ordem.getSituacaoOrdem().getId()+" .v-calendar-event-caption { "+ cor + "; }"); 
		 
		 //cor = "background:"+color.getCSS();
			
		// styles.add(".v-calendar .v-calendar-event-mycolor"+ordem.getSituacaoOrdem().getId()+" .v-calendar-event-content { "+ cor + "; }"); 
			
		 this.styleName = "mycolor"+ordem.getPrioridade().getId();
		
	 }

	 @Override
	 public Date getStart() {
		 return start;
	 }

	 @Override
	 public Date getEnd() {
		 return end;
	 }

	 @Override
	 public String getCaption() {
		 return caption;
	 }

	 @Override
	 public String getDescription() {
		 return description;
	 }

	 @Override
	 public String getStyleName() {
		 return styleName;
	 }

	 @Override
	 public boolean isAllDay() {
		 return false;
	 }

	 public void addEventChangeListener(
			 EventChangeListener listener) {
	 }

	 @Override
	 public void setCaption(String caption) {
		 // TODO Auto-generated method stub

	 }

	 @Override
	 public void setDescription(String description) {
		 // TODO Auto-generated method stub

	 }

	 @Override
	 public void setEnd(Date end) {
		 // TODO Auto-generated method stub

	 }

	 @Override
	 public void setStart(Date start) {
		 // TODO Auto-generated method stub

	 }

	 @Override
	 public void setStyleName(String styleName) {
		 //this.styleName = "mycolor"+ordem.getSituacaoOrdem().getId();
	 }

	 @Override
	 public void setAllDay(boolean isAllDay) {
		 // TODO Auto-generated method stub
	 }
}
