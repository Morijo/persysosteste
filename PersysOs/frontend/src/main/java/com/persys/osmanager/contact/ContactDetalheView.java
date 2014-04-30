package com.persys.osmanager.contact;

import com.restmb.types.Contato;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ContactDetalheView extends VerticalLayout  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label labelInfo;
	private Label labelInfoNome;

	public Button remover;
	public Button gravar;

	private Contato contato;

	@SuppressWarnings("deprecation")
	public ContactDetalheView(Contato contato) {

		this.contato = contato;

		setWidth("300px");
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
		labelInfoNome.setWidth("200px");
		labelInfoNome.setHeight("-1px");
		labelInfoNome.setValue("<b>"+contato.getNome()+"</b>");
		labelInfoNome.setContentMode(Label.CONTENT_XHTML);
		layoutPrincipal.addComponent(labelInfoNome);

		// remover
		remover = new Button();
		remover.setId("remover");
		remover.setIcon(new ThemeResource("../reindeer/Icons/edit_trash.png"));
		remover.setImmediate(true);
		remover.addStyleName("newicon");
		remover.setWidth("35px");
		remover.setHeight("-1px");
		remover.setData(this);
		remover.setVisible(false);
		layoutPrincipal.addComponent(remover);
		layoutPrincipal.setComponentAlignment(remover, new Alignment(34));
		layoutPrincipal.setExpandRatio(remover, 1.0f);

		gravar = new Button();
		gravar.setId("gravar");
		gravar.setIcon(new ThemeResource("../reindeer/Icons/save.png"));
		gravar.setImmediate(true);
		gravar.setWidth("35px");
		gravar.setHeight("-1px");
		gravar.setVisible(false);
		gravar.addStyleName("newicon");

		gravar.setData(this);
		layoutPrincipal.addComponent(gravar);
		layoutPrincipal.setComponentAlignment(gravar, new Alignment(34));

		addComponent(layoutPrincipal);

		// labelinfo
		labelInfo = new Label();
		labelInfo.setImmediate(true);
		labelInfo.setWidth("100%");
		labelInfo.setHeight("-1px");
		labelInfo.setValue(geraInfo());
		labelInfo.setContentMode(Label.CONTENT_XHTML);
		addComponent(labelInfo);
		setData(contato);
	}

	public String geraInfo(){

		String info="";

		String tipo = (contato.getTipoContato()!=null) ? contato.getTipoContato() : " ";
		String departamento = (contato.getDepartamento()!=null) ? contato.getDepartamento() : " ";
		
		try{
			if(!contato.getEndereco().getCep().isEmpty()){
				info =
						"<p>"+tipo+" "+departamento+"</p>"
								+ "<p>"+contato.getEndereco().getLogradouro()+", "+contato.getEndereco().getNumero()+", "
								+ contato.getEndereco().getBairro()+", "+contato.getEndereco().getCidade()+", "
								+ contato.getEndereco().getEstado()+", "+contato.getEndereco().getCep()+"</p>";
							
			}
			if(contato.getEmail() != null && !contato.getEmail().isEmpty()){
				info = info + "<p>Email: "+ contato.getEmail()+"</p>";
			}
			if(contato.getTelefoneFixo() != null && !contato.getTelefoneFixo().isEmpty()){
				info = info + "<p>Telefone: "+ contato.getTelefoneFixo() +" "+contato.getTelefoneMovel()+"</p>";
			}
			
		}catch(Exception e){
			info = "";
		}
		return info;
	}

	public String geraInfoEndereco(){

		String info="";
	
		try{
			if(!contato.getEndereco().getCep().isEmpty()){
				info =
						"<p>Endereço: "+contato.getEndereco().getLogradouro()+", "+contato.getEndereco().getNumero()+", "
						+ contato.getEndereco().getBairro()+", "+contato.getEndereco().getCidade()+", "
						+ contato.getEndereco().getEstado()+", "+contato.getEndereco().getCep()+"</p>";
			}
			if(contato.getEmail() != null && !contato.getEmail().isEmpty()){
				info = info + "<p>Email: "+ contato.getEmail()+"</p>";
			}
			if(contato.getTelefoneFixo() != null && !contato.getTelefoneFixo().isEmpty()){
				info = info + "<p>Telefone: "+ contato.getTelefoneFixo() +" "+contato.getTelefoneMovel()+"</p>";
			}
		}catch(Exception e){
			info = "";
		}
		return info;
	}

	/**
	 * @return the contato
	 */
	public Contato getContato() {
		return contato;
	}

	/**
	 * @param contatoCliente the contatoCliente to set
	 */
	public void setContato(Contato contato) {
		this.contato = contato;
		labelInfoNome.setValue("<b>"+contato.getNome()+"</b>");
		labelInfo.setValue(geraInfo());
		this.setData(contato);
	}

	/**
	 * @param contatoCliente the contato cliente to set
	 */
	public void setContatoEndereco(Contato contato) {
		this.contato = contato;
		labelInfoNome.setWidth("100%");
		labelInfoNome.setValue("<b>Contato Responsável: "+contato.getNome()+"</b>");
		labelInfo.setValue(geraInfoEndereco());
		this.setData(contato);
	}

	public void selecinado(){
		setStyleName("selecionado");
	}

	public void normal(){
		setStyleName("normal");
	}

	public Button getRemover(){
		return this.remover;
	}

	public Button getGravar() {
		return gravar;
	}


}
