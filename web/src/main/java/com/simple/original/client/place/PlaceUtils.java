package com.simple.original.client.place;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

public class PlaceUtils {

    public static String HashMapAsString(HashMap<String, String> parameters) {
        JSONObject jso = new JSONObject();

        if (parameters != null) {
            Set<String> keys = parameters.keySet();

            for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
                String key = iter.next();
                jso.put(key, new JSONString(parameters.get(key)));
            }
        }

        return jso.toString();
    }

    public static HashMap<String, String> StringToHashMap(String parameters) {
        HashMap<String, String> parameterMap = new HashMap<String, String>();

        JSONObject jso = (JSONObject) JSONParser.parseStrict(parameters);

        Set<String> keys = jso.keySet();

        for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
            String key = iter.next();
            JSONString value = (JSONString) jso.get(key);
            parameterMap.put(key, value.stringValue());
        }

        return parameterMap;
    }

    public static HashMap<String, String> getParameterPairs(String parameters) {
        HashMap<String, String> parameterMap = new HashMap<String, String>();

        String[] parameterPairs = parameters.split("&");

        for (int i = 0; i < parameterPairs.length; i++) {
            String[] nameAndValue = parameterPairs[i].split("=");
            parameterMap.put(nameAndValue[0], nameAndValue[1]);
        }

        return parameterMap;
    }

    public static Long longFromToken(String str) {
        Long ret;

        try {
            ret = Long.parseLong(str);
        } catch (NumberFormatException e) {
            ret = null;
        }
        return ret;
    }

    public static String tokenFromLong(Long l) {
        return (l == null) ? "" : l.toString();
    }
}
