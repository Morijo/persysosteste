package com.persys.osmanager.order.data;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.interfaces.IBaseConhecimento;

import com.persys.osmanager.componente.SearchFormWindow.Parameter;
import com.persys.osmanager.data.TransactionsContainer;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.BaseConhecimento;
import com.restmb.types.RestMbType;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;

/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 27/03/2013 Container de dados Para Base de conhecimento (TransactionsContainerBaseConhecimentoData)
 * <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/frontend/knowledgebase</p>
 */

public class TransactionsContainerBaseConhecimentoData extends TransactionsContainer<IBaseConhecimento> {
	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/knowledgebase",Locale.getDefault());
	}
    private static final long serialVersionUID = 1L;

    public TransactionsContainerBaseConhecimentoData(){
    	addContainerProperty(bundle.getString("id"), Long.class, -1);
        addContainerProperty(bundle.getString("title"), String.class, "");
        addContainerProperty(bundle.getString("code"), String.class, "");
        addContainerProperty(bundle.getString("situation"), String.class, "");
     }

    @SuppressWarnings("unchecked")
	public void addTransaction(IBaseConhecimento prioridade) {
        Object id = addItem();
        com.vaadin.data.Item item = getItem(id);
        if (item != null) {
        	item.getItemProperty(bundle.getString("id")).setValue(prioridade.getId());
            item.getItemProperty(bundle.getString("title")).setValue(prioridade.getTitulo());
            item.getItemProperty(bundle.getString("code")).setValue(prioridade.getCodigo());
            item.getItemProperty(bundle.getString("situation")).setValue(prioridade.getStatusModel()==1?"Ativo":"Inativo");
        }
    }
   
	@Override
	public void loadTable(RestMBClient client,
			com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo modo) {
	}

	@Override
	public void loadTableRest(RestMBClient client) {
		loadRest(client, "/ordem/baseconhecimento", BaseConhecimento.class);
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {}


	@Override
	public BeanContainer<Boolean, Parameter> listParameter() {
		return null;
	}
	
	public static BeanItemContainer<BaseConhecimento> list(RestMBClient client){

		BeanItemContainer<BaseConhecimento> listaBeans = new BeanItemContainer<BaseConhecimento>(BaseConhecimento.class);
		try{
			Connection<BaseConhecimento> listaCon = RestMbType.listaAll(client, "/ordem/baseconhecimento", BaseConhecimento.class);
			for(List<BaseConhecimento> lista : listaCon){
				for(BaseConhecimento baseConhecimento : lista){
					listaBeans.addBean(baseConhecimento);
				}
			}
		}catch (Exception e) {}
		return listaBeans;
	}
}