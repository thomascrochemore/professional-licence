package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.ApiURLDAO;
import org.acrobatt.project.model.mysql.ApiURL;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ApiURLServiceTest {

    private static ApiURLDAO dao;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        dao = ApiURLDAO.getInstance();

        ApiURL apx = new ApiURL("apixu_scheme", true);
        ApiURL wnd = new ApiURL("wunderground_scheme", true);

        dao.insert(apx);
        dao.insert(wnd);
    }

    @Test
    public void test_insertApiURL() {
        ApiURL owm = new ApiURL("openweathermap_scheme", true);
        dao.insert(owm);
        assertNotNull(dao.getById(owm.getId()), "ApiURL insertion failed : object is null");
    }

    @Test
    public void test_insertExistingApiURL() {
        assertThrows(PersistenceException.class, () -> {
            ApiURL apx = new ApiURL("wunderground_scheme", true);
            dao.insert(apx);
        }, "Insertion succeeded without exception");
    }

    @Test
    public void test_getSavedApiURL() {
        assertNotNull(dao.getById(1L), "ApiURL retrieval failed : object is null");
    }

    @Test
    public void test_updateApiURL() {
        ApiURL test = new ApiURL("test_scheme", true);
        ApiURL res = dao.insert(test);
        res.setForecast(false);
        dao.update(res);

        assertNotEquals(dao.getById(res.getId()), test, "ApiURL updating failed : object has not changed");
    }

    @Test
    public void test_mergeApiURL() {
        ApiURL test2 = new ApiURL("test2_scheme", true);
        ApiURL res = dao.insert(test2);
        res.setForecast(false);
        dao.merge(res);

        assertNotEquals(dao.getById(res.getId()), test2, "ApiURL merging failed : object has not changed");
    }

    @Test
    public void test_deleteApiURL() {
        ApiURL test3 = new ApiURL("test3_scheme", true);
        dao.insert(test3);
        dao.delete(test3);

        assertNull(dao.getById(test3.getId()), "ApiURL deletion failed : object still present");
    }
}
