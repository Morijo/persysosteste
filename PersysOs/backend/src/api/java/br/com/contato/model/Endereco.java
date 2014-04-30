package br.com.contato.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import com.sun.xml.bind.CycleRecoverable;
import br.com.contato.dao.EnderecoDAO;
import br.com.exception.DAOException;
import br.com.exception.ModelException;
import br.com.googlemaps.GeocodedLocation;
import br.com.googlemaps.GeocodingException;
import br.com.googlemaps.GoogleGeocoder;
import br.com.model.Model;
import br.com.model.PreconditionsModel;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="endereco")
public class Endereco extends Model<Endereco>  implements Serializable , CycleRecoverable, br.com.model.interfaces.IEndereco{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="logradouro")
	private String logradouro = ""; 
	
	@Column(name="numero")
	private String numero = ""; 
	
	@Column(name="complemento")
	private String complemento = ""; 
	
	@Column(name="bairro")
	private String bairro = ""; 
	
	@Column(name="cidade")
	private String cidade = ""; 
	
	@Column(name="estado")
	private String estado = ""; 
	
	@Column(name="cep")
	private String cep = "";
	
	@Column(name="latitude")
	private Double latitude;
	
	@Column(name="longitude")
	private Double longitude;
	
	@Column(name="altura")
	private String altura = "";
	
	public Endereco(){
		super(Endereco.class);
	}
	
	public Endereco(Long id, String cidade, String cep){
		super(Endereco.class);
		setId(id);
		this.cidade = cidade;
		this.cep = cep;
	}
	
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAltura() {
		return altura;
	}

	public void setAltura(String altura) {
		this.altura = altura;
	}
	 
	protected void getCoodernadas() throws ModelException{
			
		if(this.cep.equals("") && this.logradouro.equals("")){
			throw new ModelException("Imposs��vel determinar coordenadas, adicione cep ou lagradouro");
		}
		
		GoogleGeocoder g = GoogleGeocoder.getInstance();
		Collection<GeocodedLocation> loc;
			
		try {
			loc = g.geocode(this.logradouro+", "+this.numero+", "+this.cep+", "+this.cidade+", "+this.estado+", Brazil");
				
			for (Iterator<GeocodedLocation> i = loc.iterator(); i.hasNext();){
				  GeocodedLocation b =  i.next();
				  this.latitude = b.getLat();
				  this.longitude = b.getLon();	
			}
		} catch (GeocodingException e) {
		}
	}

	public void altera() throws ModelException{
		valida();
		EnderecoDAO enderecoDAO = new EnderecoDAO();
		try {
			enderecoDAO.altera(this);
		} catch (DAOException e) {
			throw new ModelException(e.getMessage());
		}
	}
	
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(cep, "CEP inv��lido");
		PreconditionsModel.checkEmptyString(logradouro, "Logradouro inv��lido");
		PreconditionsModel.checkEmptyString(numero, "N��mero inv��lido");
		PreconditionsModel.checkEmptyString(cidade, "Cidade inv��lido");
		getCoodernadas();
		
		if(getCodigo().isEmpty()){
					setCodigo("END"+new Date().getTime());
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		Endereco endereco = new Endereco();
		return endereco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((altura == null) ? 0 : altura.hashCode());
		result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result
				+ ((complemento == null) ? 0 : complemento.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((logradouro == null) ? 0 : logradouro.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endereco other = (Endereco) obj;
		if (altura == null) {
			if (other.altura != null)
				return false;
		} else if (!altura.equals(other.altura))
			return false;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (complemento == null) {
			if (other.complemento != null)
				return false;
		} else if (!complemento.equals(other.complemento))
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (logradouro == null) {
			if (other.logradouro != null)
				return false;
		} else if (!logradouro.equals(other.logradouro))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	
}