package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.ApiKeyDAO;
import org.acrobatt.project.model.mysql.ApiKey;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ApiKeyServiceTest {
    
    private static ApiKeyDAO dao;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        dao = ApiKeyDAO.getInstance();

        ApiKey k1 = new ApiKey("key1");
        ApiKey k2 = new ApiKey("key2");

        dao.insert(k1);
        dao.insert(k2);
    }

    @Test
    public void test_insertApiKey() {
        ApiKey k3 = new ApiKey("key3");
        dao.insert(k3);
        assertNotNull(dao.getById(k3.getId()), "ApiKey insertion failed : object is null");
    }

    @Test
    public void test_insertExistingApiKey() {
        ApiKey k2 = new ApiKey("key2");
        dao.insert(k2);
        assertNotNull(dao.getById(k2.getId()), "Additional insertion failed : object does not exist");
    }

    @Test
    public void test_getSavedApiKey() {
        //should return France (first entry)
        assertNotNull(dao.getById(1L), "ApiKey retrieval failed : object is null");
    }

    @Test
    public void test_updateApiKey() {
        ApiKey test = new ApiKey("test");
        ApiKey res = dao.insert(test);
        res.setKey_value("test1");
        dao.update(res);

        assertNotEquals(dao.getById(res.getId()), test, "ApiKey updating failed : object has not changed");
    }

    @Test
    public void test_mergeApiKey() {
        ApiKey test2 = new ApiKey("test");
        ApiKey res = dao.insert(test2);
        res.setKey_value("test2");
        dao.merge(res);

        assertNotEquals(dao.getById(res.getId()), test2, "ApiKey merging failed : object has not changed");
    }

    @Test
    public void test_deleteApiKey() {
        ApiKey test3 = new ApiKey("test3");
        dao.insert(test3);
        dao.delete(test3);

        assertNull(dao.getById(test3.getId()), "ApiKey deletion failed : object still present");
    }
}
