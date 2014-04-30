package br.com.exception;

	/**
	 * Classe de exception do serviço
	 * Será criado no serviço e tratada na view.
	 * @author 
	 * 
	 */
	public class ServiceException extends Exception{  
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String msg;  
		
		public ServiceException(String msg){  
			super(msg);  
			this.msg = msg;  
		}  
		public String getMessage(){  
			return msg;  
		}  
	}  
