package com.persys.osmanager.system;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.usuario.model.GrupoUsuario;
import br.com.usuario.model.GrupoUsuarioPermissao;
import br.com.usuario.model.Permissao;

import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.ContainerUtils;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class GrupoUsuarioForm extends CustomComponent implements IForm<GrupoUsuario>{


	private static final long serialVersionUID   = 1L;
	private TextField 	   	  textFieldNome 	 = null;
	private ComboBox 	   	  comboBoxStatus 	 = null;
	private TextField 	      textFieldCodigo    = null;
	private CheckBox 		  checkBoxAdmin 	 = null;
	final TreeTable ttable = new TreeTable("Permissões");

	private final ComponenteFactory componenteFactory = new ComponenteFactory();

	private final static ResourceBundle bundle;
	static{
		bundle = ResourceBundle.getBundle("com/persys/frontend/grupousuario",Locale.getDefault());
	}

	private GrupoUsuario grupoUsuario = null;
	private RestMBClient client       = null;
	
	public GrupoUsuarioForm(RestMBClient client) {
		this.client = client;
		
		setWidth("100.0%");
		setHeight("100.0%");
		setCompositionRoot(buildMainLayout());
	}

	private VerticalLayout buildMainLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		mainLayout.addComponent(buildHorizontalLayout());

		textFieldNome = componenteFactory.createTextFieldRequiredError(new TextField(),bundle.getString("nomegrupo"),"Erro",
				AttrDim.FORM_COM_SINGLE_WIDTH);

		mainLayout.addComponent(textFieldNome);

		mainLayout.addComponent(createTable());

		return mainLayout;
	}

	public TreeTable createTable(){
		ttable.addContainerProperty("Nome do Recurso", CheckBox.class, "");
		ttable.addContainerProperty("Identificador do Recurso", Long.class, -1l);
		ttable.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		ttable.setHeight("400px");

		checkBoxAdmin = new CheckBox("Admin");
		checkBoxAdmin.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				alteraChech((Boolean)event.getProperty().getValue());
			}
		});
		carregaPermissao();


		return ttable;
	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);

		textFieldCodigo = new TextField();
		textFieldCodigo.setCaption(bundle.getString("codigo"));
		textFieldCodigo.setImmediate(false);
		textFieldCodigo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout.addComponent(textFieldCodigo);

		comboBoxStatus = new ComboBox();
		comboBoxStatus.setCaption(bundle.getString("situacao"));
		comboBoxStatus.setImmediate(false);
		comboBoxStatus.setContainerDataSource(ContainerUtils.listaStatusBean());
		comboBoxStatus.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		comboBoxStatus.setHeight("-1px");
		comboBoxStatus.setContainerDataSource(AppData.listStatusInteger());
		comboBoxStatus.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_PROPERTY);
		comboBoxStatus.setItemCaptionPropertyId("statusNome");
		comboBoxStatus.setNullSelectionAllowed(false);
		horizontalLayout.addComponent(comboBoxStatus);

		return horizontalLayout;
	}

	public void alteraChech(boolean status){
		for(Object itemId : ttable.getItemIds()){
			Item item = ttable.getItem(itemId);
			CheckBox checkBox = (CheckBox) item.getItemProperty("Nome do Recurso").getValue();
			checkBox.setValue(status);
		}
	}

	public void setChech(){

		ArrayList<GrupoUsuarioPermissao> permissaos = 
				GrupoUsuarioPermissao.listaPermissaoPorGrupo(grupoUsuario.getId());

		int i=0;
		try{
			for(Object itemId : ttable.getItemIds()){
				Item item = ttable.getItem(itemId);
				if(i>1){
					CheckBox checkBox = (CheckBox) item.getItemProperty("Nome do Recurso").getValue();
					checkBox.setValue(permissaos.get(i-1).getStatusModel() == 1 ? true : false);
				}
				i++;
			}
		}catch (Exception e) {
			Notification.show("Não foi possível carregar a lista de permissão para este grupo");
		}
	}

	@Override
	public GrupoUsuario commit() throws ViewException {
		try{
			componenteFactory.valida();

			grupoUsuario.setCodigo(textFieldCodigo.getValue());
			grupoUsuario.setNome(textFieldNome.getValue());
			grupoUsuario.setStatusModel((Integer)comboBoxStatus.getValue());

			if(!checkBoxAdmin.getValue().booleanValue()){
				grupoUsuario.setAdministrado(false);
			}else{grupoUsuario.setAdministrado(true);}

			return grupoUsuario;
		}catch(Exception e){
			throw new ViewException(e.getMessage());
		}	
	}

	public GrupoUsuario commitPermissao(){
		for(Object itemId : ttable.getItemIds()){
			Item item = ttable.getItem(itemId);
			Long recursoId =(Long) item.getItemProperty("Identificador do Recurso").getValue();
			CheckBox checkBox =  (CheckBox) item.getItemProperty("Nome do Recurso").getValue();
			int status = checkBox.getValue() ? 1 : 0;
			if(recursoId > 0)
				grupoUsuario.getGrupoUsuarioPermissao()
				.add(GrupoUsuarioPermissao.createGrupoUsuaroPermissao(grupoUsuario.getId(), recursoId,
						status));
		}
		return grupoUsuario;
	}

	@Override
	public void initData(GrupoUsuario data) {

		this.grupoUsuario = data;
		textFieldCodigo.setValue(grupoUsuario.getCodigo());
		textFieldNome.setValue(grupoUsuario.getNome());
		comboBoxStatus.select(grupoUsuario.getStatusModel());

		if(grupoUsuario.getId() != null && grupoUsuario.getAdministrado()){
			checkBoxAdmin.setValue(true);
			alteraChech(true);
		}else if(grupoUsuario.getId() != null){
			setChech();
		}
	}

	private void carregaPermissao() {
		ttable.addItem(new Object[]{checkBoxAdmin, 0l}, 0);

		Permissao permissao = new Permissao();
		ArrayList<Permissao> arrayList = permissao.listaPorConstrutor(Permissao.construtor, client.getOauth().getApiKey());

		for(int i=0; i<arrayList.size();i++){
			permissao = arrayList.get(i);
			ttable.addItem(new Object[]{new CheckBox(permissao.getNome()), permissao.getId()}, i+1);
			ttable.setParent(i+1, 0);
		}

		ttable.setPageLength(ttable.size());

		ttable.setCollapsed(2, false);
		for (Object itemId: ttable.getItemIds())
			ttable.setCollapsed(itemId, false);
	}

	@Override
	public void modoView() {
		textFieldCodigo.setReadOnly(true);
	}

	@Override
	public void modoAdd() {
		textFieldCodigo.setReadOnly(false);
	}

}
