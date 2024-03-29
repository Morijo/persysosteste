package com.persys.osmanager.order;

import java.util.Date;
import java.util.ResourceBundle;

import org.vaadin.csvalidation.CSValidator;

import br.com.principal.helper.FormatDateHelper;

import com.persys.osmanager.componente.DialogWindow;
import com.persys.osmanager.componente.FormViewWindow;
import com.persys.osmanager.componente.SearchFormWindow;
import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IFormWindows;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.contact.ContactDetalheView;
import com.persys.osmanager.contact.ContactWindow;
import com.persys.osmanager.customer.CustomerDetailView;
import com.persys.osmanager.customer.CustomerMenu;
import com.persys.osmanager.customer.contract.ContractForm;
import com.persys.osmanager.customer.data.TransactionsContainerContractCustomer;
import com.persys.osmanager.customer.data.TransactionsContainerCustomer;
import com.persys.osmanager.data.AppData;
import com.persys.osmanager.exception.ViewException;
import com.persys.osmanager.order.data.TransactionsContainerBaseConhecimentoData;
import com.persys.osmanager.order.data.TransactionsContainerOrdemServicoData;
import com.persys.osmanager.order.data.TransactionsContainerPrioridadeData;
import com.persys.osmanager.order.data.TransactionsContainerSituacaoData;
import com.persys.osmanager.organization.DepartamentoForm;
import com.persys.osmanager.organization.UnidadeForm;
import com.persys.osmanager.organization.data.TransactionsContainerDepartamento;
import com.persys.osmanager.organization.data.TransactionsContainerUnidade;
import com.restmb.Parameter;
import com.restmb.RestMBClient;
import com.restmb.types.BaseConhecimento;
import com.restmb.types.Cliente;
import com.restmb.types.Contato;
import com.restmb.types.Contrato;
import com.restmb.types.Departamento;
import com.restmb.types.OrdemServico;
import com.restmb.types.Prioridade;
import com.restmb.types.SituacaoOrdem;
import com.restmb.types.Unidade;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/** 
@author Ricardo Sabatine
@version Revision 1.2

Form para a ordem de servico

@since 07/10/2013
 */
public class OrdemForm extends CustomComponent implements IFormWindows<OrdemServico>{

	private static final long serialVersionUID = 1L;

	private ComboBox comboBoxStatus;
	private ComboBox comboBoxTipoSituacao;
	private ComboBox comboBoxFonte;
	private ComboBox comboBoxPrioridade;
	private ComboBox comboBoxUnidade;
	private ComboBox comboBoxDepartamento;
	private ComboBox comboBoxContrato;

	private CustomerDetailView customerDetailView;
	private ContactDetalheView contactDetalheView;

	private Button   buttonCliente;
	private Button buttonCancelarAgendamento = null;
	private Button buttonNovoAgendamento = null;

	private DateField dataAbertura;
	private DateField dataConclusao;
	private DateField dataTransferencia;

	private TextField textFieldCodigo;
	private TextArea textFieldAssunto;
	private TextArea textFieldInfoAdicional;

	private OrdemFormTabView abas;
	private RestMBClient client = null;
	private ResourceBundle bundle = null;

	private Cliente cliente = null;
	private Contrato contrato = null;

	private Panel panelAgendamento = new Panel();

	private SearchFormWindow<Cliente> clienteClienteFormWindows = null;
	private ContactWindow contactWindow = null;

	private static int RESULT_TAG_CUSTOMER = 0;
	private static int RESULT_TAG_CONTACT = 1;

	private OrdemServico ordemServico = null;

	private HorizontalLayout horizontalLayoutContrato;

	public OrdemForm(RestMBClient client, ResourceBundle bundle) {
		this.client = client;
		this.bundle = bundle;

		setWidth(AttrDim.FORM_WIDTH);
		setHeight(AttrDim.FORM_HEIGHT);

		setCompositionRoot(buildMainLayout());
	}

	private VerticalLayout buildMainLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		inicializaComponente(mainLayout,"100%","100%",false);

		mainLayout.addComponent(buildPanelDadosGerais());
		mainLayout.addComponent(buildPanelAgendamento());
		mainLayout.addComponent(buildPanelCliente());
		mainLayout.addComponent(buildPanelAssunto());
		mainLayout.addComponent(buildPanelDadosSituacao());
		mainLayout.addComponent(buildHorizontalLayoutAbas());

		return mainLayout;
	}

	private Panel buildPanelDadosGerais() {

		Panel panelDadosGerais = new Panel();
		panelDadosGerais.setWidth(AttrDim.PANEL_COM_SINGLE_WIDTH);
		panelDadosGerais.setCaption("Dados Gerais");

		VerticalLayout panelLayout = new VerticalLayout();
		inicializaComponente(panelLayout,"100%","100%",true);

		panelDadosGerais.setContent(panelLayout);

		panelLayout.addComponent(buildHorizontalLayoutCodigo());
		panelLayout.addComponent(buildHorizontalLayoutDatas());
		panelLayout.addComponent(buildHorizontalLayoutFonte());

		return panelDadosGerais;
	}

	private HorizontalLayout buildHorizontalLayoutCodigo() {
		HorizontalLayout horizontalLayout_1 = createHorizontalLayout();

		// textFieldCodigo
		textFieldCodigo = new TextField();
		textFieldCodigo.setCaption(bundle.getString("codigo"));
		textFieldCodigo.setImmediate(false);
		textFieldCodigo.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCodigo.setHeight("-1px");
		horizontalLayout_1.addComponent(textFieldCodigo);

		comboBoxStatus = createComboBox("situacao");
		comboBoxStatus.setContainerDataSource(AppData.listStatusInteger());
		comboBoxStatus.setItemCaptionPropertyId("statusNome");

		horizontalLayout_1.addComponent(comboBoxStatus);

		return horizontalLayout_1;
	}


	private HorizontalLayout buildHorizontalLayoutDatas() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		dataAbertura = new DateField();
		dataAbertura.setCaption(bundle.getString("dataabertura"));
		dataAbertura.setImmediate(false);
		dataAbertura.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		dataAbertura.setHeight("-1px");
		horizontalLayout.addComponent(dataAbertura);

		dataConclusao = new DateField();
		dataConclusao.setCaption(bundle.getString("dataconclusao"));
		dataConclusao.setImmediate(false);
		dataConclusao.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		dataConclusao.setHeight("-1px");
		horizontalLayout.addComponent(dataConclusao);

		return horizontalLayout;
	}

	private Panel buildPanelAgendamento() {

		panelAgendamento.setWidth(AttrDim.PANEL_COM_SINGLE_WIDTH);
		panelAgendamento.setCaption("Agendamento");

		VerticalLayout panelLayout = new VerticalLayout();
		inicializaComponente(panelLayout,"100%","100%",true);

		panelAgendamento.setContent(panelLayout);
		panelLayout.addComponent(buildHorizontalLayoutDataAgendamento());

		return panelAgendamento;
	}

	private HorizontalLayout buildHorizontalLayoutDataAgendamento() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		buttonNovoAgendamento = new Button("Agendar");
		buttonNovoAgendamento.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		buttonNovoAgendamento.addStyleName("wide");
		buttonNovoAgendamento.addStyleName("default");
		buttonNovoAgendamento.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window window = new Window();
				getUI().addWindow(window);
				window.addCloseListener(new CloseListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
					}
				});
			}
		});
		horizontalLayout.addComponent(buttonNovoAgendamento);
		horizontalLayout.setComponentAlignment(buttonNovoAgendamento, Alignment.BOTTOM_RIGHT);

		buttonCancelarAgendamento = new Button("Cancelar");
		buttonCancelarAgendamento.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		buttonCancelarAgendamento.addStyleName("remove");
		buttonCancelarAgendamento.addStyleName("default");
		buttonCancelarAgendamento.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				DialogWindow.messageSucess(getUI(),"Deseja Cancelar","","Remover","Cancelar",false,true,true,new IMessage() {

					@Override
					public void ok() {
						try{
							ordemServico.cancelarAgendamento(client);
							ordemServico.setDataAgendamento(null);
							ordemServico.setDataAgendamentoInicio(null);
							ordemServico.setDataAgendamentoFim(null);
							ordemServico.setAgendada(false);
							loadAgendamento(false);
						}catch(Exception e){
							Notification.show("Tente novamente");
						}
					}

					@Override
					public void discard() {}

					@Override
					public void cancel() {}
				});
			}
		});
		horizontalLayout.addComponent(buttonCancelarAgendamento);
		horizontalLayout.setComponentAlignment(buttonCancelarAgendamento, Alignment.BOTTOM_RIGHT);

		return horizontalLayout;
	}

	private HorizontalLayout buildHorizontalLayoutFonte() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		dataTransferencia = new DateField();
		dataTransferencia.setCaption(bundle.getString("datatransferencia"));
		dataTransferencia.setImmediate(false);
		dataTransferencia.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		dataTransferencia.setHeight("-1px");
		horizontalLayout.addComponent(dataTransferencia);

		comboBoxFonte = createComboBox("fonte");
		comboBoxFonte.setContainerDataSource(TransactionsContainerOrdemServicoData.listFonteOrdem());
		horizontalLayout.addComponent(comboBoxFonte);

		return horizontalLayout;
	}

	private Panel buildPanelAssunto() {
		Panel panelBaseConhecimento = new Panel();
		panelBaseConhecimento.setWidth(AttrDim.PANEL_COM_SINGLE_WIDTH);
		panelBaseConhecimento.setCaption(bundle.getString("assunto"));

		VerticalLayout verticalLayout = new VerticalLayout();
		inicializaComponente(verticalLayout, "-1px", "-1px",true);
		panelBaseConhecimento.setContent(verticalLayout);

		final ComboBox comboBoxBaseConhecimento = new ComboBox(bundle.getString("baseconehcimento"));
		comboBoxBaseConhecimento.setImmediate(true);
		comboBoxBaseConhecimento.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		comboBoxBaseConhecimento.setContainerDataSource(TransactionsContainerBaseConhecimentoData.list(client));
		verticalLayout.addComponent(comboBoxBaseConhecimento);

		textFieldAssunto = new TextArea();
		textFieldAssunto.setCaption(bundle.getString("assunto"));
		textFieldAssunto.setImmediate(true);
		textFieldAssunto.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldAssunto.setHeight("-1px");
		textFieldAssunto.setRequiredError(bundle.getString("campoobrigaotrio"));
		CSValidator validator = new CSValidator();
		validator.extend(textFieldAssunto);
		validator.setJavaScript("if (value.length <= 0 ) \"Campo inválido\";");
		validator.setValidateInitialEmpty(true);
		validator.setErrorMessage(bundle.getString("campoobrigaotrio"));
		verticalLayout.addComponent(textFieldAssunto);

		comboBoxBaseConhecimento.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseConhecimento baseConhecimento = (BaseConhecimento) comboBoxBaseConhecimento.getValue();
				if(baseConhecimento != null && baseConhecimento.getTitulo() != null)
					baseConhecimento = baseConhecimento.pesquisa(client,baseConhecimento.getId());
				textFieldAssunto.setValue(textFieldAssunto.getValue() + " " +baseConhecimento.getMensagem());
			}
		});

		return panelBaseConhecimento;
	}

	private Panel buildPanelCliente() {

		Panel panelCliente = new Panel();
		panelCliente.setWidth(AttrDim.PANEL_COM_SINGLE_WIDTH);
		panelCliente.setCaption(bundle.getString("cliente")+"/"+bundle.getString("contrato"));

		VerticalLayout verticalLayout = new VerticalLayout();
		inicializaComponente(verticalLayout, "-1px", "-1px", true);
		panelCliente.setContent(verticalLayout);

		buttonCliente = new Button(bundle.getString("novocliente"));
		buttonCliente.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		buttonCliente.setVisible(true);
		buttonCliente.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				loadCliente();
			}
		});
		verticalLayout.addComponent(buttonCliente);

		customerDetailView = new CustomerDetailView(new Cliente());
		customerDetailView.cancelar.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				if(customerDetailView.isVisible()){
					customerDetailView.setVisible(false);
					buttonCliente.setVisible(true);
					contactDetalheView.setVisible(false);
				}
			}
		});

		customerDetailView.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				viewCliente();
			}		
		});
		customerDetailView.setVisible(false);
		verticalLayout.addComponent(customerDetailView);

		verticalLayout.addComponent(buildHorizontalLayoutContrato());

		contactDetalheView = new ContactDetalheView(new Contato());
		contactDetalheView.setVisible(false);
		contactDetalheView.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 1L;

			public void layoutClick(LayoutClickEvent event) {
				loadEndereco();
				contactWindow.initData(contactDetalheView.getContato());
			}
		});
		verticalLayout.addComponent(contactDetalheView);

		return panelCliente;
	}

	private HorizontalLayout buildHorizontalLayoutContrato() {

		horizontalLayoutContrato = createHorizontalLayout();
		horizontalLayoutContrato.setSpacing(false);
		horizontalLayoutContrato.setVisible(false);

		comboBoxContrato = new ComboBox();
		comboBoxContrato.setImmediate(true);
		comboBoxContrato.setCaption(bundle.getString("contrato"));
		comboBoxContrato.setImmediate(true);
		comboBoxContrato.setWidth(AttrDim.FORM_COM_SINGLE_ICON_WIDTH);
		comboBoxContrato.setHeight("-1px");
		comboBoxContrato.setNullSelectionAllowed(false);
		comboBoxContrato.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				try{
					contrato = ((Contrato)comboBoxContrato.getValue());
					contrato = (Contrato)contrato.pesquisa(client, contrato.getId());
					ordemServico.setClienteObjeto(contrato);
					setLayoutContact(contrato.getContato());
				}catch(Exception e){}
			} 
		});
		horizontalLayoutContrato.addComponent(comboBoxContrato);

		Button buttonNovoContrato = new Button();
		createButtonPlus(buttonNovoContrato);
		buttonNovoContrato.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ContractForm contractForm = new ContractForm(client);
				Contrato contrato = new Contrato();
				contrato.setCliente(cliente);
				contractForm.initData(contrato);
				contractForm.modoAdd();
				final FormViewWindow<Contrato> window = new FormViewWindow<Contrato>(client, "Novo Contrato", contractForm, new Contrato());
				window.setWidth("700px");
				window.setHeight("600px");
				window.setModal(true);
				window.center();
				getUI().addWindow(window);
				window.addCloseListener(new CloseListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						window.commit();
					}
				});
			}
		});
		horizontalLayoutContrato.addComponent(buttonNovoContrato);
		horizontalLayoutContrato.setComponentAlignment(buttonNovoContrato, Alignment.BOTTOM_CENTER);

		return horizontalLayoutContrato;
	}

	@AutoGenerated
	private Panel buildPanelDadosSituacao() {

		Panel panelDadosGerais = new Panel();
		panelDadosGerais.setWidth(AttrDim.PANEL_COM_SINGLE_WIDTH);
		panelDadosGerais.setCaption("");

		VerticalLayout panelLayout = new VerticalLayout();
		inicializaComponente(panelLayout, "100%", "100%",true);

		panelDadosGerais.setContent(panelLayout);

		panelLayout.addComponent(buildHorizontalLayoutSituacao());
		panelLayout.addComponent(buildHorizontalLayoutUnidadeDepartamento());

		textFieldInfoAdicional = new TextArea();
		textFieldInfoAdicional.setCaption(bundle.getString("infoadicional"));
		textFieldInfoAdicional.setImmediate(true);
		textFieldInfoAdicional.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldInfoAdicional.setHeight("-1px");
		panelLayout.addComponent(textFieldInfoAdicional);

		return panelDadosGerais;
	}


	@SuppressWarnings("deprecation")
	private HorizontalLayout buildHorizontalLayoutSituacao() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		HorizontalLayout horizontalLayoutSituacao = createHorizontalLayout();
		horizontalLayoutSituacao.setSpacing(false);

		comboBoxTipoSituacao = createComboBox("statusordem");
		loadSituacao();
		comboBoxTipoSituacao.setWidth(AttrDim.FORM_COM_DOUBLE_ICON_WIDTH);

		horizontalLayoutSituacao.addComponent(comboBoxTipoSituacao);

		Button buttonNovoSituacao = new Button();
		createButtonPlus(buttonNovoSituacao);
		buttonNovoSituacao.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window window = new FormViewWindow<SituacaoOrdem>(client, "Nova Situação", new SituacaoForm(), new SituacaoOrdem());
				getUI().addWindow(window);
				window.addCloseListener(new CloseListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						loadSituacao();
					}
				});
			}
		});

		horizontalLayoutSituacao.addComponent(buttonNovoSituacao);
		horizontalLayoutSituacao.setComponentAlignment(buttonNovoSituacao, Alignment.BOTTOM_CENTER);

		HorizontalLayout horizontalLayoutPrioridade = createHorizontalLayout();
		horizontalLayoutPrioridade.setSpacing(false);

		comboBoxPrioridade = createComboBox("prioridade");
		loadPrioridade();
		comboBoxPrioridade.setWidth(AttrDim.FORM_COM_DOUBLE_ICON_WIDTH);

		horizontalLayoutPrioridade.addComponent(comboBoxPrioridade);

		Button buttonNovoPrioridade = new Button();
		createButtonPlus(buttonNovoPrioridade);
		buttonNovoPrioridade.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Window window = new FormViewWindow(client,"Nova Prioridade", new PrioridadeForm(), new Prioridade());
				getUI().addWindow(window);
				window.addCloseListener(new CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						loadPrioridade();
					}
				});
			}
		});

		horizontalLayoutPrioridade.addComponent(buttonNovoPrioridade);
		horizontalLayoutPrioridade.setComponentAlignment(buttonNovoPrioridade, Alignment.BOTTOM_CENTER);
		horizontalLayout.addComponent(horizontalLayoutSituacao);
		horizontalLayout.addComponent(horizontalLayoutPrioridade);

		return horizontalLayout;
	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildHorizontalLayoutUnidadeDepartamento() {
		HorizontalLayout horizontalLayout = createHorizontalLayout();

		HorizontalLayout horizontalLayoutUnidade = createHorizontalLayout();
		horizontalLayoutUnidade.setSpacing(false);

		comboBoxUnidade = new ComboBox();
		comboBoxUnidade.setCaption(bundle.getString("unidade"));
		comboBoxUnidade.setImmediate(true);
		comboBoxUnidade.setWidth(AttrDim.FORM_COM_DOUBLE_ICON_WIDTH);
		comboBoxUnidade.setHeight("-1px");
		comboBoxUnidade.setContainerDataSource(TransactionsContainerUnidade.listUnidade(client));
		comboBoxUnidade.setNullSelectionAllowed(false);
		comboBoxUnidade.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				comboBoxDepartamento.removeAllItems();
				Unidade unidade = ((Unidade)comboBoxUnidade.getValue());
				comboBoxDepartamento.setContainerDataSource(TransactionsContainerDepartamento.loadBean(client, "/departamento/unidade/"+unidade.getId()));
			}
		});

		horizontalLayoutUnidade.addComponent(comboBoxUnidade);

		Button buttonNovoUnidade = new Button();
		createButtonPlus(buttonNovoUnidade);
		buttonNovoUnidade.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window window = new FormViewWindow<Unidade>(client, "Nova Unidade", new UnidadeForm(), new Unidade());
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
		comboBoxDepartamento.setCaption(bundle.getString("departamento"));
		comboBoxDepartamento.setImmediate(false);
		comboBoxDepartamento.setWidth(AttrDim.FORM_COM_DOUBLE_ICON_WIDTH);
		comboBoxDepartamento.setHeight("-1px");
		comboBoxDepartamento.setNullSelectionAllowed(false);
		horizontalLayoutDepartamento.addComponent(comboBoxDepartamento);

		Button buttonNovoDepartamento = new Button();
		createButtonPlus(buttonNovoDepartamento);

		buttonNovoDepartamento.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window window = new FormViewWindow<Departamento>(client, "Novo Departamento", 
						new DepartamentoForm(client), new Departamento());
				getUI().addWindow(window);
				window.addCloseListener(new CloseListener() {
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

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutAbas() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);

		abas = new OrdemFormTabView();
		horizontalLayout.addComponent(abas);

		return horizontalLayout;
	}

	private void setLayoutCustomer() {
		customerDetailView.setCliente(cliente);
		customerDetailView.setVisible(true);
		buttonCliente.setVisible(false);
		horizontalLayoutContrato.setVisible(true);
	}

	private void setLayoutContact(Contato contato) {
		ordemServico.setContato(contato);
		contactDetalheView.setContatoEndereco(contato);
		contactDetalheView.setWidth("100%");
		contactDetalheView.setVisible(true);
	}

	private ComboBox createComboBox(String caption) {
		ComboBox comboBox = new ComboBox();

		if(caption !=null)
			comboBox.setCaption(bundle.getString(caption));

		comboBox.setImmediate(false);
		comboBox.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		comboBox.setHeight("-1px");
		comboBox.setNullSelectionAllowed(false);
		comboBox.setRequiredError(bundle.getString("campoobrigaotrio")+" "+ comboBox.getCaption());
		comboBox.setRequired(true);
		return comboBox;
	}

	private HorizontalLayout createHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		inicializaComponente(horizontalLayout, "-1px", "-1px",false);
		return horizontalLayout;
	}


	private void createButtonPlus(Button buttonNovoUnidade) {
		buttonNovoUnidade.setIcon(new ThemeResource("../reindeer/Icons/plus.png"));
		buttonNovoUnidade.setImmediate(true);
		buttonNovoUnidade.setWidth("30px");
		buttonNovoUnidade.setHeight("-1px");
		buttonNovoUnidade.addStyleName("newicon");
	}

	protected void viewCliente() {
		Window window = new Window(cliente.getFantasiaSobrenome());
		window.setWidth("1000px");
		window.setHeight("600px");
		window.setModal(true);
		window.center();
		CustomerMenu customerMenu = new CustomerMenu(client);
		customerMenu.initData(this.cliente);
		customerMenu.modoView();
		window.setContent(customerMenu);

		getUI().addWindow(window);
	}

	private void inicializaComponente(AbstractOrderedLayout mainLayout, String width, String height, boolean margin) {
		mainLayout.setImmediate(false);
		mainLayout.setWidth(width);
		mainLayout.setHeight(height);
		mainLayout.setMargin(margin);
		mainLayout.setSpacing(true);
	}

	@Override
	public OrdemServico commit() throws ViewException {
		try{
			comboBoxFonte.validate();
			comboBoxContrato.validate();
			comboBoxDepartamento.validate();
			comboBoxPrioridade.validate();
			comboBoxStatus.validate();
			comboBoxTipoSituacao.validate();
			comboBoxUnidade.validate();
			textFieldAssunto.validate();

		}catch(Exception e){
			throw new ViewException(e.getMessage());
		}

		ordemServico.setAssunto(textFieldAssunto.getValue().toString());
		ordemServico.setCodigo(textFieldCodigo.getValue().toString());
		ordemServico.setDataConclusao(dataConclusao.getValue());
		ordemServico.setDataTransferencia(dataTransferencia.getValue());
		ordemServico.setDataCriacao(dataAbertura.getValue());
		ordemServico.setFonte((String)comboBoxFonte.getValue());
		ordemServico.setSituacaoOrdem((SituacaoOrdem)comboBoxTipoSituacao.getValue());
		ordemServico.setPrioridade((Prioridade)comboBoxPrioridade.getValue());
		ordemServico.setStatusModel((Integer)comboBoxStatus.getValue());
		ordemServico.setDepartamento((Departamento)comboBoxDepartamento.getValue());
		ordemServico.setInformacaoAdicional(textFieldInfoAdicional.getValue());
		return ordemServico;
	}

	@Override
	public void initData(OrdemServico data) {

		ordemServico = data;

		comboBoxStatus.select(this.ordemServico.getStatusModel());

		if(data.getDataCriacao() != null)
			dataAbertura.setValue(this.ordemServico.getDataCriacao());
		else dataAbertura.setValue(new Date());

		dataAbertura.setReadOnly(true);

		textFieldAssunto.setValue(ordemServico.getAssunto());
		textFieldCodigo.setValue(ordemServico.getCodigo());
		dataConclusao.setValue(ordemServico.getDataConclusao());
		dataTransferencia.setValue(ordemServico.getDataTransferencia());
		comboBoxFonte.select(ordemServico.getFonte());
		if(ordemServico.getInformacaoAdicional() == null)
			textFieldInfoAdicional.setValue("");
		else
			textFieldInfoAdicional.setValue(ordemServico.getInformacaoAdicional());

		if(ordemServico.getClienteObjeto() != null){
			contrato =  Contrato.pesquisa(client, "contrato/"+ordemServico.getClienteObjeto().getId(), Contrato.class, Parameter.with("status", 2));
			cliente = (Cliente) contrato.getCliente();
			customerDetailView.setCliente(cliente);
			customerDetailView.setVisible(true);
			setLayoutContact(ordemServico.getContato());
			buttonCliente.setVisible(false);

			loadContrato();
			comboBoxContrato.addItem(contrato);
			comboBoxContrato.select(contrato);

			comboBoxContrato.setVisible(true);
			horizontalLayoutContrato.setVisible(true);

			comboBoxUnidade.select((Unidade) ordemServico.getDepartamento().getUnidade());
			comboBoxDepartamento.select((Departamento) ordemServico.getDepartamento());
		}	

		comboBoxPrioridade.select(ordemServico.getPrioridade());
		comboBoxStatus.select(ordemServico.getStatusModel());
		comboBoxTipoSituacao.select(ordemServico.getSituacaoOrdem());

		loadAgendamento(ordemServico.getAgendada());
	}

	@Override
	public void commitWindows(int resultTag) throws ViewException {
		if(resultTag == RESULT_TAG_CUSTOMER){
			try{
				cliente = clienteClienteFormWindows.commit();
				setLayoutCustomer();
				loadContrato();
			}catch(Exception e){}
		}if(resultTag == RESULT_TAG_CONTACT){
			Contato contato = contactWindow.commit();
			contrato.setContato(contato);
			setLayoutContact(contato);
			ordemServico.adicionarContato(client, contato);
		}	
	}

	@Override
	public void modoView() {
		abas.setVisible(true);
		abas.createTab(client, ordemServico);
		panelAgendamento.setVisible(true);
		customerDetailView.cancelar.setVisible(false);
	}

	@Override
	public void modoAdd() {
		textFieldCodigo.setReadOnly(true);
		abas.setVisible(false);
		panelAgendamento.setVisible(false);
	}

	private void loadAgendamento(Boolean agendamento){
		if(agendamento){
			panelAgendamento.setCaption("Agendamento realizado: " + FormatDateHelper.formatTimeZoneUSToBR(ordemServico.getDataAgendamento().getTime()));
			buttonCancelarAgendamento.setVisible(true);
			buttonNovoAgendamento.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
			buttonNovoAgendamento.setCaption(FormatDateHelper.formatTimeZoneUSToBR(ordemServico.getDataAgendamentoInicio().getTime()) +" : " + FormatDateHelper.formatTimeZoneUSToBR(ordemServico.getDataAgendamentoFim().getTime()));
		}else{
			buttonNovoAgendamento.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
			buttonNovoAgendamento.setCaption("Agendar");
			buttonCancelarAgendamento.setVisible(false);
		}
	}

	private void loadContrato() {
		comboBoxContrato.getContainerDataSource().removeAllItems();
		comboBoxContrato.setContainerDataSource(TransactionsContainerContractCustomer.loadBean(client,cliente.getId()));
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
			Notification.show("Nenhum departamento encontrado para unidade");
		}
	} 

	public void loadEndereco(){
		contactWindow = new ContactWindow(client, cliente, this,RESULT_TAG_CONTACT);
		getUI().addWindow(contactWindow);
	}

	public void loadCliente(){
		clienteClienteFormWindows = new SearchFormWindow<Cliente>(client, this, RESULT_TAG_CUSTOMER,"cliente/busca",Cliente.class,
				new TransactionsContainerCustomer(),"Codigo","Nome");
		clienteClienteFormWindows.initData("");
		getUI().addWindow(clienteClienteFormWindows);
	}

	private void loadSituacao() {
		try {
			comboBoxTipoSituacao.setContainerDataSource(TransactionsContainerSituacaoData.listSituacaoOrdem(client));
		} catch (ViewException e1) {
			comboBoxTipoSituacao.setInputPrompt(e1.getMessage());
		}
	}

	private void loadPrioridade() {
		try {
			comboBoxPrioridade.setContainerDataSource(TransactionsContainerPrioridadeData.listPrioridadeOrdem(client));
		} catch (ViewException e1) {
			comboBoxPrioridade.setInputPrompt(e1.getMessage());
		}
	}
}
