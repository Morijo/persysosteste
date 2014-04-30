package com.persys.osmanager.componente.interfaces;

import com.persys.osmanager.data.TransactionsContainer;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;

public interface ITransactionsContainerData {
	
	public static final int BD = 0;
	public static final int REST = 1;
	
	public void loadTable(RestMBClient client, TransactionsContainer.TransationsContainerTipo modo) throws ViewException;
	void loadTableRest(RestMBClient client) throws ViewException;
	void loadTableDataBase(RestMBClient client) throws ViewException;
	
}
