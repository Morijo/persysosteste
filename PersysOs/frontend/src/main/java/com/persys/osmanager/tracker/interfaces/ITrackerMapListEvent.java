package com.persys.osmanager.tracker.interfaces;

import java.util.Date;
import br.com.funcionario.model.Funcionario;

public interface ITrackerMapListEvent {

	public void eventDate(Date newDate);
	public void eventSelectOrder();
	public void eventSelectTracker();
	public void eventSelectEmployees(Long id);
	public void eventUnSelectEmployees(Long id);
	public void eventSelectEmployees(Funcionario funcionario);

}
