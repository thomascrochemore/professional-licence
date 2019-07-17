package org.acrobatt.project.weatherdata;

import org.acrobatt.project.controllers.DBMigrationController;
import org.acrobatt.project.dao.mysql.WeatherDataDAO;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class WeatherDataServiceTest {
    private WeatherDataService weatherDataService = WeatherDataService.getInstance();
    private WeatherDataDAO weatherDataDAO = WeatherDataDAO.getInstance();

    public WeatherDataServiceTest() throws IOException {
    }

    @BeforeAll
    public static void initSetupMigration(){
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        DBMigrationController migrationController = new DBMigrationController();
        migrationController.migrateSprint3();
    }

    @Test
    @Disabled
    public void backupTest() throws IOException {
        weatherDataService.backupApiDataToWeatherData();
    }

    @Test
    @Disabled
    public void computeTest() throws IOException, JSONException {
        weatherDataDAO.deleteAll();
        weatherDataService.computeApisData();
    }
}
