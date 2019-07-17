DROP PROCEDURE IF EXISTS stats_proc_backup_avg_rt;
DELIMITER |
CREATE PROCEDURE stats_proc_backup_avg_rt()
  BEGIN
    DELETE FROM stats_avg_rt;
    INSERT INTO stats_avg_rt (date_req, prop_id, location_id, avg_rt)
      SELECT
        truncate_date(WEATHERDATA.inserted_at),DATAVALUE.property_id,WEATHERDATA.data_location_id,AVG(DATAVALUE.value_dec)
      FROM DATAVALUE
        LEFT JOIN WEATHERDATA
          ON DATAVALUE.weather_data_id = WEATHERDATA.id
      WHERE WEATHERDATA.is_forecast = 0
      GROUP BY
        DATAVALUE.property_id,
        WEATHERDATA.data_location_id,
        truncate_date(WEATHERDATA.inserted_at);
  END |
DELIMITER ;