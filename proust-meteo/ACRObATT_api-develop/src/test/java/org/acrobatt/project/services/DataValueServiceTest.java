package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.DataValueDAO;
import org.acrobatt.project.dao.mysql.PropertyDAO;
import org.acrobatt.project.model.mysql.DataValue;
import org.acrobatt.project.model.mysql.Property;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataValueServiceTest {
    
    private static DataValueDAO dao;
    private static PropertyDAO pdao;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        dao = DataValueDAO.getInstance();
        pdao = PropertyDAO.getInstance();

        Property temp = new Property("temperature", "kelvin");
        pdao.insert(temp);

        DataValue apx = new DataValue(temp, null, 288.65f);
        dao.insert(apx);
    }

    @Test
    public void test_insertDataValue() {
        Property pres = new Property("pressure", "hectopascal");
        pdao.insert(pres);

        DataValue dtv = new DataValue(pres, null, 1013f);
        dao.insert(dtv);

        assertNotNull(dao.getById(dtv.getId()), "DataValue insertion failed : object is null");
        assertNotNull(dao.getById(dtv.getId()).getProperty(), "DataValue insertion failed : child is null");
    }

    @Test
    public void test_insertExistingDataValue() {
        assertThrows(PersistenceException.class, () -> {
            Property temp = new Property("temperature", "kelvin");
            pdao.insert(temp);

            DataValue dtv = new DataValue(temp, null, 288.65f);
            dao.insert(dtv);
        }, "Insertion succeeded without exception");
    }

    @Test
    public void test_getSavedDataValue() {
        assertNotNull(dao.getById(1L), "DataValue retrieval failed : object is null");
    }
}
