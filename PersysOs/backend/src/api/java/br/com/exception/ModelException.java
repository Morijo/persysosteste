package br.com.exception;
/**
 * Classe de exception do DAO 
 * Ser� criado no DAO e tratada no modelo.
 * @author 
 * 
 */
public class ModelException extends Exception{  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;  
	private int    code = 0;
	
	public ModelException(String msg, int code){  
		super(msg);  
		this.msg = msg;  
		this.code = code;
	}  
	
	public ModelException(String msg){  
		super(msg);  
		this.msg = msg;  
	}  
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage(){  
		return msg;  
	}  
}  

