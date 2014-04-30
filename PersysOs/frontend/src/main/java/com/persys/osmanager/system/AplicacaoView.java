package com.persys.osmanager.system;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.usuario.model.AplicacaoAgente;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.system.data.TransactionsContainerAplicacao;
import com.restmb.RestMBClient;
import com.vaadin.data.Item;

public class AplicacaoView extends FormView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TransactionsContainerAplicacao data 		    = new TransactionsContainerAplicacao();
	private AplicacaoForm 		           aplicacaoForm	= null;
	private RestMBClient 				   restMBClient;
	
	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/aplicacao",Locale.getDefault());
	}

	public AplicacaoView(RestMBClient client) {
		this.restMBClient = client;
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data), bundle.getString("entidade"));
		buttonEditar.setVisible(false);
	}

	@Override
	public void editar() {}

	@Override
	public void adicionar() {
		aplicacaoForm = new AplicacaoForm();
		aplicacaoForm.initData(new AplicacaoAgente());
		modoAdicionarView(aplicacaoForm,bundle.getString("nova"));
	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remover"),bundle.getString("removerpergunta"),bundle.getString("remover"),"Cancelar",false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					aplicacaoForm.commit().remover();
					notificationTray(bundle.getString("remover"), "Sucesso");
					voltar();
				}catch(Exception e){
					notificationError(bundle.getString("remover"), "Erro " + e.getMessage() );
				}
			}
			@Override
			public void discard() {}

			@Override
			public void cancel() {}
		});

	}

	@Override
	public void salvar() {
		try{
			AplicacaoAgente aplicacaoAgente = aplicacaoForm.commit();
			aplicacaoAgente.setConsumerId(Long.parseLong(restMBClient.getOauth().getApiKey()));
			aplicacaoAgente.salva();
			voltar();
			notificationTray(bundle.getString("salvar"), "Sucesso");
		}catch (Exception e) {
			notificationTray(bundle.getString("errosalvar"), e.getMessage());
		}	
	}

	@Override
	public void visualizar(Object target) {
		AplicacaoAgente aplicacaoAgente = data.toModel(((Item)target));
		aplicacaoForm = new AplicacaoForm();
		aplicacaoForm.initData(AplicacaoAgente.pesquisaAgente(aplicacaoAgente.getId(), restMBClient.getOauth().getApiKey()));
		modoVisualizarView(aplicacaoForm, aplicacaoAgente.getNomeUsuario());
		buttonDeletar.setVisible(false);
	}

	@Override
	public void defaultTable() {
		try{
			data.loadTable(restMBClient,TransationsContainerTipo.BD);
		}catch (Exception e) {}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable,bundle.getString("entidade"));
		defaultTable();
		buttonEditar.setVisible(false);
	}
}
