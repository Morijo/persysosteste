package com.persys.osmanager.contact;

import java.util.List;

import com.persys.osmanager.address.AddressForm;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.componente.interfaces.IFormWindows;
import com.persys.osmanager.exception.ViewException;
import com.restmb.Connection;
import com.restmb.RestMBClient;
import com.restmb.types.Contato;
import com.restmb.types.Endereco;
import com.restmb.types.Usuario;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;


public class ContactView extends VerticalLayout implements Button.ClickListener, 
LayoutClickListener, IForm<Contato> {

	private static final long serialVersionUID = 1L;

	private VerticalLayout     contatosDetalhes;
	private ContactDetalheView contatoDetalheAtual;

	private HorizontalLayout livro;

	private AddressForm enderecoForm = null;
	private ContactForm contatoForm = new ContactForm();

	private VerticalLayout adicionar = null;

	private RestMBClient client = null;
	private Usuario<?>   usuario = null;

	private int resultTag;
	private IFormWindows<?> iFormWindows = null;

	public ContactView(RestMBClient client, Usuario<?> usuario, IFormWindows<Contato> iFormWindows, int resultTag){
		this.client = client;
		this.usuario = usuario;
		this.iFormWindows = iFormWindows;
		this.resultTag = resultTag;
	}

	public ContactView(RestMBClient client, Usuario<?> usuario){
		this.client = client;
		this.usuario = usuario;
	}

	public ContactView(RestMBClient client){
		this.client  = client;
	}

	public VerticalLayout viewLayoutAdicionar(){
		adicionar = new VerticalLayout();
		adicionar.setWidth("-1px");
		adicionar.setHeight("100%");
		adicionar.setMargin(true);
		adicionar.setSpacing(true);

		return adicionar;
	}

	public VerticalLayout modoContato(){

		livro = new HorizontalLayout();
		livro.setWidth("-1px");
		livro.setHeight("100%");

		contatosDetalhes = new VerticalLayout();
		contatosDetalhes.setSpacing(true);

		livro.addComponent(viewLayoutAdicionar());

		addComponent(livro);

		return this;

	}

	@SuppressWarnings("deprecation")
	public VerticalLayout modoLivroContato(){

		livro = new HorizontalLayout();
		livro.setWidth("-1px");
		livro.setHeight("100%");

		final Button adicionarButton = new Button("Novo Contato");
		adicionarButton.setWidth("300px");
		addComponent(adicionarButton);
		adicionarButton.addListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				Contato c = new Contato();
				criaContato(c);
			}
		});

		contatosDetalhes = new VerticalLayout();
		contatosDetalhes.setSpacing(true);
		contatosDetalhes.addComponent(adicionarButton);

		try{
			Connection<Contato> listaContatos = Contato.listaAll(client,"/usuario/"+usuario.getId()+"/contato", Contato.class);
			for(List<Contato> lista : listaContatos){
				for(Contato contato : lista){
					criaContato(contato);
				}
			}
		}
		catch (Exception e) {}	

		livro.setMargin(true);
		livro.addComponent(contatosDetalhes);
		livro.addComponent(viewLayoutAdicionar());

		if (contatosDetalhes.getComponentCount() > 1){
			contatoDetalheAtual = (ContactDetalheView) contatosDetalhes.getComponent(1);  
			setSelecionado();
		}

		addComponent(livro);

		return this;

	}

	public ContactDetalheView criaContato(Contato contrato){
		ContactDetalheView contatoDetalheView = new ContactDetalheView(contrato);
		contatoDetalheView.addLayoutClickListener(this);
		contatoDetalheView.getRemover().addClickListener(this);
		contatoDetalheView.getGravar().addClickListener(this);
		contatosDetalhes.addComponent(contatoDetalheView,1);
		return contatoDetalheView;
	}

	public AddressForm getEnderecoForm() {
		return enderecoForm;
	}

	public void setEnderecoForm(AddressForm enderecoForm) {
		this.enderecoForm = enderecoForm;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void buttonClick(ClickEvent event) {
		Button b = (Button) event.getComponent();

		if(b.getId().contains("remover")){
			ContactDetalheView contatoDetalheView = (ContactDetalheView) b.getData();
			contatoDetalheView.setVisible(false);
			this.contatosDetalhes.removeComponent(contatoDetalheView);
			this.adicionar.setVisible(false);
			try {
				Contato contato = contatoForm.commit();
				contato.remover(client,contato.getId());
			}catch (Exception e) {}
		}
		else{
			ContactDetalheView contatoDetalheView = (ContactDetalheView) b.getData();
			try {
				Contato  contato = contatoForm.commit();
				if(enderecoForm != null && enderecoForm.isVisible()){
					Endereco endereco = (Endereco) enderecoForm.commit();
					contato.setEndereco(endereco);
				}
				if(contato.getId() == null){
					try{
						Contato c = usuario.adicionarContato(client, contato);
						contato.setId(c.getId());
						contatoDetalheView.setContato(contato);
						c = null;
					}catch(Exception e){
						Notification.show("Erro", "Salvar: " + e.getMessage(),Notification.TYPE_ERROR_MESSAGE );
					}
				}else{
					try{
						contato.alterar(client);
						contatoDetalheView.setContato(contato);
					}catch(Exception e){
						Notification.show("Erro", "Alteração: " + e.getMessage(),Notification.TYPE_ERROR_MESSAGE );
					}
				}
			} catch (ViewException e1) {
				Notification.show(e1.getMessage());
			}

		}	
	}

	@Override
	public void layoutClick(LayoutClickEvent event) {
		if(!event.isDoubleClick()){
			if(contatoDetalheAtual != null){
				contatoDetalheAtual.normal();
				setNormal();
			}
			contatoDetalheAtual = (ContactDetalheView) event.getComponent();
			setSelecionado();
		}else{
			try {
				if(iFormWindows != null)
					if(contatoDetalheAtual.getContato().getId() != null){
						iFormWindows.commitWindows(this.resultTag);
					}else
						Notification.show("Contato deve ser salvo");
			} catch (ViewException e) {}
		}	
	}

	private void setSelecionado() {
		contatoDetalheAtual.gravar.setVisible(true);
		contatoDetalheAtual.remover.setVisible(true);
		selectContact();
	}

	private void setNormal() {
		contatoDetalheAtual.normal();
		contatoDetalheAtual.gravar.setVisible(false);
		contatoDetalheAtual.remover.setVisible(false);
	}

	private void selectContact() {
		this.adicionar.setVisible(true);

		contatoDetalheAtual.selecinado();
		adicionar.removeAllComponents();

		//limpa da memoria
		contatoForm = null;
		contatoForm = new ContactForm();

		enderecoForm = null;
		enderecoForm = new AddressForm();
		enderecoForm.setVisible(false);

		CheckBox addAddress = new CheckBox("Adicionar Endereço");
		addAddress.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			public void valueChange(ValueChangeEvent event) {
				if(!enderecoForm.isVisible()){
					enderecoForm.setVisible(true);
					adicionar.addComponent(enderecoForm);
				}else{
					try{
						adicionar.removeComponent(enderecoForm);
						enderecoForm.setVisible(false);
					}catch(Exception e){}
				}
			}
		});

		adicionar.addComponent(contatoForm);

		try{
			if(!contatoDetalheAtual.getContato().getEndereco().getCep().isEmpty()){
				enderecoForm = new AddressForm();
				adicionar.addComponent(enderecoForm);
				enderecoForm.initData(contatoDetalheAtual.getContato().getEndereco());
			}else{
				adicionar.addComponent(addAddress);
				enderecoForm = null;
			}
		}catch(Exception e){
			adicionar.addComponent(addAddress);
		}
		contatoForm.initData(contatoDetalheAtual.getContato());
	}

	@Override
	public Contato commit() throws ViewException {
		Contato  contato = contatoForm.commit();
		if(enderecoForm != null){
			if(enderecoForm.isVisible()){
				Endereco endereco = (Endereco) enderecoForm.commit();
				contato.setEndereco(endereco);
			}	
		}
		return contato;
	}

	@Override
	public void initData(Contato contato) {
		try{
			contatoDetalheAtual.normal();
			Boolean encontrou = false;
			if (contatosDetalhes.getComponentCount() > 0){
				for(int i=1; i< contatosDetalhes.getComponentCount() && encontrou == false; i++){
					contatoDetalheAtual = (ContactDetalheView) contatosDetalhes.getComponent(i);  
					Contato contatoAtual =contatoDetalheAtual.getContato();
					if(contatoAtual.getId().compareTo(contato.getId()) == 0){
						contatoDetalheAtual = (ContactDetalheView) contatosDetalhes.getComponent(i);  
						selectContact();
						encontrou=true;
					}
				}
			}
			if(!encontrou){
				contatoDetalheAtual = criaContato(contato);
				contatoDetalheAtual.selecinado();
				selectContact();
			}
		}catch(Exception e){}
	}

	@Override
	public void modoView() {}

	@Override
	public void modoAdd() {}	

}
