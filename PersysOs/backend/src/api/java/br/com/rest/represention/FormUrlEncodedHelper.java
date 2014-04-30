package br.com.rest.represention;

import java.util.HashMap;

public class FormUrlEncodedHelper {
    private static final HashMap<String, String> unPercentEncodingSubsitutions = new HashMap<String, String>();
    private static final HashMap<String, String> percentEncodingSubsitutions = new HashMap<String, String>();
    
    static {
        percentEncodingSubsitutions.put(" ", "%20");
        percentEncodingSubsitutions.put("!", "%21");
        percentEncodingSubsitutions.put("*", "%2A");
        percentEncodingSubsitutions.put("\"", "%22");
        percentEncodingSubsitutions.put("'", "%27");
        percentEncodingSubsitutions.put("(", "%28");
        percentEncodingSubsitutions.put(")", "%29");
        percentEncodingSubsitutions.put(";", "%3B");
        percentEncodingSubsitutions.put(":", "%3A");
        percentEncodingSubsitutions.put("@", "%40");
        percentEncodingSubsitutions.put("&", "%26");
        percentEncodingSubsitutions.put("=", "%3D");
        percentEncodingSubsitutions.put("+", "%2B");
        percentEncodingSubsitutions.put("$", "%24");
        percentEncodingSubsitutions.put(",", "%2C");
        percentEncodingSubsitutions.put("/", "%2F");
        percentEncodingSubsitutions.put("?", "%3F");
        percentEncodingSubsitutions.put("%", "%25");
        percentEncodingSubsitutions.put("#", "%23");
        percentEncodingSubsitutions.put("[", "%2B");
        percentEncodingSubsitutions.put("]", "%2D");

        // Set up the reverse for de-percent encoding a string
        for(String key : percentEncodingSubsitutions.keySet()) {
            unPercentEncodingSubsitutions.put(percentEncodingSubsitutions.get(key), key);
        }
    }
    
    public static String unPercentEncode(String toUnencode) {
        for(String key : unPercentEncodingSubsitutions.keySet()) {
            toUnencode = toUnencode.replace(key, unPercentEncodingSubsitutions.get(key));
        }
        
        return toUnencode;
    }
    
    
    public static String percentEncode(String toEncode) {
        if(toEncode == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < toEncode.length(); i++) {
            String current = new Character(toEncode.charAt(i)).toString();
            
            if(percentEncodingSubsitutions.containsKey(current)) {
                sb.append(percentEncodingSubsitutions.get(current));
            } else {
                sb.append(current);
            }
        }
        
        return sb.toString();
    }
}
