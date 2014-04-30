package com.persys.osmanager.system.data;

import java.util.ArrayList;

import br.com.usuario.model.GrupoUsuario;
import br.com.usuario.model.GrupoUsuarioPermissao;

import com.persys.osmanager.componente.interfaces.ITransactionsContainerData;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.restmb.RestMBClient;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;

public class TransactionsContainerGrupo extends com.vaadin.data.util.IndexedContainer
implements ITransactionsContainerData{

	private static final long serialVersionUID = 1L;

	public TransactionsContainerGrupo() {
		addContainerProperty("Id", Long.class, -1);
		addContainerProperty("Codigo", String.class, "");
		addContainerProperty("Grupo", String.class, "");
		addContainerProperty("Admin", Boolean.class, null);
	}

	public void addTransaction(br.com.model.interfaces.IGrupoUsuario grupoUsuario) {
		Object itemId = addItem();
		com.vaadin.data.Item item = getItem(itemId);
		setItemProperty(grupoUsuario,item);
	}

	@SuppressWarnings("unchecked")
	public void setItemProperty(br.com.model.interfaces.IGrupoUsuario grupoUsuario, com.vaadin.data.Item item) {
		if (item != null) {
			item.getItemProperty("Id").setValue(grupoUsuario.getId());
			item.getItemProperty("Grupo").setValue(grupoUsuario.getNome());
			item.getItemProperty("Codigo").setValue(grupoUsuario.getCodigo());
			item.getItemProperty("Admin").setValue(grupoUsuario.getAdministrado());
		}
	}

	@Override
	public void loadTableRest(RestMBClient client) {}

	public static ArrayList<GrupoUsuario> listaGrupo(String consumerKey){
		GrupoUsuario grupoUsuario = new GrupoUsuario();
		return grupoUsuario.listaPorConstrutor(GrupoUsuario.CONSTRUTOR,consumerKey);
	}

	public static BeanItemContainer<GrupoUsuario> beanItemContainerGrupo(RestMBClient client){

		BeanItemContainer<GrupoUsuario> listaBeans = new BeanItemContainer<GrupoUsuario>(GrupoUsuario.class);
		try{
			for(GrupoUsuario grupo : listaGrupo(client.getOauth().getApiKey())){
				listaBeans.addBean(grupo);
			}
		}catch (Exception e) {}
		return listaBeans;
	}

	public GrupoUsuario toModel(Item item){
		GrupoUsuario grupoUsuario = new GrupoUsuario();
		grupoUsuario.setId((Long) item.getItemProperty("Id").getValue());
		grupoUsuario.setNome(item.getItemProperty("Grupo").getValue().toString());
		grupoUsuario.setCodigo(item.getItemProperty("Codigo").getValue().toString());
		grupoUsuario.setAdministrado((Boolean) item.getItemProperty("Admin").getValue());;
		return grupoUsuario;
	}

	public static ArrayList<GrupoUsuarioPermissao> listaPermissao(Long idGroup){
		try{
			return GrupoUsuarioPermissao.listaPermissaoPorGrupo(idGroup);
		}catch(Exception e){
			return new ArrayList<GrupoUsuarioPermissao>();
		}
	}

	@Override
	public void loadTable(RestMBClient client, TransationsContainerTipo modo) {
		if(modo == TransationsContainerTipo.BD){
			loadTableDataBase(client);
		}
	}

	@Override
	public void loadTableDataBase(RestMBClient client) {
		removeAllItems();
		for(GrupoUsuario grupo : listaGrupo(client.getOauth().getApiKey()))
			addTransaction(grupo);
	}



}
