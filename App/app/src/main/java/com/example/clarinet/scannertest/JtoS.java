package com.example.clarinet.scannertest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kim on 2017-12-12.
 */

public class JtoS {
    public JtoS(){

    }

    public String getJtoS(JSONObject JJHjson, String KeyVal){
        String result = null;
        try {
            result = JJHjson.getString(KeyVal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
