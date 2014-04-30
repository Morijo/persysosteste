package br.com.rest.resources.oauth;

import java.util.HashMap;

public class OauthHeaderHelper {
    public static HashMap<String, String> extractOauthParamsFromAuthorizationHeader(String requestHeader) {
        HashMap<String, String> map = new HashMap<String, String>();
        
        requestHeader = requestHeader.replaceFirst("\\bOAuth ", "");
        requestHeader = requestHeader.replaceAll("\"", "");

        String[] oauthNameValuePairs = requestHeader.split(", ");
        
        for(String str : oauthNameValuePairs) {
            String[] nameAndValue = str.split("=");
            map.put(nameAndValue[0], nameAndValue[1]);
        }
        
        return map;
    }
}
