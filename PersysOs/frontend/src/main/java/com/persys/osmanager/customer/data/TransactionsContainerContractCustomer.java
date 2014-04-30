package com.persys.osmanager.customer.data;

import java.util.Date;
import java.util.List;

import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Contrato;
import com.vaadin.data.util.BeanItemContainer;

/**
 * @author ricardosabatine
 *
 */

public class TransactionsContainerContractCustomer extends com.vaadin.data.util.IndexedContainer {

    private static final long serialVersionUID = 1L;

    public TransactionsContainerContractCustomer() {
    	addContainerProperty("Id", Long.class, -1);
        addContainerProperty("Cliente", String.class, "");
        addContainerProperty("Situacao", String.class, "");
        addContainerProperty("Data Assinatura", Date.class, new Date());
        addContainerProperty("Codigo", String.class, "");
     }

    @SuppressWarnings("unchecked")
	public void addTransaction(Contrato contrato) {
        Object id = addItem();
        com.vaadin.data.Item item = getItem(id);
        if (item != null) {
        	item.getItemProperty("Id").setValue(contrato.getId());
            item.getItemProperty("Cliente").setValue(contrato.getCliente().getRazaoNome());
            item.getItemProperty("Situacao").setValue(contrato.getSituacao());
            item.getItemProperty("Data Assinatura").setValue(contrato.getDataAssinatura());
            item.getItemProperty("Codigo").setValue(contrato.getCodigo());
        }
    }
    
    public void load(RestMBClient client, String resource){
   	Connection<Contrato> lista = Contrato.listaAll(client,resource,Contrato.class);
		
		for(List<Contrato> listaContrato : lista){
			for(Contrato contrato : listaContrato){
				addTransaction(contrato);
			}
		}
    }
    
    /**
     * Lista em um Bean os contratos de um cliente
     * @param client
     * @param resource
     * @return BeanItemContainer<Contrato>
     */
    public static BeanItemContainer<Contrato> loadBean(RestMBClient client, Long id){
       	BeanItemContainer<Contrato> listaBeans = new BeanItemContainer<Contrato>(Contrato.class);
      	Connection<Contrato> lista = Contrato.listaAll(client,"/cliente/"+id+"/contrato",Contrato.class);
    		
    	for(List<Contrato> listaContrato : lista){
    		for(Contrato contrato : listaContrato){
    			listaBeans.addBean(contrato);
    		}
    	}
    	return listaBeans;
    }
    
    /**
	  * Lista os tipos de contrato
	  * Normal
	  * Especial
	  * Carência
	  * Concelado
	  * Suspenso
	  * Vencido
	 * @return BeanItemContainer<String>
	 */
	 public static BeanItemContainer<String> listSituacaoContrato(){
		 BeanItemContainer<String> beans =
			        new BeanItemContainer<String>(String.class);
			    
		beans.addBean("Normal"); 
		beans.addBean("Especial"); 
		beans.addBean("Carência"); 
		beans.addBean("Cancelado"); 
		beans.addBean("Suspenso"); 
		beans.addBean("Vencido"); 
			
		return beans;
	}
}
