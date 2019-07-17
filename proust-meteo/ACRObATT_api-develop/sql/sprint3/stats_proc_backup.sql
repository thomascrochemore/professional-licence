DROP PROCEDURE IF EXISTS stats_proc_backup;
/**
 * procédure appelée à la fin de chaque backup
 * appelle le backup des avg_rt
 * sera modifiée pour appeler le backup des indices
 */
DELIMITER |
CREATE PROCEDURE stats_proc_backup()
  BEGIN
    CALL stats_proc_backup_avg_rt();
    CALL stats_proc_backup_jump_all_rt();
    CALL stats_proc_backup_values_distance();
  END |
DELIMITER ;