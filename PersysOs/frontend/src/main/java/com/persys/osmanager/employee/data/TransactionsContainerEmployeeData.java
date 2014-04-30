package com.persys.osmanager.employee.data;

import java.net.URL;
import java.util.List;

import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Funcionario;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;


/**
 *@author ricardosabatine
 *@version 1.0
 *@since 1.0
 *
 */

public class TransactionsContainerEmployeeData extends com.vaadin.data.util.IndexedContainer {

	private static final long serialVersionUID = 1L;

	public TransactionsContainerEmployeeData() {
		addContainerProperty("Id", Long.class, -1);
		addContainerProperty("Nome", String.class, "");
		addContainerProperty("Codigo", String.class, "");
		addContainerProperty("Perfil", Image.class, "");
		addContainerProperty("Situação", String.class, "");
		addContainerProperty("CPF/CNPJ", String.class, "");
		
	}

	@SuppressWarnings("unchecked")
	public void addTransaction(Funcionario funcionario) {
		Object id = addItem();
		com.vaadin.data.Item item = getItem(id);
		if (item != null) {
			item.getItemProperty("Id").setValue(funcionario.getId());
			item.getItemProperty("Nome").setValue(funcionario.getRazaoNome());
			item.getItemProperty("Codigo").setValue(funcionario.getCodigo());
			item.getItemProperty("CPF/CNPJ").setValue(funcionario.getCnpjCpf());
			item.getItemProperty("Situação").setValue(funcionario.getStatusModel() == 1 ? "Ativo":"Inativo");

			Image profilePic = new Image();
			URL url= null;
			try {
				url = new URL(funcionario.getImagem());
				url.openStream();
				profilePic.setSource(new ExternalResource(url));
			
			}catch(Exception e){
				profilePic.setSource(new ThemeResource("img/profile-pic.png"));
			}
			profilePic.setWidth("52px");

			item.getItemProperty("Perfil").setValue(profilePic);
		}
	}

	public void load(RestMBClient client, String resource){
		Connection<Funcionario> lista = Funcionario.listaAll(client,resource,Funcionario.class);
		for(List<Funcionario> listaFuncionario : lista){
			for(Funcionario funcionario : listaFuncionario){
				addTransaction(funcionario);
			}
		}
	}

	/**
	 * Lista em um Bean funcionarios
	 * @param client
	 * @param resource
	 * @return BeanItemContainer<Funcionario>
	 */
	public static BeanItemContainer<Funcionario> loadBean(RestMBClient client, String resource){
		BeanItemContainer<Funcionario> listaBeans = new BeanItemContainer<Funcionario>(Funcionario.class);
		try{
			Connection<Funcionario> lista = Funcionario.listaAll(client,resource,Funcionario.class);

			for(List<Funcionario> listaFuncionario : lista){
				for(Funcionario funcionario : listaFuncionario){
					listaBeans.addBean(funcionario);
				}
			}
		}catch(Exception e){}
		return listaBeans;
	}

	 /**
	  * Lista os tipos de funcionarios
	  * Mensalista
	  * Horista
	  * Terceiro
	 * @return BeanItemContainer<String>
	 */
	 public static BeanItemContainer<String> listTipoFuncionario(){
		 BeanItemContainer<String> beans =
			        new BeanItemContainer<String>(String.class);
			    
		beans.addBean("Mensalista"); 
		beans.addBean("Horista"); 
		beans.addBean("Terceiro"); 
		return beans;
	 }
	 
	 /**
	  * Lista os tipos de estados civil
	  * Solteiro
	  * Casado
	  * Divorciado
	 * @return BeanItemContainer<String>
	 */
	 public static BeanItemContainer<String> listStatusCivil(){
		 BeanItemContainer<String> beans =
			        new BeanItemContainer<String>(String.class);
			    
		beans.addBean("Solteiro"); 
		beans.addBean("Casado"); 
		beans.addBean("Divorciado"); 
		beans.addBean("Viúvo"); 
		
		return beans;
	}
}
