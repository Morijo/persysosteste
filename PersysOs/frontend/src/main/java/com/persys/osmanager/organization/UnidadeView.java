/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.persys.osmanager.organization;
import com.persys.osmanager.address.AddressForm;
import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.dashboard.DashboardUI;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.organization.data.TransactionsContainerUnidade;
import com.restmb.RestMBClient;
import com.restmb.types.Endereco;
import com.restmb.types.Unidade;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class UnidadeView extends FormView implements View{

	private static final long serialVersionUID = 1L;
	private Table tableUnidade = null;

	private TransactionsContainerUnidade data = new TransactionsContainerUnidade();
	private UnidadeForm unidadeForm = null;
	private AddressForm enderecoForm =null;

	private RestMBClient client = null; 

	public UnidadeView() {
		super("Unidade");
	}

	public void modoTabela(){
		tableUnidade = new Table() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected String formatPropertyValue(Object rowId, Object colId,
					Property<?> property) {

				return super.formatPropertyValue(rowId, colId, property);
			}
		};
		tableUnidade.setSizeFull();
		tableUnidade.addStyleName("borderless");
		tableUnidade.setSelectable(true);
		tableUnidade.setColumnCollapsingAllowed(true);
		tableUnidade.setColumnReorderingAllowed(true);
		data.removeAllContainerFilters();
		tableUnidade.setContainerDataSource(data);

		tableUnidade.setVisibleColumns(new Object[] { "Codigo","Unidade","Cidade","Email"});
		tableUnidade.setColumnExpandRatio("Unidade", 1.0f);

		filter.addTextChangeListener(new TextChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(final TextChangeEvent event) {
				data.removeAllContainerFilters();
				data.addContainerFilter(new Filter() {
					/**
					 * 
					 */
					 private static final long serialVersionUID = 1L;

					 @Override
					 public boolean passesFilter(Object itemId, Item item)
							 throws UnsupportedOperationException {

						 if (event.getText() == null
								 || event.getText().equals("")) {
							 return true;
						 }

						 return filterByProperty("Email", item,
								 event.getText())
								 || filterByProperty("Unidade", item,
										 event.getText())
										 || filterByProperty("Codigo", item,
												 event.getText());
					 }

					 @Override
					 public boolean appliesToProperty(Object propertyId) {
						 if (propertyId.equals("Email")
								 || propertyId.equals("Unidade")
								 || propertyId.equals("Codigo"))
							 return true;
						 return false;
					 }

				});
			}
		});
		
		tableUnidade.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				visualizar(tableUnidade.getItem(tableUnidade.getValue()));
			} 
		});

		tableUnidade.setImmediate(true);
		defaultTable();
		modoTabelaView(tableUnidade,"Unidade");
	}

	private boolean filterByProperty(String prop, Item item, String text) {
		if (item == null || item.getItemProperty(prop) == null
				|| item.getItemProperty(prop).getValue() == null)
			return false;
		String val = item.getItemProperty(prop).getValue().toString().trim()
				.toLowerCase();
		if (val.startsWith(text.toLowerCase().trim()))
			return true;
		return false;
	}

	@Override
	public void editar() {
		try{
			Unidade unidade = ((Unidade)unidadeForm.commit()).alterar(client);
			unidade.setEndereco((Endereco) enderecoForm.commit());
			unidade.alterarEnderecoUnidade(client);
			notificationTray("Alteração", "Sucesso");
		}catch(Exception e){
			notificationError("Alteração", "Erro " + e.getMessage() );
		}
	}

	@Override
	public void adicionar() {

		unidadeForm = new UnidadeForm();
		enderecoForm = new AddressForm();

		unidadeForm.initData(new Unidade());
		enderecoForm.initData(new Endereco());

		VerticalLayout unidadeFormLayout = createLayoutForm();

		modoAdicionarView(unidadeFormLayout,"Nova Unidade");
		unidadeForm.modoAdd();

	}

	public void remover(Object target) {
		messageSucess("Remover","Deseja remover esta unidade?","Remover","Cancelar",false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					((Unidade)unidadeForm.commit()).remover(client);
					notificationTray("Remover", "Sucesso");
					voltar();
				}catch(Exception e){
					notificationError("Remover", "Erro " + e.getMessage() );
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
			Unidade unidade = (Unidade) unidadeForm.commit();
			Endereco endereco = (Endereco) enderecoForm.commit();
			if(unidade != null)
				unidade = unidade.salvar(client);
			unidade.setEndereco(endereco);
			unidade.alterarEnderecoUnidade(client);
			notificationTray("Salvar", "Sucesso");
		}catch(Exception e){
			notificationError("Salvar", "Erro " + e.getMessage() );
		}

	}

	@Override
	public void visualizar(Object unidadeItem) {
		Long id = (Long) ((Item)unidadeItem).getItemProperty("Id").getValue();

		try{

			Unidade unidade = new Unidade();
			unidade = unidade.pesquisa(client,id);
			unidadeForm = null;
			unidadeForm = new UnidadeForm();
			unidadeForm.initData(unidade);
			unidadeForm.modoView();

			enderecoForm = null;
			enderecoForm = new AddressForm();

			if(unidade.getEndereco() != null){
				enderecoForm.initData(unidade.getEndereco());
			}else enderecoForm.initData(new Endereco());

			VerticalLayout unidadeFormLayout = createLayoutForm();
			modoVisualizarView(unidadeFormLayout,unidade.getId() +" - "+ unidade.getNome());

		}catch(Exception e){

		}
	}

	private VerticalLayout createLayoutForm() {
		VerticalLayout unidadeFormLayout = new VerticalLayout();
		unidadeFormLayout.addComponent(ComponenteFactory.createPanel(unidadeForm));
		unidadeFormLayout.addComponent(ComponenteFactory.createPanel(enderecoForm));
		return unidadeFormLayout;
	}

	@Override
	public void voltar() {
		modoTabelaView(tableUnidade,"Unidade");
		defaultTable();
	}

	@Override
	public void defaultTable(){
		try {
			data.loadTableRest(client);
		} catch (ViewException e) {
			Notification.show(e.getMessage());
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		this.client = ((DashboardUI)getUI()).getClient();
		setSizeFull();
		modoTabela();
	}
}
