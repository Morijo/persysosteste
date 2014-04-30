package com.persys.osmanager.order;

import java.util.Locale;
import java.util.ResourceBundle;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.order.data.TransactionsContainerBaseConhecimentoData;
import com.restmb.RestMBClient;
import com.restmb.types.BaseConhecimento;
import com.vaadin.data.Item;

/**
* @author ricardosabatine, jpmorijo
* @version 1.0.0
* @since 24/03/2013 View para Base de Conhecimento em Ordem
* <p>Dados Carregados a partir do RestMb</p>
* <p>Traduzido Ingles, Portugues, Pacote com traducoes com/persys/frontend/notification</p>
*/

public class BaseConhecimentoView extends FormView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TransactionsContainerBaseConhecimentoData data = new TransactionsContainerBaseConhecimentoData();
	private RestMBClient 		  client 		= null;
	private BaseConhecimentoForm  baseConhecimentoForm = null;
	private final static ResourceBundle bundle;
	private Item item = null;
	
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",Locale.getDefault());
	}

	public BaseConhecimentoView(RestMBClient client) {

		this.client = client;

		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),bundle.getString("knowledgebase"),false);
		filterTable.setVisibleColumns(new Object[] { bundle.getString("code"), bundle.getString("title"), bundle.getString("situation")});
		filterTable.setColumnExpandRatio(bundle.getString("title"), 0.7f);
		
	}
	
	@Override
	public void editar() {
		try{
			BaseConhecimento baseConhecimento = (BaseConhecimento) baseConhecimentoForm.commit();
			baseConhecimento.alterar(client);
	
			notificationTray(bundle.getString("alteration"),bundle.getString("success"));
			voltar();
		} catch (Exception e) {
			notificationError(bundle.getString("alteration"),
					bundle.getString("error") + e.getMessage());
		}
	}
		
	@Override
	public void adicionar() {
		baseConhecimentoForm  = new BaseConhecimentoForm();
		baseConhecimentoForm .initData(new BaseConhecimento());
		modoAdicionarView(baseConhecimentoForm ,bundle.getString("newbase"));

	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),bundle.getString("wanttoremovethisbase"),bundle.getString("remove"),bundle.getString("cancel"),false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					BaseConhecimento baseConhecimento = (BaseConhecimento) baseConhecimentoForm.commit();
					baseConhecimento.remover(client);
					notificationTray(bundle.getString("remove"),bundle.getString("success"));
					defaultTable();
					voltar();
				}catch(Exception e){
					notificationError(bundle.getString("remove"),bundle.getString("error") + e.getMessage());				
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
			BaseConhecimento baseConhecimento = (BaseConhecimento) baseConhecimentoForm .commit();
			baseConhecimento.salvar(client);
			notificationTray(bundle.getString("save"),bundle.getString("success"));
			voltar();
		}catch (Exception e) {
			notificationTray(bundle.getString("errorsave"), e.getMessage());
		}	
	}

	@Override
	public void visualizar(Object target) {
		item = ((Item)target);
		Long id = (Long) item.getItemProperty(bundle.getString("id")).getValue();
		BaseConhecimento baseConhecimento = new BaseConhecimento();
		baseConhecimento = baseConhecimento.pesquisa(client,id);
		baseConhecimentoForm  = new BaseConhecimentoForm();
		baseConhecimentoForm .initData(baseConhecimento);
		modoVisualizarView(baseConhecimentoForm , baseConhecimento.getTitulo());
	}

	@Override
	public void defaultTable() {
		data.loadTableRest(client);
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable,bundle.getString("knowledgebase"),false);
		defaultTable();
	}

}
