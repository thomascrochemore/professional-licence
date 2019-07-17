package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.CountryDAO;
import org.acrobatt.project.model.mysql.Country;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class CountryServiceTest {

    private static CountryDAO dao;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        dao = CountryDAO.getInstance();

        Country fr = new Country("France", "FR");
        Country dk = new Country("Danemark", "DK");

        dao.insert(fr);
        dao.insert(dk);
    }

    @Test
    public void test_insertCountry() {
        Country de = new Country("Allemagne", "DE");
        dao.insert(de);
        assertNotNull(dao.getById(de.getId()), "Country insertion failed : object is null");
    }

    @Test
    public void test_insertExistingCountry() {
        assertThrows(PersistenceException.class, () -> {
            Country fr = new Country("France", "FI");
            dao.insert(fr);
        }, "Insertion succeeded without exception");
    }

    @Test
    public void test_getSavedCountry() {
        //should return France (first entry)
        assertNotNull(dao.getById(1L), "Country retrieval failed : object is null");
    }

    @Test
    public void test_getSavedCountryByName() {
        //should return France (first entry)
        assertNotNull(dao.getByName("France"), "Country retrieval failed : object is null");
    }

    @Test
    public void test_getSavedCountryByCode() {
        //should return France (first entry)
        assertNotNull(dao.getByCode("DK"), "Country retrieval failed : object is null");
    }

    @Test
    public void test_updateCountry() {
        Country test = new Country("Test", "T_");
        Country res = dao.insert(test);
        res.setCode("T1");
        dao.update(res);

        assertNotEquals(dao.getById(res.getId()), test, "Country updating failed : object has not changed");
    }

    @Test
    public void test_mergeCountry() {
        Country test2 = new Country("Test2", "T_");
        Country res = dao.insert(test2);
        res.setCode("T2");
        dao.merge(res);

        assertNotEquals(dao.getById(res.getId()), test2, "Country merging failed : object has not changed");
    }

    @Test
    public void test_deleteCountry() {
        Country test3 = new Country("Test3", "T3");
        dao.insert(test3);
        dao.delete(test3);

        assertNull(dao.getById(test3.getId()), "Country deletion failed : object still present");
    }
}
