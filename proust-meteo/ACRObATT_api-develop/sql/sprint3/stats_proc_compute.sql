DROP PROCEDURE IF EXISTS stats_proc_compute;
/**
 * procédure appelée à la fin de chaque compute
 * lui fournissant la date de requête en paramètre
 * sera modifiée par la suite pour compute les indices
 */
DELIMITER |
CREATE PROCEDURE stats_proc_compute(IN date_req DATETIME)
  BEGIN
    CALL stats_proc_compute_avg_rt(date_req);
    CALL stats_proc_compute_jump_all_rt(date_req);
    CALL stats_proc_compute_values_distance(date_req);
  END |
DELIMITER ;