package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.ApiDAO;
import org.acrobatt.project.model.mysql.Api;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ApiServiceTest {
    
    private static ApiDAO dao;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        dao = ApiDAO.getInstance();

        Api apx = new Api("Apixu", new ArrayList<>(), new HashSet<>());
        Api wnd = new Api("Wunderground", new ArrayList<>(), new HashSet<>());

        dao.insert(apx);
        dao.insert(wnd);
    }

    @Test
    public void test_insertApi() {
        Api owm = new Api("OpenWeatherMap", new ArrayList<>(), new HashSet<>());
        dao.insert(owm);
        assertNotNull(dao.getById(owm.getId()), "Api insertion failed : object is null");
    }

    @Test
    public void test_insertExistingApi() {
        assertThrows(PersistenceException.class, () -> {
            Api apx = new Api("Apixu", new ArrayList<>(), new HashSet<>());
            dao.insert(apx);
        }, "Insertion succeeded without exception");
    }

    @Test
    public void test_getSavedApi() {
        //should return Helsinki (first entry)
        assertNotNull(dao.getById(1L), "Api retrieval failed : object is null");
    }

    @Test
    public void test_getSavedApiByName() {
        //should return Helsinki (first entry)
        assertNotNull(dao.getByName("Apixu"), "Api retrieval failed : object is null");
    }

    @Test
    public void test_updateApi() {
        Api test = new Api("Test", new ArrayList<>(), new HashSet<>());
        Api res = dao.insert(test);
        res.setName("TestUpdate");
        dao.update(res);

        assertNotEquals(dao.getById(res.getId()), test, "Api updating failed : object has not changed");
    }

    @Test
    public void test_mergeApi() {
        Api test2 = new Api("Test2", new ArrayList<>(), new HashSet<>());
        Api res = dao.insert(test2);
        res.setName("Test2Update");
        dao.merge(res);

        assertNotEquals(dao.getById(res.getId()), test2, "Api merging failed : object has not changed");
    }

    @Test
    public void test_deleteApi() {
        Api test3 = new Api("Test3", new ArrayList<>(), new HashSet<>());
        dao.insert(test3);
        dao.delete(test3);

        assertNull(dao.getById(test3.getId()), "Api deletion failed : object still present");
    }
}
