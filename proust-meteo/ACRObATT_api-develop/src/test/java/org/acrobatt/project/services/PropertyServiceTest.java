package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.PropertyDAO;
import org.acrobatt.project.model.mysql.Property;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyServiceTest {
    
    private static PropertyDAO dao;

    private static long firstId;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        dao = PropertyDAO.getInstance();
        for(Property property : dao.getAll()){
            dao.delete(property);
        }

        Property temp = new Property("temperature", "celsius");
        Property pres = new Property("pressure", "millibar");

        temp = dao.insert(temp);
        firstId = temp.getId();
        dao.insert(pres);
    }

    @Test
    public void test_insertProperty() {
        Property humi = new Property("humidity", "percentage");
        dao.insert(humi);
        assertNotNull(dao.getById(humi.getId()), "Property insertion failed : object is null");
    }

    @Test
    public void test_insertExistingProperty() {
        assertThrows(PersistenceException.class, () -> {
            Property humi = new Property("humidity", "percentage");
            dao.insert(humi);
        }, "Insertion succeeded without exception");
    }

    @Test
    public void test_getSavedProperty() {
        assertNotNull(dao.getById(firstId), "Property retrieval failed : object is null");
    }

    @Test
    public void test_getSavedPropertyByName() {
        assertNotNull(dao.getByName("temperature"), "Property retrieval failed : object is null");
    }

    @Test
    public void test_updateProperty() {
        Property test = new Property("Test", "t_");
        Property res = dao.insert(test);
        res.setName("t_1");
        dao.update(res);

        assertNotEquals(dao.getById(res.getId()), test, "Property updating failed : object has not changed");
    }

    @Test
    public void test_mergeProperty() {
        Property test2 = new Property("Test2", "t_");
        Property res = dao.insert(test2);
        res.setUnit("t_2");
        dao.merge(res);
        res = dao.getById(res.getId());
        assertEquals("Test2",res.getName() );
        assertEquals("t_2",res.getUnit(),"Property merging failed : object has not changed");
    }

    @Test
    public void test_deleteProperty() {
        Property test3 = new Property("Test3", "t_3");
        dao.insert(test3);
        dao.delete(test3);

        assertNull(dao.getById(test3.getId()), "Property deletion failed : object still present");
    }
}
