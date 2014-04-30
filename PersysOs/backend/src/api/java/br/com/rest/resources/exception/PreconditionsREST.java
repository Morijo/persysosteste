package br.com.rest.resources.exception;

import java.util.regex.Pattern;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.exception.ModelException;
import br.com.rest.represention.ErrorEntity;

/**
 * Utils for checking preconditions and invariants
 * 
 * @author Ricardo Sabatine
 */
public class PreconditionsREST
{
  private static final String DEFAULT_MESSAGE = "Received an invalid parameter";
  
  // scheme = alpha *( alpha | digit | "+" | "-" | "." )
  private static final Pattern URL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*://\\S+");

  private PreconditionsREST(){}

  /**
   * Checks that an object is not null.
   * 
   * @param object any object
   * @param errorMsg error message
 * @throws ModelException 
   * 
   * @throws IllegalArgumentException if the object is null
   */
  public static void checkNotNull(Object object, String errorMsg) 
  {
    check(object != null, errorMsg);
  }

  public static void checkNull(Object object, String errorMsg) 
  {
    check(object == null, errorMsg);
  }

  /**
   * Checks that a string is not null or empty
   * 
   * @param string any string
   * @param errorMsg error message
 * @throws ModelException 
   * 
   * @throws IllegalArgumentException if the string is null or empty
   */
  public static void checkEmptyString(String string, String errorMsg) 
  {
    check(string != null && !string.trim().equals(""), errorMsg);
  }

  /**
   * Checks that a URL is valid
   * 
   * @param url any string
   * @param errorMsg error message
 * @throws ModelException 
   */
  public static void checkValidUrl(String url, String errorMsg)
  {
    checkEmptyString(url, errorMsg);
    check(isUrl(url), errorMsg);
  }
  
  private static boolean isUrl(String url)
  {
    return URL_PATTERN.matcher(url).matches();
  }
  
  public static void error(String errorMsg)
  {
    check(false, errorMsg);
  }
  
  public static void checkBoolean(boolean requirements, String error)
  {
	 check(requirements, error);
  }
  
  public static void checkNumber(Object requirements)
  {
	  	try{
	  		Integer.parseInt(requirements.toString());
	  	}catch(Exception e) {
			throw new WebApplicationException(
				Response.status(Status.NOT_ACCEPTABLE) .
				type(MediaType.APPLICATION_JSON) .entity(new ErrorEntity("RESTException",e.getMessage())).build());
		}	
	}
  
  
  private static void check(boolean requirements, String error)
  {
    String message = (error == null || error.trim().length() <= 0) ? DEFAULT_MESSAGE : error;
    if (!requirements)
    {
    	throw new WebApplicationException(
				Response.status(Status.NOT_ACCEPTABLE) .
				type(MediaType.APPLICATION_JSON) .entity(new ErrorEntity("RESTException",message)).build());
    }
  }
  
}
