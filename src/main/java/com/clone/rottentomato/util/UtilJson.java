package com.clone.rottentomato.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class UtilJson {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static JSONObject convertToJSONObject(String jsonStr) throws ParseException {
        return new JSONObject(jsonStr);
    }

    public static JSONArray convertToJSONArray(String jsonStr, String arrayKey) throws ParseException {
        JSONObject object = convertToJSONObject(jsonStr);
        return convertToJSONArray(object, arrayKey);
    }

    public static JSONArray convertToJSONArray(JSONObject jsonObj, String arrayKey) {
        return jsonObj.getJSONArray(arrayKey);
    }

    public static String stringfy(Object jsonStr) {
        try {
            return objectMapper.writeValueAsString(jsonStr);
        }catch (Exception e) {
            log.error("[UtilJson Error - stringfy]{}", e.getMessage());
        }
        return null;
    }

}
