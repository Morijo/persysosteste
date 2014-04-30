package com.persys.osmanager.data;

import java.util.List;

import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Nota;


public class TransactionsContainerNotaData extends com.vaadin.data.util.IndexedContainer {

    private static final long serialVersionUID = 1L;

    public TransactionsContainerNotaData(){
        addContainerProperty("Titulo", String.class, "");
        addContainerProperty("Nota", String.class, "");
     }

    @SuppressWarnings("unchecked")
	public void addTransaction(Nota nota) {
        Object id = addItem();
        com.vaadin.data.Item item = getItem(id);
        if (item != null) {
            item.getItemProperty("Titulo").setValue(nota.getTitulo());
            item.getItemProperty("Nota").setValue(nota.getNota());
        }
    }
    
    public void load(RestMBClient client, String resource){
    	
    	Connection<Nota> lista = Nota.listaAll(client,resource,Nota.class);
		
		for(List<Nota> listaNota : lista){
			for(Nota nota : listaNota){
				addTransaction(nota);
			}
		}
    }
}