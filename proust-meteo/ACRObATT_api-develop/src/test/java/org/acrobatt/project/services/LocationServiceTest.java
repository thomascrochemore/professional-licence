package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.LocationDAO;
import org.acrobatt.project.model.mysql.Location;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LocationServiceTest {

    private static LocationDAO locDAO;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        locDAO = LocationDAO.getInstance();

        Location hsk = new Location("Helsinki", 60.1333f, 25.3894f);
        Location sxb = new Location("Strasbourg", 48.58392f, 7.74553f);

        locDAO.insert(hsk);
        locDAO.insert(sxb);
    }

    @Test
    public void test_insertLocation() {
        Location ogm = new Location("Orgrimmar", 46.0f, 12.0f);
        locDAO.insert(ogm);
        assertNotNull(locDAO.getById(ogm.getId()), "Location insertion failed : object is null");
    }

    @Test
    public void test_getExistingByCity(){
        List<Location> locations = locDAO.getByCity("Helsinki");
        assertEquals(1,locations.size());
        Location hsk = locations.get(0);
        assertNotNull(hsk);
        assertEquals("Helsinki",hsk.getCity());
        assertEquals(60.1333f,hsk.getLatitude(),0.0001);
        assertEquals(25.3894f,hsk.getLongitude(),0.0001);
    }

    @Test
    public void test_getNotExistingByCity(){
        List<Location> locations = locDAO.getByCity("An unknown city");
        assertEquals(0,locations.size());
    }

    @Test
    public void test_insertExistingLocation() {
        assertThrows(PersistenceException.class, () -> {
            Location hsk = new Location("Helsinki", 60.1333f, 25.3894f);
            locDAO.insert(hsk);
        }, "Insertion succeeded without exception");
    }

    @Test
    public void test_getSavedLocation() {
        //should return Helsinki (first entry)
        assertNotNull(locDAO.getById(1L), "Location retrieval failed : object is null");
    }

    @Test
    public void test_updateLocation() {
        Location test = new Location("Test", 32f, 32f);
        Location res = locDAO.insert(test);
        res.setCity("TestUpdate");
        locDAO.update(res);

        assertNotEquals(locDAO.getById(res.getId()), test, "Location updating failed : object has not changed");
    }

    @Test
    public void test_mergeLocation() {
        Location test2 = new Location("Test2", 30f, 30f);
        Location res = locDAO.insert(test2);
        res.setCity("Test2Update");
        locDAO.merge(res);

        assertNotEquals(locDAO.getById(res.getId()), test2, "Location merging failed : object has not changed");
    }

    @Test
    public void test_deleteLocation() {
        Location test3 = new Location("Test3", 42f, 42f);
        locDAO.insert(test3);
        locDAO.delete(test3);

        assertNull(locDAO.getById(test3.getId()), "Location deletion failed : object still present");
    }
}
