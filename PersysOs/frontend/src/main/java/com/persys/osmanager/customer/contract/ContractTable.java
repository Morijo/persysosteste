package com.persys.osmanager.customer.contract;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class ContractTable {

	public static Table createTabela(TextField filter, final IndexedContainer data){
		Table tableContract = new Table() {
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
		tableContract.setSizeFull();
		tableContract.addStyleName("borderless");
		tableContract.setSelectable(true);
		tableContract.setColumnCollapsingAllowed(true);
		tableContract.setColumnReorderingAllowed(true);
		data.removeAllContainerFilters();
		tableContract.setContainerDataSource(data);

		tableContract.setVisibleColumns(new Object[] { "Id","Cliente","Situacao","Data Assinatura","Codigo",});

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

						 return filterByProperty("Cliente", item,
								 event.getText())
								 || filterByProperty("Situacao", item,
										 event.getText());
					 }

					 @Override
					 public boolean appliesToProperty(Object propertyId) {
						 if (propertyId.equals("Cliente")
								 || propertyId.equals("Situacao"))
							 return true;
						 return false;
					 }

				});
			}
		});
		
		tableContract.setImmediate(true);
		return tableContract;
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
