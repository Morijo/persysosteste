package com.persys.osmanager.employee;

import java.util.Locale;
import java.util.ResourceBundle;
import org.vaadin.csvalidation.CSValidator;
import br.com.model.interfaces.IFuncionario;
import br.com.usuario.model.GrupoUsuario;
import com.persys.osmanager.componente.ComponenteFactory;
import com.persys.osmanager.componente.FormViewWindow;
import com.persys.osmanager.componente.PreconditionsView;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.employee.data.TransactionsContainerEmployeeData;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.organization.DepartamentoForm;
import com.persys.osmanager.organization.UnidadeForm;
import com.persys.osmanager.organization.data.TransactionsContainerDepartamento;
import com.persys.osmanager.organization.data.TransactionsContainerUnidade;
import com.persys.osmanager.system.data.TransactionsContainerGrupo;
import com.persys.osmanager.user.UserWindow;
import com.restmb.Parameter;
import com.restmb.RestMBClient;
import com.restmb.types.Departamento;
import com.restmb.types.Funcionario;
import com.restmb.types.Unidade;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class EmployeeForm extends CustomComponent implements IForm<IFuncionario>{

	private static final long serialVersionUID = 1L;

	private ComboBox comboBoxStatus;
	private ComboBox comboBoxEstadoCivil;
	private ComboBox comboBoxUnidade;
	private ComboBox comboBoxDepartamento;
	private ComboBox comboBoxGrupo;
	private TextField textFieldCodigo;
	private TextField textFieldNome;
	private TextField textFieldApelido;
	private TextField textFieldRGIE;
	private TextField textFieldCnpjCpf;
	private TextField textFieldEmailPrincipal;
	private DateField dateFieldDataNascimento;
	private TextField textFieldUsuario;
	private Button    buttonAlterarSenha;
	private Button    buttonRecuperarSenha;

	private RestMBClient client;

	private IFuncionario funcionario;

	private final ComponenteFactory componenteFactory = new ComponenteFactory();
	private static String errorMessage;

	private final static ResourceBundle resourceBundle;
	static{
		resourceBundle = ResourceBundle.getBundle("com/persys/frontend/employee",Locale.getDefault());
		errorMessage = resourceBundle.getString("requirederror");
	}

	public EmployeeForm(RestMBClient client) {
		this.client = client;

		setWidth(AttrDim.FORM_WIDTH);
		setHeight(AttrDim.FORM_HEIGHT);

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

		textFieldNome = componenteFactory.createTextFieldRequiredError(new TextField(), resourceBundle.getString("name"), 
				errorMessage,AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldNome.setCaption(resourceBundle.getString("name"));
		mainLayout.addComponent(textFieldNome);

		textFieldApelido = new TextField();
		textFieldApelido.setCaption(resourceBundle.getString("nick"));
		textFieldApelido.setImmediate(false);
		textFieldApelido.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldApelido.setHeight("-1px");
		mainLayout.addComponent(textFieldApelido);

		mainLayout.addComponent(buildHorizontalLayoutRGCPF());

		mainLayout.addComponent(buildHorizontalLayoutTipUni());

		mainLayout.addComponent(buildHorizontalLayoutNascimEstadoC());

		textFieldEmailPrincipal = new TextField();
		textFieldEmailPrincipal.setCaption(resourceBundle.getString("email"));
		textFieldEmailPrincipal.setImmediate(false);
		textFieldEmailPrincipal.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldEmailPrincipal.setHeight("-1px");
		CSValidator validator = new CSValidator();
		validator.extend(textFieldEmailPrincipal);
		validator.setRegExp(PreconditionsView.EMAIL_PATTERN);
		validator.setErrorMessage("Email: "+errorMessage);
		mainLayout.addComponent(textFieldEmailPrincipal);
	
		mainLayout.addComponent(buildHorizontalLayoutUsuario());
		
		mainLayout.addComponent(buildHorizontalLayoutSenha());

		return mainLayout;
	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildHorizontalLayout() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		textFieldCodigo = new TextField();
		textFieldCodigo.setCaption(resourceBundle.getString("code"));
		textFieldCodigo.setImmediate(false);
		textFieldCodigo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout.addComponent(textFieldCodigo);

		comboBoxStatus = componenteFactory.createComboBoxRequiredError(new ComboBox(), resourceBundle.getString("status"), errorMessage);
		comboBoxStatus.setContainerDataSource(AppData.listStatusInteger());
		comboBoxStatus.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_PROPERTY);
		comboBoxStatus.setItemCaptionPropertyId("statusNome");
		comboBoxStatus.setNullSelectionAllowed(false);
		horizontalLayout.addComponent(comboBoxStatus);

		horizontalLayout.addComponent(comboBoxStatus);

		return horizontalLayout;
	}

	private HorizontalLayout buildHorizontalLayoutRGCPF() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		textFieldCnpjCpf = new TextField();
		textFieldCnpjCpf.setCaption(resourceBundle.getString("cpf"));
		textFieldCnpjCpf.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCnpjCpf.setRequiredError(errorMessage +": " +resourceBundle.getString("cpf"));
		textFieldCnpjCpf.setRequired(true);
		horizontalLayout.addComponent(textFieldCnpjCpf);

		textFieldRGIE = new TextField();
		textFieldRGIE.setCaption(resourceBundle.getString("rg"));
		textFieldRGIE.setImmediate(false);
		textFieldRGIE.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldRGIE.setHeight("-1px");
		horizontalLayout.addComponent(textFieldRGIE);

		return horizontalLayout;

	}

	private HorizontalLayout buildHorizontalLayoutTipUni() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		HorizontalLayout horizontalLayoutUnidade = createHorizontalLayout();
		horizontalLayoutUnidade.setSpacing(false);

		comboBoxUnidade = new ComboBox();
		comboBoxUnidade.setCaption(resourceBundle.getString("unit"));
		comboBoxUnidade.setImmediate(true);
		comboBoxUnidade.setWidth(AttrDim.FORM_COM_DOUBLE_ICON_WIDTH);
		comboBoxUnidade.setHeight("-1px");
		comboBoxUnidade.setContainerDataSource(TransactionsContainerUnidade.listUnidade(client));
		comboBoxUnidade.setNullSelectionAllowed(false);
		comboBoxUnidade.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				loadDepartamento();
			}
		});

		horizontalLayoutUnidade.addComponent(comboBoxUnidade);

		Button buttonNovoUnidade = new Button();
		buttonNovoUnidade.setIcon(new ThemeResource("../reindeer/Icons/plus.png"));
		buttonNovoUnidade.setImmediate(true);
		buttonNovoUnidade.setWidth("30px");
		buttonNovoUnidade.setHeight("-1px");
		buttonNovoUnidade.addStyleName("newicon");

		buttonNovoUnidade.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window window = new FormViewWindow<Unidade>(client, "Nova Unidade", 
						new UnidadeForm(), new Unidade());
				getUI().addWindow(window);
				window.addCloseListener(new CloseListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						loadUnidade();
					}
				});
			}
		});
		horizontalLayoutUnidade.addComponent(buttonNovoUnidade);
		horizontalLayoutUnidade.setComponentAlignment(buttonNovoUnidade, Alignment.BOTTOM_CENTER);

		HorizontalLayout horizontalLayoutDepartamento = createHorizontalLayout();
		horizontalLayoutDepartamento.setSpacing(false);

		comboBoxDepartamento = new ComboBox();
		comboBoxDepartamento.setCaption(resourceBundle.getString("department"));
		comboBoxDepartamento.setImmediate(false);
		comboBoxDepartamento.setWidth(AttrDim.FORM_COM_DOUBLE_ICON_WIDTH);
		comboBoxDepartamento.setHeight("-1px");
		comboBoxDepartamento.setNullSelectionAllowed(false);
		comboBoxDepartamento.setRequiredError(errorMessage+": " +resourceBundle.getString("department") );
		comboBoxDepartamento.setRequired(true);

		horizontalLayoutDepartamento.addComponent(comboBoxDepartamento);

		Button buttonNovoDepartamento = new Button();
		buttonNovoDepartamento.setIcon(new ThemeResource("../reindeer/Icons/plus.png"));
		buttonNovoDepartamento.setImmediate(true);
		buttonNovoDepartamento.setWidth("30px");
		buttonNovoDepartamento.setHeight("-1px");
		buttonNovoDepartamento.addStyleName("newicon");

		buttonNovoDepartamento.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window window = new FormViewWindow<Departamento>(client, "Novo Departamento", 
						new DepartamentoForm(client), new Departamento());
				getUI().addWindow(window);
				window.addCloseListener(new CloseListener() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						loadDepartamento();
					}
				});
			}
		});
		horizontalLayoutDepartamento.addComponent(buttonNovoDepartamento);
		horizontalLayoutDepartamento.setComponentAlignment(buttonNovoDepartamento, Alignment.BOTTOM_CENTER);

		horizontalLayout.addComponent(horizontalLayoutUnidade);
		horizontalLayout.addComponent(horizontalLayoutDepartamento);
		return horizontalLayout;
	}

	private HorizontalLayout buildHorizontalLayoutNascimEstadoC() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		comboBoxEstadoCivil = new ComboBox();
		comboBoxEstadoCivil.setCaption(resourceBundle.getString("maritalstatus"));
		comboBoxEstadoCivil.setImmediate(false);
		comboBoxEstadoCivil.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		comboBoxEstadoCivil.setHeight("-1px");
		comboBoxEstadoCivil.setNullSelectionAllowed(false);
		comboBoxEstadoCivil.setContainerDataSource( TransactionsContainerEmployeeData.listStatusCivil());
		horizontalLayout.addComponent(comboBoxEstadoCivil);

		dateFieldDataNascimento = new DateField();
		dateFieldDataNascimento.setCaption(resourceBundle.getString("birthdate"));
		dateFieldDataNascimento.setImmediate(false);
		dateFieldDataNascimento.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		dateFieldDataNascimento.setHeight("-1px");
		horizontalLayout.addComponent(dateFieldDataNascimento);

		return horizontalLayout;
	}

	
	private HorizontalLayout buildHorizontalLayoutUsuario() {
		// common part: create layout
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		// textFieldCodigo
		textFieldUsuario = componenteFactory.createTextFieldRequiredError(new TextField(), resourceBundle.getString("user"), 
				errorMessage,AttrDim.FORM_COM_DOUBLE_WIDTH);
		horizontalLayout.addComponent(textFieldUsuario);

		comboBoxGrupo = new ComboBox();
		comboBoxGrupo.setCaption(resourceBundle.getString("group"));
		comboBoxGrupo.setImmediate(true);
		comboBoxGrupo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		comboBoxGrupo.setHeight("-1px");
		comboBoxGrupo.setContainerDataSource(TransactionsContainerGrupo.beanItemContainerGrupo(client));
		comboBoxGrupo.setNullSelectionAllowed(false);
		comboBoxGrupo.setRequiredError(errorMessage+": "+resourceBundle.getString("group"));
		comboBoxGrupo.setRequired(true);
	horizontalLayout.addComponent(comboBoxGrupo);

		return horizontalLayout;
	}

	private HorizontalLayout buildHorizontalLayoutSenha() {
		HorizontalLayout horizontalLayoutButton = createHorizontalLayout();

		buttonAlterarSenha = new Button();
		buttonAlterarSenha.setCaption(resourceBundle.getString("alterpwd"));
		buttonAlterarSenha.setImmediate(false);
		buttonAlterarSenha.setWidth("100%");
		buttonAlterarSenha.setHeight("-1px");
		buttonAlterarSenha.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				UserWindow userWindow = new UserWindow(funcionario.getId());
				userWindow.modoPassword();
				getUI().addWindow(userWindow);
			} 
		});
		horizontalLayoutButton.addComponent(buttonAlterarSenha);
		horizontalLayoutButton.setComponentAlignment(buttonAlterarSenha, Alignment.BOTTOM_CENTER);

		buttonRecuperarSenha = new Button();
		buttonRecuperarSenha.setCaption(resourceBundle.getString("restorepwd"));
		buttonRecuperarSenha.setImmediate(false);
		buttonRecuperarSenha.setWidth("100%");
		buttonRecuperarSenha.setHeight("-1px");
		buttonRecuperarSenha.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				try{
					if(Funcionario.recuperaSenha(client, Parameter.with("id", funcionario.getId())) != null){
						Notification.show(resourceBundle.getString("msgrestorepwd")+" "+funcionario.getEmailPrincipal());
					}else{
						Notification.show(resourceBundle.getString("msgfailrestorepwd"));
					}
				}catch(Exception e){
					Notification.show(resourceBundle.getString("msgfailrestorepwd"));
				}
			} 
		});
		horizontalLayoutButton.addComponent(buttonRecuperarSenha);
		horizontalLayoutButton.setComponentAlignment(buttonRecuperarSenha, Alignment.BOTTOM_CENTER);
		return horizontalLayoutButton;
	}

	private HorizontalLayout createHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		return horizontalLayout;
	}

	@Override
	public void modoView() {
		textFieldCodigo.setReadOnly(true);
		textFieldUsuario.setReadOnly(true);
		buttonAlterarSenha.setVisible(true);
		buttonRecuperarSenha.setVisible(true);
	}

	@Override
	public void modoAdd() {
		textFieldCodigo.setReadOnly(true);
		buttonAlterarSenha.setVisible(false);
		buttonRecuperarSenha.setVisible(false);
	}

	@Override
	public IFuncionario commit() throws ViewException{
		try{
			componenteFactory.valida();
			textFieldCnpjCpf.validate();
			comboBoxDepartamento.validate();
			textFieldEmailPrincipal.validate();
			comboBoxGrupo.validate();
			
			funcionario.setCnpjCpf(textFieldCnpjCpf.getValue().replace(".", "").replace("-", ""));
			funcionario.setCodigo(textFieldCodigo.getValue());
			funcionario.setFantasiaSobrenome(textFieldApelido.getValue());
			funcionario.setNomeUsuario(textFieldUsuario.getValue());
			funcionario.setRazaoNome(textFieldNome.getValue());
			funcionario.setIeRg(textFieldRGIE.getValue());
			funcionario.setDataNascimento(dateFieldDataNascimento.getValue());
			funcionario.setEmailPrincipal(textFieldEmailPrincipal.getValue());
			funcionario.setEstadoCivil((String)comboBoxEstadoCivil.getValue());
			funcionario.setStatusModel((Integer) comboBoxStatus.getValue());
			funcionario.setDepartamento((Departamento)comboBoxDepartamento.getValue());
			funcionario.setGrupoUsuario(
					(br.com.model.interfaces.IGrupoUsuario) new com.restmb.types.GrupoUsuario(((GrupoUsuario) comboBoxGrupo.getValue()).getId()));
			return funcionario;
		}catch(Exception e){
			throw new ViewException(e.getMessage());
		}
	}

	@Override
	public void initData(IFuncionario data) {

		textFieldCnpjCpf.setValue(data.getCnpjCpf());
		textFieldCodigo.setValue(data.getCodigo());
		textFieldApelido.setValue(data.getFantasiaSobrenome());
		textFieldNome.setValue(data.getRazaoNome());
		textFieldRGIE.setValue(data.getIeRg());
		dateFieldDataNascimento.setValue(data.getDataNascimento());
		textFieldEmailPrincipal.setValue(data.getEmailPrincipal());
		comboBoxEstadoCivil.select(data.getEstadoCivil());
		comboBoxStatus.select(data.getStatusModel());
		textFieldUsuario.setValue(data.getNomeUsuario());

		if(data.getDepartamento() != null){
			comboBoxUnidade.select(data.getDepartamento().getUnidade());
			comboBoxDepartamento.addItem(data.getDepartamento());
			comboBoxDepartamento.select(data.getDepartamento());
		}
		
		if(data.getGrupoUsuario() != null && data.getGrupoUsuario().getId() != null)
			comboBoxGrupo.select(new GrupoUsuario(data.getGrupoUsuario().getId(),"","",true));
		
		funcionario = data;
	}

	private void loadUnidade(){
		comboBoxUnidade.getContainerDataSource().removeAllItems();
		comboBoxUnidade.setContainerDataSource(TransactionsContainerUnidade.listUnidade(client));
	}

	private void loadDepartamento() {
		comboBoxDepartamento.removeAllItems();
		Unidade unidade = (Unidade)comboBoxUnidade.getValue();
		try{
			comboBoxDepartamento.setContainerDataSource(TransactionsContainerDepartamento.loadBean(client, "/departamento/unidade/"+unidade.getId()));
		}catch(Exception e){
			Notification.show("Nenhum departamento encontrado para unidade ");
		}
	} 
}
