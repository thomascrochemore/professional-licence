Voici un ensemble de table, vues, procédures et fonctions SQL.
On doit les créer dans le bon ordre, certaines dépendent d'autres, voici l'ordre en question :
1. truncate_date.sql
2. stats_avg_rt.sql
3. stats_proc_backup_avg_rt.sql
4. stats_proc_backup.sql
5. stats_proc_compute_avg_rt.sql
6. stats_proc_compute.sql
7. stats_values_forecast.sql
8. stats_v_distance.sql

Les tables/vues à utiliser en java :
1. stats_values_forecast :
    * select type, unit, datavalue
  from stats_values_forecast
  where city=? and delay=? and property = ? and inserted_at between(?,?)
    * Récupère tous les forecast + moyenne des realtimes sur une période donnée
2. stats_proc_backup :
    * appelée à la fin de chaque backup
    * met à jour la table avg_rt
    * sera modifiée pour mettre à jour la table d'indices
3. stats_proc_compute :
    * appelée à la fin de chaque compute
    * met à jour la table avg_rt
    * sera modifiée pour mettre à jour la table d'indices
4. stats_v_distance :
   * select api, avg_rt, value_forecast, distance
    from stats_v_distance
    where date_req between(?,?) and city = ? and property = ?


Les autres tables/vues sont normalement prévue pour le traitement interne en java, il ne sera donc pas nécessaire de les appeler en java.
