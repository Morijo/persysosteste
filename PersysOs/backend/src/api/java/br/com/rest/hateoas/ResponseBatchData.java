package br.com.rest.hateoas;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.rest.represention.ResponseBatch;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseBatchData {
  
   @XmlElement(name = "data")
   private Collection<ResponseBatch> dados = new ArrayList<ResponseBatch>();

   @XmlElement(name = "response")
   private Integer response = 0;
  
   public ResponseBatchData() { }
   
   public ResponseBatchData(Collection<ResponseBatch> dados, Integer response) {
	  if(dados != null) 
           this.dados = dados;
	  this.response = response;
   }
  
	public Collection<ResponseBatch> getDados() {
		return dados;
	}
	
	public void setDados(Collection<ResponseBatch> dados) {
		this.dados = dados;
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(Integer response) {
		this.response = response;
	}
	
	
}
