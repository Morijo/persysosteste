package br.com.recurso.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.exception.ModelException;
import br.com.model.Model;

@XmlRootElement
@Entity
@Table(name = "recursohabilitado")
public class RecursoHabilitado extends Model<RecursoHabilitado> {

	@OneToOne
	Material material = null;

	@OneToOne
	Dispositivo dispositivo = null;

	public RecursoHabilitado() {}

	public RecursoHabilitado(Recurso recurso) {
		if(recurso instanceof Material){
			material = (Material) recurso;
		}
		
		if(recurso instanceof Dispositivo){
			dispositivo = (Dispositivo) recurso;
		}
	}

	@Override
	public void valida() throws ModelException {
		// TODO Auto-generated method stub
		
	}
}	
	
