package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.*;
import org.acrobatt.project.model.mysql.*;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherDataServiceTest {

    private static WeatherDataDAO wdao;
    private static DataValueDAO dvdao;
    private static ApiDAO adao;
    private static PropertyDAO pdao;
    private static DelayDAO ddao;
    private static CountryDAO cdao;
    private static LocationDAO ldao;

    @BeforeAll
    public static void loadTestData() {
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        wdao = WeatherDataDAO.getInstance();
        dvdao = DataValueDAO.getInstance();
        adao = ApiDAO.getInstance();
        pdao = PropertyDAO.getInstance();
        ddao = DelayDAO.getInstance();
        cdao = CountryDAO.getInstance();
        ldao = LocationDAO.getInstance();

        //api submodel
        Api dks = new Api("Darksky",Arrays.asList(
                        new ApiKey("7fe8fca452d52e8c"),
                        new ApiKey("4cce82a958a2636f")
                ),
                new HashSet<ApiURL>(Arrays.asList(
                        new ApiURL("test.darksky.net/api/v2/", false)
                ))
        );
        adao.persist(dks);

        //delay submodel
        Delay delay = new Delay(168);
        ddao.insert(delay);

        //country and location submodel
        Country ch = new Country("Switzerland", "CH");
        Location zurich = new Location("Zürich",47.36667f,8.55f, ch);
        Location bern = new Location("Bern", 46.94809f, 7.44744f, ch);
        ldao.persist(zurich);
        ldao.insert(bern);

        //property submodel
        Property alt = new Property("altitude", "meter");
        Property snow = new Property("snow_ground", "centimeter");
        pdao.insert(alt);
        pdao.insert(snow);

        DataValue altval = new DataValue(alt, null, 408f);
        DataValue snowval = new DataValue(snow, null, 8f);
        dvdao.insert(altval);
        dvdao.insert(snowval);

        //weatherdata entry
        WeatherData wdata = new WeatherData(delay, zurich, dks, new HashSet<>(Arrays.asList(altval, snowval)), false);
        altval.setWeather_data(wdata);
        snowval.setWeather_data(wdata);

        wdao.insert(wdata);
    }

    @Test
    public void test_insertWeatherData() {
        Api dks = adao.getByName("Darksky");
        Delay d = ddao.getByDuration(168);
        Country ch = cdao.getByCode("CH");
        Location zur = ldao.getByCityAndCountry("Zürich", ch);
        Property snow = pdao.getByName("snow_ground");
        Property alt = pdao.getByName("altitude");

        DataValue dval = new DataValue(snow, null, 395f);
        WeatherData wdata = new WeatherData(d, zur, dks, new HashSet<>(Arrays.asList(dval)), false);
        dval.setWeather_data(wdata);

        WeatherData res = wdao.insert(wdata);

        assertNotNull(res.getId(), "Insertion failed : ID is null");
        //assertNotNull(res.getData_value(), "Insertion failed : DataValue is null");
        assertNotNull(res.getData_location(), "WeatherData insertion failed : Location is null");
    }

    /*
    @Test
    public void test_insertExistingWeatherData() {
        assertThrows(PersistenceException.class, () -> {
            Api dks = adao.getByName("Darksky");
            Delay d = ddao.getByDuration(168);
            Country ch = cdao.getByCode("CH");
            Location zur = ldao.getByCityAndCountry("Zürich", ch);
            Property snow = pdao.getByName("snow_ground");
            Property alt = pdao.getByName("altitude");

            DataValue dval = new DataValue(snow, null, 395f);
            WeatherData wdata = new WeatherData(d, zur, dks, new HashSet<>(Arrays.asList(dval)), false);
            dval.setWeather_data(wdata);

            WeatherData res = wdao.insert(wdata);
        }, "Insertion succeeded without exception");
    }
    */

    @Test
    public void test_getSavedWeatherData() {
        assertNotNull(wdao.getById(1L), "WeatherData retrieval failed : object is null");
    }

    @Test
    public void test_updateWeatherData() {
        WeatherData wdata = wdao.getById(1L);
        wdata.setForecast(true);
        wdao.update(wdata);
        assertNotEquals(wdao.getById(wdata.getId()).getForecast(), false, "WeatherData updating failed : object has not changed");
    }

    @Test
    public void test_mergeWeatherData() {
        WeatherData wdata = wdao.getById(1L);
        wdata.setForecast(true);
        wdao.merge(wdata);
        assertNotEquals(wdao.getById(wdata.getId()).getForecast(), false, "WeatherData updating failed : object has not changed");
    }

    @Test
    public void test_deleteWeatherData() {
        Api dks = adao.getByName("Darksky");
        Delay d = ddao.getByDuration(168);
        Country ch = cdao.getByCode("CH");
        Location zur = ldao.getByCityAndCountry("Zürich", ch);
        Property snow = pdao.getByName("snow_ground");
        Property alt = pdao.getByName("altitude");

        DataValue dval = new DataValue(snow, null, 395f);
        WeatherData wdata = new WeatherData(d, zur, dks, new HashSet<>(Arrays.asList(dval)), false);
        dval.setWeather_data(wdata);

        WeatherData res = wdao.insert(wdata);

        wdao.delete(res);
        assertNull(wdao.getById(res.getId()), "WeatherData deletion failed : object still present");
    }
}
