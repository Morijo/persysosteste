package br.com.rest.resources.filters;

public class FilterHelper {
    public static String extractValueFromKeyValuePairs(String key, String nameValuePairs) {
        if (key == null || nameValuePairs == null || !nameValuePairs.contains(key)) {
            return null;
        }

        int startOfText = nameValuePairs.lastIndexOf(key + "=\"");

        if (startOfText < 0) {
            return null;
        }

        // Yuck! Nasty string parsing in here.
        String str = nameValuePairs.substring(startOfText + ((key + "=\"").length()));
        str = str.substring(0, str.indexOf("\""));

        return str;
    }
}