package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.PropertyDAO;
import org.acrobatt.project.model.mysql.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyService {
    private static PropertyDAO pdao = PropertyDAO.getInstance();

    private PropertyService() {}

    /**
     * Finds all the currently stored properties
     * @return the list of properties
     */
    public static List<Property> findAllProperties() {
        return pdao.getAll();
    }

    /**
     * Finds one property using its ID
     * @param id the id
     * @return the location, or null if it doesn't exist
     */
    public static Property findPropertyById(Long id) throws IllegalArgumentException {
        if(id == null) throw new IllegalArgumentException("argument cannot be null");
        return pdao.getById(id);
    }

    /**
     * Finds one property using its name (unused)
     * @param name the name of the property
     * @return the property, or null if none are found
     * @throws IllegalArgumentException if the name is null
     */
    public static Property findPropertyForName(String name) throws IllegalArgumentException {
        if(name == null) throw new IllegalArgumentException("arguments cannot be null");
        return pdao.getByName(name);
    }

    /**
     * Returns a compat list of the location names. For use client-side in dropdown lists.
     * @return the list of names
     */
    public static List<String> getCompactNameList() {
        List<String> names = new ArrayList<>();
        List<Property> props = pdao.getAll();
        for(Property p : props) {
            names.add(p.getName());
        }
        return names;
    }
}
