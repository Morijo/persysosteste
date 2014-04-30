package br.com.funcionario.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.exception.ModelException;
import br.com.model.Model;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="rastreador")
public class Rastreador extends Model<Rastreador> {

	@Column(name = "rastreador")
	private Boolean  rastreador = true;
	
	@Column(name = "distanciaIntervalo")
	private Long    distanciaIntervalo = 2000l;
	
	@Column(name = "tempoIntervalo")
	private Long    tempoIntervalo = 6000l;
	
	@Column(name = "precisao")
	private Integer precisao = 1;
	
	@Column(name = "monitorarPercurso")
	private Boolean monitorarPercurso = false;
	
	@Column(name = "monitorarAusente")
	private Boolean monitorarAusente = false;
	
	@Column(name = "monitorarAtendimento")
	private Boolean monitorarAtendimento = false;
	
	public Rastreador(){
		super(Rastreador.class);
	}

	public Boolean getStatus() {
		return rastreador;
	}

	public void setStatus(Boolean status) {
		this.rastreador = status;
	}

	public Long getDistanciaIntervalo() {
		return distanciaIntervalo;
	}

	public void setDistanciaIntervalo(Long distanciaIntervalo) {
		this.distanciaIntervalo = distanciaIntervalo;
	}

	public Long getTempoIntervalo() {
		return tempoIntervalo;
	}

	public void setTempoIntervalo(Long tempoIntervalo) {
		this.tempoIntervalo = tempoIntervalo;
	}

	public Integer getPrecisao() {
		return precisao;
	}

	public void setPrecisao(Integer precisao) {
		this.precisao = precisao;
	}

	public Boolean getMonitorarPercurso() {
		return monitorarPercurso;
	}

	public void setMonitorarPercurso(Boolean monitorarPercurso) {
		this.monitorarPercurso = monitorarPercurso;
	}

	public Boolean getMonitorarAusente() {
		return monitorarAusente;
	}

	public void setMonitorarAusente(Boolean monitorarAusente) {
		this.monitorarAusente = monitorarAusente;
	}

	public Boolean getMonitorarAtendimento() {
		return monitorarAtendimento;
	}

	public void setMonitorarAtendimento(Boolean monitorarAtendimento) {
		this.monitorarAtendimento = monitorarAtendimento;
	}

	@Override
	public void valida() throws ModelException {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
