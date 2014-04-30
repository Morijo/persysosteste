package br.com.rest.resources.helper;

public class ParameterRequestRest {

	//A timestamp (mm/dd/YYYY hh:mm) value that points to the start of the range of time-based data.
	//
	public static final String SINCE = "since";
	
	// A Unix timestamp value that points to the end of the range of time-based data.
	public static final String UNTIL = "until";

	//limit : This is the number of individual objects that are returned in each page.
	public static final String LIMIT = "limit";
	 
	//fields: campos desejados no retorno (verificar construtor)
	public static final String FIELDS = "fields";
	
	//valor inicial
	public static final String OFFSET = "offset";
	
	//status 0 Inativo, 1 Ativo, 2 Removido
	public static final String STATUS = "status";
}
