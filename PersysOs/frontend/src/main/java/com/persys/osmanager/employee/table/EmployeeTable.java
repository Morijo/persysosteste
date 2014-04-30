package com.persys.osmanager.employee.table;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class EmployeeTable {
	
	public static Table createTable(TextField filter, final IndexedContainer data){
	 
		Table tableFuncionario = new Table() {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	        protected String formatPropertyValue(Object rowId, Object colId,
	                Property<?> property) {
	          
	            return super.formatPropertyValue(rowId, colId, property);
	        }
	    };
	    tableFuncionario.setSizeFull();
	    tableFuncionario.addStyleName("borderless");
	    tableFuncionario.setSelectable(true);
	    tableFuncionario.setColumnCollapsingAllowed(true);
	    tableFuncionario.setColumnReorderingAllowed(true);
	    data.removeAllContainerFilters();
	    tableFuncionario.setContainerDataSource(data);
	    tableFuncionario.setVisibleColumns(new Object[] {"Codigo","Perfil","Nome", "Situação"});
	    tableFuncionario.setColumnExpandRatio("Nome", 1.0f);
	    filter.addTextChangeListener(new TextChangeListener() {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	        public void textChange(final TextChangeEvent event) {
	            data.removeAllContainerFilters();
	            data.addContainerFilter(new Filter() {
	                /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
	                public boolean passesFilter(Object itemId, Item item)
	                        throws UnsupportedOperationException {
	
	                    if (event.getText() == null
	                            || event.getText().equals("")) {
	                        return true;
	                    }
	
	                    return filterByProperty("Nome", item,
	                                    event.getText())
	                            || filterByProperty("Codigo", item,
	                                    event.getText());
	                }
	
	                @Override
	                public boolean appliesToProperty(Object propertyId) {
	                    if (propertyId.equals("Nome")
	                            || propertyId.equals("Codigo"))
	                        return true;
	                    return false;
	                }
	
	            });
	        }
	    });
	 
	    tableFuncionario.setImmediate(true);
	    
	    return tableFuncionario;
	}

	private static boolean filterByProperty(String prop, Item item, String text) {
	    if (item == null || item.getItemProperty(prop) == null
	            || item.getItemProperty(prop).getValue() == null)
	        return false;
	    String val = item.getItemProperty(prop).getValue().toString().trim()
	            .toLowerCase();
	    if (val.startsWith(text.toLowerCase().trim()))
	        return true;
	    return false;
	}
}
