DROP PROCEDURE IF EXISTS stats_proc_compute_jump_all_rt;
/**
 * procédure appelée à la fin de chaque backup
 * appelle le backup des values_distance
 */
DELIMITER ||
CREATE PROCEDURE stats_proc_compute_jump_all_rt(IN date_req DATETIME)
  BEGIN
    INSERT INTO jump_all_rt  (id,inserted_at,is_forecast,data_api_id,data_delay_id,data_location_id)
      SELECT DISTINCT id,truncate_date(WEATHERDATA.inserted_at),WEATHERDATA.is_forecast,WEATHERDATA.data_api_id,WEATHERDATA.data_delay_id,WEATHERDATA.data_location_id
      FROM WEATHERDATA
      WHERE WEATHERDATA.is_forecast=0
      AND truncate_date(date_req) = truncate_date(WEATHERDATA.inserted_at);
  END ||
DELIMITER ;