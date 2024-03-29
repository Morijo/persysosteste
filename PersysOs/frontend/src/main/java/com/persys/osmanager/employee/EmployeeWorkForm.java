package com.persys.osmanager.employee;


import com.persys.osmanager.componente.helper.AttrDim;
import com.persys.osmanager.componente.interfaces.IForm;
import com.persys.osmanager.employee.data.TransactionsContainerEmployeeData;
import com.persys.osmanager.exception.ViewException;
import com.restmb.RestMBClient;
import com.restmb.types.Funcionario;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class EmployeeWorkForm extends CustomComponent implements IForm<Funcionario>{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout mainLayout;
	
	private ComboBox comboBoxTipoFuncionario;
	private TextField textFieldNomePai;
	private TextField textFieldNomeMae;
	private TextField textFieldNumeroPis;
	private TextField textFieldCtps;
	private TextField textFieldSalarioBase;
	private TextField textFieldCustoFuncionario;
	private TextField textFieldHorasSemanais;
	private TextField textFieldHorasMensal;
	private TextField textFieldHorasDiarias;
	private TextField textFieldValorHora;
	
	private DateField dataAdmissao;
	private DateField dataDemissao;
	
	private ComboBox comboBoxUnidade;
	RestMBClient client;
	
	private Funcionario funcionario = new Funcionario();
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public EmployeeWorkForm(RestMBClient client) {
		this.client = client;
		buildMainLayout();
		setCompositionRoot(mainLayout);
		
		// TODO add user code here
	}

	@Override
	public Funcionario commit() throws ViewException {
		try{
			funcionario.setNumeroPis(textFieldNumeroPis.getValue());
			funcionario.setNomeMae(textFieldNomeMae.getValue());
			funcionario.setNomePai(textFieldNomePai.getValue());
			funcionario.setCtps(textFieldCtps.getValue());
			funcionario.setDataAdmissao(dataAdmissao.getValue());
			funcionario.setDataDemissao(dataDemissao.getValue());
			funcionario.setTipoFuncionario(comboBoxTipoFuncionario.getValue().toString());

			return funcionario;
		}catch(Exception e){
			throw new ViewException(e.getMessage());
		}	
	}

	@Override
	public void initData(Funcionario data) {
		
		textFieldNumeroPis.setValue(data.getNumeroPis());
		textFieldNomeMae.setValue(data.getNomeMae());
		textFieldNomePai.setValue(data.getNomePai());
		textFieldCtps.setValue(data.getCtps());
		dataAdmissao.setValue(data.getDataAdmissao());
		dataDemissao.setValue(data.getDataDemissao());
		comboBoxTipoFuncionario.select(data.getTipoFuncionario());
		funcionario = data;
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		// top-level component properties
		setWidth(AttrDim.FORM_WIDTH);
		setHeight(AttrDim.FORM_HEIGHT);
		
		// horizontalLayout_1
		mainLayout.addComponent( buildHorizontalLayoutTipo());
				
		// textFieldNomePai
		textFieldNomePai = new TextField();
		textFieldNomePai.setCaption("Nome Pai");
		textFieldNomePai.setImmediate(false);
		textFieldNomePai.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldNomePai.setHeight("-1px");
		mainLayout.addComponent(textFieldNomePai);
		
		// textFieldNomePai
		textFieldNomeMae = new TextField();
		textFieldNomeMae.setCaption("Nome m??e");
		textFieldNomeMae.setImmediate(false);
		textFieldNomeMae.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		textFieldNomeMae.setHeight("-1px");
		mainLayout.addComponent(textFieldNomeMae);
	
		mainLayout.addComponent(buildHorizontalLayoutCarteira());
	
		mainLayout.addComponent(buildHorizontalLayoutAdDem());
		
		mainLayout.addComponent(buildHorizontalLayoutSalario());
		
		mainLayout.addComponent(buildHorizontalLayoutHoras());
		
		mainLayout.addComponent(buildHorizontalLayoutValor());
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutTipo() {
		// common part: create layout
		HorizontalLayout horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);
		
		// comboBoxTipoFuncionario
		comboBoxTipoFuncionario = new ComboBox();
		comboBoxTipoFuncionario.setCaption("Tipo de Funcion??rio");
		comboBoxTipoFuncionario.setImmediate(false);
		comboBoxTipoFuncionario.setWidth(AttrDim.FORM_COM_SINGLE_WIDTH);
		comboBoxTipoFuncionario.setHeight("-1px");
		comboBoxTipoFuncionario.setContainerDataSource(TransactionsContainerEmployeeData.listTipoFuncionario());
		comboBoxTipoFuncionario.setNullSelectionAllowed(false);
		horizontalLayout_1.addComponent(comboBoxTipoFuncionario);
		
		return horizontalLayout_1;
	}
	
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutCarteira() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		
		// textFieldCodigo
		textFieldCtps = new TextField();
		textFieldCtps.setCaption("N??mero CTPS");
		textFieldCtps.setImmediate(false);
		textFieldCtps.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCtps.setHeight("-1px");
		horizontalLayout.addComponent(textFieldCtps);
		
		// comboBoxTipoFuncionario
		textFieldNumeroPis = new TextField();
		textFieldNumeroPis.setCaption("N??mero Pis");
		textFieldNumeroPis.setImmediate(false);
		textFieldNumeroPis.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldNumeroPis.setHeight("-1px");
		horizontalLayout.addComponent(textFieldNumeroPis);
		
		
		return horizontalLayout;
	}

	
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutAdDem() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		
		dataAdmissao = new DateField();
		dataAdmissao.setCaption("Data Admiss??o");
		dataAdmissao.setImmediate(false);
		dataAdmissao.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		dataAdmissao.setHeight("-1px");
		horizontalLayout.addComponent(dataAdmissao);
		
		dataDemissao = new DateField();
		dataDemissao.setCaption("Data Demiss??o");
		dataDemissao.setImmediate(false);
		dataDemissao.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		dataDemissao.setHeight("-1px");
		horizontalLayout.addComponent(dataDemissao);
		
	
		return horizontalLayout;
	}
	
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutSalario() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		
		textFieldSalarioBase = new TextField();
		textFieldSalarioBase.setCaption("Sal??rio Base");
		textFieldSalarioBase.setImmediate(false);
		textFieldSalarioBase.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldSalarioBase.setHeight("-1px");
		horizontalLayout.addComponent(textFieldSalarioBase);
		
		textFieldCustoFuncionario = new TextField();
		textFieldCustoFuncionario.setCaption("Custo Final");
		textFieldCustoFuncionario.setImmediate(false);
		textFieldCustoFuncionario.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldCustoFuncionario.setHeight("-1px");
		horizontalLayout.addComponent(textFieldCustoFuncionario);
		
		
		return horizontalLayout;
	}
	
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutHoras() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		
		textFieldHorasDiarias = new TextField();
		textFieldHorasDiarias.setCaption("Horas Di??rias");
		textFieldHorasDiarias.setImmediate(false);
		textFieldHorasDiarias.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldHorasDiarias.setHeight("-1px");
		horizontalLayout.addComponent(textFieldHorasDiarias);
		
		textFieldHorasSemanais = new TextField();
		textFieldHorasSemanais.setCaption("Horas Semanais");
		textFieldHorasSemanais.setImmediate(false);
		textFieldHorasSemanais.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldHorasSemanais.setHeight("-1px");
		horizontalLayout.addComponent(textFieldHorasSemanais);
		
		
		return horizontalLayout;
	}
	
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutValor() {
		// common part: create layout
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setImmediate(false);
		horizontalLayout.setWidth("-1px");
		horizontalLayout.setHeight("-1px");
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		
		textFieldHorasMensal = new TextField();
		textFieldHorasMensal.setCaption("Horas Mensal");
		textFieldHorasMensal.setImmediate(false);
		textFieldHorasMensal.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldHorasMensal.setHeight("-1px");
		horizontalLayout.addComponent(textFieldHorasMensal);
		
		textFieldValorHora = new TextField();
		textFieldValorHora.setCaption("Valor Hora");
		textFieldValorHora.setImmediate(false);
		textFieldValorHora.setWidth(AttrDim.FORM_COM_DOUBLE_WIDTH);
		textFieldValorHora.setHeight("-1px");
		horizontalLayout.addComponent(textFieldValorHora);
		
		
		return horizontalLayout;
	}

	
	@Override
	public void modoView() {
		// TODO Auto-generated method stub
		comboBoxUnidade.setVisible(false);
		comboBoxTipoFuncionario.setVisible(true);
	}

	@Override
	public void modoAdd() {	}
	    
}
