package com.persys.osmanager.order.data;

import java.util.List;

import br.com.model.interfaces.IAnexo;

import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Anexo;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;

 /**
 *@author ricardosabatine
 *@version 1.0
 *@since 1.0
 *
 */

public class TransactionsContainerAnexoData extends com.vaadin.data.util.IndexedContainer {

    private static final long serialVersionUID = 1L;

    public TransactionsContainerAnexoData() {
    	addContainerProperty("Id", Long.class, -1);
        addContainerProperty("Descricao", String.class, "");
        addContainerProperty("Imagem", Image.class, new Image());
     }

    @SuppressWarnings("unchecked")
	public void addTransaction(IAnexo anexo) {
        Object id = addItem();
        com.vaadin.data.Item item = getItem(id);
        if (item != null) {
        	item.getItemProperty("Id").setValue(anexo.getId());
            item.getItemProperty("Descricao").setValue(anexo.getDescricao());
           
            Image profilePic = new Image();
            try{
            	profilePic.setSource(new ExternalResource(anexo.getCaminho()));
            }catch(Exception e){
            	profilePic.setSource(new ThemeResource("../reindeer/Icons/attached.png"));
            }
            profilePic.setWidth("52px");
            
            item.getItemProperty("Imagem").setValue(profilePic);
        }
    }
    
    public void load(RestMBClient client, String resource){
    	Connection<Anexo> lista = Anexo.listaAll(client,resource,Anexo.class);
		for(List<Anexo> listaAnexo : lista){
			for(Anexo anexo : listaAnexo){
				addTransaction(anexo);
			}
		}
    }
  
    
}
