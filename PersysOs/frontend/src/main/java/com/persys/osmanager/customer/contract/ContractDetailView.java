package com.persys.osmanager.customer.contract;

import br.com.principal.helper.FormatDateHelper;

import com.persys.osmanager.componente.helper.AttrDim;
import com.restmb.types.Contrato;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ContractDetailView extends VerticalLayout  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label labelInfoNome;
	public Button cancelar;

	private Contrato contrato;
	
	@SuppressWarnings("deprecation")
	public ContractDetailView(final Contrato contrato) {

		this.contrato = contrato;
		
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

		String situacao = (contrato.getSituacao().isEmpty()) ? "": contrato.getSituacao();
		String dataAssinatura = (contrato.getDataAssinatura() == null) ? ""
				: FormatDateHelper.formatTimeZoneBRDATE(contrato.getDataAssinatura().getTime());
	
		String vigencia = (contrato.getDataVigenciaInicio() == null) ? ""
				: FormatDateHelper.formatTimeZoneBRDATE(contrato.getDataVigenciaInicio().getTime());
	
		vigencia +=vigencia +" - "+  ((contrato.getDataVigenciaFim() == null) ? ""
				: FormatDateHelper.formatTimeZoneBRDATE(contrato.getDataVigenciaFim().getTime()));
		
		String info = 	"<b>"+contrato.getCodigo()+" "+situacao+"</b>"
						+ "<p> Data Assinatura "+dataAssinatura+"</p>"
						+ "<p> Vigência: "+vigencia+"</p>";

		return info;
	}

	/**
	 * @return the clietne
	 */
	public Contrato getContrato() {
		return contrato;
	}

	/**
	 * @param contatoCliente the contatoCliente to set
	 */
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
		labelInfoNome.setValue(geraInfo());
		this.setData(contrato);
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
