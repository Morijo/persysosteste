package br.com.model;

import java.util.regex.Pattern;

import br.com.exception.ModelException;

/**
 * Utils for checking preconditions and invariants
 * 
 * @author Ricardo Sabatine
 */
public class PreconditionsModel
{
  private static final String DEFAULT_MESSAGE = "Received an invalid parameter";
  
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

  // scheme = alpha *( alpha | digit | "+" | "-" | "." )
  private static final Pattern URL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*://\\S+");

  private static final Pattern TELEFONE_PATTERN = Pattern.compile("^([1-9][0-9])[2-9][0-9]{7}[\\(0-9)]?");
	 
  private static final Pattern TELEFONE_FIXO_PATTERN = Pattern.compile("^([1-9][0-9])[2-5][0-9]{7}[\\(0-9)]?");
	
  private static final Pattern CPFCNPJ_FIXO_PATTERN = Pattern.compile("^([0-9]{11})|([0-9]{14})");
  
  private PreconditionsModel(){}

  /**
   * Checks that an object is not null.
   * 
   * @param object any object
   * @param errorMsg error message
 * @throws ModelException 
   * 
   * @throws IllegalArgumentException if the object is null
   */
  public static void checkNotNull(Object object, String errorMsg) throws ModelException
  {
    check(object != null, errorMsg);
  }

  public static void checkNull(Object object, String errorMsg) throws ModelException
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
  public static void checkEmptyString(String string, String errorMsg) throws ModelException
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
  public static void checkValidUrl(String url, String errorMsg) throws ModelException
  {
    checkEmptyString(url, errorMsg);
    check(isUrl(url), errorMsg);
  }
  
  private static boolean isUrl(String url)
  {
    return URL_PATTERN.matcher(url).matches();
  }
  
  public static void checkValidTelefone(Long telefone, String errorMsg) throws ModelException
  {
    checkEmptyString(String.valueOf(telefone), errorMsg);
    check(isTelefone(String.valueOf(telefone)), errorMsg);
  }
  
  public static void checkValidEmail(String email, String errorMsg) throws ModelException
  {
    checkEmptyString(email, errorMsg);
    check(isEmail(email), errorMsg);
  }
  
  public static void checkValidCPFCNPJ(String cpfcnjp, String errorMsg) throws ModelException
  {
    checkEmptyString(cpfcnjp, errorMsg);
    check(isCpfCnpj(cpfcnjp), errorMsg);
  }
  
  public static boolean isCpfCnpj(final String cpfcnpj) {
		return CPFCNPJ_FIXO_PATTERN.matcher(cpfcnpj).matches();
	 }
  
  
  /**
	* Valida email com base na expressão regular.
	* 
	* @param email
	*            para validação
	* @return true se o email é valida, false email invalido
	*/
	public static boolean isEmail(final String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	 }
	
	/**
	* Valida telefone com base na expressão regular.
	* 
	* @param telefone
	*            para validação
	* @return true se o telefon é valida, false telefone invalido
	*/
	public static boolean isTelefone(final String telefone) {
		return TELEFONE_PATTERN.matcher(telefone).matches();
	 }
	
	public static boolean isTelefoneFixo(final String telefone) {
		return TELEFONE_FIXO_PATTERN.matcher(telefone).matches();
	 }
  
  private static void check(boolean requirements, String error) throws ModelException
  {
    String message = (error == null || error.trim().length() <= 0) ? DEFAULT_MESSAGE : error;
    if (!requirements)
    {
      throw new ModelException(message);
    }
  }
  
 	
}
