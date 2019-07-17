DROP TABLE IF EXISTS stats_avg_rt;

/*
 * on doit insérer les stats toutes les 6h au compute
 * + mettre à jour pour le backup
 */
CREATE TABLE IF NOT EXISTS stats_avg_rt(
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  date_req DATETIME NOT NULL,
  prop_id BIGINT(20) NOT NULL,
  location_id BIGINT(20) NOT NULL,
  avg_rt FLOAT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_vm_avgrt_city_id FOREIGN KEY (location_id) REFERENCES location(id),
  CONSTRAINT fk_vm_avgrt_prop_id FOREIGN KEY (prop_id) REFERENCES property(id)
) ENGINE=MyISAM;
