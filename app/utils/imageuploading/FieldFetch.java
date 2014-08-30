package utils.imageuploading;

import play.Logger;

import java.util.Map;

public class FieldFetch {

    private Map<String, String[]> map;


    public FieldFetch(Map<String, String[]> map) {
        this.map = map;
    }

    public String getFieldValue(String key){
        String[] paramArray = map.get(key);
        String val = paramArray[0];

        Logger.debug("key: " + key + ", val: " + val);

        return val;
    }

    public static String getFieldValue(String key, Map<String, String[]> paramsMap){
        String[] paramArray = paramsMap.get(key);
        String val = paramArray[0];

        Logger.debug("key: " + key + ", val: " + val);

        return val;
    }
}
