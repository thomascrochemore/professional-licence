package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.DelayDAO;
import org.acrobatt.project.model.mysql.Delay;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class DelayServiceTest {
    
    private static DelayDAO dao;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        dao = DelayDAO.getInstance();

        Delay zero = new Delay(0);
        Delay three = new Delay(3);

        dao.insert(zero);
        dao.insert(three);
    }

    @Test
    public void test_insertDelay() {
        Delay six = new Delay(6);
        dao.insert(six);
        assertNotNull(dao.getById(six.getId()), "Delay insertion failed : object is null");
    }

    @Test
    public void test_insertExistingDelay() {
        assertThrows(PersistenceException.class, () -> {
            Delay six = new Delay(3);
            dao.insert(six);
        }, "Insertion succeeded without exception");
    }

    @Test
    public void test_getSavedDelay() {
        //should return France (first entry)
        assertNotNull(dao.getById(1L), "Delay retrieval failed : object is null");
    }

    @Test
    public void test_updateDelay() {
        Delay test = new Delay(20);
        Delay res = dao.insert(test);
        res.setValue(24);
        dao.update(res);

        assertNotEquals(dao.getById(res.getId()), test, "Delay updating failed : object has not changed");
    }

    @Test
    public void test_mergeDelay() {
        Delay test2 = new Delay(40);
        Delay res = dao.insert(test2);
        res.setValue(48);
        dao.merge(res);

        assertNotEquals(dao.getById(res.getId()), test2, "Delay merging failed : object has not changed");
    }

    @Test
    public void test_deleteDelay() {
        Delay test3 = new Delay(72);
        dao.insert(test3);
        dao.delete(test3);

        assertNull(dao.getById(test3.getId()), "Delay deletion failed : object still present");
    }
    
}
