package br.com.rest.represention;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JsonStringAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String v) throws Exception {
		return v;
        // TODO convert from your format
    }

    @Override
    public String marshal(String v) throws Exception {
		return v;
        // TODO convert to your format
    }

}
