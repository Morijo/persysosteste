package com.persys.osmanager.data;

import java.util.ArrayList;
import java.util.List;

import br.com.exception.ServiceException;
import br.com.model.interfaces.IModel;

import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.componente.interfaces.ITransactionsContainerData;
import com.restmb.Connection;
import com.restmb.Parameter;
import com.restmb.RestMBClient;
import com.restmb.types.RestMbType;
import com.vaadin.data.util.BeanContainer;

public abstract class TransactionsContainer<T extends IModel> extends com.vaadin.data.util.IndexedContainer implements
	ITransactionsContainerData{

	private static final long serialVersionUID = 1L;

	public abstract void addTransaction(T model);
	
	public abstract BeanContainer<Boolean, SearchFormWindow.Parameter> listParameter();

	public void searchRest(RestMBClient client,String recurso,Class<?> classe, String termo, String parametro) throws ServiceException{
		removeAllItems();
		if(!termo.isEmpty())
			try{
				Connection<T> listaBusca = RestMbType.listaAll(client,recurso, classe, Parameter.with(parametro,termo));
				for(List<T> lista : listaBusca){
					for(T t: lista){
						addTransaction(t);
					}
				}
			}catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}
	}
	
	protected ArrayList<T> loadRest(RestMBClient client,String recurso,Class<?> classe) {
    	removeAllItems();
		ArrayList<T> listaRest = new ArrayList<T>();
		try{
			Connection<T> listaConnection = RestMbType.listaAll(client,recurso,classe);
			for(List<T> lista : listaConnection){
				for(T model : lista){
					addTransaction(model);
					listaRest.add(model);
				}
			}
		}catch (Exception e) {
			return new ArrayList<T>();
		}
		return listaRest;
	}
	
	public enum TransationsContainerTipo{
		BD(0), REST(1);
		
		private int status;
		
		TransationsContainerTipo(int status){
			this.status = status;
		}
		
		public int getStatus(){
			return this.status;
		}
	}
	
}
