package com.persys.osmanager.customer;

import com.persys.osmanager.componente.helper.AttrDim;
import com.restmb.types.Cliente;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CustomerDetailView extends VerticalLayout  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label labelInfoNome;
	public Button cancelar;

	private Cliente cliente;
	
	@SuppressWarnings("deprecation")
	public CustomerDetailView(final Cliente cliente) {

		this.cliente = cliente;
		
		setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		setHeight("100.0%");
		setMargin(true);
		setStyleName("normal");

		HorizontalLayout layoutPrincipal= new HorizontalLayout();
		layoutPrincipal.setImmediate(false);
		layoutPrincipal.setWidth("100.0%");
		layoutPrincipal.setHeight("100.0%");
		layoutPrincipal.setMargin(false);

		// labelinfo
		labelInfoNome = new Label();
		labelInfoNome.setImmediate(true);
		labelInfoNome.setWidth("400px");
		labelInfoNome.setHeight("-1px");
		labelInfoNome.setValue(geraInfo());
		labelInfoNome.setContentMode(Label.CONTENT_XHTML);
		layoutPrincipal.addComponent(labelInfoNome);

		// remover
		cancelar = new Button();
		cancelar.setId("remover");
		cancelar.setIcon(new ThemeResource("../reindeer/Icons/cancel.png"));
		cancelar.setImmediate(true);
		cancelar.addStyleName("newicon");
		cancelar.setWidth("32px");
		cancelar.setHeight("-1px");
		cancelar.setData(this);
		cancelar.setVisible(true);
		layoutPrincipal.addComponent(cancelar);
		layoutPrincipal.setComponentAlignment(cancelar, new Alignment(34));
		layoutPrincipal.setExpandRatio(cancelar, 1.0f);
		addComponent(layoutPrincipal);

	}

	public String geraInfo(){

		String info="";
		String cpf = (cliente.getCnpjCpf()!=null) ? "CPF/CNPJ: "+ cliente.getCnpjCpf() : " ";
		String rg = (cliente.getIeRg()!=null) ? "RG/IE" + cliente.getIeRg() : " ";

		StringBuffer mediaString = new StringBuffer(cliente.getEmailPrincipal());

		info = 	"<b>"+cliente.getCodigo()+" "+cliente.getRazaoNome()+"</b>"
				+ "<p> "+cpf+" "+rg+"</p>"
				+ "<p> Email: "+mediaString+"</p>";

		return info;
	}

	/**
	 * @return the clietne
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param contatoCliente the contatoCliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
		labelInfoNome.setValue(geraInfo());
		this.setData(cliente);
	}

	public void selecinado(){
		setStyleName("selecionado");
	}

	public void normal(){
		setStyleName("normal");
	}

	public Button getCancelar(){
		return this.cancelar;
	}
}
