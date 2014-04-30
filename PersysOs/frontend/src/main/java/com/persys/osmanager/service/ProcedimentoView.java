package com.persys.osmanager.service;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.model.Model;
import br.com.servico.model.Procedimento;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.service.data.TransactionsContainerProcedimento;
import com.restmb.RestMBClient;
import com.vaadin.data.Item;
/**
 * @author ricardosabatine, jpmorijo
 * @version 1.0.0
 * @since 18/02/2013 View Procedimento
 */

public class ProcedimentoView extends FormView{

	private static final long     serialVersionUID = 1L;
	private RestMBClient 		  client = null;
	private Item				  item;
	private ProcedimentoForm	  procedimentoForm;
	
	private TransactionsContainerProcedimento data = new TransactionsContainerProcedimento();
	
	private final static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com/persys/frontend/notification",
				Locale.getDefault());
	}	
	public ProcedimentoView(RestMBClient client) {
		this.client = client;
		setSizeFull();
		modoTabela();
	}


	public void modoTabela(){
		 modoTabelaView(buildPagedFilterTable(data),bundle.getString("procedure"),false);
		 filterTable.setVisibleColumns(bundle.getString("code"),bundle.getString("procedure"),bundle.getString("situation"));
		 filterTable.setColumnExpandRatio(bundle.getString("procedure"),0.7f);
	}

	@Override
	public void editar() {
		try{
			Procedimento procedimento = (br.com.servico.model.Procedimento) procedimentoForm.commit();
			procedimento.alterar();
			notificationTray(bundle.getString("alteration"), bundle.getString("success"));
			data.setItemProperty(procedimento, item);
			defaultTable();
			voltar();
		}catch(Exception e){
			notificationError(bundle.getString("alteration"), bundle.getString("error") + e.getMessage() );
		}
	}

	@Override
	public void adicionar() {
		procedimentoForm = new ProcedimentoForm();
		procedimentoForm.initData(new Procedimento());
		modoAdicionarView(procedimentoForm,bundle.getString("newprocedure"));
	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remove"),bundle.getString("wanttoremovethisprocedure"),bundle.getString("remove"),bundle.getString("cancel"),false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					Procedimento Procedimento = (br.com.servico.model.Procedimento) procedimentoForm.commit();
					Procedimento.removerLogico();
					notificationTray(bundle.getString("remove"),bundle.getString("success"));
					defaultTable();
					voltar();
				}catch(Exception e){
					notificationError(bundle.getString("remove"),bundle.getString("error") + e.getMessage() );
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
			Procedimento procedimento = (Procedimento) procedimentoForm.commit();
			procedimento.setConsumerId(Long.parseLong(client.getOauth().getApiKey()));
			procedimento.salvar();
			notificationTray(bundle.getString("save"),bundle.getString("success"));
			data.addTransaction(procedimento);
			defaultTable();
			voltar();
		} catch (ViewException e) {
			notificationTray(bundle.getString("errorsave"), e.getMessage());
		}catch (Exception e) {
			notificationTray(bundle.getString("errorsave"), e.getMessage());
		}	
	}

	@Override
	public void visualizar(Object target) {
		try{
			item = ((Item)target);
			Long id = (Long) item.getItemProperty(bundle.getString("id")).getValue();
			Procedimento procedimento = new Procedimento();
			procedimento = (Procedimento) Model.pesquisaPorId(Procedimento.class, id);
			procedimentoForm = new ProcedimentoForm();
			procedimentoForm.initData(procedimento);
			modoVisualizarView(procedimentoForm, procedimento.getTitulo());
		}catch (Exception e) {}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable,bundle.getString("procedure"),false);
	}

	@Override
	public void defaultTable() {
		data.loadTableDataBase(client);
	}
}
