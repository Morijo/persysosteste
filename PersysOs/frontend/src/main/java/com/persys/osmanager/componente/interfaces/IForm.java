package com.persys.osmanager.componente.interfaces;

import com.persys.osmanager.exception.ViewException;
import com.vaadin.ui.Component;

public interface IForm<T> extends Component {

	public T commit() throws ViewException;
	public void initData(T data);
	
	public void modoView();
	public void modoAdd();
}
