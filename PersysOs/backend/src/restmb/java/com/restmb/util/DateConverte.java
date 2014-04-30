package com.restmb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverte {

	

	public static Date getTime(String time) throws Exception {
		
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
	        return format.parse(time);
	    } catch (Exception e) {
	        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    }
		try {
		        return format.parse(time);
		    } catch (Exception e) {
		        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		    }
	    try {
	        return format.parse(time);
	    } catch (Exception e) {
	        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	    }
	    try {
	        return format.parse(time);
	    } catch (Exception e) {
	        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    }
	    try {
	        return format.parse(time);
	    } catch (Exception e) {
	        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
	    }
	    try {
	        return format.parse(time);
	    } catch (Exception e) {
	        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");
	    }
	   
	  	
	    return format.parse(time);
	}
}
