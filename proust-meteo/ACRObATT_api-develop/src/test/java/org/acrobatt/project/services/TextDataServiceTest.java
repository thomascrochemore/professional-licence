package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.TextDataDAO;
import org.acrobatt.project.model.mysql.Property;
import org.acrobatt.project.model.mysql.TextData;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TextDataServiceTest {
    private static TextDataDAO txdDAO;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        txdDAO = TextDataDAO.getInstance();

        Property cat = new Property("Clouds", null);
        TextData sun = new TextData("Partially cloudy",5, cat);

        txdDAO.persist(sun);
    }

    @Test
    public void test_insertTextData() {
        Property cat = new Property("Rain", null);
        TextData sno = new TextData("Light rain", 3, cat);
        txdDAO.persist(sno);

        assertNotNull(txdDAO.getById(sno.getId()), "TextData insertion failed : object is null");
    }

    /*
    @Test
    public void test_getExistingByName(){
        TextData sun = txdDAO.getByName();
        assertNotNull(sun);
        assertEquals("Sunlight",sun.getName());
    }
    */

    @Test
    public void test_getNotExistingByText(){
        TextData txt = txdDAO.getByText("UnknownTextData");
        assertNull(txt,"Get failed : TextData is not null");
    }

    @Test
    public void test_insertExistingTextData() {
        assertThrows(PersistenceException.class, () -> {
            Property cat = new Property("Clouds", null);
            TextData sun = new TextData("Partially cloudy", 5, cat);
            txdDAO.persist(sun);
        }, "Insertion succeeded without exception");
    }

    @Test
    public void test_getSavedTextData() {
        assertNotNull(txdDAO.getById(1L), "TextData retrieval failed : object is null");
    }

    @Test
    public void test_updateTextData() {
        TextData test = new TextData("Test", 0, null);
        TextData res = txdDAO.insert(test);
        res.setText("TestUpdate");
        txdDAO.update(res);

        assertNotEquals(txdDAO.getById(res.getId()), test, "TextData updating failed : object has not changed");
    }

    @Test
    public void test_mergeTextData() {
        TextData test2 = new TextData("Test2", 0, null);
        TextData res = txdDAO.insert(test2);
        res.setText("Test2Update");
        txdDAO.merge(res);

        assertNotEquals(txdDAO.getById(res.getId()), test2, "TextData merging failed : object has not changed");
    }

    @Test
    public void test_deleteTextData() {
        TextData test3 = new TextData("Test3", 0, null);
        txdDAO.insert(test3);
        txdDAO.delete(test3);

        assertNull(txdDAO.getById(test3.getId()), "TextData deletion failed : object still present");
    }
}
