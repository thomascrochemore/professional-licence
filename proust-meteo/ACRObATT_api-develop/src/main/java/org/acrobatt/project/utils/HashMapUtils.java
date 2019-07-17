package org.acrobatt.project.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HashMapUtils {

    private HashMapUtils() {}

    /**
     * Function that checks if the supplied HashMap has the given number of null values for the list of entries corresponding to the supplied keys
     * @param map the map to check
     * @param keys list of keys to map entries with
     * @param expectedNulls number of expected null values for the calculated sub-HashMap
     * @return true if the computed sub-HashMap has the specified number of null values
     */
    public static boolean matchesNullValues(HashMap<?,?> map, List<Object> keys, int expectedNulls) {
        // get the sub-map from the supplied map
        HashMap<?,?> subMap = new HashMap<>(map);
        subMap.keySet().retainAll(keys);

        // get the list of entries with null values
        List<Map.Entry> nullEntries = subMap.entrySet().stream()
                .filter(entry -> entry.getValue() == null)
                .collect(Collectors.toList());

        return nullEntries.size() == expectedNulls;
    }


    /**
     * Function that checks if the supplied HashMap has a minimum number of null values for the list of entries corresponding to the supplied keys
     * @param map the map to check
     * @param keys list of keys to map entries with
     * @param expectedMinNulls minimum number of expected null values for the calculated sub-HashMap
     * @return true if the computed sub-HashMap has more than the specified number of null values
     */
    public static boolean hasMinNullValues(HashMap<?,?> map, List<Object> keys, int expectedMinNulls) {
        // get the sub-map from the supplied map
        HashMap<?,?> subMap = new HashMap<>(map);
        subMap.keySet().retainAll(keys);

        // get the list of entries with null values
        List<Map.Entry> nullEntries = subMap.entrySet().stream()
                .filter(entry -> entry.getValue() == null)
                .collect(Collectors.toList());

        return nullEntries.size() >= expectedMinNulls;
    }

    /**
     * Function that checks if the supplied HashMap has a maximum number of null values for the list of entries corresponding to the supplied keys
     * @param map the map to check
     * @param keys list of keys to map entries with
     * @param expectedMaxNulls maximum number of expected null values for the calculated sub-HashMap
     * @return true if the computed sub-HashMap has less than the specified number of null values
     */
    public static boolean hasMaxNullValues(HashMap<?,?> map, List<Object> keys, int expectedMaxNulls) {
        // get the sub-map from the supplied map
        HashMap<?,?> subMap = new HashMap<>(map);
        subMap.keySet().retainAll(keys);

        // get the list of entries with null values
        List<Map.Entry> nullEntries = subMap.entrySet().stream()
                .filter(entry -> entry.getValue() == null)
                .collect(Collectors.toList());

        return nullEntries.size() <= expectedMaxNulls;
    }

    /**
     * Function that checks if the supplied HashMap has between one number of null values and another, for the list of entries corresponding to the supplied keys
     * @param map the map to check
     * @param keys list of keys to map entries with
     * @param expectedMinNulls minimum number of expected null values for the calculated sub-HashMap
     * @param expectedMaxNulls maximum number of expected null values for the calculated sub-HashMap
     * @return true if the computed sub-HashMap has more than the specified min and less than the specified max
     */
    public static boolean hasBetweenNullValues(HashMap<?,?> map, List<Object> keys, int expectedMinNulls, int expectedMaxNulls) {
        // get the sub-map from the supplied map
        HashMap<?,?> subMap = new HashMap<>(map);
        subMap.keySet().retainAll(keys);

        // get the list of entries with null values
        List<Map.Entry> nullEntries = subMap.entrySet().stream()
                .filter(entry -> entry.getValue() == null)
                .collect(Collectors.toList());

        return (nullEntries.size() >= expectedMinNulls) && (nullEntries.size() <= expectedMaxNulls);
    }
}
