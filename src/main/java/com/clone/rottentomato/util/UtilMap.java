package com.clone.rottentomato.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** map 유틸 클래스 */
public class UtilMap  {

    public static Map<String, Object> makeMap(String name, Object value){
        return new HashMap<>(){ {put(name, value);}};
    }

    public static Map<String, Object> makeMap(String name){
        return makeMap(name, null);
    }
}
