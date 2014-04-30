package br.com.rest.hateoas.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import br.com.rest.represention.JsonDateAdapter;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class EventoDTO {

	private Long  id = null;
	
	private String mensagem = null;
	
	private Double latitudeInicio = null;
	
	private Double longitudeInicio = null;
	
	private Double latitudeFim = null;
	
	private Double longitudeFim = null;
	
	@XmlJavaTypeAdapter(value=JsonDateAdapter.class)
	private Date  dataInicio = null;
	
	@XmlJavaTypeAdapter(value=JsonDateAdapter.class)
	private Date  dataFim = null;
	
	private String nomeUsuario;
	
	private Long   idUsuario;
	
	private String tipoEvento;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	@Override
	public String toString() {
		return "EventoDTO [id=" + id + ", mensagem=" + mensagem
				+ ", latitudeInicio=" + latitudeInicio + ", longitudeInicio="
				+ longitudeInicio + ", latitudeFim=" + latitudeFim
				+ ", longitudeFim=" + longitudeFim + ", dataInicio="
				+ dataInicio + ", dataFim=" + dataFim + ", nomeUsuario="
				+ nomeUsuario + ", idUsuario=" + idUsuario + ", tipoEvento="
				+ tipoEvento + "]";
	}
	
	
	
	
}
