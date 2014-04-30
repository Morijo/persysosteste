package com.restmb.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.restmb.RestMB;

public class Evento extends RestMbType<Evento>{
	
	//@RestMB("tipoEvento")
	private TipoEvento tipoEvento = null;
	
	@RestMB("mensagem")
	private String mensagem = null;
	
	@RestMB("latitudeInicio")
	private Double latitudeInicio = null;
	
	@RestMB("longitudeInicio")
	private Double longitudeInicio = null;
	
	@RestMB("latitudeFim")
	private Double latitudeFim = null;
	
	@RestMB("longitudeFim")
	private Double longitudeFim = null;

	@RestMB("dataInicio")
	private Date  dataInicio = null;
	
	@RestMB("dataFim")
	private Date  dataFim = null;
	
	@RestMB("acaoFator")
	private String acaoFator = null;
	
	@RestMB
	private Usuario<?> usuario = null;
	
	@RestMB("ponto")
	private List<Ponto> ponto = new ArrayList<Ponto>();
	
	public Evento(String resourcePath, Class<Evento> paClass) {
		super(resourcePath, paClass);
		// TODO Auto-generated constructor stub
	}
	
	public Evento() {
		super("evento", Evento.class);
	}
	
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public Double getLatitudeInicio() {
		return latitudeInicio;
	}
	public void setLatitudeInicio(Double latitudeInicio) {
		this.latitudeInicio = latitudeInicio;
	}
	public Double getLongitudeInicio() {
		return longitudeInicio;
	}
	public void setLongitudeInicio(Double longitudeInicio) {
		this.longitudeInicio = longitudeInicio;
	}
	public Double getLatitudeFim() {
		return latitudeFim;
	}
	public void setLatitudeFim(Double latitudeFim) {
		this.latitudeFim = latitudeFim;
	}
	public Double getLongitudeFim() {
		return longitudeFim;
	}
	public void setLongitudeFim(Double longitudeFim) {
		this.longitudeFim = longitudeFim;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public String getAcaoFator() {
		return acaoFator;
	}

	public void setAcaoFator(String acaoFator) {
		this.acaoFator = acaoFator;
	}

	public Usuario<?> getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario<?> usuario) {
		this.usuario = usuario;
	}

	public List<Ponto> getPonto() {
		return ponto;
	}
	public void setPonto(List<Ponto> ponto) {
		this.ponto = ponto;
	}
	
	
	
}

