package com.persys.osmanager.system;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.usuario.model.GrupoUsuario;
import br.com.usuario.model.GrupoUsuarioPermissao;

import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.system.data.TransactionsContainerGrupo;
import com.restmb.RestMBClient;
import com.vaadin.data.Item;


public class GrupoUsuarioView extends FormView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TransactionsContainerGrupo data 			= new TransactionsContainerGrupo();
	private GrupoUsuarioForm 		  grupoUsuarioForm= null;
	private RestMBClient restMBClient;
	
	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/grupousuario",Locale.getDefault());
	}

	public GrupoUsuarioView(RestMBClient client) {
		this.restMBClient = client;
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data), bundle.getString("entidade"));
	}

	@Override
	public void editar() {
		try{
			GrupoUsuario grupoUsuario = grupoUsuarioForm.commit();
			grupoUsuario.altera();
			
			if(!grupoUsuario.getAdministrado()){
				grupoUsuario = grupoUsuarioForm.commitPermissao();
				for(GrupoUsuarioPermissao grupoUsuarioPermissao : grupoUsuario.getGrupoUsuarioPermissao()){
					grupoUsuarioPermissao.alteraPermissao();
				}
			}
			//voltar();
			notificationTray(bundle.getString("alteracao"), "Sucesso");
		}catch(Exception e){
			notificationError(bundle.getString("alteracao"), "Erro " + e.getMessage() );
		}
	}

	@Override
	public void adicionar() {
		grupoUsuarioForm = new GrupoUsuarioForm(restMBClient);
		grupoUsuarioForm.initData(new GrupoUsuario());
		modoAdicionarView(grupoUsuarioForm,bundle.getString("novagrupo"));

	}

	@Override
	public void remover(Object target) {
		messageSucess(bundle.getString("remover"),bundle.getString("removerpergunta"),bundle.getString("remover"),"Cancelar",false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					grupoUsuarioForm.commit().remover();
					notificationTray(bundle.getString("remover"), "Sucesso");
					voltar();
				}catch(Exception e){
					notificationError(bundle.getString("remover"), "Erro " + e.getMessage() );
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
			GrupoUsuario grupoUsuario = grupoUsuarioForm.commit();
			grupoUsuario.salvar();
			if(!grupoUsuario.getAdministrado())
				grupoUsuarioForm.commitPermissao().alterar();
			voltar();
			notificationTray(bundle.getString("salvar"), "Sucesso");
		}catch (Exception e) {
			notificationTray(bundle.getString("errosalvar"), e.getMessage());
		}	
	}

	@Override
	public void visualizar(Object target) {
		GrupoUsuario grupoUsuario = data.toModel(((Item)target));
		grupoUsuarioForm = new GrupoUsuarioForm(restMBClient);
		grupoUsuarioForm.initData(grupoUsuario);
		modoVisualizarView(grupoUsuarioForm, grupoUsuario.getNome());
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
	}
}
