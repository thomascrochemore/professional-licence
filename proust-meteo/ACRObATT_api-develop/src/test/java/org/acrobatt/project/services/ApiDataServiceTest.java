package org.acrobatt.project.services;

import org.acrobatt.project.dao.mongo.ApiDataDAO;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ApiDataServiceTest {

    private static ApiDataDAO dao;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        //dao = ApiDataDAO.getInstance();
    }

    @Test
    public void testCompute() {

    }
}
