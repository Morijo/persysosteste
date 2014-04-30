package br.com.rest.represention;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JsonDateAdapter extends XmlAdapter<String, Date> {

    @Override
    public Date unmarshal(String v) throws Exception {
    	Date date = new Date(Long.parseLong(v));
		return date;
        // TODO convert from your format
    }

    @Override
    public String marshal(Date v) throws Exception {
		return String.valueOf(v.getTime());
        // TODO convert to your format
    }

}
