package br.com.eventos.model;

import java.io.Serializable;
import java.util.Date;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Type;

import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.rest.represention.JsonDateAdapter;

import com.sun.xml.bind.CycleRecoverable;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="ponto")
public class Ponto extends Model<Ponto> implements Serializable, CycleRecoverable{

	private static final long serialVersionUID = 1L;

	public static int geoSRID = 4326;

	@Column(columnDefinition="Geometry(Point,4326)")
	@Type(type = "org.hibernate.spatial.GeometryType")
	private Point location;

	@Column(name="velocidade")
	private Double velocidade = null;

	@XmlJavaTypeAdapter(value=JsonDateAdapter.class)
	@Column(name = "dataregistro")
	private Date  dataRegistro = null;

	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "eventoidpk", referencedColumnName="id"),
	})
	private Evento evento = null;

	public Ponto() {
		super(Ponto.class);
	}
	
	public Double getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(Double velocidade) {
		this.velocidade = velocidade;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}

	@Override
	public void valida() throws ModelException {
		//PreconditionsModel.checkNotNull(location, "location not found");
	}


	public static Point criarPonto(double x, double y) {

		Point ponto = null;
		GeometryFactory gf = null;

		try {

			gf = new GeometryFactory(new PrecisionModel(PrecisionModel.maximumPreciseValue), geoSRID);
			ponto = gf.createPoint(new Coordinate(x, y));
			ponto.setSRID(geoSRID);
		} catch (Exception ex) {
			ponto = null;
		} finally {
			gf = null;
		}

		return ponto;
	}
}
