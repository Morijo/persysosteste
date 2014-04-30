package com.persys.osmanager.order.data;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IPrioridade;

import com.persys.osmanager.componente.CssColor;
import com.persys.osmanager.componente.SearchFormWindow.Parameter;
import com.persys.osmanager.data.TransactionsContainer;
import com.persys.osmanager.exception.ViewException;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Prioridade;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CssLayout;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 28/03/2013 Container de dados Para Prioridade (TransactionsContainerPrioridadeData)
 * <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/frontend/priority</p>
 */

public class TransactionsContainerPrioridadeData extends TransactionsContainer<IPrioridade> {
	
	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/priority",Locale.getDefault());
	}
    private static final long serialVersionUID = 1L;

    public TransactionsContainerPrioridadeData(){
    	addContainerProperty(bundle.getString("id"), Long.class, -1);
        addContainerProperty(bundle.getString("entity"), String.class, "");
        addContainerProperty(bundle.getString("code"), String.class, "");
        addContainerProperty(bundle.getString("color"), CssLayout.class, new CssLayout());
        addContainerProperty(bundle.getString("situation"), String.class, "");
     }

    @SuppressWarnings("unchecked")
	public void addTransaction(IPrioridade prioridade) {
        Object id = addItem();
        com.vaadin.data.Item item = getItem(id);
        if (item != null) {
        	item.getItemProperty(bundle.getString("id")).setValue(prioridade.getId());
            item.getItemProperty(bundle.getString("entity")).setValue(prioridade.getPrioridade());
            item.getItemProperty(bundle.getString("code")).setValue(prioridade.getCodigo());
            item.getItemProperty(bundle.getString("color")).setValue(CssColor.carregaCor(prioridade.getCor()));
            item.getItemProperty(bundle.getString("situation")).setValue(prioridade.getStatusModel()==1?"Ativo" :"Inativo");
            
        }
    }
   
   @Override
	public void loadTableRest(RestMBClient client) {
		loadRest(client, "/ordem/prioridade", Prioridade.class);	
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
	
	}

	@Override
	public BeanContainer<Boolean, Parameter> listParameter() {
		return null;
	}

	@Override
	public void loadTable(RestMBClient client,
			com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo modo) {}

	
	public static BeanItemContainer<Prioridade> listPrioridadeOrdem(RestMBClient client) throws ViewException{
		 
		 BeanItemContainer<Prioridade> listaBeans = new BeanItemContainer<Prioridade>(Prioridade.class);
		 try{
			 Connection<Prioridade> lista = Prioridade.listaAll(client,"/ordem/prioridade",Prioridade.class);
				
				for(List<Prioridade> listaPrioridadeOrdem : lista){
					for(Prioridade prioridadeOrdem : listaPrioridadeOrdem){
						listaBeans.addBean(prioridadeOrdem);
					}
				}
			}catch (Exception e) {
	    		throw new ViewException("Sem registro");
	    	}
	    	return listaBeans;
		}
	
}