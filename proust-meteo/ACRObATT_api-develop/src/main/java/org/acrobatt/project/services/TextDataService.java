package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.TextDataDAO;
import org.acrobatt.project.model.mysql.TextData;

import java.util.ArrayList;
import java.util.List;

public class TextDataService {
    private static TextDataDAO tdao = TextDataDAO.getInstance();

    private TextDataService() {}

    /**
     * Finds all the currently stored text-data
     * @return the list of text-data
     */
    public static List<TextData> findAllTextData() {
        return tdao.getAll();
    }

    /**
     * Finds one text-data using its ID
     * @param id the id
     * @return the text-data, or null if it doesn't exist
     */
    public static TextData findTextDataById(Long id) throws IllegalArgumentException {
        if(id == null) throw new IllegalArgumentException("argument cannot be null");
        return tdao.getById(id);
    }

    /**
     * Returns a compat list of the location names. For use client-side in dropdown lists.
     * @return the list of names
     */
    public static List<String> getCompactNameList() {
        List<String> names = new ArrayList<>();
        List<TextData> txts = tdao.getAll();
        for(TextData t : txts) {
            names.add(t.getText());
        }
        return names;
    }
}
