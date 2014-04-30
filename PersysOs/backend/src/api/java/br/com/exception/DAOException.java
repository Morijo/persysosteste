package br.com.exception;
/**
 * Classe de exception do DAO 
 * Ser� criado no DAO e tratada no modelo.
 * @author 
 * 
 */
public class DAOException extends Exception{  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;  
	private int    code = 0;
	
	public DAOException(String msg, int code){  
		super(msg);  
		this.msg = createMsg(msg);
		this.code = code;
	}
	
	public DAOException(String msg){  
		super(msg);  
		this.msg = createMsg(msg);
	}  
	
	public String getMessage(){  
		return msg;  
	}  
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String createMsg(String msg){
		if(msg.contains("value too long for type character")){
			return "Valor do campo inválido";
		}
		return msg;
	}
}  

