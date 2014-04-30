package com.persys.osmanager.componente;

import java.util.regex.Pattern;

/**
 * Utils for checking preconditions and invariants
 * 
 * @author Ricardo Sabatine
 */
public class PreconditionsView
{
  
  public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  public static final Pattern URL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*://\\S+");

  public static final Pattern TELEFONE_PATTERN = Pattern.compile("^([1-9][0-9])[2-9][0-9]{7}[\\(0-9)]?");
	 
  public static final Pattern TELEFONE_FIXO_PATTERN = Pattern.compile("^([1-9][0-9])[2-5][0-9]{7}[\\(0-9)]?");
	
  public static final Pattern CPFCNPJ_FIXO_PATTERN = Pattern.compile("^([0-9]{11})|([0-9]{14})");
  }
