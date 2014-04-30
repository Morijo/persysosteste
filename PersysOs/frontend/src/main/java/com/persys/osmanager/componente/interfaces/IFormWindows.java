package com.persys.osmanager.componente.interfaces;

import com.persys.osmanager.exception.ViewException;

public interface IFormWindows<T> {

	public T commit() throws ViewException;
	public void commitWindows(int resultTag) throws ViewException;
	public void initData(T data);
	
	public void modoView();
	public void modoAdd();
}
