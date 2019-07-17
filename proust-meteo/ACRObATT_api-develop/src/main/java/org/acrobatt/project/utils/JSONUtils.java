package org.acrobatt.project.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONUtils {

    private static Logger logger = LogManager.getLogger(JSONUtils.class);

    private JSONUtils() {}

    /**
     * Returns true if the JSONObject has any of the fields inside a list of fields
     * @param object the object
     * @param fields the list of fields
     * @return true if it is the case
     */
    private static boolean hasAnyOfFields(JSONObject object, List<String> fields) {
        for(String s : fields) {
            if(object.has(s)) return true;
        }
        return false;
    }

    /**
     * The starting function of the recursive finding strategy to get a map of key-values corresponding to a list of given fields
     * @param json
     * @param fields
     * @return
     * @throws IllegalArgumentException
     */
    public static Map<String, Object> findValueBootstrap(Object json, List<String> fields) throws IllegalArgumentException {
        Map<String, Object> fieldMap = new HashMap<>();
        try {

            if(json instanceof JSONObject) fieldMap = findValueObject((JSONObject) json, fields, fieldMap);
            else if(json instanceof JSONArray) fieldMap = findValueArray((JSONArray) json, fields, fieldMap);
            else throw new IllegalArgumentException("Invalid JSON object");
        } catch(JSONException e) {
            logger.error("Couldn't search JSON object", e);
        }
        return fieldMap;
    }

    /**
     * Searches for a list of fields in a sub-JSONObject and returns the key-value pair in a map
     * @param node the node to search inside of
     * @param fields the list of fields to find
     * @param container the key-value container
     * @return the updated container map
     * @throws JSONException if the search fails
     */
    public static Map<String, Object> findValueObject(JSONObject node, List<String> fields, Map<String, Object> container) throws JSONException {

        if(hasAnyOfFields(node, fields)) {
            Iterator<?> jsonkeys = node.keys();
            while(jsonkeys.hasNext()) {
                String key = (String) jsonkeys.next();
                if(fields.contains(key) && !container.containsKey(key)) container.put(key, node.get(key));
            }
        } else {
            Iterator keys = node.keys();
            while(keys.hasNext()) {
                Object undeterminedField = node.get((String)keys.next());
                if(undeterminedField instanceof JSONObject) findValueObject((JSONObject)undeterminedField, fields, container);
                else if(undeterminedField instanceof JSONArray) findValueArray((JSONArray) undeterminedField, fields, container);
            }
        }
        return container;
    }

    /**
     * Searches for a list of fields in a sub-JSONArray and returns the key-value pair in a map
     * @param node the node to search inside of
     * @param fields the list of fields to find
     * @param container the key-value container
     * @return the updated container map
     * @throws JSONException if the search fails
     */
    public static Map<String, Object> findValueArray(JSONArray node, List<String> fields, Map<String, Object> container) throws JSONException {
        for (int i = 0; i < node.length(); i++) {
            Object item = node.get(i);
            if (item instanceof JSONObject) findValueObject((JSONObject) item, fields, container);
            else if (item instanceof JSONArray) findValueArray((JSONArray) item, fields, container);
        }
        return container;
    }
}
