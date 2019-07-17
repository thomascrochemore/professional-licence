/*
 * usage : select type, property, datavalue
 * from stats_values_forecast
 * where city=? and delay=? and inserted_at between(?,?)
 */
CREATE OR REPLACE VIEW stats_v_distance as(
  SELECT
    truncate_date(wd_rt.inserted_at) as date_req,
    LOCATION.city as city,
    API.api_name as api,
    PROPERTY.property_name as property,
    DELAY.delay_value as delay,
    dv_rt.value_dec as value_realtime,
    dv_fcst.value_dec as value_forecast,
    ABS(dv_rt.value_dec-dv_fcst.value_dec) as distance
  FROM DATAVALUE dv_fcst
  LEFT JOIN WEATHERDATA as wd_fcst ON dv_fcst.weather_data_id = wd_fcst.id
  LEFT JOIN LOCATION ON wd_fcst.data_location_id = LOCATION.id
  LEFT JOIN API ON wd_fcst.data_api_id = API.id
  LEFT JOIN PROPERTY ON dv_fcst.property_id = PROPERTY.id
  LEFT JOIN DELAY ON wd_fcst.data_delay_id = DELAY.id
  LEFT JOIN DATAVALUE dv_rt ON dv_fcst.property_id = dv_rt.property_id
  LEFT JOIN WEATHERDATA wd_rt ON dv_rt.weather_data_id = wd_rt.id
    AND wd_rt.data_location_id = wd_fcst.data_location_id
    AND wd_rt.data_api_id = wd_fcst.data_api_id
  WHERE wd_fcst.is_forecast=1
  AND wd_rt.is_forecast=0
  AND truncate_date(wd_rt.inserted_at) = SUBDATE(truncate_date(wd_fcst.inserted_at),INTERVAL DELAY.delay_value HOUR)
  );
