package com.persys.osmanager.employee;
import java.util.Iterator;

import br.com.model.interfaces.IFuncionario;

import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.contact.ContactView;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.restmb.types.Funcionario;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class EmployeeMenu extends HorizontalLayout implements IForm<Funcionario>{


	private CssLayout menu;
	private CssLayout content;

	EmployeeForm     funcionarioForm 			= null;
	EmployeeWorkForm funcionarioTrabalhistaForm = null;
	ContactView      contatoView	        	= null;

	Button bDados = null;
	Button bTrabalhista = null;
	Button bContatos = null;

	private static int MODODADOS = 0;
	private static int MODOTRABALHISTA = 1;
	private static int MODOCONTATO = 2;
	private static int STATUSVIEW = 0;

	RestMBClient client = null;

	public EmployeeMenu(RestMBClient client){

		this.client = client;

		menu = new CssLayout();
		content = new CssLayout();

		setSizeFull();
		addStyleName("main-view");
		addComponent(new VerticalLayout() {
			// Sidebar
			{
				addStyleName("sidebar-invert");
				setWidth(null);
				setHeight("100%");
				menu.addStyleName("menu");
				menu.setHeight("100%");
				addComponent(menu);
				setExpandRatio(menu, 1);

			}
		});

		addComponent(content);
		content.setSizeFull();
		setExpandRatio(content, 1);        


		bDados = new NativeButton("    Dados Gerais   ".toUpperCase());
		bDados.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				clearMenuSelection();
				event.getButton().addStyleName("selected-invert");
				content.removeAllComponents();
				content.addComponent(funcionarioForm);
				STATUSVIEW = MODODADOS;
			}
		});
		menu.addComponent(bDados);

		/*   bTrabalhista = new NativeButton("  Dados Trabalhista   ".toUpperCase());
        bTrabalhista.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
           	 clearMenuSelection();
                event.getButton().addStyleName("selected-invert");
                content.removeAllComponents();
                content.addComponent(funcionarioTrabalhistaForm);
                STATUSVIEW = MODOTRABALHISTA;
            }
        });
        menu.addComponent(bTrabalhista);*/

		bContatos = new NativeButton("   Contatos   ".toUpperCase());
		bContatos.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				clearMenuSelection();
				event.getButton().addStyleName("selected-invert");
				content.removeAllComponents();
				content.addComponent(contatoView);
				STATUSVIEW = MODOCONTATO;
			}
		});
		menu.addComponent(bContatos);
	}

	private void clearMenuSelection() {
		for (@SuppressWarnings("deprecation")
		Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
			Component next = it.next();
			if (next instanceof NativeButton) {
				next.removeStyleName("selected-invert");
			} else if (next instanceof DragAndDropWrapper) {
				((DragAndDropWrapper) next).iterator().next()
				.removeStyleName("selected-invert");
			}
		}
	}

	@Override
	public Funcionario commit() throws ViewException {
		Funcionario funcionario = null;

		try{
			if(STATUSVIEW == MODODADOS )
				funcionario = (Funcionario) funcionarioForm.commit();

			if (STATUSVIEW == MODOTRABALHISTA)
				funcionario = funcionarioTrabalhistaForm.commit();

			return funcionario;
		}catch(Exception e){
			throw new ViewException(e.getMessage());
		}	
	}

	@Override
	public void initData(Funcionario funcionario) {

		content.removeAllComponents();
		funcionarioForm = null;
		funcionarioTrabalhistaForm = null;
		contatoView = null;

		funcionarioForm = new EmployeeForm(client); 
		funcionarioForm.initData((IFuncionario) funcionario);
		content.addComponent(funcionarioForm);
		STATUSVIEW = MODODADOS;

		funcionarioTrabalhistaForm = new EmployeeWorkForm(client); 
		funcionarioTrabalhistaForm.initData(funcionario);

		contatoView = new ContactView(client, funcionario);
		contatoView.modoLivroContato();

	}

	@Override
	public void modoView() {
		bContatos.setVisible(true);
		funcionarioForm.modoView();
	}

	@Override
	public void modoAdd() {
		bContatos.setVisible(false);
		funcionarioForm.modoAdd();
	}

}
