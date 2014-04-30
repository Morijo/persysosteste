package com.persys.osmanager.order;

import br.com.model.interfaces.IOrdem;

import com.persys.osmanager.customer.contract.ContractProdutoView;
import com.persys.osmanager.employee.EmployeeOrderView;
import com.persys.osmanager.resource.OrdemResourceView;
import com.persys.osmanager.service.OrdemServiceView;
import com.restmb.RestMBClient;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public class OrdemFormTabView extends TabSheet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrdemFormTabView(){
		setWidth("750px");
		setHeight("400px");
		addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component component = getSelectedTab();
				if(component instanceof OrdemProcedimentoView){
					((OrdemProcedimentoView) component).defaultTable();
				}	
			}
		});
	}
	
	public void createTab(RestMBClient client, IOrdem ordemServico){
		addTab(new OrdemNotaView(client,ordemServico),"Notas");
		addTab(new OrdemServiceView(client, ordemServico),"Serviços");
		addTab(new ContractProdutoView(client, ordemServico.getClienteObjeto()),"Produtos");
		addTab(new OrdemResourceView(client, ordemServico),"Recurso");
		addTab(new EmployeeOrderView(client, ordemServico),"Empregado Execução");
		addTab(new OrdemAnexoView(client, ordemServico),"Observações");
		addTab(new OrdemProcedimentoView(ordemServico),"Procedimentos");
	}
}
