package com.persys.osmanager.organization;

import java.io.File;

import com.persys.osmanager.address.AddressForm;
import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IImage;
import com.persys.osmanager.dashboard.DashboardUI;
import com.restmb.RestMBClient;
import com.restmb.exception.RestMBException;
import com.restmb.types.Endereco;
import com.restmb.types.Organizacao;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class OrganizationView extends FormView implements View, IImage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Organizacao organizacao;
	private Endereco    endereco;
	
	private OrganizationForm organizationForm = new OrganizationForm();
	private AddressForm      addressForm = new AddressForm();
	private RestMBClient     client;
	    
	public OrganizationView(){
		super("Organização");
	}

	@Override
	public void editar() {
		try{
			organizacao = (Organizacao) organizationForm.commit();
			organizacao.alterar(client);
			
			endereco = (Endereco) addressForm.commit();
			endereco.setResourcePath("/organizacao/"+organizacao.getId()+"/endereco");
			endereco.salvar(client);
			notificationTray("Alteração", "Sucesso");
		}catch(Exception e){
			notificationError("Alteração", "Erro " + e.getMessage() );
		}
	}

	@Override
	public void adicionar() {}

	@Override
	public void remover(Object target) {}

	@Override
	public void voltar() {}

	@Override
	public void salvar() {}

	@Override
	public void visualizar(Object organizacao) {
		
		organizationForm = null;
		organizationForm = new OrganizationForm();
		
		addressForm = null;
		addressForm = new AddressForm();
		
		organizationForm.initData(this.organizacao);
		addressForm.initData(this.organizacao.getEndereco());
			
		VerticalLayout organizacaoFormLayout = createLayoutForm();
    	
		modoVisualizarView(organizacaoFormLayout,this.organizacao.getNomeFantasia(),this.organizacao.getLogo());
		buttonVoltar.setVisible(false);
		buttonDeletar.setVisible(false);
	}

	private VerticalLayout createLayoutForm() {
		VerticalLayout organizacaoFormLayout = new VerticalLayout();
		organizacaoFormLayout.addComponent(ComponenteFactory.createPanel(organizationForm));
		organizacaoFormLayout.addComponent(ComponenteFactory.createPanel(addressForm));
		return organizacaoFormLayout;
	}

	@Override
	public void defaultTable() {
		organizationForm = new OrganizationForm();
		organizacao = Organizacao.pesquisaHome(client);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		 this.client = ((DashboardUI)getUI()).getClient();
		 setSizeFull();
		 defaultTable();
		 visualizar(new Organizacao());
	}
	
	public void image(File file){
    	super.image(file);
    	try{
    		organizacao.setLogo(client, file);
    	}catch(RestMBException exception){}	
	}
	
}
