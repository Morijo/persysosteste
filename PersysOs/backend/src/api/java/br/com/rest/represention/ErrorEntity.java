package br.com.rest.represention;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorEntity {

	private ErrorMessage error = null;
	
	public ErrorEntity(){}
	
	public ErrorEntity(String type, String message){
		setError(new ErrorMessage(type,message));
	}
	
		 public ErrorMessage getError() {
		return error;
	}

	public void setError(ErrorMessage error) {
		this.error = error;
	}

		@XmlRootElement
		 @XmlAccessorType(XmlAccessType.FIELD)
		  public static class ErrorMessage {
			private String type;
			private String message;
			
			public ErrorMessage(){}
			
			public ErrorMessage(String type, String message){
				this.type = type;
				this.message = message;
			}
			
			public String getType() {
				return type;
			}
		
			public void setType(String type) {
				this.type = type;
			}
		
			public String getMessage() {
				return message;
			}
		
			public void setMessage(String message) {
				this.message = message;
			}
		}
	
}
