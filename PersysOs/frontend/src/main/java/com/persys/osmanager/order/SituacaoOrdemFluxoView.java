package com.persys.osmanager.order;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.model.interfaces.ISituacaoOrdem;

import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.order.data.TransactionsContainerSituacaoData;
import com.restmb.RestMBClient;
import com.vaadin.data.Item;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TreeTable;

public class SituacaoOrdemFluxoView extends TreeTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ISituacaoOrdem situacaoOrdem = null;
	private TransactionsContainerSituacaoData data = null;
	private RestMBClient restMBClient;
	public HashMap<Long, ISituacaoOrdem> lisHashMap = new HashMap<Long, ISituacaoOrdem>();


	public SituacaoOrdemFluxoView(RestMBClient restMBClient,TransactionsContainerSituacaoData data){
		this.restMBClient = restMBClient;
		this.data = data;
		setCaption("Fluxo");
	}

	public void createTable(){
		addContainerProperty("Situacao", CheckBox.class, "");
		addContainerProperty("Identificador do Recurso", Long.class, -1l);
		setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		setHeight("400px");
		carregaPermissao();
	}

	public void setChech(){

		ArrayList<ISituacaoOrdem> fluxoList = new ArrayList<ISituacaoOrdem>();

		int i=0;
		try{
			for(Object itemId : getItemIds()){
				Item item = getItem(itemId);
				if(i>1){
					CheckBox checkBox = (CheckBox) item.getItemProperty("Situacao").getValue();
					checkBox.setValue(fluxoList.get(i-1).getStatusModel() == 1 ? true : false);
				}
				i++;
			}
		}catch (Exception e) {
			Notification.show("Não foi possível carregar a lista de permissão para este grupo");
		}
	}

	private void carregaPermissao() {

		HashMap<Long,ISituacaoOrdem> hashMap = data.load(restMBClient);
		hashMap.remove(situacaoOrdem.getId());
		
		int i =0;
		for(ISituacaoOrdem situacao : hashMap.values()){
			addItem(new Object[]{new CheckBox(situacao.getNome()), situacao.getId()}, i);
			i++;
		}
		
		setPageLength(size());
		for (Object itemId: getItemIds())
			setCollapsed(itemId, false);
	}

	public ISituacaoOrdem getSituacaoOrdem() {
		return situacaoOrdem;
	}

	public void setSituacaoOrdem(ISituacaoOrdem situacaoOrdem) {
		this.situacaoOrdem = situacaoOrdem;
	}
}