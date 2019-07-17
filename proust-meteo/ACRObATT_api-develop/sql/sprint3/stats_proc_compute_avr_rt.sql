DROP PROCEDURE IF EXISTS stats_proc_compute_avg_rt;
DELIMITER ||
CREATE PROCEDURE stats_proc_compute_avg_rt(IN date_req DATETIME)
  BEGIN
    INSERT INTO stats_avg_rt (date_req, prop_id, location_id, avg_rt)
      SELECT
        truncate_date(date_req),DATAVALUE.property_id,WEATHERDATA.data_location_id,AVG(DATAVALUE.value_dec)
        FROM DATAVALUE
        LEFT JOIN WEATHERDATA
            ON DATAVALUE.weather_data_id = WEATHERDATA.id
        WHERE truncate_date(date_req) = truncate_date(WEATHERDATA.inserted_at)
        AND WEATHERDATA.is_forecast=0
      GROUP BY DATAVALUE.property_id, WEATHERDATA.data_location_id;
  END ||
DELIMITER ;
