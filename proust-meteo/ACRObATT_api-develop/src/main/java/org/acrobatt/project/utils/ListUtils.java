package org.acrobatt.project.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    private ListUtils() {}

    /**
     * removes duplicates in a list (unused)
     * @param list the list
     * @return the same list without duplicates
     */
    public static List<Object> removeDuplicates(List<Object> list) {
        List<Object> noDup = new ArrayList<>();
        list.forEach(item -> {
            if(!noDup.contains(item)) noDup.add(item);
        });
        return noDup;
    }


}
