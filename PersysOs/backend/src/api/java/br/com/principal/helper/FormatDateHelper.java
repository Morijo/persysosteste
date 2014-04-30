package br.com.principal.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FormatDateHelper {

	public static Date formatTimeZone(Date data){
			TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");  
	        TimeZone.setDefault(tz);  
	        Calendar ca = GregorianCalendar.getInstance(tz); 
	        ca.setTime(data);
			return ca.getTime();
	}
	
	public static Date formatTimeZoneBR(String data){
	  try {
		Locale BRAZIL = new Locale("pt","BR"); 
		return  new SimpleDateFormat("dd/MM/yyyy HH:mm", BRAZIL).parse(data);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String formatTimeZoneBRDATE(Long data){
		Date date = new Date(data);  
		Locale BRAZIL = new Locale("pt","BR"); 
		return  new SimpleDateFormat("dd/MM/yyyy", BRAZIL).format(date);
	}
	
	public static String formatTimeZoneUSToBR(Long data){
		Date date = new Date(data);  
		Locale BRAZIL = new Locale("pt","BR"); 
		return  new SimpleDateFormat("dd/MM/yyyy HH:mm", BRAZIL).format(date);
	}
	
	public static String formatDateToString(Integer hora, Integer minuto){
		  try {
			java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Calendar cal = Calendar.getInstance();  
		    cal.set(Calendar.HOUR_OF_DAY, hora);
		    cal.set(Calendar.MINUTE, minuto);
		  	return df.format(cal.getTime());
		  } catch (Exception e) {
				return null;
		}	
	}	 
	
	public static Date formatDateToDate(Integer dia, Integer mes, Integer ano, Integer hora, Integer minuto){
		  try {
			Calendar cal = Calendar.getInstance();  
			cal.set(ano, mes, dia);
		    cal.set(Calendar.HOUR_OF_DAY, hora);
		    cal.set(Calendar.MINUTE, minuto);
		  	return cal.getTime();
		  } catch (Exception e) {
				return null;
		}	
	}	 
	
	public static String formatDateToString(Integer dia, Integer mes, Integer ano, Integer hora, Integer minuto){
		  try {
			java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Calendar cal = Calendar.getInstance();  
			cal.set(ano, mes, dia);
		    cal.set(Calendar.HOUR_OF_DAY, hora);
		    cal.set(Calendar.MINUTE, minuto);
		  	return df.format(cal.getTime());
		  } catch (Exception e) {
				return null;
		}	
	}	 
	
	@SuppressWarnings("deprecation")
	public static Date formatTimeToDate(Integer hora, Integer minuto){
		  try {
			Date date = new Date();  
			Calendar cal = Calendar.getInstance();  
			cal.set(date.getYear(), date.getMonth(), date.getDay());
			cal.set(Calendar.HOUR_OF_DAY, hora);
		    cal.set(Calendar.MINUTE, minuto);
		  	return cal.getTime();
		  } catch (Exception e) {
				return null;
		}	
	}	 
	
	@SuppressWarnings("deprecation")
	public static String formatTimeToString(Integer hora, Integer minuto){
		  try {
			java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date date = new Date();  
			Calendar cal = Calendar.getInstance();
			cal.set(date.getYear(), date.getMonth(), date.getDay());
			cal.set(Calendar.HOUR_OF_DAY, hora);
		    cal.set(Calendar.MINUTE, minuto);
		  	return df.format(cal.getTime());
		  } catch (Exception e) {
				return null;
		}	
	}	
	
	@SuppressWarnings("deprecation")
	public static String formatTimeToStringUS(Integer hora, Integer minuto){
		  try {
			Date date = new Date();  
			Calendar cal = Calendar.getInstance();
			cal.set(date.getYear(), date.getMonth(), date.getDay());
			cal.set(Calendar.HOUR_OF_DAY, hora);
		    cal.set(Calendar.MINUTE, minuto);
		  	return cal.getTime().toGMTString();
		  } catch (Exception e) {
				return null;
		}	
	}	
	
	@SuppressWarnings("deprecation")
	public static Long formatTimeToLong(Integer hora, Integer minuto){
		  try {
			Date date = new Date();  
			Calendar cal = Calendar.getInstance();
			cal.set(date.getYear(), date.getMonth(), date.getDay());
			cal.set(Calendar.HOUR_OF_DAY, hora);
		    cal.set(Calendar.MINUTE, minuto);
		  	return cal.getTime().getTime();
		  } catch (Exception e) {
				return null;
		}	
	}	 
	
	public static String formatTimeZoneBRRequest(Long data){
		Date date = new Date(data);  
		Locale BRAZIL = new Locale("pt","BR"); 
		return  new SimpleDateFormat("MM/dd/yyyy", BRAZIL).format(date);
	}
}
