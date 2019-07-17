package org.acrobatt.project.utils;

/**
 * SHOULD MOVE TO A CONFIG FILE.
 * A list of useful constants for JPA processing (aliases)
 */
public class GlobalVariableRegistry {

    private GlobalVariableRegistry() {}

    public static final String HIBERNATE_CFG_LOCAL = "WEB-INF/classes/hibernate_local.cfg.xml";
    public static final String HIBERNATE_CFG_PROD = "WEB-INF/classes/hibernate_prod.cfg.xml";

    public static final String HIBERNATE_CFG_TEST_LOCAL = "src/main/resources/hibernate_local_test.cfg.xml";

    public static final String DATETIME_ALIAS = "Date/heure";
    public static final String FIRST_DATETIME_ALIAS = "Première date";
    public static final String LAST_DATETIME_ALIAS = "Dernière date";
    public static final String DELAY_ALIAS = "Délai";
    public static final String API_ALIAS = "API";
    public static final String PROPERTY_ALIAS = "Propriété";
    public static final String CITY_ALIAS = "Ville";
    public static final String VALUE_ALIAS = "Valeur";
    public static final String UNIT_ALIAS = "Unité";

    public static final String SP_TYPE_ALIAS = "Type";

    public static final String SP_FORECAST_ALIAS = "Prévision";
    public static final String SP_REALTIME_ALIAS = "Observé";

    public static final String SP_AVERAGE_ALIAS = "Moyenne";
    public static final String SP_DISTANCE_ALIAS = "Distance";
    public static final String SP_SCORING_ALIAS = "Score";
    public static final String SP_SUM_SCORE_ALIAS = "Total des scores";
    public static final String SP_INDEX_ALIAS = "Indice de confiance";

    public static final String SP_WIDGET_FORECASTS_ALIAS = "Prévisions optimisées";

    public static final String PROPERTY_TEMPERATURE = "Température";
    public static final String PROPERTY_PRESSURE = "Pression";
    public static final String PROPERTY_HUMIDITY = "Humidité";
    public static final String PROPERTY_WIND_SPEED = "Vitesse du vent";

}
