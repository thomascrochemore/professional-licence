DROP PROCEDURE IF EXISTS stats_proc_backup_jump_all_rt;
/**
 * procédure appelée à la fin de chaque backup
 * appelle le backup des values_distance
 */
DELIMITER ||
CREATE PROCEDURE stats_proc_backup_jump_all_rt()
  BEGIN
    DELETE FROM jump_all_rt;
    INSERT INTO jump_all_rt  (id,inserted_at,is_forecast,data_api_id,data_delay_id,data_location_id)
      SELECT DISTINCT id,truncate_date(WEATHERDATA.inserted_at),WEATHERDATA.is_forecast,WEATHERDATA.data_api_id,WEATHERDATA.data_delay_id,WEATHERDATA.data_location_id
      FROM WEATHERDATA
      WHERE WEATHERDATA.is_forecast=0;
  END ||
DELIMITER ;