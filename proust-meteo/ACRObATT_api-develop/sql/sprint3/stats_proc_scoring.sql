DROP PROCEDURE IF EXISTS stats_proc_scoring;
/**
 * procédure appelée à la fin de chaque compute
 * lui fournissant la date de requête en paramètre
 * sera modifiée par la suite pour compute les indices
 */
DELIMITER |
CREATE PROCEDURE stats_proc_scoring(IN start_date DATETIME,IN end_date DATETIME,IN city_param VARCHAR(255),IN property_param VARCHAR(255))
  BEGIN
    SELECT
      API.api_name as api,
      PROPERTY.property_name as property,
      LOCATION.city as city,
      DELAY.delay_value as delay,
      COALESCE(1/ABS((AVG(dv_rt.value_dec)-AVG(dv_fcst.value_dec))),1/0.0001) as score
    FROM datavalue dv_fcst
      LEFT JOIN WEATHERDATA as wd_fcst ON dv_fcst.weather_data_id = wd_fcst.id
      LEFT JOIN LOCATION ON wd_fcst.data_location_id = LOCATION.id
      LEFT JOIN API ON wd_fcst.data_api_id = API.id
      LEFT JOIN PROPERTY ON dv_fcst.property_id = PROPERTY.id
      LEFT JOIN delay ON wd_fcst.data_delay_id = DELAY.id
      LEFT JOIN datavalue dv_rt ON dv_fcst.property_id = dv_rt.property_id
      LEFT JOIN WEATHERDATA wd_rt ON dv_rt.weather_data_id = wd_rt.id
                                     AND wd_rt.data_location_id = wd_fcst.data_location_id
                                     AND wd_rt.data_api_id = wd_fcst.data_api_id
    WHERE wd_fcst.is_forecast=1
          AND wd_rt.is_forecast=0
          AND truncate_date(wd_rt.inserted_at) = SUBDATE(truncate_date(wd_fcst.inserted_at),INTERVAL DELAY.delay_value HOUR)
          AND truncate_date(wd_rt.inserted_at) BETWEEN start_date AND end_date
          AND lower(PROPERTY.property_name) = lower(property_param)
          AND lower(LOCATION.city) = lower(city_param)
    GROUP BY
      API.api_name,
      PROPERTY.property_name,
      LOCATION.city,
      DELAY.delay_value;
    /*SELECT
      stats_v_distance.api as api,
      stats_v_distance.property as property,
      stats_v_distance.city as city,
      stats_v_distance.delay as delay,
      COALESCE(1/AVG(stats_v_distance.distance),1/0.0001) as score
    FROM stats_v_distance
    WHERE stats_v_distance.date_req BETWEEN start_date AND end_date
    GROUP BY
      stats_v_distance.api,
      stats_v_distance.property,
      stats_v_distance.city,
      stats_v_distance.delay;*/
  END |
DELIMITER ;