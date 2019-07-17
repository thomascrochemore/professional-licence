package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.ApiDAO;
import org.acrobatt.project.model.mysql.Api;
import org.acrobatt.project.model.mysql.ApiKey;
import org.acrobatt.project.model.mysql.ApiURL;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApiService {

    private static ApiDAO adao = ApiDAO.getInstance();

    private ApiService() {}

    /**
     * Finds all the currently stored APIs
     * @return the list of APIs
     */
    public static List<Api> findAllApis() {
        return adao.getAll();
    }

    /**
     * Finds all the currently stored APIs with their config
     * @return the list of APIs with configuration
     */
    public static List<Api> findAllApisWithConfig() {
        return adao.getAllWithKeysAndUrls();
    }


    /**
     * Returns an API with its ID, if it exists
     * @param id ID of the API
     * @return the found API, or null if not found
     * @throws IllegalArgumentException if the ID is null
     */
    public static Api findApiById(Long id) throws IllegalArgumentException {
        if(id == null) throw new IllegalArgumentException("argument cannot be null");
        return adao.getById(id);
    }

    /**
     * Returns an API with its name, if it exists
     * @param name name of the API
     * @return the found API, or null if not found
     * @throws IllegalArgumentException if the name is null
     */
    public static Api findApiForName(String name) throws IllegalArgumentException {
        if(name == null) throw new IllegalArgumentException("argument cannot be null");
        return adao.getByName(name);
    }

    /**
     * Adds a new API to the system (currently unused)
     * @param name name of the API
     * @param keys list of keys the API uses
     * @param schemes list of URL schemes the API uses
     * @return the newly added API
     */
    public static Api addNewApi(String name, List<ApiKey> keys, Set<ApiURL> schemes) {
        Api a = new Api(name, keys, schemes);
        return adao.insert(a);
    }

    /**
     * Retunrs a compat list of the API names. For use client-side in dropdown lists.
     * @return the list of names
     */
    public static List<String> getCompactNameList() {
        List<String> names = new ArrayList<>();
        List<Api> apis = adao.getAll();
        for(Api a : apis) {
            names.add(a.getName());
        }
        return names;
    }
}
