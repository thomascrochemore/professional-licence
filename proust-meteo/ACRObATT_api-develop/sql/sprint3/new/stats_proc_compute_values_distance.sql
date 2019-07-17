DROP PROCEDURE IF EXISTS stats_proc_compute_values_distance;
/**
 * procédure appelée à la fin de chaque backup
 * appelle le backup des values_distance
 */
DELIMITER ||
CREATE PROCEDURE stats_proc_compute_values_distance(IN date_request DATETIME)
  BEGIN
    INSERT INTO stats_values_distance (date_req, api, prop, location, delay, value_fcst, value_rt, distance)
      SELECT DISTINCT
        truncate_date(WEATHERDATA.inserted_at) as date_req,
        API.api_name as api,
        PROPERTY.property_name as property,
        LOCATION.city as city,
        delay_fc.delay_value as delay,
        dv_fc.value_dec as value_fcst,
        dv_rt.value_dec as value_rt,
        ROUND(ABS(dv_fc.value_dec-dv_rt.value_dec), 2) as distance
      FROM DATAVALUE dv_fc
        LEFT JOIN WEATHERDATA ON dv_fc.weather_data_id = WEATHERDATA.id
        LEFT JOIN LOCATION ON WEATHERDATA.data_location_id = LOCATION.id
        LEFT JOIN API ON WEATHERDATA.data_api_id = API.id
        LEFT JOIN PROPERTY ON dv_fc.property_id = PROPERTY.id
        LEFT JOIN DELAY delay_fc ON WEATHERDATA.data_delay_id = delay_fc.id

        LEFT JOIN DATAVALUE dv_rt ON dv_rt.property_id = PROPERTY.id
        LEFT JOIN jump_all_rt ON dv_rt.weather_data_id = jump_all_rt.id
        LEFT JOIN LOCATION loc_rt ON jump_all_rt.data_location_id = loc_rt.id
        LEFT JOIN API api_rt ON jump_all_rt.data_api_id = api_rt.id
        LEFT JOIN PROPERTY prop_rt ON dv_rt.property_id = prop_rt.id
        LEFT JOIN DELAY delay_rt ON jump_all_rt.data_delay_id = delay_rt.id
      WHERE DATE_SUB(truncate_date(WEATHERDATA.inserted_at), INTERVAL delay_fc.delay_value HOUR) =
            truncate_date(jump_all_rt.inserted_at)
            AND loc_rt.id = LOCATION.id
            AND api_rt.id = API.id
            AND prop_rt.id = PROPERTY.id
            AND delay_fc.delay_value != 0
            AND truncate_date(WEATHERDATA.inserted_at) = truncate_date('2018-05-25 14:00:00');
  END ||
DELIMITER ;