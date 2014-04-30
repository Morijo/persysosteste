/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.persys.osmanager.organization;
import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.dashboard.DashboardUI;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.organization.data.TransactionsContainerDepartamento;
import com.restmb.RestMBClient;
import com.restmb.types.Departamento;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

public class DepartamentoView extends FormView implements View{

    private static final long serialVersionUID = 1L;
    private Table tableDepartamento = null;
    
    private TransactionsContainerDepartamento data = new TransactionsContainerDepartamento();
    private DepartamentoForm departamentoForm;
    private RestMBClient client;
    
    public DepartamentoView() {
        super("");
   }

    public void modoTabela(){
	    tableDepartamento = new Table() {
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
	    tableDepartamento.setSizeFull();
	    tableDepartamento.addStyleName("borderless");
	    tableDepartamento.setSelectable(true);
	    tableDepartamento.setColumnCollapsingAllowed(true);
	    tableDepartamento.setColumnReorderingAllowed(true);
	    data.removeAllContainerFilters();
	    tableDepartamento.setContainerDataSource(data);
	   
	    tableDepartamento.setVisibleColumns(new Object[] { "Codigo","Nome","Tipo","Unidade","Telefone"});
	
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
	 
	  tableDepartamento.addValueChangeListener(new ValueChangeListener() {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
	
			@Override
	        public void valueChange(ValueChangeEvent event) {
	                visualizar(tableDepartamento.getItem(tableDepartamento.getValue()));
	            } 
	    });
	    
	    tableDepartamento.setImmediate(true);
	    defaultTable();
		modoTabelaView(tableDepartamento,"Departamento");
	}
	
	private boolean filterByProperty(String prop, Item item, String text) {
	    if (item == null || item.getItemProperty(prop) == null
	            || item.getItemProperty(prop).getValue() == null)
	        return false;
	    String val = item.getItemProperty(prop).getValue().toString().trim()
	            .toLowerCase();
	    if (val.startsWith(text.toLowerCase().trim()))
	        return true;
	    return false;
	}
    
  @Override
	public void editar() {
	  try{
			Departamento departamento = (Departamento) departamentoForm.commit();
			departamento.alterar(client);
			notificationTray("Alteração", "Sucesso");
		}catch(Exception e){
			notificationError("Alteração", "Erro " + e.getMessage() );
		}
	}

	@Override
	public void adicionar() {
		departamentoForm = new DepartamentoForm(client);
		departamentoForm.initData(new Departamento());
		modoAdicionarView(departamentoForm,"Novo Departamento");
	}

	@Override
	public void remover(Object target) {
		 messageSucess("Remover","Deseja remover este departamento?","Remover","Cancelar",false,true,true,new IMessage() {
				
				@Override
				public void ok() {
					try{
						Departamento departamento = (Departamento) departamentoForm.commit();
						departamento.remover(client);
						notificationTray("Remover", "Sucesso");
						voltar();
					}catch(Exception e){
						notificationError("Remover", "Erro " + e.getMessage() );
					}
				}
				@Override
				public void discard() {
					
				}
				
				@Override
				public void cancel() {
					
				}
			});
	}

	@Override
	public void salvar() {
		try{
			Departamento departamento= (Departamento) departamentoForm.commit();
			if(departamento != null)
				departamento.salvar(client);
				notificationTray("Salvar", "Sucesso");
		}catch(Exception e){
			notificationError("Salvar", "Erro " + e.getLocalizedMessage() );
		}
	}

	@Override
	public void visualizar(Object departamentoItem) {
		Long id = (Long) ((Item)departamentoItem).getItemProperty("Id").getValue();
		Departamento departamento = new Departamento();
		try{
			departamento = departamento.pesquisa(client,id);
			departamentoForm = null;
			departamentoForm = new DepartamentoForm(client);
			departamentoForm.initData(departamento);
			modoVisualizarView(departamentoForm,departamento.getId() +" - "+ departamento.getNomeDepartamento());
		}catch(Exception e){
			
		}
	}

	@Override
	public void voltar() {
		modoTabelaView(tableDepartamento,"Departamento");
		defaultTable();
	} 
	
	 @Override
	    public void defaultTable(){
	    	try {
				data.loadTableRest(client);
			} catch (ViewException e) {
				Notification.show(e.getMessage());
			}
	 }

	@Override
	public void enter(ViewChangeEvent event) {
		 this.client = ((DashboardUI)getUI()).getClient();
		 setSizeFull();
	   	 modoTabela();
	}
}
