package com.persys.osmanager.componente;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.restmb.types.Status;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;

public class ContainerUtils {
	 
	 public static String CAPTION_PROPERTY_NAME = "caption";
	 public static Map<Integer, String> lista =new HashMap<Integer, String>();
		
	 @SuppressWarnings("unchecked")
	 public static Container createContainerFromMap(Map<?, String> hashMap) {
	      IndexedContainer container = new IndexedContainer();
	      container.addContainerProperty(CAPTION_PROPERTY_NAME, String.class, "");
	       
	      Iterator<?> iter = hashMap.keySet().iterator();
	      while(iter.hasNext()) {
	         Object itemId = iter.next();
	         container.addItem(itemId);
	         container.getItem(itemId).getItemProperty(CAPTION_PROPERTY_NAME).setValue(hashMap.get(itemId));
	     }
	     
	     return container;
	}
	 
	 public static Container listaStatus(){
		lista.put(1, "Ativo");
		lista.put(2, "Inativo");
		return createContainerFromMap(lista);
		}
	
	public static BeanItemContainer<Status> listaStatusBean(){
		BeanItemContainer<Status> beans = new BeanItemContainer<Status>(Status.class);
		beans.addBean(new Status(1, "Ativo"));
		beans.addBean(new Status(2, "Inativo"));
		return beans;
	}
}
