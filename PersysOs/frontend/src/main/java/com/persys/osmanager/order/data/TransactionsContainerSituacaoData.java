package com.persys.osmanager.order.data;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.ISituacaoOrdem;

import com.persys.osmanager.componente.SearchFormWindow.Parameter;
import com.persys.osmanager.data.TransactionsContainer;
import com.persys.osmanager.exception.ViewException;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.RestMbType;
import com.restmb.types.SituacaoOrdem;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 27/03/2013 Container de dados Para Situacao (TransactionsContainerSituacaoData)
 * <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/frontend/situation</p>
 */

public class TransactionsContainerSituacaoData extends TransactionsContainer<ISituacaoOrdem> {
	
	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/situation",Locale.getDefault());
	}
    private static final long serialVersionUID = 1L;

    public TransactionsContainerSituacaoData(){
    	addContainerProperty(bundle.getString("id"), Long.class, -1);
        addContainerProperty(bundle.getString("entity"), String.class, "");
        addContainerProperty(bundle.getString("code"), String.class, "");
        addContainerProperty(bundle.getString("situation"), String.class, "");
     }

    @SuppressWarnings("unchecked")
	public void addTransaction(ISituacaoOrdem situacaoOrdem) {
        Object id = addItem();
        com.vaadin.data.Item item = getItem(id);
        if (item != null) {
        	item.getItemProperty(bundle.getString("id")).setValue(situacaoOrdem.getId());
            item.getItemProperty(bundle.getString("entity")).setValue(situacaoOrdem.getNome());
            item.getItemProperty(bundle.getString("code")).setValue(situacaoOrdem.getCodigo());
            item.getItemProperty(bundle.getString("situation")).setValue(situacaoOrdem.getStatusModel()==1?"Ativo" :"Inativo");
        }
        
    }
    
    public HashMap<Long,ISituacaoOrdem> load(RestMBClient client) {
    	HashMap<Long,ISituacaoOrdem> situacaoOrdems = new HashMap<Long,ISituacaoOrdem>();
    	
		try{
			Connection<ISituacaoOrdem> listaConnection = RestMbType.listaAll(client,"/ordem/situacao",SituacaoOrdem.class);
			for(List<ISituacaoOrdem> lista : listaConnection){
				for(ISituacaoOrdem model : lista){
					situacaoOrdems.put(model.getId(), model);
				}
			}
		}catch (Exception e) {
			return situacaoOrdems;
		}
		return situacaoOrdems;
	}

	@Override
	public void loadTable(RestMBClient client,
			com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo modo) {
		if(TransationsContainerTipo.REST == modo){
			loadTableRest(client);
		}
		
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		removeAllItems();
		try{
			Connection<ISituacaoOrdem> listaConnection = RestMbType.listaAll(client,"/ordem/situacao",SituacaoOrdem.class);
			for(List<ISituacaoOrdem> lista : listaConnection){
				for(ISituacaoOrdem model : lista){
					addTransaction(model);
				}
			}
		}catch (Exception e) {}
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		
	}

	@Override
	public BeanContainer<Boolean, Parameter> listParameter() {
		return null;
	}
	
	public static BeanItemContainer<SituacaoOrdem> listSituacaoOrdem(RestMBClient client) throws ViewException{
		 
		 BeanItemContainer<SituacaoOrdem> listaBeans = new BeanItemContainer<SituacaoOrdem>(SituacaoOrdem.class);
		 try{
			 Connection<SituacaoOrdem> lista = SituacaoOrdem.listaAll(client,"/ordem/situacao",SituacaoOrdem.class);
				
				for(List<SituacaoOrdem> listaSituacaoOrdem : lista){
					for(SituacaoOrdem situacaoOrdem : listaSituacaoOrdem){
						listaBeans.addBean(situacaoOrdem);
					}
				}
			}catch (Exception e) {
				throw new ViewException("Sem registro");
	    	}
	    	return listaBeans;
		}
}